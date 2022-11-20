package Inforeg;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;
import Inforeg.Save.saveManager;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author inky19
 */
public abstract class ActionMenu {

    /**
     * Renomme un nœud.
     *
     * @param d Zone de dessin concernée
     * @param n Nœud à renommer
     */
    public static void renameNode(Draw d, Node n) {
        boolean validName = false;
        String lbl = "";
        while (!validName) {
            lbl = JOptionPane.showInputDialog("Entrer label :");
            if (!lbl.isEmpty() && lbl.contains(saveManager.SEP)) {
                JOptionPane.showMessageDialog(null, "Un label ne peut pas comporter \"" + saveManager.SEP + "\"\n(Motif réservé pour la sauvegarde)", "Nom invalide", JOptionPane.WARNING_MESSAGE);
            } else {
                validName = true;
            }
        }
        if (lbl != null) {
            String currentLbl = n.getLabel();
            n.setLabel(lbl);
            d.repaint();
            d.getTransitions().createLog(History.LABEL_NODE, n, currentLbl, lbl);
        }
    }

    /**
     * Supprime un nœud.
     *
     * @param d Zone de dessin concernée
     * @param n Nœud à supprimer
     */
    public static void deleteNode(Draw d, Node n) {
        d.saveState(false);
        d.getG().removeNode(n);
        d.repaint();
        d.getTransitions().createLog(History.REMOVE_NODE, n);
    }

    static void colorNode(Draw d, Node n, Color c) {
        n.setColor(c);
        d.repaint();
    }
}
