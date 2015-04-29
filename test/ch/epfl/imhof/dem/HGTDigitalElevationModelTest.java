package ch.epfl.imhof.dem;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

public class HGTDigitalElevationModelTest {

    @Test
    public void montHeightBlancTest() {
        PointGeo montBlanc = new PointGeo(Math.toRadians(6.865), Math.toRadians(45.833611));
        File elevationModel = new File("data/HGT/N45E006.hgt");
        try {
            HGTDigitalElevationModel hgt = new HGTDigitalElevationModel(elevationModel);
            Vector3 v = hgt.normalAt(montBlanc);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
