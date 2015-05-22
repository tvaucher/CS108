package ch.epfl.imhof.gui;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class OpenFileButton extends FileButton {
    public OpenFileButton(String text, JTextField textField) {
        super(text, textField);
    }
    
    public OpenFileButton(JTextField textField) {
        this("Choose File...", textField);
    }

    @Override
    protected int showDialog() {
        return fc.showOpenDialog(null);
    }
}
