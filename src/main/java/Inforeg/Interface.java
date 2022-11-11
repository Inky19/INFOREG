package Inforeg;

/*=============================================
Classe Interface
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 08/03/2022
=============================================*/
import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.MyLine;
import Inforeg.Save.ExportLatex;
import Inforeg.ObjetGraph.Node;
import Inforeg.Save.saveManager;
import Inforeg.UI.ButtonTabComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class Interface {

    public static final String VERSION = "2.0";
    protected JFrame frame;

    /**
     * Les JPanel.
     */
    protected JTabbedPane tabsPanel;
    protected JToolBar toolBarButtons;
    protected JPanel paneImage;
    protected Draw d;

    protected LinkedList<Draw> tabs;
    private int currentTab;

    /**
     * Le Menu.
     */
    protected JMenuBar menuBar;
    protected JMenu exporter;

    /**
     * Les boutons.
     */
    
    protected JButton save;
    protected JButton load;
    protected JButton clearSelection;
    protected JButton back;
    protected JButton forward;

    /**
     * Reference to the original image.
     */
    protected BufferedImage originalImage;
    /**
     * Image used to make changes.
     */
    protected BufferedImage canvasImage;

    /**
     * Couleur utilisée.
     */
    protected Color color = Color.WHITE;
    protected BufferedImage colorSample = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    protected Rectangle selection;
    protected static Color colorBg;

    protected RenderingHints renderingHints;
    protected JLabel imageLabel;

    /**
     * Tools pour savoir l'état/bouton selectionné.
     */
    protected static int activeTool;
    public static final int SELECT_TOOL = 10;
    public static final int NOEUD_TOOL = 11;
    public static final int ARC_TOOL = 12;
    public static final int LABEL_TOOL = 13;
    protected static int mode;
    public static final int EDITION_MODE = 1;
    public static final int TRAITEMENT_MODE = 2;
    public static final int DEPLACEMENT_MODE = 3;
    protected static int activeTraitement;
    public static final int PRIM_TRAITEMENT = 21;
    public static final int DIJKSTRA_TRAITEMENT = 22;
    public static final int KRUSKAL_TRAITEMENT = 23;
    public static final int FORD_FULKERSON_TRAITEMENT = 24;
    public static final int COLORATION_TRAITEMENT = 25;
    
    private static final ImageIcon tabIco = new ImageIcon("asset/icons/tab.png");
    private static final ImageIcon unsavedTabIco = new ImageIcon("asset/icons/unsaved_tab.png");
    private static final ImageIcon moveCursor = new ImageIcon("asset/icons/move.png");
    private static final ImageIcon selectCursor = new ImageIcon("asset/icons/select.png");
    private static final ImageIcon arcIco = new ImageIcon("asset/icons/arc.png");
    private static final ImageIcon nodeIco = new ImageIcon("asset/icons/node.png");
    private static final ImageIcon labelIco = new ImageIcon("asset/icons/label.png");

    /**
     * Attribut pour la taille des Noeuds.
     */
    protected static int taille;
    /**
     * Attribut pour l'épaisseur des Arcs.
     */
    protected static int epaisseur;

    /**
     * Actions
     */
    /**
     * Action de sauvegarde du graphe dans une sauvegarde existante
     */
    public final AbstractAction Save = new AbstractAction() {
        {
            putValue(Action.NAME, "Enregistrer");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(Action.SHORT_DESCRIPTION, "Sauvegarde le graphe actuel (CTRL+S)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean save_success = false;
            // Si un fichier de sauvegarde existe déjà, on l'écrase et on effectue une nouvelle sauvegarde
            if (d.getPathSauvegarde() != " ") {
                File f = new File(d.getPathSauvegarde());
                save_success = saveManager.saveToFile(d, d.getPathSauvegarde());
                // Sinon, on créé un nouveau fichier de sauvegarde
            } else {
                save_success = saveManager.save(d);
                if (d != null) {
                    tabsPanel.setTitleAt(tabsPanel.getSelectedIndex(), d.getFileName());
                    tabsPanel.updateUI();
                }
            }
            tabSaved(save_success);
        }
    ;
    };

    /** Création d'unnouveau fichier de sauvegarde */
    public final AbstractAction SaveAs = new AbstractAction() {
        {
            putValue(Action.NAME, "Enregistrer Sous");
            putValue(Action.SHORT_DESCRIPTION, "Sauvegarde le graphe actuel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                boolean save_success = saveManager.save(d);
                if (d != null) {
                    tabsPanel.setTitleAt(tabsPanel.getSelectedIndex(), d.getFileName());
                    tabsPanel.updateUI();
                }
                tabSaved(save_success);
        }
    ;
    };

    /**
     * Constructeur d'une interface
     * @param d = instance de Draw permettant de dessiner le graphe
     */
    public Interface(Draw d) {
        this.d = d;
        this.tabs = new LinkedList<>();
        tabs.add(d);
        d.setInterface(this);
        currentTab = 0;
    }

    /**
     * Affichage de l'interface
     */
    public void createAndShowGui() {

        frame = new JFrame("INFOREG " + d.getPathSauvegarde());
        //fermer la fenêtre quand on quitte
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Position de la fenètre
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        // Icone de l'application
        ImageIcon icon = new ImageIcon("asset/icon.png");
        frame.setIconImage(icon.getImage());

        initTabs();
        initToolBar();
        addToolBar();
        initPaneImage();
        initLeftMenuBar();
        addMenuBar();
        initRightMenuBar();

        frame.add(toolBarButtons, BorderLayout.LINE_START);
        frame.add(paneImage, BorderLayout.CENTER);
        Interface.colorBg = paneImage.getBackground();
        frame.setJMenuBar(menuBar);

        //frame.getContentPane().add(this.d);
        frame.add(tabsPanel);
        this.d.repaint();

        //frame.pack(); remi : Je pense pas que c'est utile ici
        frame.setVisible(true);

    }

    /**
     * JPanel pour les boutons
     *
     */
    public void initToolBar(){
        toolBarButtons = new JToolBar(null, JToolBar.VERTICAL);
        //Panel le long de l'axe Y
        toolBarButtons.setLayout(new BoxLayout(toolBarButtons, BoxLayout.Y_AXIS));

        
        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();
        
        JButton colorButton = new JButton("Color");
        colorButton.setMnemonic('o');
        colorButton.setToolTipText("Choose a Color");
        ActionListener colorListener;
        colorListener = (ActionEvent arg0) -> {
            Color c = JColorChooser.showDialog(frame, "Choose a color", color);
            if (c != null) {
                for (int i = 1; i < colorSample.getHeight(); i++) {
                    for (int j = 1; j < colorSample.getHeight(); j++) {
                        colorSample.setRGB(i, j, c.getRGB());
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
        final SpinnerNumberModel spinnerNumTaille = new SpinnerNumberModel(20, 1, 100, 1);
        JSpinner spinnerTaille = new JSpinner(spinnerNumTaille);
        ChangeListener listenerTaille = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                Object o = spinnerNumTaille.getValue();
                Integer i = (Integer) o;
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
        final SpinnerNumberModel spinnerNumEpaisseur = new SpinnerNumberModel(20, 1, 100, 1);
        JSpinner spinnerEpaisseur = new JSpinner(spinnerNumEpaisseur);
        ChangeListener strokeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                Object o = spinnerNumEpaisseur.getValue();
                Integer i = (Integer) o;
                epaisseur = i;
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
        
        JToolBar moveAndSelect = new JToolBar(null, JToolBar.HORIZONTAL);
        moveAndSelect.setFloatable(false);
        moveAndSelect.setBorderPainted(true);
        moveAndSelect.setAlignmentX(FlowLayout.LEFT);
        JButton move = new JButton(moveCursor);
        move.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                mode = DEPLACEMENT_MODE;
            }  
        });  
        moveAndSelect.add(move);
        
        JButton select = new JButton(selectCursor);
        select.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                mode = EDITION_MODE;
                activeTool = SELECT_TOOL;
            }  
        });  
        moveAndSelect.add(select);
        toolBarButtons.add(moveAndSelect);
        toolBarButtons.addSeparator();
        JLabel l1 = new JLabel("  Ajouter :");
        toolBarButtons.add(l1);
        toolBarButtons.addSeparator();
        JButton nodeButton = new JButton("Nœud", nodeIco);
        nodeButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                mode = EDITION_MODE;
                activeTool = NOEUD_TOOL;
            }  
        });  
        toolBarButtons.add(nodeButton);
        
        JButton arcButton = new JButton("Arc", arcIco);
        arcButton.setHorizontalAlignment(SwingConstants.LEFT);
        arcButton.setMaximumSize(nodeButton.getMaximumSize());
        arcButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                mode = EDITION_MODE;
                activeTool = ARC_TOOL;
            }  
        });  
        toolBarButtons.add(arcButton);
        
        JButton labelButton = new JButton("Label", labelIco);
        labelButton.setMaximumSize(nodeButton.getMaximumSize());
        labelButton.setHorizontalAlignment(SwingConstants.LEFT);
        labelButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                mode = EDITION_MODE;
                activeTool = LABEL_TOOL;
            }  
        });  
        toolBarButtons.add(labelButton);
        
        toolBarButtons.addSeparator();
        JLabel l2 = new JLabel("  Traitement :");
        toolBarButtons.add(l2);
        toolBarButtons.addSeparator();
        
        JButton connexeButton = new JButton("Connexe");
        connexeButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                mode = TRAITEMENT_MODE;
                connexe();
            }  
        });  
        toolBarButtons.add(connexeButton);
    };
    
    public abstract void connexe();

    public abstract void addToolBar();

    private void initTabs() {
        tabsPanel = new JTabbedPane();
        FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);
        JPanel pnlTab = new JPanel(f);
        pnlTab.setOpaque(false);
        JButton addTabButton = new JButton("+");
        addTabButton.setOpaque(false); //
        //addTabButton.setBorder (null);
        addTabButton.setContentAreaFilled(false);
        addTabButton.setFocusPainted(false);
        addTabButton.setFocusable(false);

        tabsPanel.addTab("", null, new JScrollPane());
        pnlTab.add(addTabButton);
        tabsPanel.setTabComponentAt(tabsPanel.getTabCount() - 1, pnlTab);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab();
            }
        };

        ChangeListener changeListenenr = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) ce.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index > 0) {
                    d = (Draw) tabsPanel.getSelectedComponent();
                    currentTab = index;
                } else {
                    if (sourceTabbedPane.getTabCount() > 1) {
                        tabsPanel.setSelectedIndex(currentTab);
                    }
                }
            }
        };
        if (d.getFileName() == null || d.getFileName().equals("")){
            tabsPanel.addTab("Graphe 1", tabIco, d);
            d.setFileName("Graphe 1");
        } else {
            tabsPanel.addTab(d.getFileName(), tabIco, d);
        }
        
        tabsPanel.setTabComponentAt(1, new ButtonTabComponent(tabsPanel, tabIco));
        //tabsPanel.setMnemonicAt(0, KeyEvent.VK_1);
        addTabButton.setFocusable(false);
        addTabButton.addActionListener(listener);
        tabsPanel.addChangeListener(changeListenenr);
        tabsPanel.setSelectedIndex(1);

        tabsPanel.setVisible(true);

    }
    
    public void tabSaved(boolean saved){
        int ind = tabsPanel.getSelectedIndex();
        if (saved){
            tabsPanel.setIconAt(ind, tabIco);
            tabsPanel.setTitleAt(ind, d.getFileName());
        } else {
            tabsPanel.setIconAt(ind, unsavedTabIco);
            tabsPanel.setTitleAt(ind, "*"+d.getFileName());
        }
        tabsPanel.revalidate();
        tabsPanel.repaint();
        tabsPanel.updateUI();
    }
    
    private void addNewTab(){
        String title = "Graphe " + String.valueOf(tabsPanel.getTabCount());
        
        Draw newD = new Draw(d.getOriente(),d.getPondere());
        newD.setFileName(title);
        newD.setInterface(Interface.this);
        tabsPanel.addTab(title, tabIco, newD);
        tabsPanel.setTabComponentAt(tabsPanel.getTabCount() - 1, new ButtonTabComponent(tabsPanel, tabIco));
        tabsPanel.setSelectedIndex(tabsPanel.getTabCount() - 1);
    }
    
    private void addNewTab(Draw newD){
        ImageIcon tabIco = new ImageIcon("asset/icons/tab.png");
        newD.setInterface(Interface.this);
        tabsPanel.addTab(newD.getFileName(), tabIco, newD);
        tabsPanel.setTabComponentAt(tabsPanel.getTabCount() - 1, new ButtonTabComponent(tabsPanel, tabIco));
        tabsPanel.setSelectedIndex(tabsPanel.getTabCount() - 1);
    }

    public void initLeftMenuBar() {

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem ouvrir = new JMenuItem("Ouvrir");
        ouvrir.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                Draw newD = saveManager.load();
                newD.repaint();
                d = newD;
                addNewTab(newD);                
            }
        });
        
        exporter = new JMenu("Exporter");

        JMenuItem exportLatex = new JMenuItem("Exporter au format LaTeX");
        exportLatex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ExportLatex frameLatex = new ExportLatex();
                frameLatex.frameLatex(d);
            }
        });
        exporter.add(exportLatex);
        fileMenu.add(ouvrir);
        fileMenu.addSeparator();
        fileMenu.add(Save);
        fileMenu.add(SaveAs);
        fileMenu.addSeparator();
        fileMenu.add(exporter);
        menuBar.add(fileMenu);
    }

    public abstract void addMenuBar();

    public void initRightMenuBar() {
        JMenu helpMenu = new JMenu("Aide");
        JMenu aboutMenu = new JMenu("A propos");

        //Sub Menus de Aide
        JMenu helpSubMenu = new JMenu("Utilisation des boutons");
        JMenuItem helpSubMenuItem1 = new JMenuItem("Création de noeud");
        helpSubMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String str = "Pour créer des noeuds : \n"
                        + "\n"
                        + "    - veillez à ce que le bouton 'Noeud' soit activé \n"
                        + "\n"
                        + "    - puis, déplacer votre souris sur une zone et cliquer pour créer un noeud \n"
                        + "\n"
                        + "    - pour déplacer un noeud : maintenez le clique gauche sur un noeud, déplacez vers une zone de l'écran et relâchez\n"
                        + "\n"
                        + "    - pour supprimer un noeud : double-cliquez sur un noeud\n";
                JOptionPane.showMessageDialog(frame, str, "Bouton Noeud", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JMenuItem helpSubMenuItem2 = new JMenuItem("Help Sub menu item 2");
        helpMenu.add(helpSubMenu);
        helpSubMenu.add(helpSubMenuItem1);
        helpSubMenu.add(helpSubMenuItem2);

        JMenuItem credits = new JMenuItem("Crédits");
        aboutMenu.add(credits);

        credits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String creditStr = "Application créée par Béryl CASSEL, Cristobal CARRASCO DE RODT, Jorge QUISPE , Isaías VENEGAS et Samy AMAL \n"
                        + "\n"
                        + "dans le cadre du projet de groupe INFOREG \n"
                        + "\n"
                        + "encadré par Olivier ROUX";
                JOptionPane.showMessageDialog(frame, creditStr, "Credits", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menuBar.add(helpMenu);
        menuBar.add(aboutMenu);

        //CTRL Z / CTRL Y
        ImageIcon iconBack = new ImageIcon("asset/icons/back.png");
        ImageIcon iconForward = new ImageIcon("asset/icons/forward.png");
        //resize
        //Image imageBack = iconBack.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        //Image imageForward = iconForward.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_AREA_AVERAGING);
        //iconBack = new ImageIcon(imageBack);
        //iconForward = new ImageIcon(imageForward);
        back = new JButton(iconBack);
        back.setPreferredSize(new Dimension(50, 32));
        back.setFocusPainted(false);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                History piles = d.getTransitions();
                piles.back(d);
            }
        });
        forward = new JButton(iconForward);
        forward.setPreferredSize(new Dimension(50, 32));
        forward.setFocusPainted(false);
        forward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                History piles = d.getTransitions();
                piles.forward(d);
            }
        });
        //placer les back/forward à droite 
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(back);
        menuBar.add(forward);

    }

    /**
     * Méthode permettant de modifier la couleur
     *
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
        //clear(colorSample);
    }

    /*
     *   Méthodes suivantes incertaines/inutiles pour l'instant
     */
 /*
     * JPanel pour le BufferedImage (méthode getgui de BasicPaint)
     */
    public void initPaneImage() {

        Map<RenderingHints.Key, Object> hintsMap = new HashMap<>();
        hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderingHints = new RenderingHints(hintsMap);

        setImage(new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB));
        paneImage = new JPanel(new BorderLayout(4, 4));
        paneImage.setBorder(new EmptyBorder(5, 3, 5, 3));

        JPanel imageView = new JPanel(new GridBagLayout());
        imageView.setPreferredSize(new Dimension(480, 320));
        imageLabel = new JLabel(new ImageIcon(canvasImage));
        JScrollPane imageScroll = new JScrollPane(imageView);
        imageView.add(imageLabel);
        //imageLabel.addMouseMotionListener(new ImageMouseMotionListener());
        //imageLabel.addMouseListener(new ImageMouseListener());
        paneImage.add(imageScroll, BorderLayout.CENTER);

        clear(colorSample);
        clear(canvasImage);

    }

    /**
     * Méthode venant de BasicPaint
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.originalImage = image;
        int w = image.getWidth();
        int h = image.getHeight();
        canvasImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = this.canvasImage.createGraphics();
        g.setRenderingHints(renderingHints);
        g.drawImage(image, 0, 0, paneImage);
        g.setColor(this.color);
        g.dispose();

        selection = new Rectangle(0, 0, w, h);
        if (this.imageLabel != null) {
            imageLabel.setIcon(new ImageIcon(canvasImage));
            this.imageLabel.repaint();
        }
        //if (gui!=null) {
        //    gui.invalidate();
        //}
    }

    /**
     * Clears the entire image area by painting it with the current color.
     */
    public void clear(BufferedImage bi) {
        Graphics2D g = bi.createGraphics();
        g.setRenderingHints(renderingHints);
        g.setColor(color);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        g.dispose();
        imageLabel.repaint();
    }    
    
    public static int getMode() {
        return mode;
    }

    public static int getActiveTool() {
        return activeTool;
    }

    public static int getActiveTraitement() {
        return activeTraitement;
    }

    public static int getTaille() {
        return taille;
    }

    public static int getEpaisseur() {
        return epaisseur;
    }

}
