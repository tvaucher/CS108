package ch.epfl.imhof.gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * The GUIConsoleOutputStream is an OutputStream that prints in a JTextArea in
 * the GUI. In this projet, we need it in order to use System.out.println and
 * have that output directly to the GUI's console.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class GUIConsoleOutputStream extends OutputStream {
    private JTextArea textArea;

    /**
     * Constructs a new GUIConsoleOutputStream.
     * 
     * @param textArea
     *            The JTextArea that the streams outputs to.
     */
    public GUIConsoleOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
    }
}
