package ch.epfl.imhof.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.epfl.imhof.utility.MapMaker;

public class GraphicalUserInterface {
    private static final int WIDTH = 400, HEIGHT = 100, TEXT_WIDTH = 200;

    public static void createUI() {
        JFrame frame = new JFrame("The Imhof Project");
        JPanel content = new JPanel(new GridBagLayout());
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        JPanel tab1 = new JPanel(new GridBagLayout());
        JPanel tab2 = new JPanel(new GridBagLayout());
        JPanel actionButtons = new JPanel();

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
        create.addActionListener(e -> {
            if (tabs.getSelectedIndex() == 0) {
                String[] args = { osmField.getText(), hgtField.getText(),
                        blLongField.getText(), blLatField.getText(),
                        trLongField.getText(), trLatField.getText(),
                        String.valueOf(dpiField.getValue()), outField.getText() };
                MapMaker m = new MapMaker(args);
            } else {
                String[] args = { autoBlLongField.getText(),
                        autoBlLatField.getText(), autoTrLongField.getText(),
                        autoTrLatField.getText(),
                        String.valueOf(autoDpiField.getValue()),
                        autoOutField.getText() };
                MapMaker m = new MapMaker(args);
            }
        });
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));

        // //////////////////
        // //// PANELS //////
        // //////////////////
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
        actionButtons.add(create);
        actionButtons.add(exit);
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(actionButtons, gbc);

        // ////////////////////////
        // ///// TAB 1: MANUAL ////
        // ////////////////////////
        // Here, we put all the components into a content field.
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

    private static JTextField formatted(JTextField tf) {
        tf.setPreferredSize(new Dimension(TEXT_WIDTH,
                tf.getPreferredSize().height));
        return tf;
    }

}