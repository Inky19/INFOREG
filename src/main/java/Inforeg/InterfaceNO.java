package Inforeg;

/*=============================================
Classe InterfaceNO
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 24/03/2022
=============================================*/
import Inforeg.Algo.Coloration;
import Inforeg.Draw.Draw;
import Inforeg.Algo.PrimMST;
import Inforeg.Algo.KruskalMST;
import Inforeg.Graph.GraphFunction.*;
import static Inforeg.Graph.GraphFunction.connected;
import static Inforeg.Interface.TRAITEMENT_MODE;
import static Inforeg.Interface.mode;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InterfaceNO extends Interface {

    public InterfaceNO(Draw d) {
        super(d);
    }

    /**
     * Actions
     */
    public final AbstractAction Prim = new AbstractAction() {
        {
            putValue(Action.NAME, "Prim");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(Action.SHORT_DESCRIPTION, "Applique l'algorithme de Prim pour trouver \n"
                    + "l'arbre couvrant minimal du graphe (CTRL+P)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mode = Interface.TRAITEMENT_MODE;
            d.exportGraphe();
            activeTraitement = Interface.PRIM_TRAITEMENT;
            d.reinit();
            d.repaint();
            (new PrimMST()).primMST(d);
            
        }
    };

    public final AbstractAction Kruskal = new AbstractAction() {
        {
            putValue(Action.NAME, "Kruskal");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_K);
            putValue(Action.SHORT_DESCRIPTION, "Applique l'algorithme de Kruskal pour trouver \n"
                    + "l'arbre couvrant minimal du graphe (CTRL+K)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            d.exportGraphe();
            mode = Interface.TRAITEMENT_MODE;
            activeTraitement = Interface.KRUSKAL_TRAITEMENT;
            d.reinit();
            d.repaint();
            (new KruskalMST()).kruskalMST(d);
            
        }

    };
    
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

    public final AbstractAction Coloration = new AbstractAction() {
        {
            putValue(Action.NAME, "Coloration");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(Action.SHORT_DESCRIPTION, "Applique l'algorithme de coloration gloutonne de graphe");

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            d.exportGraphe();
            mode = Interface.TRAITEMENT_MODE;
            activeTraitement = Interface.COLORATION_TRAITEMENT;
            d.reinit();
            d.repaint();
            (new Coloration(d)).colorationGlouton();
            
        }
    };    
    
    
    
    
    @Override
    public void initToolBar() {
        super.initToolBar();

        JLabel l2 = new JLabel("  Mode");
        //On crée un ButtonGroup pour que seul l'un puisse être activé à la fois 

        //On ajoute les éléments au JPanel

        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        Prim.setEnabled(true);
        Kruskal.setEnabled(true);
        Coloration.setEnabled(true);



        //toolBarButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBarButtons.setAlignmentX(FlowLayout.CENTER);
        toolBarButtons.setFloatable(false);
        toolBarButtons.setBorderPainted(true);

    }

    @Override
    public void addToolBar() {
        toolBarButtons.add(Prim);
        toolBarButtons.addSeparator();
        toolBarButtons.add(Kruskal);
        toolBarButtons.addSeparator();
        toolBarButtons.add(Coloration);
    }

    @Override
    public void addMenuBar() {
        JMenu traitMenu = new JMenu("Traitement");
        traitMenu.add(Prim);
        traitMenu.add(Kruskal);
        traitMenu.add(Coloration);
        menuBar.add(traitMenu);
        exporter.add(new JMenuItem(ExportGraphNO));
    }

}
