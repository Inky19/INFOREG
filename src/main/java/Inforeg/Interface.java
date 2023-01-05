package Inforeg;

import Inforeg.Algo.AlgorithmS;
import Inforeg.Algo.AlgorithmST;
import static Inforeg.AssetLoader.*;
import Inforeg.Draw.Draw;
import static Inforeg.Graph.GraphFunction.connected;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import Inforeg.Save.ExportPNG;
import Inforeg.Save.SaveManager;
import Inforeg.UI.AlgoBox;
import Inforeg.UI.AlgoWindow;
import Inforeg.UI.ButtonTabComponent;
import Inforeg.UI.LatexWindow;
import Inforeg.UI.CheckBox;
import Inforeg.UI.CreditsWindow;
import Inforeg.UI.GraphTypeWindow;
import Inforeg.UI.CustomButton;
import Inforeg.UI.MatrixWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Interface de la fenêtre principale
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 * @author Samy AMAL
 */

public class Interface {

    public static final String VERSION = "2.0";
    protected JFrame frame;

    /**
     * Les JPanel.
     */
    protected JTabbedPane tabsPanel;
    protected JToolBar toolBarButtons;
    protected JPanel paneImage;
    private JPanel resultContainer; // Contient la zone de résultats et la barre avec le bouton pour la réduire
    private JPanel resultPanel; // Zone de résultats
    private JLabel stepByStepLabel;
    private JScrollPane resultScrollPane; // Contient la zone de résultats (resultPanel) et permet d'utiliser des barres de défilement si cette zone est trop grande.
    protected Draw d;

    protected LinkedList<Draw> tabs;
    private int currentTab;
    private int resultZoneSize; // Taille du panel (seulement) qui contient les résultats
    private int resultTitleSize; // Taille du panel qui contient le titre "Résultats :" et le bouton pour cacher la zone de résultats
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
    private JButton back;
    private JButton forward;
    private CustomButton algoButton;
    private JButton previousStep;
    private JButton nextStep;
    private JToolBar stepBystepBar;

    private CustomButton selectedButton;

    private JCheckBox autoStart;
    private JCheckBox stepByStep;

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
    public static final int COLOR_TOOL = 14;
    public static final int PIN_TOOL = 15;
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

    /**
     * Attribut pour la taille des Noeuds.
     */
    protected static int taille;
    /**
     * Attribut pour l'épaisseur des Arcs.
     */
    protected static int epaisseur;

    private static Dimension buttonSize = new Dimension(92, 44);

    private JPopupMenu menuNode;

