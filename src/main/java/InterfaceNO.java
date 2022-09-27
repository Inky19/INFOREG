/*=============================================
Classe InterfaceNO
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 24/03/2022
=============================================*/

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


public class InterfaceNO extends Interface implements Connexe{

    public InterfaceNO(Draw d){
        super(d);
    }
    
    /** Actions */

    public final AbstractAction Prim = new AbstractAction(){
        {
            putValue(Action.NAME,"Prim");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(Action.SHORT_DESCRIPTION,"Applique l'algorithme de Prim pour trouver \n"
                                            + "l'arbre couvrant minimal du graphe (CTRL+P)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if (mode==Interface.TRAITEMENT_MODE){
                activeTraitement = Interface.PRIM_TRAITEMENT;
                d.reinit();
                d.repaint();
                (new PrimMST()).primMST(d);
            }
        }
    };

    public final AbstractAction Kruskal = new AbstractAction(){
        {
            putValue(Action.NAME,"Kruskal");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_K);
            putValue(Action.SHORT_DESCRIPTION,"Applique l'algorithme de Kruskal pour trouver \n"
                                            + "l'arbre couvrant minimal du graphe (CTRL+K)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_K,KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if (mode==Interface.TRAITEMENT_MODE){
                activeTraitement = Interface.KRUSKAL_TRAITEMENT;
                d.reinit();
                d.repaint();
                (new KruskalMST()).kruskalMST(d);
            }
        }
        
        
    };

    public final AbstractAction ConnexiteNO = new AbstractAction() {
        {
            putValue(Action.NAME,"Connexité");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(Action.SHORT_DESCRIPTION,"Vérifie si le graphe est connexe \n"
                                            + "(CTRL+L)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_DOWN_MASK));
        }
        @Override
            public void actionPerformed(ActionEvent ea) {
                if (mode==Interface.TRAITEMENT_MODE){
                    if (connexe((GNonOriente) d.getG())){
                        JOptionPane.showMessageDialog(d, "Le graphe est connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(d, "Le graphe n'est pas connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
    };

    public final AbstractAction ExportGraphNO = new AbstractAction() {
        {
            putValue(Action.NAME,"Export Matrice d'Adjacence");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.SHORT_DESCRIPTION,"Affiche la matrice d'adjacence du graphe (CTRL+A)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_DOWN_MASK));
        }
        @Override
            public void actionPerformed(ActionEvent ea) {
                d.exportGraphe();
                JOptionPane.showMessageDialog(d, "La matrice d'adjacence du graphe non orienté est :\n\n" + d.getG().afficher(), "Matrice d'adjacence", JOptionPane.INFORMATION_MESSAGE);
            }
    };
    
    
    @Override
    public void initToolBar(){
                //paneButtons = new JPanel();
        toolBarButtons = new JToolBar(null, JToolBar.VERTICAL);
        //Panel le long de l'axe Y
        toolBarButtons.setLayout(new BoxLayout(toolBarButtons, BoxLayout.Y_AXIS));
        
        //intialise les boutons 
        select = new JRadioButton("Select");
        noeud = new JRadioButton("Noeud");
        arc = new JRadioButton("Arc"); 
        label = new JRadioButton("Label");
        edition = new JRadioButton("Édition");
        traitement = new JRadioButton("Traitement");
        
        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        
        JButton colorButton = new JButton("Color");
        colorButton.setMnemonic('o');
        colorButton.setToolTipText("Choose a Color");
        ActionListener colorListener;
        colorListener = (ActionEvent arg0) -> {
            Color c = JColorChooser.showDialog(frame, "Choose a color", color);
            if (c!=null) {
                for (int i=1;i<colorSample.getHeight();i++){
                    for (int j=1;j<colorSample.getHeight();j++){
                        colorSample.setRGB(i,j,c.getRGB());
                    }
                }
                setColor(c);
                d.setCurrentColor(c);
            }
        };
        colorButton.addActionListener(colorListener);
        colorButton.setIcon(new ImageIcon(colorSample));
        toolBarButtons.add(colorButton);
        setColor(this.color);
        
        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        
        //Taille
        final SpinnerNumberModel spinnerNumTaille = new SpinnerNumberModel(20,1,100,1);
        JSpinner spinnerTaille = new JSpinner(spinnerNumTaille);
        ChangeListener listenerTaille = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                Object o = spinnerNumTaille.getValue();
                Integer i = (Integer)o; 
                taille = i;
                d.tailleCirc();
                } 
        };
        spinnerTaille.addChangeListener(listenerTaille);
        spinnerTaille.setMaximumSize(spinnerTaille.getPreferredSize());
        JLabel spinnerTailleLabel = new JLabel(" Taille Noeuds");
        spinnerTailleLabel.setLabelFor(spinnerTaille);
        toolBarButtons.add(spinnerTailleLabel);
        toolBarButtons.add(spinnerTaille);
        spinnerTaille.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        
        //Epaisseur
        final SpinnerNumberModel spinnerNumEpaisseur = new SpinnerNumberModel(20,1,100,1);
        JSpinner spinnerEpaisseur = new JSpinner(spinnerNumEpaisseur);
        ChangeListener strokeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                Object o = spinnerNumEpaisseur.getValue();
                Integer i = (Integer)o; 
                epaisseur= i;
                d.epaisseurLines();
                } 
        };
        spinnerEpaisseur.addChangeListener(strokeListener);
        spinnerEpaisseur.setMaximumSize(spinnerEpaisseur.getPreferredSize());
        JLabel spinnerEpaisseurLabel = new JLabel(" Epaisseur Arcs");
        spinnerEpaisseurLabel.setLabelFor(spinnerEpaisseur);

        toolBarButtons.add(spinnerEpaisseurLabel);
        toolBarButtons.add(spinnerEpaisseur);
        spinnerEpaisseur.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        
        
        JLabel l1 = new JLabel("  Édition");
        JLabel l2 = new JLabel("  Mode");
        //On crée un ButtonGroup pour que seul l'un puisse être activé à la fois 
        ButtonGroup groupMode = new ButtonGroup();
        groupMode.add(edition);
        groupMode.add(traitement);
        ButtonGroup groupAction = new ButtonGroup();
        groupAction.add(select);
        groupAction.add(noeud);
        groupAction.add(arc);
        groupAction.add(label);
        //On ajoute les éléments au JPanel
        toolBarButtons.add(l2);
        toolBarButtons.addSeparator();
        toolBarButtons.add(edition);
        toolBarButtons.add(traitement);
        toolBarButtons.addSeparator();
        toolBarButtons.add(l1);
        toolBarButtons.addSeparator();
        toolBarButtons.add(select);
        toolBarButtons.add(noeud);
        toolBarButtons.add(arc);
        toolBarButtons.add(label);
        //pane.add(Box.createVerticalGlue());

        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        JLabel l = new JLabel("  Traitement"); 
        toolBarButtons.add(l);
        toolBarButtons.addSeparator();
          
        //Action Listener
        ActionListener toolGroupListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource()==select) { 
                    activeTool = SELECT_TOOL;
                } else if (ae.getSource()==noeud) {
                    activeTool = NOEUD_TOOL;   
                } else if (ae.getSource()==arc) {
                    activeTool = ARC_TOOL;
                } else if (ae.getSource()==label){
                    activeTool = LABEL_TOOL;
                } 

            }
        };
        ActionListener modeGroupListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource()==edition) {
                    mode = EDITION_MODE;
                    select.setEnabled(true);
                    noeud.setEnabled(true);
                    arc.setEnabled(true);
                    label.setEnabled(true);
                    Prim.setEnabled(false);
                    Kruskal.setEnabled(false);
                    ConnexiteNO.setEnabled(false);
                } else if (ae.getSource()==traitement) {
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
                    d.exportGraphe();
                }
            }
        };

        select.addActionListener(toolGroupListener);
        //select.setSelected(true);//select activé au démarrage
        noeud.addActionListener(toolGroupListener);
        arc.addActionListener(toolGroupListener);
        label.addActionListener(toolGroupListener);
        
        edition.addActionListener(modeGroupListener);
        //edition.setSelected(true);//edition activé au démarrage
        traitement.addActionListener(modeGroupListener);
        
        //toolBarButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBarButtons.setAlignmentX(FlowLayout.CENTER);
        toolBarButtons.setFloatable(false);
        toolBarButtons.setBorderPainted(true);
        
    }
    
    
    @Override
    public void addToolBar(){
        toolBarButtons.add(Prim);
        toolBarButtons.addSeparator();
        toolBarButtons.add(Kruskal);
        toolBarButtons.addSeparator();
        toolBarButtons.add(ConnexiteNO);
    }
    
    @Override
    public void addMenuBar(){
        JMenu traitMenu = new JMenu("Traitement");
        traitMenu.add(Prim);
        traitMenu.add(Kruskal);
        traitMenu.add(ConnexiteNO);
        menuBar.add(traitMenu);
        exporter.add(new JMenuItem(ExportGraphNO));
    }
    

}
    
 