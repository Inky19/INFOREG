package Inforeg.UI;

import Inforeg.AssetLoader;
import Inforeg.Draw.Draw;
import Inforeg.Save.ExportLatex;
import java.awt.BorderLayout;
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
import javax.swing.JCheckBox;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Fenêtre de l'export LaTeX
 *
 * @author François MARIE
 * @auhtor Rémi RAVELLI
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

    private JCheckBox showNails;
    private JCheckBox adaptSize;

    private JTextArea exportArea;
    private CustomButton copyButton;

    public LatexWindow(JFrame frame, Draw d) {
        super(frame, "Exporter en format LaTeX");
        nButtonColor = Color.WHITE;
        nButtonColor = Color.BLACK;
        nColor = null;
        aColor = null;
        nSize = (int) d.getNodeRadius();
        aSize = (int) d.getLineWidth();
        exportArea = null;

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

        Dimension containerSize = new Dimension(400, 170);

        // ONGLET DE CONFIGURATION
        JPanel nodes = new JPanel();
        nodes.setMaximumSize(containerSize);
        nodes.setPreferredSize(containerSize);
        nodes.setLayout(new BoxLayout(nodes, BoxLayout.Y_AXIS));
        nodes.setBorder(BorderFactory.createTitledBorder("Nœuds"));
        JPanel nodesContainer = new JPanel();
        JRadioButton nodeSizeGraph = new JRadioButton("Taille du graphe");
        nodeSizeGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nSize = (int) d.getNodeRadius();
            }
        });

        JRadioButton nodeSizeCust = new JRadioButton("Personnalisée (mm) : ");
        JTextField nodeSizeEntry = new JTextField();
        nodeSizeCust.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nSize = Integer.valueOf(nodeSizeEntry.getText());
            }
        });

        ButtonGroup nodeSizeGroup = new ButtonGroup();
        nodeSizeGroup.add(nodeSizeGraph);
        nodeSizeGroup.add(nodeSizeCust);
        nodeSizeGraph.setSelected(true);

        JPanel nodesSize = createContainer("Taille", new JComponent[]{nodeSizeGraph, nodeSizeCust, nodeSizeEntry});
        nodesContainer.add(nodesSize);

        // Menu de sélection des couleurs
        JRadioButton nodeColGraph = new JRadioButton("Couleur du graphe");
        nodeColGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nColor = null;
                System.out.println(aColor.getRGB());
            }
        });

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
        CustomButton nodeColorButton = createColorButton(frame, NODE_COLOR_BUTTON);

        custNodePanel.add(nodeColorButton);
        JPanel nodesColor = createContainer("Couleur", new JComponent[]{nodeColGraph, nodeColWhite, custNodePanel});
        nodesColor.setLayout(new BoxLayout(nodesColor, BoxLayout.Y_AXIS));
        nodesContainer.add(nodesColor);

        adaptSize = new JCheckBox("Adapter la taille des nœuds en fonction de leur label");
        JPanel wrapperNode = new JPanel(new BorderLayout());
        wrapperNode.add(adaptSize, BorderLayout.PAGE_START);
        nodes.add(nodesContainer);
        nodes.add(wrapperNode);
        config.add(nodes);

        JPanel arcs = new JPanel();
        arcs.setMaximumSize(containerSize);
        arcs.setPreferredSize(containerSize);
        arcs.setLayout(new BoxLayout(arcs, BoxLayout.Y_AXIS));
        JPanel arcsContainers = new JPanel();
        arcs.setBorder(BorderFactory.createTitledBorder("Arcs"));
        JRadioButton arcSizeGraph = new JRadioButton("Épaisseur du graphe");
        arcSizeGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aSize = (int) d.getLineWidth();
            }
        });

        JRadioButton arcSizeCust = new JRadioButton("Personnalisée (mm) : ");
        JTextField arcSizeEntry = new JTextField();
        arcSizeCust.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aSize = Integer.valueOf(arcSizeEntry.getText());
            }
        });

        ButtonGroup arcSizeGroup = new ButtonGroup();
        arcSizeGroup.add(arcSizeGraph);
        arcSizeGroup.add(arcSizeCust);
        arcSizeGraph.setSelected(true);

        JPanel arcsSize = createContainer("Épaisseur", new JComponent[]{arcSizeGraph, arcSizeCust, arcSizeEntry});
        arcsContainers.add(arcsSize);

        // Menu de sélection des couleurs
        JRadioButton arcColGraph = new JRadioButton("Couleur du graphe");
        arcColGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aColor = null;
            }
        });

        JRadioButton arcColWhite = new JRadioButton("Noir");
        arcColWhite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aColor = Color.BLACK;
            }
        });

        arcColCust = new JRadioButton("Personnalisée");
        arcColCust.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aColor = aButtonColor;
            }
        });

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
        CustomButton arcColorButton = createColorButton(frame, ARC_COLOR_BUTTON);

        custArcPanel.add(arcColorButton);
        JPanel arcsColor = createContainer("Couleur", new JComponent[]{arcColGraph, arcColWhite, custArcPanel});
        arcsColor.setLayout(new BoxLayout(arcsColor, BoxLayout.Y_AXIS));
        arcsContainers.add(arcsColor);
        showNails = new JCheckBox("Afficher les clous");
        JPanel wraperArc = new JPanel(new BorderLayout());
        wraperArc.add(showNails, BorderLayout.PAGE_START);

        arcs.add(arcsContainers);
        arcs.add(wraperArc);
        config.add(arcs);

        // ONGLET DE L'EXPORT
        export.setLayout(new BoxLayout(export, BoxLayout.Y_AXIS));
        JPanel wrapperArea = new JPanel();
        exportArea = new JTextArea();
        exportArea.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {}
        @Override
        public void insertUpdate(DocumentEvent e) {
            copyButton.setText("Copier");
            copyButton.setIcon(AssetLoader.copyIco);
            copyButton.unselect();
        }
        @Override
        public void changedUpdate(DocumentEvent arg0) {}
    });

        JScrollPane exportScrollPane = new JScrollPane(exportArea);
        exportScrollPane.setPreferredSize(new Dimension(400, 400));
        wrapperArea.add(exportScrollPane);
        export.add(wrapperArea);

        CustomButton exportButton = new CustomButton("Exporter", AlgoBox.BUTTON_COLOR, AlgoBox.BUTTON_COLOR, null);
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ExportLatex exportLatex = new ExportLatex();
                setExport(exportLatex.export(d, nColor, aColor, nSize, aSize, adaptSize.isSelected(), showNails.isSelected()));
            }
        });
        copyButton = new CustomButton("Copier", AssetLoader.copyIco, AlgoBox.BUTTON_COLOR, AlgoBox.BUTTON_COLOR, Color.decode("#94bc63"));
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                copyButton.setText("Copié");
                copyButton.select();
                copyButton.setIcon(AssetLoader.checkIco);
                Inforeg.Save.Utils.copyToClipboard(exportArea.getText());
            }
        });
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel buttonsContainer = new JPanel();
        
        buttonsContainer.add(exportButton);
        buttonsContainer.add(copyButton);
        
        export.add(buttonsContainer);

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

    private CustomButton createColorButton(Frame frame, int targetColor) {
        CustomButton colorButton = new CustomButton("", null, TOOL_BUTTON_FOCUS_COLOR, TOOL_BUTTON_SELECTED_COLOR);
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
                                if (nodeColCust.isSelected()) { // Si la case est déjà sélectionnée, on actualise la valeur de la couleur.
                                    nColor = nButtonColor;
                                }
                                break;
                            case ARC_COLOR_BUTTON:
                                aButtonColor = c;
                                if (arcColCust.isSelected()) { // Si la case est déjà sélectionnée, on actualise la valeur de la couleur.
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

    public void setExport(String string) {
        exportArea.setText(string);
        exportArea.revalidate();
        exportArea.repaint();
    }

}
