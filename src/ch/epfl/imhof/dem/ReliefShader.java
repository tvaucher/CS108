package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

public final class ReliefShader {
    private final Projection projection;
    private final HGTDigitalElevationModel model;
    private final Vector3 lightSource;

    private final double DELTA = 1e-5;

    public ReliefShader(Projection projection, HGTDigitalElevationModel model,
            Vector3 lightSource) {
        this.projection = projection;
        this.model = model;
        this.lightSource = lightSource.normalized();
        // so the scalar product of 2 unitary vectors => cos
    }

    public BufferedImage shadedRelief(Point bl, Point tr, int width,
            int height, double blurRadius) {
        if (height < 0 || width < 0)
            throw new IllegalArgumentException(
                    "Width and height must be bigger than 0");
        PointGeo blGeo = projection.inverse(bl);
        PointGeo trGeo = projection.inverse(tr);
        if (!model.contains(blGeo)
                || !model.contains(trGeo))
            throw new IllegalArgumentException(
                    "The model doesn't contain the whole zone you want to draw : " + blGeo.latitude() + " lat " + blGeo.longitude() + " lon " + trGeo.latitude() + " lat " + trGeo.longitude() + " lon");
        if (blurRadius < 0)
            throw new IllegalArgumentException(
                    "Variance (aka blur radius) cannot be negative, was : "
                            + blurRadius);

        if (blurRadius < DELTA)
            return brutRelief(bl, tr, width, height, 0);
        
        Kernel kernel = gaussianBlurKernel(blurRadius);
        int overflowSize = kernel.getWidth()/2;
        return bluredRelief(kernel, brutRelief(bl, tr, width, height, overflowSize), width, height, overflowSize);
    }

    private BufferedImage brutRelief(Point bl, Point tr, int width, int height,
            int overflowSize) {
        PointGeo blGeo = projection.inverse(bl);
        PointGeo trGeo = projection.inverse(tr);
        double deltaX = (trGeo.longitude() - blGeo.longitude()) / width;
        double deltaY = (trGeo.latitude() - blGeo.latitude()) / height;
        PointGeo tlGeo = new PointGeo(blGeo.longitude()
                - (overflowSize * deltaX), trGeo.latitude()
                + (overflowSize * deltaY));
        int newWidth = width + 2 * overflowSize;
        int newHeight = height + 2 * overflowSize;

        BufferedImage brut = new BufferedImage(newWidth, newHeight,
                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < newWidth; ++i) {
            for (int j = 0; j < newHeight; ++j) {
                Vector3 normal = model.normalAt(new PointGeo(tlGeo.longitude()
                        + i * deltaX, tlGeo.latitude() - j * deltaY));
                double cosAngle = normal.scalarProduct(lightSource);
                //System.out.println(cosAngle);
                double rg = (cosAngle + 1) / 2d;
                brut.setRGB(i, j, Color.rgb(rg, rg, (0.7 * cosAngle + 1) / 2d)
                        .packedRBG());
            }
        }
        
        return brut;
    }

    private Kernel gaussianBlurKernel(double blurRadius) {
        double sigma = blurRadius/3;
        int middle = (int) Math.ceil(blurRadius);
        int n = 2*middle+1;
        float[] data = new float[n];
        float sum = 0;
        for (int i = 0; i < n; ++i) {
            int cord = i - middle;
            data[i] = (float) Math.exp(-(cord*cord)/(2*sigma*sigma));
            sum += data[i];
        }
        for (int i = 0; i < n; ++i)
            data[i] = data[i]/sum;
        return new Kernel(n, 1, data);
    }

    private BufferedImage bluredRelief(Kernel kernel, BufferedImage brutRelief, int width,
            int height, int overflowSize) {
        ConvolveOp transformerX = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        ConvolveOp transformerY = new ConvolveOp(new Kernel(1, kernel.getWidth(), kernel.getKernelData(null)), ConvolveOp.EDGE_NO_OP, null);
        return transformerY.filter(transformerX.filter(brutRelief, null), null).getSubimage(overflowSize, overflowSize, width, height);
    }

}
