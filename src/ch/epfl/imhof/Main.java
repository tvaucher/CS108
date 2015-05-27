package ch.epfl.imhof;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ch.epfl.imhof.gui.GraphicalUserInterface;

/**
 * The Main class is what ties everything else together. It's the main
 * orchestrator of the rest of the code, which manages inputs and outputs.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
                new GraphicalUserInterface();
            } catch (Exception e) {
                System.out.println("ERROR : " + e.getMessage());
            }
            
        });
    }
}
