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
    // Defining attributes as they are named by the OpenStreetMaps
    // specification:
    private static final String NODE = "node", WAY = "way", ND = "nd",
            RELATION = "relation", MEMBER = "member", TAG = "tag", ID = "id",
            REFERENCE = "ref", LONGITUDE = "lon", LATITUDE = "lat", KEY = "k",
            VALUE = "v", TYPE = "type", ROLE = "role";

    private OSMMapReader() {
    }; // Constructor is private in order to prevent instantiation

    //@formatter:off
    /**
     * Reads the OSM file, if no fatal error encountered (file missing/ wrong
     * extension) builds a OSMMap with the complete OSMEntites that are
     * described in the read file.
     * 
     * Only takes the following entities (markup) into account: 
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
     *            the path of the osm file containing the map to be read
     * @param unGZip
     *            true if file is in gzip, false otherwise
     * @return newly built OSMMap
     */
    //@formatter:on
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws SAXException, IOException {
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
                    case NODE: {
                        long id = Long.parseLong(atts.getValue(ID));
                        double lon = Double.parseDouble(atts
                                .getValue(LONGITUDE));
                        double lat = Double.parseDouble(atts.getValue(LATITUDE));
                        PointGeo point = new PointGeo(Math.toRadians(lon), Math
                                .toRadians(lat));
                        nodeBuilder = new OSMNode.Builder(id, point);
                        currentParentBuilder = nodeBuilder;
                        break;
                    }
                    case WAY: {
                        long id = Long.parseLong(atts.getValue(ID));
                        wayBuilder = new OSMWay.Builder(id);
                        currentParentBuilder = wayBuilder;
                        break;
                    }
                    case ND: {
                        long ref = Long.parseLong(atts.getValue(REFERENCE));
                        OSMNode referencedNode = mapBuilder.nodeForId(ref);
                        if (referencedNode != null)
                            wayBuilder.addNode(referencedNode);
                        else
                            wayBuilder.setIncomplete();
                        break;
                    }
                    case RELATION: {
                        long id = Long.parseLong(atts.getValue(ID));
                        relationBuilder = new OSMRelation.Builder(id);
                        currentParentBuilder = relationBuilder;
                        break;
                    }
                    case MEMBER: {
                        String type = atts.getValue(TYPE);
                        long ref = Long.parseLong(atts.getValue(REFERENCE));
                        String role = atts.getValue(ROLE);
                        switch (type) {
                        case NODE:
                            OSMNode referencedNode = mapBuilder.nodeForId(ref);
                            if (referencedNode != null)
                                relationBuilder.addMember(
                                        OSMRelation.Member.Type.NODE, role,
                                        referencedNode);
                            else
                                relationBuilder.setIncomplete();
                            break;
                        case WAY:
                            OSMWay referencedWay = mapBuilder.wayForId(ref);
                            if (referencedWay != null)
                                relationBuilder.addMember(
                                        OSMRelation.Member.Type.WAY, role,
                                        referencedWay);
                            else
                                relationBuilder.setIncomplete();
                            break;
                        case RELATION:
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
                    case TAG:
                        String k = atts.getValue(KEY);
                        String v = atts.getValue(VALUE);
                        currentParentBuilder.setAttribute(k, v);
                        break;
                    }
                }

                @Override
                public void endElement(String uri, String lName, String qName)
                        throws SAXException {
                    switch (qName) {
                    case NODE:
                        if (!nodeBuilder.isIncomplete())
                            mapBuilder.addNode(nodeBuilder.build());
                        break;
                    case WAY:
                        if (!wayBuilder.isIncomplete())
                            mapBuilder.addWay(wayBuilder.build());
                        break;
                    case RELATION:
                        if (!relationBuilder.isIncomplete())
                            mapBuilder.addRelation(relationBuilder.build());
                        break;
                    }
                }
            });
            r.parse(new InputSource(i));
            return mapBuilder.build();
        }
    }
}
