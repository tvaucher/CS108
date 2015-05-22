package ch.epfl.imhof.gui;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public abstract class FileButton extends JButton {
    protected String defaultExtension = "";
    // Empty per default. This is a workaround for the fact that it isn't
    // possible to get the extensions of the selected FileFilter.

    protected JFileChooser fc = new JFileChooser();
    private File start = new File(System.getProperty("user.dir"));
    private JTextField textField;

    public FileButton(String text, JTextField textField) {
        super(text);
        this.textField = textField;
        activate();
    }

    public void setFileFilter(FileFilter filter) {
        fc.setFileFilter(filter);
    }

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

    abstract protected int showDialog();
}
