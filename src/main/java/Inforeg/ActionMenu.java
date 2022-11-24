package Inforeg;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
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

    public static void colorNode(Draw d, Node n, Color c) {
        n.setColor(c);
        d.repaint();
    }

    public static void colorArc(Draw d, Arc a, Color c) {
        a.setColor(c);
        d.repaint();
    }

    public static void deleteArc(Draw d, Arc a) {
        d.getG().removeLine(a);
        d.repaint();
        d.getTransitions().createLog(History.REMOVE_ARC, a);
    }

    public static void setPoids(Draw d, Arc a) {
        String text = JOptionPane.showInputDialog("Entrer le nouveau poids de l'Arc (seuls les entiers seront acceptés):");
        try {
            int pds = Integer.parseInt(text);
            Arc line = a;
            int currentPds = line.getPoids();
            line.setPoids(pds);
            d.repaint();
            // On ajoute l'action à la pile
            d.getTransitions().createLog(History.LABEL_ARC, line, Integer.toString(currentPds), text);
        } catch (Exception e) {
            System.out.println("Pas un entier !");
        }
    }
}
