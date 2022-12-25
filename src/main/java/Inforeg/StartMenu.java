package Inforeg;

/**
 * Lancement du logiciel
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */

import Inforeg.Draw.Draw;
import Inforeg.UI.GraphTypeWindow;
import java.awt.Taskbar;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * Fenêtre de démarrage de l'application
 *
 * @author remir
 */
public class StartMenu {

    public static void main(String[] args) throws IOException {

        try {
            //set icon for mac os (and other systems which do support this method)
            final Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(AssetLoader.appIco.getImage());
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }
        GraphTypeWindow window = new GraphTypeWindow();
        Draw d = window.chooseGraph();
        if (d != null) {
            SwingUtilities.invokeLater(new Interface(d)::createAndShowGui);
        }
    }

}
