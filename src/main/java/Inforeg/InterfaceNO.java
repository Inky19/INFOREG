package Inforeg;

/*=============================================
Classe InterfaceNO
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 24/03/2022
=============================================*/
import Inforeg.Draw.Draw;
import static Inforeg.Graph.GraphFunction.connected;
import static Inforeg.Interface.TRAITEMENT_MODE;
import static Inforeg.Interface.mode;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class InterfaceNO extends Interface {

    public InterfaceNO(Draw d) {
        super(d);
    }

    /**
     * Actions
     */
    
    @Override
    public void connexe(){
        mode = TRAITEMENT_MODE;
        d.getG().updateVariable();
        if (connected(d.getG())) {
            JOptionPane.showMessageDialog(d, "Le graphe est connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(d, "Le graphe n'est pas connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public final AbstractAction ExportGraphNO = new AbstractAction() {
        {
            putValue(Action.NAME, "Export Matrice d'Adjacence");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.SHORT_DESCRIPTION, "Affiche la matrice d'adjacence du graphe (CTRL+A)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent ea) {
            d.exportGraphe();
            JOptionPane.showMessageDialog(d, "La matrice d'adjacence du graphe non orienté est :\n\n" + d.getG().afficher(), "Matrice d'adjacence", JOptionPane.INFORMATION_MESSAGE);
        }
    };
    
    @Override
    public void addMenuBar() {
        JMenu traitMenu = new JMenu("Traitement");
        menuBar.add(traitMenu);
        exporter.add(new JMenuItem(ExportGraphNO));
    }

}
