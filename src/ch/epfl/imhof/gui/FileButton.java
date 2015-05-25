package ch.epfl.imhof.gui;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 * A FileButton is a Button that is inherently tied to a JTextArea. When you
 * click on it, it opens a JFileChooser in a new window. When you've selected a
 * file from the file chooser, it will put the path of said file into the
 * JTextField.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
@SuppressWarnings("serial")
public abstract class FileButton extends JButton {
    protected String defaultExtension = "";
    // Empty per default. This is a workaround for the fact that it isn't
    // possible to get the extensions of the selected FileFilter.

    protected JFileChooser fc = new JFileChooser();
    private File start = new File(System.getProperty("user.dir"));
    private JTextField textField;

    /**
     * Constructs a new FileButton.
     * 
     * @param text
     *            The text that will be displayed on the button.
     * @param textField
     *            The JTextField that will hold the path of the selected file.
     */
    public FileButton(String text, JTextField textField) {
        super(text);
        this.textField = textField;
        activate();
    }

    /**
     * Sets a FileFilter to the file chooser, so that it only is possible to
     * view and select certain file types from it.
     * 
     * @param filter
     *            A FileFilter object; a FileNameExtensionFilter works very
     *            well, for instance.
     */
    public void setFileFilter(FileFilter filter) {
        fc.setFileFilter(filter);
    }

    /**
     * Defines the standard behavior of the button.
     */
    private void activate() {
        addActionListener(e -> {
            try {
                String currentPath = textField.getText();
                if (!currentPath.equals(""))
                    start = new File(new File(currentPath).getParent());
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } finally {
                fc.setCurrentDirectory(start);
                if (showDialog() == JFileChooser.APPROVE_OPTION) {
                    String filePath = fc.getSelectedFile().getPath();
                    if (!filePath.endsWith(defaultExtension))
                        filePath += "." + defaultExtension;
                    textField.setText(filePath);
                }
            }
        });
    }

    /**
     * This is an abstract class, and what type of dialog file to show may vary
     * with the type of button. Therefore, each child of this class is to define
     * what filepicking dialog they want to show.
     * 
     * @return the int of the selection. See {@link JFileChooser}'s OPTION
     *         fields.
     */
    abstract protected int showDialog();
}
