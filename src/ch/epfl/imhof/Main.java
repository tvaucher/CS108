package ch.epfl.imhof;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.gui.OpenFileButton;
import ch.epfl.imhof.gui.SaveFileButton;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.SwissPainter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.utility.QueryGenerator;

/**
 * The Main class is what ties everything else together. It's the main
 * orchestrator of the rest of the code, which manages inputs and outputs.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public class Main {
    private final static double INCHES_PER_METRE = 39.3700787; // in
                                                               // inches/metre
    private final static double BLUR_RADIUS = 1.7; // in millimetres.
    private final static Pattern gzPattern = Pattern.compile(".+\\.gz$");
    private final static Vector3 LIGHT_VECTOR = new Vector3(-1, 1, 1);

    private static final int WIDTH = 400, HEIGHT = 100, TEXT_WIDTH = 200;
    private JTabbedPane tabs;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName()); // Still prettier
                                                           // than the default
                                                           // IMO
            } catch (Exception e) {
                e.printStackTrace();
            }
            createUI();
        });
    }

    private void createUI() {
        JFrame frame = new JFrame("The Imhof Project");

        // ////////////////////
        // //// COMPONENTS ////
        // ////////////////////
        // Here, we will be defining the individual components.

        // Image:
        JLabel logoLabel = null;
        try {
            BufferedImage logo = ImageIO.read(new File("epfl_logo.jpg"));
            logoLabel = new JLabel(new ImageIcon(logo));
        } catch (IOException e) {
            System.out.println("Error reading epfl_logo.jpg");
            e.printStackTrace();
        }

        // Labels:
        JLabel osmLabel = new JLabel("OSM File"), hgtLabel = new JLabel(
                "HGT File"), blLongLabel = new JLabel("Bottom Left Longitude"), blLatLabel = new JLabel(
                "Bottom Left Latitude"), trLongLabel = new JLabel(
                "Top Right Longitude"), trLatLabel = new JLabel(
                "Top Right Latitude"), dpiLabel = new JLabel("Resolution"), outLabel = new JLabel(
                "Output File");

        osmLabel.setToolTipText("A .osm or .osm.gz file");
        hgtLabel.setToolTipText("A 1-degree .hgt file");
        blLongLabel
                .setToolTipText("The longitude (in CH1903 or WGS83) of the bottom left point of the map");
        blLatLabel
                .setToolTipText("The latitude (in CH1903 or WGS83) of the bottom left point of the map");
        trLongLabel
                .setToolTipText("The longitude (in CH1903 or WGS83) of the top right point of the map");
        trLatLabel
                .setToolTipText("The latitude (in CH1903 or WGS83) of the top right point of the map");
        dpiLabel.setToolTipText("The resolution of the image (in dpi)");
        outLabel.setToolTipText("The name of the map image that will be created (*.png)");

        JLabel autoBlLongLabel = new JLabel("Bottom Left Longitude"), autoBlLatLabel = new JLabel(
                "Bottom Left Latitude"), autoTrLongLabel = new JLabel(
                "Top Right Longitude"), autoTrLatLabel = new JLabel(
                "Top Right Latitude"), autoDpiLabel = new JLabel("Resolution"), autoOutLabel = new JLabel(
                "Output File");

        // Text Fields:
        JTextField osmField = new JTextField(), hgtField = new JTextField(), blLongField = new JTextField(), blLatField = new JTextField(), trLongField = new JTextField(), trLatField = new JTextField(), outField = new JTextField();
        SpinnerNumberModel model = new SpinnerNumberModel(300, 10, 2000, 10);
        JSpinner dpiField = new JSpinner(model);

        JTextField autoBlLongField = new JTextField(), autoBlLatField = new JTextField(), autoTrLongField = new JTextField(), autoTrLatField = new JTextField(), autoOutField = new JTextField();
        JSpinner autoDpiField = new JSpinner(model);

        // Buttons:
        OpenFileButton osmButton = new OpenFileButton(osmField);
        osmButton.setFileFilter(new FileNameExtensionFilter(
                "Open Street Maps (*.osm, *.osm.gz)", "osm", "gz"));

        OpenFileButton hgtButton = new OpenFileButton(hgtField);
        hgtButton.setFileFilter(new FileNameExtensionFilter("HGT (*.hgt)",
                "hgt"));

        SaveFileButton outButton = new SaveFileButton(outField);
        FileFilter filter = new FileNameExtensionFilter("PNG (*.png)", "png");
        outButton.setFileFilter(filter);
        outButton.setDefaultExtension("png");

        SaveFileButton autoOutButton = new SaveFileButton(autoOutField);
        autoOutButton.setFileFilter(filter);
        autoOutButton.setDefaultExtension("png");

        JButton create = new JButton("Create Map");
        create.addActionListener(e -> createMap(osmField.getText(),
                osmField.getText(), blLongField.getText(),
                blLatField.getText(), trLongField.getText(),
                trLatField.getText(), String.valueOf(dpiField.getValue()),
                outField.getText(), autoBlLongField.getText(),
                autoBlLatField.getText(), autoTrLongField.getText(),
                autoTrLatField.getText(),
                String.valueOf(autoDpiField.getValue()), autoOutField.getText()));
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));

        // //////////////////
        // //// PANELS //////
        // //////////////////
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints pb_gbc = new GridBagConstraints();
        // Default values
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pb_gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 1, 1, 1);

        // Add the logo to the top right.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        content.add(logoLabel, gbc);

        // The inputs will occupy position (0, 1)

        // Add buttons to bottom right
        JPanel actionButtons = new JPanel();
        actionButtons.add(create);
        actionButtons.add(exit);
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(actionButtons, gbc);

        // ////////////////////////
        // ///// TAB 1: MANUAL ////
        // ////////////////////////
        // Here, we put all the components into a content field.

        JPanel tab1 = new JPanel(new GridBagLayout());
        GridBagConstraints inc = new GridBagConstraints();
        inc.fill = GridBagConstraints.HORIZONTAL;
        inc.insets = new Insets(1, 1, 1, 1);

        // Add input lines:
        inc.anchor = GridBagConstraints.WEST;

        // OSM file:
        inc.gridx = 0;
        inc.gridy = 0;
        tab1.add(osmLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(osmField), inc);
        inc.gridx = 2;
        tab1.add(osmButton, inc);

        // HGT file:
        inc.gridx = 0;
        inc.gridy = 1;
        tab1.add(hgtLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(hgtField), inc);
        inc.gridx = 2;
        tab1.add(hgtButton, inc);

        // BL longitude
        inc.gridx = 0;
        inc.gridy = 2;
        tab1.add(blLongLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(blLongField), inc);

        // BL latitude
        inc.gridx = 0;
        inc.gridy = 3;
        tab1.add(blLatLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(blLatField), inc);

        // TR longitude
        inc.gridx = 0;
        inc.gridy = 4;
        tab1.add(trLongLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(trLongField), inc);

        // TR latitude
        inc.gridx = 0;
        inc.gridy = 5;
        tab1.add(trLatLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(trLatField), inc);

        // Resolution
        inc.gridx = 0;
        inc.gridy = 6;
        tab1.add(dpiLabel, inc);
        inc.gridx = 1;
        tab1.add(dpiField, inc);

        // Output name
        inc.gridx = 0;
        inc.gridy = 7;
        tab1.add(outLabel, inc);
        inc.gridx = 1;
        tab1.add(formatted(outField), inc);
        inc.gridx = 2;
        tab1.add(outButton, inc);
        // Style the tab1
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ///////////////////////////
        // ///// TAB 2: AUTOMATIC ////
        // ///////////////////////////
        JPanel tab2 = new JPanel(new GridBagLayout());
        // BL longitude
        inc.gridx = 0;
        inc.gridy = 0;
        tab2.add(autoBlLongLabel, inc);
        inc.gridx = 1;
        tab2.add(formatted(autoBlLongField), inc);

        // BL latitude
        inc.gridx = 0;
        inc.gridy = 1;
        tab2.add(autoBlLatLabel, inc);
        inc.gridx = 1;
        tab2.add(formatted(autoBlLatField), inc);

        // TR longitude
        inc.gridx = 0;
        inc.gridy = 2;
        tab2.add(autoTrLongLabel, inc);
        inc.gridx = 1;
        tab2.add(formatted(autoTrLongField), inc);

        // TR latitude
        inc.gridx = 0;
        inc.gridy = 3;
        tab2.add(autoTrLatLabel, inc);
        inc.gridx = 1;
        tab2.add(formatted(autoTrLatField), inc);

        // Resolution
        inc.gridx = 0;
        inc.gridy = 4;
        tab2.add(autoDpiLabel, inc);
        inc.gridx = 1;
        tab2.add(autoDpiField, inc);

        // Output name
        inc.gridx = 0;
        inc.gridy = 5;
        tab2.add(autoOutLabel, inc);
        inc.gridx = 1;
        tab2.add(formatted(autoOutField), inc);
        inc.gridx = 2;
        tab2.add(autoOutButton, inc);

        // ///////////////
        // //// FRAME ////
        // ///////////////

        // Finally, we can create the frame.
        tabs = new JTabbedPane(JTabbedPane.TOP);

        tabs.add("Manual mode", tab1);
        tabs.add("Automatic mode", tab2);
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(tabs, gbc);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);

    }

    private JTextField formatted(JTextField tf) {
        tf.setPreferredSize(new Dimension(TEXT_WIDTH,
                tf.getPreferredSize().height));
        return tf;
    }

    private void createMap(String osmFile, String hgtFile, String blLong,
            String blLat, String trLong, String trLat, String dpi, String out,
            String autoBlLong, String autoBlLat, String autoTrLong,
            String autoTrLat, String autoDpi, String autoOut) {
        if (tabs.getSelectedIndex() == 0) {
            String[] args = { osmFile, hgtFile, blLong, blLat, trLong, trLat,
                    dpi, out };
            manuallyCreateMap(args);
        } else {
            String[] args = { autoBlLong, autoBlLat, autoTrLong, autoTrLat,
                    autoDpi, autoOut };
            automaticallyCreateMap(args);
        }
    }

    // @formatter:off
    /**
     * The method to execute to create a map.
     * 
     * @param args
     *            It takes a list of 8 strings: 
     *            - The name of the .osm or .osm.gz file 
     *            - The name of the .hgt file
     *            - The longitude of the bottom left point
     *            - The latitude of the bottom left point
     *            - The longitude of the top right point
     *            - The latitude of the top right point
     *            - The resolution (in dpi) of the map
     *            - The name of the outputted file.
     * @throws IllegalArgumentException
     *             if there are more or less than 8 arguments.
     */
    // @formatter:on
    private void manuallyCreateMap(String[] args) {
        String mapName = args[0];
        String hgtName = args[1];

        // Coordinates will directly be stored in radians:
        double blLongitude = Math.toRadians(Double.parseDouble(args[2]));
        double blLatitude = Math.toRadians(Double.parseDouble(args[3]));
        double trLongitude = Math.toRadians(Double.parseDouble(args[4]));
        double trLatitude = Math.toRadians(Double.parseDouble(args[5]));
        int dpi = Integer.parseInt(args[6]);
        String outputName = args[7];

        // Now, convert the input to metrics that we can use:
        Projection projector = new CH1903Projection();
        Point bl = projector.project(new PointGeo(blLongitude, blLatitude));
        Point tr = projector.project(new PointGeo(trLongitude, trLatitude));

        double resolution = dpi * INCHES_PER_METRE;
        double blur = (resolution * BLUR_RADIUS) / (1000d); // in pixels
        int height = (int) Math.round(resolution * (1d / 25000)
                * (trLatitude - blLatitude) * Earth.RADIUS);
        int width = (int) Math.round(height * (tr.x() - bl.x())
                / (tr.y() - bl.y()));

        Painter painter = SwissPainter.painter();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projector);
        OSMMap osmMap = null;

        try {
            osmMap = OSMMapReader.readOSMFile(mapName,
                    gzPattern.matcher(mapName).matches());

        } catch (Exception e) {
            System.out.println("An error occured while reading the file.");
            e.printStackTrace();
        }

        Map map = transformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi,
                Color.WHITE);
        try (HGTDigitalElevationModel model = new HGTDigitalElevationModel(
                new File(hgtName))) {
            ReliefShader rel = new ReliefShader(projector, model, LIGHT_VECTOR);
            BufferedImage relief = rel
                    .shadedRelief(bl, tr, width, height, blur);
            painter.drawMap(map, canvas);
            ImageIO.write(mix(canvas.image(), relief), "png", new File(
                    outputName));
        } catch (Exception e) {
            System.out.println("An error occured while writing the file.");
            e.printStackTrace();
        }
    }

    private void automaticallyCreateMap(String[] args) {
        String string = args[1] + " " + args[0] + " " + args[3] + " " + args[2];
        QueryGenerator qg = new QueryGenerator(string);
        Point bl = qg.bl();
        Point tr = qg.tr();

        String urlOsm = qg.getURLosm();
        String urlHgt = qg.getURLhgt();

        int dpi = Integer.parseInt(args[4]);
        String outputName = args[5];

        // Now, convert the input to metrics that we can use:
        Projection projector = new CH1903Projection();
        double resolution = dpi * INCHES_PER_METRE;
        double blur = (resolution * BLUR_RADIUS) / (1000d); // in pixels
        int height = (int) Math.round(resolution * (1d / 25000) * qg.getDiff()
                * Earth.RADIUS);
        int width = (int) Math.round(height * (tr.x() - bl.x())
                / (tr.y() - bl.y()));

        Painter painter = SwissPainter.painter();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projector);
        OSMMap osmMap = null;

        try {
            osmMap = OSMMapReader.readOSMFile(urlOsm);

        } catch (Exception e) {
            System.out.println("An error occured while reading the file.");
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);
        for (Attributed<Point> place : map.places()) {
            System.out.println(place.attributeValue("name") + " "
                    + place.value().x() + " " + place.value().y());
        }
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi,
                Color.WHITE);
        try (HGTDigitalElevationModel model = new HGTDigitalElevationModel(
                urlHgt);) {
            ReliefShader rel = new ReliefShader(projector, model, LIGHT_VECTOR);
            BufferedImage relief = rel
                    .shadedRelief(bl, tr, width, height, blur);
            painter.drawMap(map, canvas);
            ImageIO.write(mix(canvas.image(), relief), "png", new File(
                    outputName));
        } catch (Exception e) {
            System.out.println("An error occured while writing the file.");
            e.printStackTrace();
        }
    }

    private static BufferedImage mix(BufferedImage i1, BufferedImage i2) {
        BufferedImage out = new BufferedImage(i1.getWidth(), i1.getHeight(),
                i1.getType());
        for (int i = 0; i < i1.getWidth(); ++i) {
            for (int j = 0; j < i1.getHeight(); ++j) {
                Color mixed = Color.rgb(i1.getRGB(i, j)).multiplyWith(
                        Color.rgb(i2.getRGB(i, j)));
                out.setRGB(i, j, mixed.packedRBG());
            }
        }
        return out;
    }
}
