package ch.epfl.imhof.dem;

import java.io.File;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

public class HGTDigitalElevationModel implements DigitalElevationModel {
    
    public HGTDigitalElevationModel(File hgt) {
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

}
