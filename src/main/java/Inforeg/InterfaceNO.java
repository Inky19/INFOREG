package Inforeg;

/*=============================================
Classe InterfaceNO
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 24/03/2022
=============================================*/
import Inforeg.Algo.Coloration;
import Inforeg.Graph.GraphNO;
import Inforeg.Draw.Draw;
import Inforeg.Algo.PrimMST;
import Inforeg.Algo.KruskalMST;
import Inforeg.Algo.Connexe;
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

public class InterfaceNO extends Interface implements Connexe {

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
            if (mode == Interface.TRAITEMENT_MODE) {
                activeTraitement = Interface.PRIM_TRAITEMENT;
                d.reinit();
                d.repaint();
                (new PrimMST()).primMST(d);
            }
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
            if (mode == Interface.TRAITEMENT_MODE) {
                activeTraitement = Interface.KRUSKAL_TRAITEMENT;
                d.reinit();
                d.repaint();
                (new KruskalMST()).kruskalMST(d);
            }
        }

    };

    public final AbstractAction ConnexiteNO = new AbstractAction() {
        {
            putValue(Action.NAME, "Connexité");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(Action.SHORT_DESCRIPTION, "Vérifie si le graphe est connexe \n"
                    + "(CTRL+L)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent ea) {
            if (mode == Interface.TRAITEMENT_MODE) {
                if (connexe((GraphNO) d.getG())) {
                    JOptionPane.showMessageDialog(d, "Le graphe est connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(d, "Le graphe n'est pas connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };

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
            if (mode == Interface.TRAITEMENT_MODE) {
                activeTraitement = Interface.COLORATION_TRAITEMENT;
                d.reinit();
                d.repaint();
                (new Coloration(d)).colorationGlouton();
            }
        }
    };    
    
    
    
    
    @Override
    public void initToolBar() {
        super.initToolBar();



        

        JLabel l1 = new JLabel("  Édition");
        JLabel l2 = new JLabel("  Mode");
        //On crée un ButtonGroup pour que seul l'un puisse être activé à la fois 
        ButtonGroup groupMode = new ButtonGroup();
        groupMode.add(edition);
        groupMode.add(traitement);
        groupMode.add(deplacement);
        ButtonGroup groupAction = new ButtonGroup();
        groupAction.add(select);
        groupAction.add(noeud);
        groupAction.add(arc);
        groupAction.add(label);
        //On ajoute les éléments au JPanel
        toolBarButtons.add(l2);
        toolBarButtons.addSeparator();
        toolBarButtons.add(deplacement);
        toolBarButtons.add(edition);
        toolBarButtons.add(traitement);
        toolBarButtons.addSeparator();

        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        JLabel l = new JLabel("  Traitement");
        toolBarButtons.add(l);
        toolBarButtons.addSeparator();

        ActionListener modeGroupListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == edition) {
                    mode = EDITION_MODE;
                    select.setEnabled(true);
                    noeud.setEnabled(true);
                    arc.setEnabled(true);
                    label.setEnabled(true);
                    Prim.setEnabled(false);
                    Kruskal.setEnabled(false);
                    ConnexiteNO.setEnabled(false);
                    Coloration.setEnabled(false);
                } else if (ae.getSource() == traitement) {
                    d.reinit();
                    d.repaint();
                    mode = TRAITEMENT_MODE;
                    select.setEnabled(false);
                    noeud.setEnabled(false);
                    arc.setEnabled(false);
                    label.setEnabled(false);
                    Prim.setEnabled(true);
                    Kruskal.setEnabled(true);
                    ConnexiteNO.setEnabled(true);
                    Coloration.setEnabled(true);
                    d.exportGraphe();
                } else if (ae.getSource() == deplacement) {
                    mode = DEPLACEMENT_MODE;
                    select.setEnabled(false);
                    noeud.setEnabled(false);
                    arc.setEnabled(false);
                    label.setEnabled(false);
                    Prim.setEnabled(false);
                    Kruskal.setEnabled(false);
                    ConnexiteNO.setEnabled(false);
                    Coloration.setEnabled(false);
                }
            }
        };

        edition.addActionListener(modeGroupListener);
        //edition.setSelected(true);//edition activé au démarrage
        traitement.addActionListener(modeGroupListener);
        deplacement.addActionListener(modeGroupListener);
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
        toolBarButtons.add(ConnexiteNO);
        toolBarButtons.addSeparator();
        toolBarButtons.add(Coloration);
    }

    @Override
    public void addMenuBar() {
        JMenu traitMenu = new JMenu("Traitement");
        traitMenu.add(Prim);
        traitMenu.add(Kruskal);
        traitMenu.add(ConnexiteNO);
        traitMenu.add(Coloration);
        menuBar.add(traitMenu);
        exporter.add(new JMenuItem(ExportGraphNO));
    }

}
