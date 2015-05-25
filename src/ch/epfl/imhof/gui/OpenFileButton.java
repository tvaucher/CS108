package ch.epfl.imhof.gui;

import javax.swing.JTextField;

/**
 * A file selection button for opening an existing file.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
@SuppressWarnings("serial")
public final class OpenFileButton extends FileButton {
    /**
     * Constructs a new file selection button.
     * 
     * @param text
     *            The text that the button will contain.
     * @param textField
     *            The JTextField that the path of the selected file will be put
     *            into.
     */
    public OpenFileButton(String text, JTextField textField) {
        super(text, textField);
    }

    /**
     * Constructs a new file selection button with the standard text of
     * "Choose File..."
     * 
     * @param textField
     *            The JTextField that the path of the selected file will be put
     *            into.
     */
    public OpenFileButton(JTextField textField) {
        this("Choose File...", textField);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.epfl.imhof.gui.FileButton#showDialog()
     */
    @Override
    protected int showDialog() {
        return fc.showOpenDialog(null);
    }
}
