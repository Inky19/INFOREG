/*=============================================
Classe Interface
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 08/03/2022
=============================================*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class Interface{

    protected JFrame frame;
    
    /** Les JPanel. */
    protected JToolBar toolBarButtons;
    protected JPanel paneImage;
    protected Draw d;
    
    
    /** Le Menu. */ 
    protected JMenuBar menuBar;
    protected JMenu exporter;
    
    /** Les boutons. */
    protected JRadioButton select;
    protected JRadioButton noeud;
    protected JRadioButton arc;
    protected JRadioButton label;
    protected JRadioButton edition;
    protected JRadioButton traitement;
    protected JButton save;
    protected JButton load;
    protected JButton clearSelection;
    protected JButton back ;
    protected JButton forward;
    
    /** Reference to the original image. */
    protected BufferedImage originalImage;
    /** Image used to make changes. */
    protected BufferedImage canvasImage;
    
    /** Couleur utilisée. */
    protected Color color = Color.WHITE;
    protected BufferedImage colorSample = new BufferedImage(16,16,BufferedImage.TYPE_INT_RGB);
    protected Rectangle selection;
    protected static Color colorBg ;
    
    protected RenderingHints renderingHints;
    protected JLabel imageLabel;
    
    /** Tools pour savoir l'état/bouton selectionné. */
    protected static int activeTool;
    public static final int SELECT_TOOL = 10;
    public static final int NOEUD_TOOL = 11;
    public static final int ARC_TOOL = 12;
    public static final int LABEL_TOOL = 13;
    protected static int mode;
    public static final int EDITION_MODE = 1;
    public static final int TRAITEMENT_MODE = 2;
    protected static int activeTraitement;
    public static final int PRIM_TRAITEMENT = 21;
    public static final int DIJKSTRA_TRAITEMENT = 22;
    public static final int KRUSKAL_TRAITEMENT = 23;
    public static final int FORD_FULKERSON_TRAITEMENT = 24;
    
    /** Attribut pour la taille des Noeuds. */
    protected static int taille ;
    /** Attribut pour l'épaisseur des Arcs. */
    protected static int epaisseur ;

        /** Actions */
    /** Action de sauvegarde du graphe dans une sauvegarde existante */
    public final AbstractAction Save = new AbstractAction(){
        {
            putValue(Action.NAME,"Enregistrer");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(Action.SHORT_DESCRIPTION,"Sauvegarde le graphe actuel (CTRL+S)");
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e){
            // Si un fichier de sauvegarde existe déjà, on l'écrase et on effectue une nouvelle sauvegarde
            if (d.getPathSauvegarde()!=" "){
                File f = new File(d.getPathSauvegarde());
                (new SauvDraw(f)).sauvegarderDraw(d);
            // Sinon, on créé un nouveau fichier de sauvegarde
            } else {
                try {
                    JFileChooser dialogue = new JFileChooser(".");
                    if (dialogue.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                        File fichier = dialogue.getSelectedFile();
                        String source = fichier.getName();
                        if (source.length() < 8 || !source.toLowerCase().substring(source.length()-8).equals(".inforeg")) {
                            d.setPathSauvegarde(fichier.getPath() + ".inforeg");
                        } else {
                            d.setPathSauvegarde(fichier.getPath());
                        }
                        (new SauvDraw(fichier)).sauvegarderDraw(d);
                    }
                } catch (Exception NullPointerException){
                    System.out.println("Opération annulée");
                }
            }
        };
    };

    /** Création d'unnouveau fichier de sauvegarde */
    public final AbstractAction SaveAs = new AbstractAction(){
        {
            putValue(Action.NAME,"Enregistrer Sous");
            putValue(Action.SHORT_DESCRIPTION,"Sauvegarde le graphe actuel");
        }

        @Override
        public void actionPerformed(ActionEvent e){
            try {
                JFileChooser dialogue = new JFileChooser(".");
                if (dialogue.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    File fichier = dialogue.getSelectedFile();
                    String source = fichier.getName();
                    if (source.length() < 8 || !source.toLowerCase().substring(source.length()-8).equals(".inforeg")) {
                        d.setPathSauvegarde(fichier.getPath() + ".inforeg");
                    } else {
                        d.setPathSauvegarde(fichier.getPath());
                    }
                    (new SauvDraw(fichier)).sauvegarderDraw(d);
                }
            } catch (Exception NullPointerException){
                System.out.println("Opération annulée");
            }
        };
    };

    /**
     * Constructeur d'une interface
     * @param d = instance de Draw permettant de dessiner le graphe
     */
    public Interface(Draw d){
        this.d = d;
    }

    /**
     * Affichage de l'interface
     */
    public void createAndShowGui() {

        frame = new JFrame("INFOREG "+d.getPathSauvegarde());
        //fermer la fenêtre quand on quitte
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        initToolBar();  
        addToolBar();      
        initPaneImage();
        initLeftMenuBar();
        addMenuBar();
        initRightMenuBar();
        frame.add(toolBarButtons, BorderLayout.LINE_START);
        frame.add(paneImage,BorderLayout.CENTER);
        Interface.colorBg = paneImage.getBackground();
        frame.setJMenuBar(menuBar);
        
        frame.getContentPane().add(this.d);
        this.d.repaint();
        
        frame.pack();
        
        frame.setVisible(true);
            
    }
    
    /**
     * JPanel pour les boutons 
     **/
    public abstract void initToolBar() ;

    public abstract void addToolBar();

    public void initLeftMenuBar(){

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem ouvrir = new JMenuItem("Ouvrir");
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

    public void initRightMenuBar(){
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
                JOptionPane.showMessageDialog(frame ,str,"Bouton Noeud", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JMenuItem helpSubMenuItem2 = new JMenuItem("Help Sub menu item 2");
        helpMenu.add(helpSubMenu);
        helpSubMenu.add(helpSubMenuItem1);
        helpSubMenu.add(helpSubMenuItem2);
        
        JMenuItem credits = new JMenuItem("Crédits");
        aboutMenu.add(credits);
        
        credits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String creditStr = "Application créée par Béryl CASSEL, Cristobal CARRASCO DE RODT, Jorge QUISPE , Isaías VENEGAS et Samy AMAL \n"
                                    + "\n"
                                    + "dans le cadre du projet de groupe INFOREG \n"
                                    + "\n"
                                    + "encadré par Olivier ROUX";
                JOptionPane.showMessageDialog(frame ,creditStr,"Credits", JOptionPane.INFORMATION_MESSAGE);
            }
        });
                
        menuBar.add(helpMenu); 
        menuBar.add(aboutMenu);
        
        //CTRL Z / CTRL Y
        ImageIcon iconBack = new ImageIcon("back.png");
        ImageIcon iconForward = new ImageIcon("forward.png");
        //resize
        Image imageBack = iconBack.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_AREA_AVERAGING);
        Image imageForward = iconForward.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_AREA_AVERAGING);
        iconBack = new ImageIcon(imageBack); 
        iconForward = new ImageIcon(imageForward);
        Transitions piles = d.getTransitions();
        back = new JButton(iconBack);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Collection<Enregistrement> pileZ = piles.getPreviousStates();
                if(pileZ.isEmpty()){
                    return;
                }
                Ellipse2D.Double[] circles = d.getCirc();
                Enregistrement lastReg = piles.getPreviousState();
                // Pour chaque action, on effectue l'action inverse
                // Ensuite, on déplace l'action sur l'autre pile
                switch(lastReg.action){
                    case "addCircle":
                        d.remove(d.find(lastReg.noeud));
                        break;
                    case "moveCircle":
                        // On omettra les registres de mouvements intermédiaires
                        Enregistrement previousReg = lastReg;
                        Enregistrement temp = piles.getPreviousState();
                        // Pour cela, comparons que l'enregistrement précédent 
                        // a comme position de fin la position de début de 
                        // l'enregistrement actuel.
                        if(temp != null){
                            while(
                                    temp.action.equals("moveCircle") && 
                                    Double.compare(temp.x2, previousReg.x) == 0 && 
                                    Double.compare(temp.y2, previousReg.y) == 0)
                            {
                                // Si c'est le cas, nous stockons cet enregistrement intermédiaire 
                                // jusqu'à ce que l'enregistrement précédent soit 
                                // une action ou un élément différent.
                                previousReg = temp;
                                temp = piles.getPreviousState();
                                if(temp==null){break;}
                            }
                        }
                        // On ajoute l'enregistrement à la liste 
                        // comme il a été retiré dans le cycle précédent.
                        if(temp != null)
                        {
                            piles.addPreviousState(temp);
                        }
                        Ellipse2D.Double noeud = circles[d.find(lastReg.noeud)];
                        noeud.x = previousReg.x;
                        noeud.y = previousReg.y;
                        d.repaint();
                        // On crée l'enregistrement équivalent qui sera pris par la pile Ctrl+Y. 
                        // Il ne sera pas nécessaire de répéter cette procédure de nettoyage.
                        piles.reCreateLog("moveCircle", noeud, previousReg.x, previousReg.y, lastReg.x2, lastReg.y2);
                        // On le prend pour qu'il soit finalement ajouté à la pile Ctrl+Y
                        lastReg = piles.getPreviousState();
                        break;
                    case "deleteCircle":
                        d.add(lastReg.x, lastReg.y);
                        break;
                    case "addLine":
                        int fromIndex = d.find(lastReg.noeud);
                        int toIndex = d.find(lastReg.noeud2);
                        d.removeArc(d.findLine(fromIndex,toIndex));
                    case "moveLine":
                        MyLine line = lastReg.arc;
                        line.setClou(lastReg.noeud);
                        d.repaint();
                        break;
                    case "deleteLine":
                        MyLine l = lastReg.arc;
                        // Mettre à jour les nœuds source et destination
                        Ellipse2D.Double pFrom = circles[d.find(lastReg.noeud)];
                        Ellipse2D.Double pTo = circles[d.find(lastReg.noeud2)];
                        MyLine updatedL = new MyLine(pFrom, pTo, l.getPoids(), l.getC());
                        d.addLine(updatedL);
                        break;
                    case "updateLbl":
                        d.getCircLbl()[d.find(lastReg.noeud)] = lastReg.lastLbl;
                        d.repaint();
                        break;
                    case "updatePds":
                        lastReg.arc.setPoids(Integer.parseInt(lastReg.lastLbl));
                        d.repaint();
                        break;
                    default:
                        break;
                }
                piles.addNextState(lastReg);
            }
        });
        forward = new JButton(iconForward);
        forward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Collection<Enregistrement> pileY = piles.getNextStates();
                if(pileY.isEmpty()){
                    return;
                }
                Ellipse2D.Double[] circles = d.getCirc();
                Enregistrement nextReg = piles.getNextState();
                // Pour chaque action, on l'exécute
                // Ensuite, on déplace l'action sur l'autre pile
                switch(nextReg.action){
                    case "addCircle":
                        d.add(nextReg.x, nextReg.y);
                        break;
                    case "moveCircle":
                        Ellipse2D.Double noeud = circles[d.find(nextReg.noeud)];
                        noeud.x = nextReg.x2;
                        noeud.y = nextReg.y2;
                        d.repaint();
                        break;
                    case "deleteCircle":
                        d.remove(d.find(nextReg.noeud));
                        break;
                    case "addLine":
                        MyLine l = nextReg.arc;
                        // Mettre à jour les nœuds source et destination
                        Ellipse2D.Double pFrom = circles[d.find(nextReg.noeud)];
                        Ellipse2D.Double pTo = circles[d.find(nextReg.noeud2)];
                        MyLine updatedL = new MyLine(pFrom, pTo, l.getPoids(), l.getC());
                        d.addLine(updatedL);
                        break;
                    case "moveLine":
                        MyLine line = nextReg.arc;
                        line.setClou(nextReg.noeud2);
                        d.repaint();
                        break;
                    case "deleteLine":
                        int fromIndex = d.find(nextReg.noeud);
                        int toIndex = d.find(nextReg.noeud2);
                        d.removeArc(d.findLine(fromIndex,toIndex));
                        break;
                    case "updateLbl":
                        d.getCircLbl()[d.find(nextReg.noeud)] = nextReg.newLbl;
                        d.repaint();
                        break;
                    case "updatePds":
                        nextReg.arc.setPoids(Integer.parseInt(nextReg.newLbl));
                        d.repaint();
                        break;
                    default:
                        break;
                }
                piles.addPreviousState(nextReg);
            }
        });
        //placer les back/forward à droite 
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(back);
        menuBar.add(forward);

    }
    
    /** 
     * Méthode permettant de modifier la couleur
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
    public void initPaneImage(){

        Map<RenderingHints.Key, Object> hintsMap = new HashMap<>();
        hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderingHints = new RenderingHints(hintsMap); 

        setImage(new BufferedImage(320,240,BufferedImage.TYPE_INT_RGB));
        paneImage = new JPanel(new BorderLayout(4,4));
        paneImage.setBorder(new EmptyBorder(5,3,5,3));
        
        JPanel imageView = new JPanel(new GridBagLayout());
        imageView.setPreferredSize(new Dimension(480,320));
        imageLabel = new JLabel(new ImageIcon(canvasImage));
        JScrollPane imageScroll = new JScrollPane(imageView);
        imageView.add(imageLabel);
        //imageLabel.addMouseMotionListener(new ImageMouseMotionListener());
        //imageLabel.addMouseListener(new ImageMouseListener());
        paneImage.add(imageScroll,BorderLayout.CENTER);
        
        clear(colorSample);
        clear(canvasImage);
        
    }
    
    /**
     * Méthode venant de BasicPaint
     * @param image 
     */
    public void setImage(BufferedImage image) {
        this.originalImage = image;
        int w = image.getWidth();
        int h = image.getHeight();
        canvasImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = this.canvasImage.createGraphics();
        g.setRenderingHints(renderingHints);
        g.drawImage(image, 0, 0, paneImage);
        g.setColor(this.color);
        g.dispose();

        selection = new Rectangle(0,0,w,h); 
        if (this.imageLabel!=null) {
            imageLabel.setIcon(new ImageIcon(canvasImage));
            this.imageLabel.repaint();
        }
        //if (gui!=null) {
        //    gui.invalidate();
        //}
    }
    
    /** Clears the entire image area by painting it with the current color. */
    public void clear(BufferedImage bi) {
        Graphics2D g = bi.createGraphics();
        g.setRenderingHints(renderingHints);
        g.setColor(color);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        g.dispose();
        imageLabel.repaint();
    }
    
}
