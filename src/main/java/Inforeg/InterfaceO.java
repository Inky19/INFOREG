package Inforeg;

/*=============================================
Classe InterfaceO
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 08/03/2022
=============================================*/
import Inforeg.Graph.GraphO;
import Inforeg.Draw.Draw;
import static Inforeg.Graph.GraphFunction.connected;
import static Inforeg.Interface.TRAITEMENT_MODE;
import static Inforeg.Interface.activeTraitement;
import static Inforeg.Interface.mode;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
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
    public final AbstractAction Dijkstra = new AbstractAction() {
        {
            putValue(Action.NAME, "Dijkstra");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(Action.SHORT_DESCRIPTION, "Applique l'algorithme de Dijkstra pour trouver \n"
                    + "le plus court chemin entre 2 sommets \n"
                    + "-Cliquez sur le nœud de départ \n"
                    + "-Cliquez sur le nœud d'arrivée \n"
                    + "(CTRL+D)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            d.reinit();
            d.repaint();
            mode = TRAITEMENT_MODE;
            activeTraitement = Interface.DIJKSTRA_TRAITEMENT;
            d.reinit();
            d.repaint();
            JOptionPane.showMessageDialog(null, "Sélectionnez un sommet de départ et un sommet d'arrivée pour calculer le plus court chemin entre les deux s'il existe.",
                    "Dijkstra - PCC", JOptionPane.INFORMATION_MESSAGE);

        }
    };

    public final AbstractAction FordFulkerson = new AbstractAction() {
        {
            putValue(Action.NAME, "Ford Fulkerson");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
            putValue(Action.SHORT_DESCRIPTION, "Applique l'algorithme de Ford Fulkerson pour calculer \n"
                    + "le flot maximal entre 2 sommets \n"
                    + "-Cliquez sur le nœud source \n"
                    + "-Cliquez sur le nœud de sortie \n"
                    + "(CTRL+F)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent ea) {
            mode = TRAITEMENT_MODE;
            activeTraitement = Interface.FORD_FULKERSON_TRAITEMENT;
            d.reinit();
            d.repaint();
            JOptionPane.showMessageDialog(null, "Sélectionnez un sommet source et un sommet cible pour calculer le flot maximal entre les deux.",
                    "Ford-Fulkerson - Flot maximal", JOptionPane.INFORMATION_MESSAGE);
        }
    };

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

        Dijkstra.setEnabled(true);
        FordFulkerson.setEnabled(true);

            
        toolBarButtons.setAlignmentX(FlowLayout.CENTER);
        toolBarButtons.setFloatable(false);
        toolBarButtons.setBorderPainted(true);

    }

    @Override
    public void addToolBar() {
        toolBarButtons.add(Dijkstra);
        toolBarButtons.addSeparator();
        toolBarButtons.add(FordFulkerson);
        toolBarButtons.addSeparator();
    }

    @Override
    public void addMenuBar() {
        JMenu traitMenu = new JMenu("Traitement");
        traitMenu.add(Dijkstra);
        traitMenu.add(FordFulkerson);
        menuBar.add(traitMenu);
        exporter.add(new JMenuItem(ExportGraphO));
    }


}
