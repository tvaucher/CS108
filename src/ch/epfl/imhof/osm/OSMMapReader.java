package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * Utility class that allows to read a OSM file using SAX library to read the
 * xml markups + attributes Builds a OSMMap with the read attributes.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public final class OSMMapReader {
    private OSMMapReader() {
    }; // Constructor is private in order to prevent instanciation

    //@formatter:off
    /**
     * Reads the OSM file, if no fatal error encountered (file missing/ wrong
     * extension) builds a OSMMap with the complete OSMEntites that are
     * described in the read file.
     * 
     * Only take into account the following entities (markup): 
     *   - OSMNode (node)
     *     - unique identifier (id) 
     *     - latitude in degree (lat) 
     *     - longitude in degree (lon) 
     *   - OSMWay (way) 
     *     - unique identifier (id) 
     *     - node included in the way (nd red="id") 
     *     - attributes (tag k="key" v="value") 
     *   - OSMRelation (relation) 
     *     - unique identifier (id) 
     *     - included member (member) 
     *       - type of member (type="type") 
     *       - unique identifier of member (ref="id") 
     *       - role of member, normally inner or outer (role="outer") 
     *     - attributes (tag k="key" v="value")
     * 
     * @param fileName
     *            path of the osm file containing the map to be read
     * @param unGZip
     *            true if file is in gzip, false otherwise
     * @return newly build OSMMap
     */
    //@formatter:on
    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws SAXException, IOException {
        OSMMap.Builder mapBuilder = new OSMMap.Builder();
        try (BufferedInputStream inStream = new BufferedInputStream(
                new FileInputStream(fileName));
                InputStream i = unGZip ? new GZIPInputStream(inStream)
                        : inStream;) {
            XMLReader r = XMLReaderFactory.createXMLReader();

            r.setContentHandler(new DefaultHandler() {
                private OSMEntity.Builder currentParentBuilder;
                private OSMRelation.Builder relationBuilder;
                private OSMWay.Builder wayBuilder;
                private OSMNode.Builder nodeBuilder;

                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                    case "node": {
                        long id = Long.parseLong(atts.getValue("id"));
                        double lon = Double.parseDouble(atts.getValue("lon"));
                        double lat = Double.parseDouble(atts.getValue("lat"));
                        PointGeo point = new PointGeo(Math.toRadians(lon), Math
                                .toRadians(lat));
                        nodeBuilder = new OSMNode.Builder(id, point);
                        currentParentBuilder = nodeBuilder;
                        break;
                    }
                    case "way": {
                        long id = Long.parseLong(atts.getValue("id"));
                        wayBuilder = new OSMWay.Builder(id);
                        currentParentBuilder = wayBuilder;
                        break;
                    }
                    case "nd": {
                        long ref = Long.parseLong(atts.getValue("ref"));
                        OSMNode referencedNode = mapBuilder.nodeForId(ref);
                        if (referencedNode != null)
                            wayBuilder.addNode(referencedNode);
                        else
                            wayBuilder.setIncomplete();
                        break;
                    }
                    case "relation": {
                        long id = Long.parseLong(atts.getValue("id"));
                        relationBuilder = new OSMRelation.Builder(id);
                        currentParentBuilder = relationBuilder;
                        break;
                    }
                    case "member": {
                        String type = atts.getValue("type");
                        long ref = Long.parseLong(atts.getValue("ref"));
                        String role = atts.getValue("role");
                        switch (type) {
                        case "node":
                            OSMNode referencedNode = mapBuilder.nodeForId(ref);
                            if (referencedNode != null)
                                relationBuilder.addMember(
                                        OSMRelation.Member.Type.NODE, role,
                                        referencedNode);
                            else
                                relationBuilder.setIncomplete();
                            break;
                        case "way":
                            OSMWay referencedWay = mapBuilder.wayForId(ref);
                            if (referencedWay != null)
                                relationBuilder.addMember(
                                        OSMRelation.Member.Type.WAY, role,
                                        referencedWay);
                            else
                                relationBuilder.setIncomplete();
                            break;
                        case "relation":
                            OSMRelation referencedRelation = mapBuilder
                                    .relationForId(ref);
                            if (referencedRelation != null)
                                relationBuilder.addMember(
                                        OSMRelation.Member.Type.RELATION, role,
                                        referencedRelation);
                            else
                                relationBuilder.setIncomplete();
                            break;
                        }
                        break;
                    }
                    case "tag":
                        String k = atts.getValue("k");
                        String v = atts.getValue("v");
                        currentParentBuilder.setAttribute(k, v);
                        break;
                    }
                }

                @Override
                public void endElement(String uri, String lName, String qName)
                        throws SAXException {
                    switch (qName) {
                    case "node":
                        if (!nodeBuilder.isIncomplete())
                            mapBuilder.addNode(nodeBuilder.build());
                        break;
                    case "way":
                        if (!wayBuilder.isIncomplete())
                            mapBuilder.addWay(wayBuilder.build());
                        break;
                    case "relation":
                        if (!relationBuilder.isIncomplete())
                            mapBuilder.addRelation(relationBuilder.build());
                        break;
                    }
                }
            });
            r.parse(new InputSource(i));
            return mapBuilder.build();
        }
        catch (SAXException saxE) {
            System.out.println("SAXException");
            throw new SAXException("Failed reading the file", saxE);
        }
        catch (IOException ioE) {
            System.out.println("IOException");
            throw new IOException("Cannot find file : " + fileName);
        }
        catch (Exception e) {
            System.out.println("Another exception raised while reading " + fileName
                    + (unGZip ? "(Gzipped)" : ""));
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
