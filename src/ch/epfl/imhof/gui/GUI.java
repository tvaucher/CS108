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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

public class GUI {
    private static final int WIDTH = 400, HEIGHT = 100, TEXT_WIDTH = 200;
    
    private static void createUI() {
        JFrame frame = new JFrame("The Imhof Project");
        
        // COMPONENTS
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
        JButton osmButton = new JButton("Choose File...");
        FileFilter osmFilter = new FileNameExtensionFilter("Open Street Maps (*.osm, *.osm.gz)", "osm", "gz");
        osmButton.addActionListener(e -> fileChooser(frame, osmField, osmFilter));
        
        JButton hgtButton = new JButton("Choose File...");
        FileFilter hgtFilter = new FileNameExtensionFilter("HGT (*.hgt)", "hgt");
        hgtButton.addActionListener(e -> fileChooser(frame, hgtField, hgtFilter));
        
        JButton outButton = new JButton("Save As...");
        FileFilter outFilter = new FileNameExtensionFilter("PNG (*.png)", "png");
        outButton.addActionListener(e -> saveAsChooser(frame, outField, outFilter));
        
        JButton create = new JButton("Create Map");
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));
        

        // CONTENT PANEL
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Default values
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 1, 1, 1);
        
        // Add the logo to the top right.
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        content.add(logoLabel, gbc);
        
        // Add input lines:
        gbc.anchor = GridBagConstraints.WEST;
        
        // OSM file:
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(osmLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(osmField), gbc);
        gbc.gridx = 2;
        content.add(osmButton, gbc);

        // HGT file:
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(hgtLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(hgtField), gbc);
        gbc.gridx = 2;
        content.add(hgtButton, gbc);
        
        // BL longitude
        gbc.gridx = 0;
        gbc.gridy = 3;
        content.add(blLongLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(blLongField), gbc);
        
        // BL latitude
        gbc.gridx = 0;
        gbc.gridy = 4;
        content.add(blLatLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(blLatField), gbc);
        
        // TR longitude
        gbc.gridx = 0;
        gbc.gridy = 5;
        content.add(trLongLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(trLongField), gbc);
        
        // TR latitude
        gbc.gridx = 0;
        gbc.gridy = 6;
        content.add(trLatLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(trLatField), gbc);
        
        // Resolution
        gbc.gridx = 0;
        gbc.gridy = 7;
        content.add(dpiLabel, gbc);
        gbc.gridx = 1;
        content.add(dpiField, gbc);
        
        // Output name
        gbc.gridx = 0;
        gbc.gridy = 8;
        content.add(outLabel, gbc);
        gbc.gridx = 1;
        content.add(formatted(outField), gbc);
        gbc.gridx = 2;
        content.add(outButton, gbc);
              
        // Add buttons to bottom right
        JPanel actionButtons = new JPanel();
        actionButtons.add(create);
        actionButtons.add(exit);
        gbc.gridx = 4;
        gbc.gridy = 10;
        content.add(actionButtons, gbc);
        
        
        // Style the content
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // FRAME
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);
    }
    
    private static void fileChooser(JFrame frame, JTextField text, FileFilter filter) {
        JFileChooser fc = new JFileChooser();
        File start = new File(System.getProperty("user.dir"));
        try {
            String currentPath = text.getText();
            System.out.println(currentPath);
            if (!currentPath.equals("")) {
                start = new File(new File(currentPath).getParent());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            fc.setCurrentDirectory(start); // start in the current working directory
            fc.setFileFilter(filter);
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                text.setText(fc.getSelectedFile().getPath());
            }
        }
    }
    
    private static void saveAsChooser(JFrame frame, JTextField text, FileFilter filter) {
        JFileChooser fc = new JFileChooser();
        File start = new File(System.getProperty("user.dir"));
        try {
            String currentPath = text.getText();
            if (!currentPath.equals("")) {
                start = new File(new File(currentPath).getParent());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            fc.setCurrentDirectory(start); // start in the current working directory
            fc.setFileFilter(filter);
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String file = fc.getSelectedFile().getPath();
                if (!file.endsWith(".png")) {
                    file += ".png";
                }
                text.setText(file);
            }
        }
    }
    
    private static JTextField formatted (JTextField tf) {
        tf.setPreferredSize(new Dimension(TEXT_WIDTH, tf.getPreferredSize().height));
        return tf;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Still prettier than the default IMO
            } catch (Exception e) {
                e.printStackTrace();
            }
            createUI();
        });
    }
}
