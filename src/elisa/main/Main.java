package elisa.main;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author Adam Goscicki
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
}
