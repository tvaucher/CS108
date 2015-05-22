package ch.epfl.imhof.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

/**
 * The GUI class is the View of our MVC architecture. It shows a window that
 * makes it easier to input data.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public class GUI {
    private static final int WIDTH = 400, HEIGHT = 100, TEXT_WIDTH = 200;
    private List<JButton> fileOpenButtons = new ArrayList<JButton>();

    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
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

    public List<JButton> getFileOpenButtons() { // Temp.
        return fileOpenButtons; // Not immutable.
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
            System.out.println("Errror reading epfl_logo.jpg");
            e.printStackTrace();
        }

        // Labels:
        JLabel osmLabel = new JLabel("OSM File");
        JLabel hgtLabel = new JLabel("HGT File");
        JLabel blLongLabel = new JLabel("Bottom Left Longitude");
        JLabel blLatLabel = new JLabel("Bottom Left Latitude");
        JLabel trLongLabel = new JLabel("Top Right Longitude");
        JLabel trLatLabel = new JLabel("Top Right Latitude");
        JLabel dpiLabel = new JLabel("Resolution");
        JLabel outLabel = new JLabel("Output File");

        // Text Fields:
        JTextField osmField = new JTextField();
        JTextField hgtField = new JTextField();
        MaskFormatter longMask = null, latMask = null;
        try {
            longMask = new MaskFormatter("#.####");
            latMask = new MaskFormatter("##.####");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JFormattedTextField blLongField = new JFormattedTextField(longMask);
        JFormattedTextField blLatField = new JFormattedTextField(latMask);
        JFormattedTextField trLongField = new JFormattedTextField(longMask);
        JFormattedTextField trLatField = new JFormattedTextField(latMask);
        SpinnerNumberModel model = new SpinnerNumberModel(300, 10, 2000, 10);
        JSpinner dpiField = new JSpinner(model);
        JFormattedTextField outField = new JFormattedTextField();

        // Buttons:
        OpenFileButton osmButton = new OpenFileButton(osmField);
        osmButton.setFileFilter(new FileNameExtensionFilter(
                "Open Street Maps (*.osm, *.osm.gz)", "osm", "gz"));

        OpenFileButton hgtButton = new OpenFileButton("Choose File...", hgtField);
        hgtButton.setFileFilter(new FileNameExtensionFilter("HGT (*.hgt)",
                "hgt"));

        SaveFileButton outButton = new SaveFileButton(outField);
        outButton.setFileFilter(new FileNameExtensionFilter("PNG (*.png)",
                "png"));
        outButton.setDefaultExtension("png");

        JButton create = new JButton("Create Map");
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));

        // ////////////////////////
        // ///// CONTENT PANEL ////
        // ////////////////////////
        // Here, we put all the components into a content field.
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Default values
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 1, 1, 1);
        // Add the logo to the top right.
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        content.add(logoLabel, gbc);

        // //////////////////
        // // INPUTS PANEL //
        // //////////////////
        // We're putting the inputs into a separate panel to make the layout
        // easier to change.
        JPanel inputs = new JPanel(new GridBagLayout());
        GridBagConstraints inc = new GridBagConstraints();
        inc.fill = GridBagConstraints.HORIZONTAL;
        inc.insets = new Insets(1, 1, 1, 1);

        // Add input lines:
        inc.anchor = GridBagConstraints.WEST;

        // OSM file:
        inc.gridx = 0;
        inc.gridy = 0;
        inputs.add(osmLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(osmField), inc);
        inc.gridx = 2;
        inputs.add(osmButton, inc);

        // HGT file:
        inc.gridx = 0;
        inc.gridy = 1;
        inputs.add(hgtLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(hgtField), inc);
        inc.gridx = 2;
        inputs.add(hgtButton, inc);

        // BL longitude
        inc.gridx = 0;
        inc.gridy = 2;
        inputs.add(blLongLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(blLongField), inc);

        // BL latitude
        inc.gridx = 0;
        inc.gridy = 3;
        inputs.add(blLatLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(blLatField), inc);

        // TR longitude
        inc.gridx = 0;
        inc.gridy = 4;
        inputs.add(trLongLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(trLongField), inc);

        // TR latitude
        inc.gridx = 0;
        inc.gridy = 5;
        inputs.add(trLatLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(trLatField), inc);

        // Resolution
        inc.gridx = 0;
        inc.gridy = 6;
        inputs.add(dpiLabel, inc);
        inc.gridx = 1;
        inputs.add(dpiField, inc);

        // Output name
        inc.gridx = 0;
        inc.gridy = 7;
        inputs.add(outLabel, inc);
        inc.gridx = 1;
        inputs.add(formatted(outField), inc);
        inc.gridx = 2;
        inputs.add(outButton, inc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(inputs, gbc);
        // Add buttons to bottom right
        JPanel actionButtons = new JPanel();
        actionButtons.add(create);
        actionButtons.add(exit);
        gbc.gridx = 1;
        gbc.gridy = 2;
        content.add(actionButtons, gbc);

        // Style the content
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ///////////////
        // //// FRAME ////
        // ///////////////
        // Finally, we can create the frame.
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
}
