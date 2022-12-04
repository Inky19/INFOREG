package Inforeg.UI;

import Inforeg.Draw.Draw;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author inky19
 */
public class LatexWindow extends JDialog {

    private static final Color TOOL_BUTTON_FOCUS_COLOR = Color.decode("#d1d1d1");
    private static final Color TOOL_BUTTON_SELECTED_COLOR = Color.decode("#85b8d4");

    // Entiers utilisés lors de la création d'un bouton de choix de couleur pour savoir quelle valeur modifiée
    private static final int NODE_COLOR_BUTTON = 0;
    private static final int ARC_COLOR_BUTTON = 1;

    // Valeurs utilisées pour construire le style du graphe.
    private Color nColor;
    private Color aColor;
    private int nSize;
    private int aSize;
    
    private Color nButtonColor;
    private Color aButtonColor;
    
    // Couleurs temporaires pour stocker les valeurs personnalisées des boutons même s'ils ne sont pas sélectionnés.
    private JRadioButton nodeColCust;
    private JRadioButton arcColCust;
    

    public LatexWindow(JFrame frame, Draw d) {
        super(frame, "Exporter en format LaTeX");
        nButtonColor = Color.WHITE;
        nButtonColor = Color.BLACK;
        nColor = Color.WHITE;
        aColor = Color.BLACK;
        nSize = 0;
        aSize = 0;
        
        // Paramètres de la fenêtre
        this.setResizable(false);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        
        // Création des onglets
        JTabbedPane tabs = new JTabbedPane();
        JPanel config = new JPanel();
        JPanel export = new JPanel();
        tabs.addTab("Configuration", config);
        tabs.addTab("Export", export);

        // ONGLET DE CONFIGURATION
        JPanel nodes = new JPanel();
        nodes.setBorder(BorderFactory.createTitledBorder("Nœuds"));
        JRadioButton nodeSizeGraph = new JRadioButton("Taille du graphe");
        JRadioButton nodeSizeCust = new JRadioButton("Personnalisée (mm) : ");
        JTextField nodeSizeEntry = new JTextField();

        ButtonGroup nodeSizeGroup = new ButtonGroup();
        nodeSizeGroup.add(nodeSizeGraph);
        nodeSizeGroup.add(nodeSizeCust);
        nodeSizeGraph.setSelected(true);

        JPanel nodesSize = createContainer("Taille", new JComponent[]{nodeSizeGraph, nodeSizeCust, nodeSizeEntry});
        nodes.add(nodesSize);

        // Menu de sélection des couleurs
        JRadioButton nodeColGraph = new JRadioButton("Couleur du graphe");
        nodeColGraph.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {nColor = null;System.out.println(aColor.getRGB());}});

        JRadioButton nodeColWhite = new JRadioButton("Blanc");
        nodeColCust = new JRadioButton("Personnalisée");

        // Groupement des boutons
        ButtonGroup nodeColGroup = new ButtonGroup();
        nodeColGroup.add(nodeColGraph);
        nodeColGroup.add(nodeColWhite);
        nodeColGroup.add(nodeColCust);
        nodeColGraph.setSelected(true);

        // Panel pour afficher le bouton de sélection de la couleur sur la même ligne que le bouton radio correspondant.
        JPanel custNodePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        custNodePanel.add(nodeColCust);

        // Bouton de sélection de la couleur
        ToolButton nodeColorButton = createColorButton(frame, NODE_COLOR_BUTTON);

        custNodePanel.add(nodeColorButton);
        JPanel nodesColor = createContainer("Couleur", new JComponent[]{nodeColGraph, nodeColWhite, custNodePanel});
        nodesColor.setLayout(new BoxLayout(nodesColor, BoxLayout.Y_AXIS));
        nodes.add(nodesColor);

        config.add(nodes);

        JPanel arcs = new JPanel();
        arcs.setBorder(BorderFactory.createTitledBorder("Arcs"));
        JRadioButton arcSizeGraph = new JRadioButton("Épaisseur du graphe");
        JRadioButton arcSizeCust = new JRadioButton("Personnalisée (mm) : ");
        JTextField arcSizeEntry = new JTextField();

        ButtonGroup arcSizeGroup = new ButtonGroup();
        arcSizeGroup.add(arcSizeGraph);
        arcSizeGroup.add(arcSizeCust);
        arcSizeGraph.setSelected(true);

        JPanel arcsSize = createContainer("Épaisseur", new JComponent[]{arcSizeGraph, arcSizeCust, arcSizeEntry});
        arcs.add(arcsSize);

        // Menu de sélection des couleurs
        JRadioButton arcColGraph = new JRadioButton("Couleur du graphe");
        arcColGraph.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {aColor = null;}});
        
        JRadioButton arcColWhite = new JRadioButton("Noir");
        arcColWhite.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {aColor = Color.BLACK;}});
        
        arcColCust = new JRadioButton("Personnalisée");
        arcColCust.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {aColor = aButtonColor;}});

        // Groupement des boutons
        ButtonGroup arcColGroup = new ButtonGroup();
        arcColGroup.add(arcColGraph);
        arcColGroup.add(arcColWhite);
        arcColGroup.add(arcColCust);
        arcColGraph.setSelected(true);

        // Panel pour afficher le bouton de sélection de la couleur sur la même ligne que le bouton radio correspondant.
        JPanel custArcPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        custArcPanel.add(arcColCust);

        // Bouton de sélection de la couleur
        ToolButton arcColorButton = createColorButton(frame, ARC_COLOR_BUTTON);

        custArcPanel.add(arcColorButton);
        JPanel arcsColor = createContainer("Couleur", new JComponent[]{arcColGraph, arcColWhite, custArcPanel});
        arcsColor.setLayout(new BoxLayout(arcsColor, BoxLayout.Y_AXIS));
        arcs.add(arcsColor);

        config.add(arcs);

        // ONGLET DE L'EXPORT
        JTextArea exportArea = new JTextArea("test");
        JScrollPane exportScrollPane = new JScrollPane(exportArea);
        exportScrollPane.setPreferredSize(new Dimension(400, 400));
        export.add(exportScrollPane);

        this.add(tabs);
        this.setModal(true);
    }

    private static JPanel createContainer(String name, JComponent[] compos) {
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createTitledBorder(name));
        BoxLayout layout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(layout);

        for (int i = 0, n = compos.length; i < n; i++) {
            compos[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            container.add(compos[i]);
        }
        return container;
    }

    private ToolButton createColorButton(Frame frame, int targetColor) {
        ToolButton colorButton = new ToolButton("", null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
        colorButton.setToolTipText("Choisir une couleur");
        BufferedImage colorSample = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        ActionListener colorListener;
        colorListener = (ActionEvent arg0) -> {
            Color c = JColorChooser.showDialog(frame, "Choisir une couleur", Color.WHITE);
            if (c != null) {
                for (int i = 1; i < colorSample.getHeight(); i++) {
                    for (int j = 1; j < colorSample.getHeight(); j++) {
                        colorSample.setRGB(i, j, c.getRGB());
                        switch (targetColor) {
                            case NODE_COLOR_BUTTON:
                                nButtonColor = c;
                                if (nodeColCust.isSelected()){ // Si la case est déjà sélectionnée, on actualise la valeur de la couleur.
                                    nColor = nButtonColor;
                                }
                                break;
                            case ARC_COLOR_BUTTON:
                                aButtonColor = c;
                                if (arcColCust.isSelected()){ // Si la case est déjà sélectionnée, on actualise la valeur de la couleur.
                                    aColor = aButtonColor;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        };
        colorButton.addActionListener(colorListener);
        colorButton.setIcon(new ImageIcon(colorSample));
        return colorButton;
    }

}
