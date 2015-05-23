package ch.epfl.imhof.gui;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public final class SaveFileButton extends FileButton {
    public SaveFileButton(String text, JTextField textField) {
        super(text, textField);
    }

    public SaveFileButton(JTextField textField) {
        this("Save File...", textField);
    }

    /**
     * Add a file extension if it isn't specified by the user.
     * <p>
     * I would've loved to get the file extension from the filter, but the
     * FileFilter doesn't offer a way to get the allowed extension of the
     * currently selected file.
     * 
     * @param defaultExtension
     *            The extension that should be added to the fileName if it hasn't been specified
     *            by the user (e.g. "png", no leading dot.)
     */
    public void setDefaultExtension(String defaultExtension) {
        this.defaultExtension = defaultExtension;
    }

    @Override
    protected int showDialog() {
        return fc.showSaveDialog(null);
    }

}
