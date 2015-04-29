package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

public class HGTDigitalElevationModel implements DigitalElevationModel {
    //private final PointGeo bl;
    private final FileInputStream stream;
    private final ShortBuffer buffer;
    
    public HGTDigitalElevationModel(File hgt) throws IOException {
        
        long length = 100; //useless value ATM
        stream = new FileInputStream(hgt);
        buffer = stream.getChannel()
                .map(MapMode.READ_ONLY, 0, length)
                .asShortBuffer();
    }
    
    
    @Override
    public void close() throws Exception {
        stream.close();
    }

    @Override
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
        return null;
    }
    
}
