package ch.epfl.imhof.gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public final class GUIConsoleOutputStream extends OutputStream {
    private JTextArea textArea;

    public GUIConsoleOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
    }
}
