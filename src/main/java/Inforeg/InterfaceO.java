package Inforeg;

/*=============================================
Classe InterfaceO
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 08/03/2022
=============================================*/
import Inforeg.Draw.Draw;
import static Inforeg.Graph.GraphFunction.connected;
import static Inforeg.Interface.TRAITEMENT_MODE;
import static Inforeg.Interface.mode;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class InterfaceO extends Interface {

    public InterfaceO(Draw d) {
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
            JOptionPane.showMessageDialog(d, "Le graphe est fortement connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(d, "Le graphe n'est pas fortement connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
   
    public final AbstractAction ExportGraphO = new AbstractAction() {
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
            JOptionPane.showMessageDialog(d, "La matrice d'adjacence du graphe orienté est :\n\n" + d.getG().afficher(), "Matrice d'adjacence", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    /**
     * JPanel pour les boutons 
     *
     */
    @Override
    public void initToolBar() {
        super.initToolBar();

        //On crée un ButtonGroup pour que seul l'un puisse être activé à la fois 
        //On ajoute les éléments au JPanel

        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
            
        toolBarButtons.setAlignmentX(FlowLayout.CENTER);
        toolBarButtons.setFloatable(false);
        toolBarButtons.setBorderPainted(true);

    }

    @Override
    public void addMenuBar() {
        JMenu traitMenu = new JMenu("Traitement");
        menuBar.add(traitMenu);
        exporter.add(new JMenuItem(ExportGraphO));
    }


}