    private static final Color TOOL_BUTTON_COLOR = Color.decode("#d9d9d9");
    private static final Color TOOL_BUTTON_FOCUS_COLOR = Color.decode("#d1d1d1");
    private static final Color TOOL_BUTTON_SELECTED_COLOR = Color.decode("#85b8d4");
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
                save_success = SaveManager.saveToFile(d, d.getPathSauvegarde());
                // Sinon, on créé un nouveau fichier de sauvegarde
            } else {
                save_success = SaveManager.save(d);
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
            boolean save_success = SaveManager.save(d);
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
        resultZoneSize = 100;
        resultTitleSize = 30;
        selectedButton = null;
    }

    /**
     * Affichage de l'interface
     */
    public void createAndShowGui() {

        frame = new JFrame("INFOREG " + VERSION);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(frame);
        //fermer la fenêtre quand on quitte
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Position de la fenètre
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        // Icone de l'application
        ImageIcon icon = appIco;
        frame.setIconImage(icon.getImage());
        resultContainer = new JPanel(new BorderLayout());
        resultPanel = new JPanel(new BorderLayout());
        resultScrollPane = new JScrollPane(resultPanel);

        initToolBar();
        initTabs();
        initPaneImage();
        initLeftMenuBar();
        initRightMenuBar();
        initContextMenu();

        JPanel contentPanel = new JPanel(new BorderLayout());
        frame.add(toolBarButtons, BorderLayout.WEST);
        frame.setJMenuBar(menuBar);
        initShortcuts(contentPanel);
        //frame.getContentPane().add(this.d);
        Interface.colorBg = paneImage.getBackground();
        contentPanel.add(paneImage, BorderLayout.CENTER);
        tabsPanel.setPreferredSize(new Dimension(500, 500));

        contentPanel.add(tabsPanel);

        this.d.repaint();
        // ZONE DES RÉSULTATS

        resultContainer.setPreferredSize(new Dimension(Integer.MAX_VALUE, resultZoneSize));
        // Titre de la zone :
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleResult = new JLabel("     Résultats :");
        CustomButton showResult = new CustomButton(downArrow, null, TOOL_BUTTON_FOCUS_COLOR, null);
        showResult.setOpaque(false);
        showResult.addActionListener((ActionEvent e) -> {
            if (resultContainer.getPreferredSize().height <= resultTitleSize) {
                resultContainer.setPreferredSize(new Dimension(Integer.MAX_VALUE, resultZoneSize));
                showResult.setIcon(downArrow);
                showResult.unselect();
            } else {
                resultContainer.setPreferredSize(new Dimension(Integer.MAX_VALUE, resultTitleSize));
                showResult.setIcon(upArrow);
                showResult.unselect();
            }
            resultContainer.revalidate();
            resultContainer.repaint();
        });
        titlePanel.add(titleResult, BorderLayout.LINE_START);
        titlePanel.add(showResult, BorderLayout.LINE_END);
        titlePanel.setBackground(new Color(227, 239, 247));
        titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, resultTitleSize));
        resultContainer.add(titlePanel, BorderLayout.NORTH);
        // Zone en elle même :

        resultScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, resultZoneSize - resultTitleSize));
        resultContainer.add(resultScrollPane, BorderLayout.SOUTH);

        // Ajout de la zone
        contentPanel.add(resultContainer, BorderLayout.SOUTH);
        frame.add(contentPanel);
        //frame.pack();// remi : Je pense pas que c'est utile ici
        frame.setVisible(true);

    }

    /**
     * JPanel pour les boutons
     *
     */
    public void initToolBar() {
        toolBarButtons = new JToolBar(null, JToolBar.VERTICAL);
        //toolBarButtons.setBackground(TOOL_BUTTON_COLOR);
        //Panel le long de l'axe Y
        toolBarButtons.setLayout(new BoxLayout(toolBarButtons, BoxLayout.Y_AXIS));
        toolBarButtons.setFloatable(false);

        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();

        CustomButton colorButton = new CustomButton("Couleur", null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        colorButton.setMaximumSize(buttonSize);
        colorButton.setMnemonic('o');
        colorButton.setToolTipText("Choisir une couleur");
        ActionListener colorListener;
        colorListener = (ActionEvent arg0) -> {
            Color c = JColorChooser.showDialog(frame, "Choisir une couleur", color);
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

        setColor(this.color);

        //ajoute un séparateur de taille par défaut
        toolBarButtons.addSeparator();

        //Taille
        final SpinnerNumberModel spinnerNumTaille = new SpinnerNumberModel(Draw.RINIT, 1, 100, 1);
        JSpinner spinnerTaille = new JSpinner(spinnerNumTaille);
        ChangeListener listenerTaille = (ChangeEvent arg0) -> {
            Object o = spinnerNumTaille.getValue();
            Integer i = (Integer) o;
            taille = i;
            d.tailleCirc();
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
        ChangeListener strokeListener = (ChangeEvent arg0) -> {
            Object o = spinnerNumEpaisseur.getValue();
            Integer i = (Integer) o;
            epaisseur = i;
            d.epaisseurLines();
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

        JToolBar tools = new JToolBar(null, JToolBar.HORIZONTAL);
        tools.setFloatable(false);
        tools.setBorderPainted(false);
        tools.setOpaque(false);
        tools.setAlignmentX(FlowLayout.LEFT);
        tools.setLayout(new GridLayout(2, 2));
        // Move Button
        CustomButton moveButton = new CustomButton(moveCursor, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        moveButton.setToolTipText("Déplacement");
        moveButton.addActionListener((ActionEvent e) -> {
            mode = DEPLACEMENT_MODE;
            selectButton(moveButton);
        });
        tools.add(moveButton);
        // Select Button
        CustomButton selectButton = new CustomButton(selectCursor, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        selectButton.setToolTipText("Sélection");
        selectButton.setBackground(TOOL_BUTTON_COLOR);
        selectButton.addActionListener((ActionEvent e) -> {
            mode = EDITION_MODE;
            activeTool = SELECT_TOOL;
            selectButton(selectButton);
        });
        tools.add(selectButton);
        toolBarButtons.add(tools);
        // Brush Button
        CustomButton brushButton = new CustomButton(colorIco, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        brushButton.setToolTipText("Pinceau");
        brushButton.addActionListener((ActionEvent e) -> {
            mode = EDITION_MODE;
            activeTool = COLOR_TOOL;
            selectButton(brushButton);
        });
        tools.add(brushButton);
        // Pin Button
        CustomButton labelButton = new CustomButton(labelIco, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        labelButton.setToolTipText("Label");
        labelButton.addActionListener((ActionEvent e) -> {
            mode = EDITION_MODE;
            activeTool = LABEL_TOOL;
            selectButton(labelButton);
        });
        tools.add(labelButton);

        toolBarButtons.add(colorButton);
        toolBarButtons.addSeparator();
        JLabel l1 = new JLabel("  Ajouter :");
        toolBarButtons.add(l1);
        toolBarButtons.addSeparator();
        // Node Button
        CustomButton nodeButton = new CustomButton("Noeud", nodeIco, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        nodeButton.setFocusPainted(false);
        nodeButton.setBackground(TOOL_BUTTON_COLOR);
        nodeButton.addActionListener((ActionEvent e) -> {
            mode = EDITION_MODE;
            activeTool = NOEUD_TOOL;
            selectButton(nodeButton);
        });
        toolBarButtons.add(nodeButton);
        nodeButton.setMaximumSize(buttonSize);
        // Arc Button
        CustomButton arcButton = new CustomButton("Arc", arcIco, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        arcButton.setFocusPainted(false);
        arcButton.setBackground(TOOL_BUTTON_COLOR);
        arcButton.setHorizontalAlignment(SwingConstants.LEFT);
        arcButton.setMaximumSize(buttonSize);
        arcButton.addActionListener((ActionEvent e) -> {
            mode = EDITION_MODE;
            activeTool = ARC_TOOL;
            selectButton(arcButton);
        });
        toolBarButtons.add(arcButton);
        // Label Button
        CustomButton pinButton = new CustomButton("Clou", pinIco, null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        pinButton.setFocusPainted(false);
        pinButton.setBackground(TOOL_BUTTON_COLOR);
        pinButton.setMaximumSize(buttonSize);
        pinButton.setHorizontalAlignment(SwingConstants.LEFT);
        pinButton.addActionListener((ActionEvent e) -> {
            mode = EDITION_MODE;
            activeTool = PIN_TOOL;
            selectButton(pinButton);
        });
        toolBarButtons.add(pinButton);

        toolBarButtons.addSeparator();
        JLabel l2 = new JLabel("  Traitement :");
        toolBarButtons.add(l2);
        toolBarButtons.addSeparator();

        CustomButton connexeButton = new CustomButton("Connexe", AlgoBox.BUTTON_COLOR, AlgoBox.BUTTON_SELECTED_COLOR, null);
        connexeButton.setHorizontalAlignment(SwingConstants.CENTER);
        connexeButton.setMaximumSize(new Dimension(buttonSize.width, connexeButton.getMaximumSize().height));
        connexeButton.addActionListener((ActionEvent e) -> {
            mode = TRAITEMENT_MODE;
            connexe();
        });
        toolBarButtons.add(connexeButton);
        toolBarButtons.addSeparator();

        algoButton = new CustomButton("▼", AlgoBox.BUTTON_COLOR, AlgoBox.BUTTON_SELECTED_COLOR, null);
        Dimension algoButtonSize = new Dimension(buttonSize.width, algoButton.getMaximumSize().height);
        algoButton.setMaximumSize(algoButtonSize);
        algoButton.setPreferredSize(algoButtonSize);
        algoButton.setHorizontalAlignment(SwingConstants.LEFT);
        algoButton.addActionListener((ActionEvent e) -> {
            AlgoWindow window = new AlgoWindow(frame, d);
            window.setVisible(true);

            updateAlgoSelector();
        });
        toolBarButtons.add(algoButton);
        JPanel algoPanel = new JPanel();
        algoPanel.setMaximumSize(new Dimension(buttonSize.width, Integer.MAX_VALUE));
        algoPanel.setPreferredSize(algoPanel.getMaximumSize());
        algoPanel.setAlignmentX(0);
        CustomButton algoGo = new CustomButton(playIco, null , AlgoBox.BUTTON_COLOR, null);
        algoGo.setToolTipText("Lancer l'algorithme");
        algoGo.addActionListener((ActionEvent e) -> {
            if (d.getAlgo() == null) {
                JOptionPane.showMessageDialog(null, "Aucun algorithme sélectionné.", "Algorithme", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mode = Interface.TRAITEMENT_MODE;
                d.exportGraphe();
                d.reinit();
                selectButton(null);
                stepBystepBar.setVisible(false);
                d.stepBysStep.clear();
                if (d.getAlgo() instanceof AlgorithmST) {
                    d.setStatus(Draw.ALGO_INPUT);
                }
                if (d.getAlgo() instanceof AlgorithmST || (d.getAlgo() instanceof AlgorithmS && !isAuto())) {
                    d.getInfoTop().setText("Sélectionner le nœud source");
                }
                d.getAlgo().process(d);
                d.repaint();
            }
        });
        CustomButton resetButton = new CustomButton(resetIco, null, AlgoBox.BUTTON_COLOR, null);
        resetButton.addActionListener(((ActionEvent e) -> {
            d.reinit();
            stepBystepBar.setVisible(false);
            d.stepBysStep.clear();
            d.repaint();
        }));
        resetButton.setToolTipText("Réinitialiser le graphe");
        algoPanel.add(resetButton);

        JToolBar goAndReset = new JToolBar();
        goAndReset.setBorderPainted(false);
        goAndReset.setFloatable(false);
        goAndReset.setOpaque(false);

        algoPanel.add(goAndReset);
        goAndReset.add(algoGo);
        goAndReset.add(resetButton);

        stepByStep = new CheckBox("Pas à pas");
        algoPanel.add(stepByStep);
        autoStart = new CheckBox("<html><body>Départ auto</body></html>");
        autoStart.setVisible(false);
        algoPanel.add(autoStart);

        stepBystepBar = new JToolBar();
        stepBystepBar.setLayout(new BorderLayout());
        stepBystepBar.setFloatable(false);
        stepBystepBar.setBorderPainted(false);
        
        previousStep = new CustomButton(previousIco, null, TOOL_BUTTON_FOCUS_COLOR, null);
        previousStep.setFocusPainted(false);
        nextStep = new CustomButton(nextIco, null, TOOL_BUTTON_FOCUS_COLOR, null);
        previousStep.addActionListener((ActionEvent e) -> {
            if (d.stepBysStep.isLastStep()) {
                nextStep.setEnabled(true);
            }
            d.stepBysStep.executePreviousStep(d);
            stepByStepLabel.setText("Etape " + d.stepBysStep.getCurrentStepIndex() + " / " + d.stepBysStep.getNbStep());
            if (d.stepBysStep.isFirstStep()) {
                previousStep.setEnabled(false);
            }
        });
        nextStep.addActionListener((ActionEvent e) -> {
            if (d.stepBysStep.isFirstStep()) {
                previousStep.setEnabled(true);
            }
            d.stepBysStep.executeNextStep(d);
            stepByStepLabel.setText("Etape " + d.stepBysStep.getCurrentStepIndex() + " / " + d.stepBysStep.getNbStep());
            if (d.stepBysStep.isLastStep()) {
                nextStep.setEnabled(false);
            }
        });
        stepBystepBar.setVisible(false);
        stepBystepBar.setOpaque(false);

        stepByStepLabel = new JLabel("");
        stepBystepBar.add(stepByStepLabel, BorderLayout.NORTH);

        stepBystepBar.add(previousStep, BorderLayout.WEST);
        stepBystepBar.add(nextStep, BorderLayout.EAST);
        algoPanel.add(stepBystepBar);

        toolBarButtons.add(algoPanel);

    }

    private void updateAlgoSelector() {
        if (d.getAlgo() == null) {
            algoButton.setText("▼");
        } else {
            algoButton.setText("▼  " + d.getAlgo().getName());
            autoStart.setVisible(!d.getAlgo().isAutoStart());
            toolBarButtons.revalidate();
            toolBarButtons.repaint();
        }
    }

    public void connexe() {
        mode = TRAITEMENT_MODE;
        d.getG().updateVariable();
        String ori = "";
        if (d.oriente) {
            ori = " fortement";
        }
        if (connected(d.getG())) {
            JOptionPane.showMessageDialog(d, "Le graphe est" + ori + " connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(d, "Le graphe n'est pas" + ori + " connexe.", "Connexité", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void initTabs() {

        tabsPanel = new JTabbedPane();
        FlowLayout f = new FlowLayout(FlowLayout.CENTER, 0, 0);
        JPanel pnlTab = new JPanel(f);
        pnlTab.setOpaque(false);
        JButton addTabButton = new CustomButton(plusIco, null, Color.LIGHT_GRAY, null);
        addTabButton.setPreferredSize(new Dimension(50,30));

        tabsPanel.addTab("", null, new JScrollPane());
        pnlTab.add(addTabButton);
        tabsPanel.setTabComponentAt(tabsPanel.getTabCount() - 1, pnlTab);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab();
            }
        };

        ChangeListener switchTab = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) ce.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index > 0) {
                    d = (Draw) tabsPanel.getSelectedComponent();
                    if (d.getAlgo() != null) {
                        algoButton.setText("▼  " + d.getAlgo().getName());
                        autoStart.setVisible(d.getAlgo() instanceof AlgorithmS);
                    } else {
                        algoButton.setText("▼");
                        autoStart.setVisible(false);
                    }
                    if (d.stepBysStep.getNbStep() == 0) {
                        stepBystepBar.setVisible(false);
                    } else {
                        stepByStepLabel.setText("Etape " + d.stepBysStep.getCurrentStepIndex() + " / " + d.stepBysStep.getNbStep());
                        stepBystepBar.setVisible(true);
                        nextStep.setEnabled(!d.stepBysStep.isLastStep());
                        previousStep.setEnabled(!d.stepBysStep.isFirstStep());
                    }
                    currentTab = index;
                    refreshResult();

                } else {
                    if (sourceTabbedPane.getTabCount() > 1) {
                        tabsPanel.setSelectedIndex(currentTab);
                    } else {
                        resultPanel.removeAll();
                        resultContainer.revalidate();
                        resultContainer.repaint();
                    }
                }
            }
        };
        if (d.getFileName() == null || d.getFileName().equals("")) {
            tabsPanel.addTab("Graphe 1", tabIco, d);
            d.setFileName("Graphe 1");
        } else {
            tabsPanel.addTab(d.getFileName(), tabIco, d);
        }
        tabsPanel.setTabComponentAt(1, new ButtonTabComponent(tabsPanel, d.oriente ? orienteIco : norienteIco, d.pondere ? pondereIco : npondereIco));
        //tabsPanel.setMnemonicAt(0, KeyEvent.VK_1);
        addTabButton.setFocusable(false);
        addTabButton.addActionListener(listener);
        tabsPanel.addChangeListener(switchTab);
        tabsPanel.setSelectedIndex(1);

        tabsPanel.setVisible(true);

    }

    public void tabSaved(boolean saved) {
        int ind = tabsPanel.getSelectedIndex();
        if (saved) {
            ((ButtonTabComponent) tabsPanel.getTabComponentAt(ind)).setTitleColor(Color.BLACK);
            tabsPanel.setTitleAt(ind, d.getFileName());
        } else {
            ((ButtonTabComponent) tabsPanel.getTabComponentAt(ind)).setTitleColor(Color.decode("#0c6d96"));
            tabsPanel.setTitleAt(ind, "*" + d.getFileName());
        }
        tabsPanel.revalidate();
        tabsPanel.repaint();
        tabsPanel.updateUI();
    }

    private void addNewTab() {
        GraphTypeWindow graphWin = new GraphTypeWindow();
        Draw newD = graphWin.chooseGraph();
        if (newD != null){
            String title;
            if (newD.getFileName() == null || newD.getFileName().equals("")) {
                title = "Graphe " + String.valueOf(tabsPanel.getTabCount());
            } else {
                title = newD.getFileName();
            }
            newD.setFileName(title);
            newD.setInterface(Interface.this);
            tabsPanel.addTab(title, tabIco, newD);
            tabsPanel.setTabComponentAt(tabsPanel.getTabCount() - 1, new ButtonTabComponent(tabsPanel, newD.oriente ? orienteIco : norienteIco, newD.pondere ? pondereIco : npondereIco));
            tabsPanel.setSelectedIndex(tabsPanel.getTabCount() - 1);
        }

    }

    private void addNewTab(Draw newD) {
        newD.setInterface(Interface.this);
        tabsPanel.addTab(newD.getFileName(), tabIco, newD);
        tabsPanel.setTabComponentAt(tabsPanel.getTabCount() - 1, new ButtonTabComponent(tabsPanel, newD.oriente ? orienteIco : norienteIco, newD.pondere ? pondereIco : npondereIco));
        tabsPanel.setSelectedIndex(tabsPanel.getTabCount() - 1);
    }

    public void initLeftMenuBar() {

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem ouvrir = new JMenuItem("Ouvrir");
        ouvrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw newD = SaveManager.load();
                if (newD != null) {
                    newD.repaint();
                    d = newD;
                    addNewTab(newD);
                }

            }
        });

        exporter = new JMenu("Exporter");

        JMenuItem exportLatex = new JMenuItem("Format LaTeX");
        exportLatex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                LatexWindow latexWin = new LatexWindow(frame, d);
                latexWin.setVisible(true);
            }
        });
        JMenuItem exportPNG = new JMenuItem("Image PNG");
        exportPNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ExportPNG.export(d);
            }
        });

        exporter.add(exportLatex);
        exporter.add(exportPNG);
        exporter.add(ExportMatrix);
        fileMenu.add(ouvrir);
        fileMenu.addSeparator();
        fileMenu.add(Save);
        fileMenu.add(SaveAs);
        fileMenu.addSeparator();
        fileMenu.add(exporter);
        menuBar.add(fileMenu);

        //JMenu traitMenu = new JMenu("Traitement");
        //menuBar.add(traitMenu);
    }

    public final AbstractAction ExportMatrix = new AbstractAction() {
        {
            putValue(Action.NAME, "Export Matrice d'Adjacence");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(Action.SHORT_DESCRIPTION, "Affiche la matrice d'adjacence du graphe (CTRL+A)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent ea) {
            String non = "";
            if (!d.oriente) {
                non = "non ";
            }
            d.exportGraphe();
            MatrixWindow matrixWindow = new MatrixWindow(frame , d);
            matrixWindow.setVisible(true);
        }
    };

    public void initRightMenuBar() {
        JMenu helpMenu = new JMenu("Aide");
        JMenu aboutMenu = new JMenu("A propos");

        //Sub Menus de Aide
        JMenuItem helpSubMenu = new JMenuItem("Documentation");
        helpSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String inputPdf = "asset/doc.pdf";
                InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream(inputPdf);

                Path tempOutput = null;
                try {
                    tempOutput = Files.createTempFile("TempManual", ".pdf");
                    tempOutput.toFile().deleteOnExit();
                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);
                    File userManual = new File (tempOutput.toFile().getPath());
                    if (userManual.exists()){
                        Desktop.getDesktop().open(userManual);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        helpMenu.add(helpSubMenu);

        JMenuItem credits = new JMenuItem("Crédits");
        aboutMenu.add(credits);

        credits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                CreditsWindow credits;
                try {
                    credits = new CreditsWindow(frame);
                    credits.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuBar.add(helpMenu);
        menuBar.add(aboutMenu);

        //CTRL Z / CTRL Y
        back = new CustomButton(backIco, null, TOOL_BUTTON_COLOR, null);
        back.setPreferredSize(new Dimension(50, 32));
        back.addActionListener((ActionEvent ae) -> {
            History piles = d.getTransitions();
            piles.back(d);
        });
        forward = new CustomButton(forwardIco, null, TOOL_BUTTON_FOCUS_COLOR, null);
        forward.setPreferredSize(new Dimension(50, 32));
        forward.addActionListener((ActionEvent ae) -> {
            History piles = d.getTransitions();
            piles.forward(d);
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

    private void initShortcuts(JPanel contentPanel) {

        Action actionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                d.deleteSelected();
            }
        };
        KeyStroke del = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, true);
        InputMap inputMap = contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        String DEL_KEY = "delAction";
        inputMap.put(del, DEL_KEY);
        ActionMap actionMap = contentPanel.getActionMap();
        actionMap.put(DEL_KEY, actionListener);
        contentPanel.setActionMap(actionMap);
    }

    public void initContextMenu() {
        menuNode = new JPopupMenu();

    }

    public void rightClickNode(Node n, int x, int y) {
        menuNode = new JPopupMenu();
        JMenuItem renameNode = new JMenuItem("Renommer");
        renameNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ActionMenu.renameNode(d, n);
            }

        });
        JMenuItem colorNode = new JMenuItem("Couleur");
        colorNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(frame, "Choose a color", color);
                if (c != null) {
                    ActionMenu.colorNode(d, n, c);
                }
            }
        });

        JMenuItem deleteNode = new JMenuItem("Supprimer");
        deleteNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ActionMenu.deleteNode(d, n);
                d.getTransitions().push();
            }

        });
        menuNode.add(renameNode);
        menuNode.add(colorNode);
        menuNode.add(deleteNode);
        menuNode.show(d, x, y);
    }

    public void rightClickArc(Arc a, int x, int y) {
        menuNode = new JPopupMenu();
        JMenuItem renameArc = new JMenuItem("Changer poids");
        renameArc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ActionMenu.setPoids(d, a);
            }

        });
        JMenuItem colorArc = new JMenuItem("Couleur");
        colorArc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(frame, "Choose a color", color);
                if (c != null) {
                    ActionMenu.colorArc(d, a, c);
                }
            }
        });

        JMenuItem deleteArc = new JMenuItem("Supprimer");
        deleteArc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ActionMenu.deleteArc(d, a);
            }

        });
        if (d.pondere) {
            menuNode.add(renameArc);
        }
        menuNode.add(colorArc);
        menuNode.add(deleteArc);
        menuNode.show(d, x, y);
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

    public void refreshResult() {
        resultPanel.removeAll();
        JTextField text = new JTextField(d.getResultat());
        text.setEditable(false);
        resultPanel.add(text);
        resultContainer.revalidate();
        resultContainer.repaint();
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

    public Color getColor() {
        return color;
    }

    public boolean isAuto() {
        return autoStart.isSelected();
    }

    public void selectButton(CustomButton button) {
        if (selectedButton != null) {
            selectedButton.unselect();
        }
        selectedButton = button;
        if (button != null) {
            button.select();
        }
    }

    public void showResult() {
        if (stepByStep.isSelected()) {
            stepBystepBar.setVisible(true);
            stepByStepLabel.setText("Etape " + d.stepBysStep.getCurrentStepIndex() + " / " + d.stepBysStep.getNbStep());
            previousStep.setEnabled(false);
            nextStep.setEnabled(d.stepBysStep.getNbStep() > 0);
        } else {
            stepBystepBar.setVisible(false);
        }
    }
}
