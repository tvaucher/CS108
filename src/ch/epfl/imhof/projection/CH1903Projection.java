package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public final class CH1903Projection {
    public Point project(PointGeo point) {
        double longitudeDegrees = Math.toDegrees(point.longitude());
        double latitudeDegrees =  Math.toDegrees(point.latitude());
        double longitude1 = (1./10000)*(longitudeDegrees*3600-26782.5);
        double latitude1 = (1./10000)*(latitudeDegrees*3600-169028.66);
        double x = 600072.37 
                + 211455.93 * longitude1
                - 10938.51 * longitude1 * latitude1
                - .36 * longitude1 * latitude1 * latitude1
                - 44.54 * Math.pow(longitude1, 3);
        double y = 200147.07
                + 308807.95 * latitude1
                + 3745.25 * longitude1 * longitude1
                + 76.63 * latitude1 * latitude1
                - 194.56 * longitude1 * longitude1 * latitude1
                + 119.79 * Math.pow(latitude1,3);
      
        return new Point(x, y);
    }
    
    public PointGeo inverse(Point point) {
        
        
        return new PointGeo(point.x(), point.y());
    }
}
