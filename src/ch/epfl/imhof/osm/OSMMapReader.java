package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

public final class OSMMapReader {
    private OSMMapReader() {
    };

    public static OSMMap readOSMFile(String fileName, boolean unGZip) {
        OSMMap.Builder mapBuilder = new OSMMap.Builder();
        try {
            InputStream i = new FileInputStream(fileName);
            i = unGZip ? new GZIPInputStream(i) : i;
            
            XMLReader r = XMLReaderFactory.createXMLReader();
    
            r.setContentHandler(new DefaultHandler() {
                private OSMEntity.Builder currentParentBuilder;
                private OSMRelation.Builder relationBuilder;
                private OSMWay.Builder wayBuilder;
                private OSMNode.Builder nodeBuilder;
                
                @Override
                public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                        case "node": {
                            long id = Long.parseLong(atts.getValue("id"));
                            double lon = Double.parseDouble(atts.getValue("lon"));
                            double lat = Double.parseDouble(atts.getValue("lat"));
                            //I don't get this line. Why do you use a projection ? Ok points are in degrees, but you just have to convert them into radians.
                            //Because the given values are already in WGS 84
                            //PointGeo point = new CH1903Projection().inverse(new Point(lon, lat));
                            PointGeo point = new PointGeo(Math.toRadians(lon), Math.toRadians(lat));
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
                                        relationBuilder.addMember(OSMRelation.Member.Type.NODE, role, referencedNode);
                                    else
                                        relationBuilder.setIncomplete();
                                    break;
                                case "way":
                                    OSMWay referencedWay = mapBuilder.wayForId(ref);
                                    if (referencedWay != null)
                                        relationBuilder.addMember(OSMRelation.Member.Type.WAY, role, referencedWay);
                                    else
                                        relationBuilder.setIncomplete();
                                    break;
                                case "relation":
                                    OSMRelation referencedRelation = mapBuilder.relationForId(ref);
                                    if (referencedRelation != null)
                                        relationBuilder.addMember(OSMRelation.Member.Type.RELATION, role, referencedRelation);
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
                public void endElement(String uri, String lName, String qName) throws SAXException {
                    switch (qName) {
                        case "node":
                            if (!nodeBuilder.isIncomplete()) mapBuilder.addNode(nodeBuilder.build());
                            nodeBuilder = null;
                            break;
                        case "way":
                            if (!wayBuilder.isIncomplete()) mapBuilder.addWay(wayBuilder.build());
                            wayBuilder = null;
                            break;
                        case "relation":
                            if (!relationBuilder.isIncomplete()) mapBuilder.addRelation(relationBuilder.build());
                            relationBuilder = null;
                            break;
                    }
                }
            });
            r.parse(new InputSource(i));
            return mapBuilder.build();
        }
        catch (Exception e) {
            System.out.println("Exception raised while reading " + fileName + (unGZip ? "(Gzipped)" : ""));
            System.out.println("Exception: " + e.getMessage()); // TODO maybe remove this line before final hand-in?
            e.printStackTrace();
            return null;
        }
    }
}
