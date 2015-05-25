package ch.epfl.imhof.gui;

import javax.swing.JTextField;

/**
 * A file selection button for saving to a new file.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
@SuppressWarnings("serial")
public final class SaveFileButton extends FileButton {
    /**
     * Constructs a new file selection button.
     * 
     * @param text
     *            The text that the button will contain.
     * @param textField
     *            The JTextField that the path of the selected file will be put
     *            into.
     */
    public SaveFileButton(String text, JTextField textField) {
        super(text, textField);
    }

    /**
     * Constructs a new file selection button with the standard text of
     * "Save File..."
     * 
     * @param textField
     *            The JTextField that the path of the selected file will be put
     *            into.
     */
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
     *            The extension that should be added to the fileName if it
     *            hasn't been specified by the user (e.g. "png", no leading
     *            dot.)
     */
    public void setDefaultExtension(String defaultExtension) {
        this.defaultExtension = defaultExtension;
    }

    /* (non-Javadoc)
     * @see ch.epfl.imhof.gui.FileButton#showDialog()
     */
    @Override
    protected int showDialog() {
        return fc.showSaveDialog(null);
    }

}
