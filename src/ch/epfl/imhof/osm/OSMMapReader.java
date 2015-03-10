package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Stack;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

public final class OSMMapReader {
    private OSMMapReader() { };
    
    public static OSMMap readOSMFile(String fileName, boolean unGZip) {
        OSMMap.Builder mapBuilder = new OSMMap.Builder();
        try {
            InputStream i = new FileInputStream(fileName);
            i = unGZip ? new GZIPInputStream(i) : i;
            
            XMLReader r = XMLReaderFactory.createXMLReader();
    
            r.setContentHandler(new DefaultHandler() {
                private OSMWay currentWay;
                private OSMRelation currentRelation;
                private OSMEntity currentParent;
                
                private OSMRelation.Builder relationBuilder;
                private OSMNode.Builder nodeBuilder;
                private OSMWay.Builder wayBuilder;
                
                
                @Override
                public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                        case "osm": // Nothing.
                            break;
                        case "node": {
                            long id = Long.parseLong(atts.getValue("id"));
                            double lon = Double.parseDouble(atts.getValue("lon"));
                            double lat = Double.parseDouble(atts.getValue("lat"));
                            nodeBuilder = new OSMNode.Builder(id, new PointGeo(lon, lat));
                            break;
                        }
                        case "way": {
                            long id = Long.parseLong(atts.getValue("id"));
                            wayBuilder = new OSMWay.Builder(id);
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
                            break;
                        }
                        case "member": {
                            String type = atts.getValue("type");
                            long ref = Long.parseLong(atts.getValue("ref"));
                            String role = atts.getValue("role");
                            
                            if (type.equals("node")) {
                                OSMNode referencedNode = mapBuilder.nodeForId(ref);
                                if (referencedNode != null)
                                    // ABSOLUTELY TEMPORARY!!
                                    // TODO finish this code.
                                    relationBuilder.addMember(, role, referencedMember);
                                    
                            }
                            
                            
                            break;
                        }
                        case "tag":
                            break;
                        
                    }
                }
                
                
                @Override
                public void endElement(String uri, String lName, String qName) throws SAXException {
                    switch (qName) {
                        case "osm": // Nothing.
                            break;
                        case "node":
                            mapBuilder.addNode(nodeBuilder.build());
                            nodeBuilder = null;
                            break;
                        case "way":
                            mapBuilder.addWay(wayBuilder.build());
                            wayBuilder = null;
                            break;
                        case "nd": // Nothing.
                            break;
                        case "relation":
                            mapBuilder.addRelation(relationBuilder.build());
                            relationBuilder = null;
                            break;
                        case "member":
                            break;
                        case "tag":
                            break;
                    }
                }
            });
            
            
            r.parse(new InputSource(i));
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
}

    