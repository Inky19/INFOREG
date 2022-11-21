package Inforeg.Draw;

/*=============================================
Classe Draw permettant de dessiner les noeuds et arcs
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 08/03/2022
=============================================*/
import Inforeg.ActionMenu;
import Inforeg.Algo.Algorithm;
import Inforeg.Algo.AlgorithmST;
import Inforeg.Graph.Graph;
import Inforeg.Interface;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import Inforeg.ObjetGraph.Nail;
import Inforeg.History;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import Inforeg.UI.Vector2D;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Draw extends JPanel implements MouseMotionListener, DrawFunction {

    private Interface inter;
    /**
     * Piles Ctrl+Z et Ctrl+Y *
     */
    private History transitions = new History();

    //Pour les Nœuds :
    /**
     * Rayon intial des cercles représentants les Nœuds
     */
    private static final double RINIT = 17;
    /**
     * Rayon des cercles représentant les Nœuds, initialisé au rayon initial
     */
    private static double nodeRadius = Draw.RINIT;
    /**
     * Nombre maximum de nœuds d'un graphe, défini dans la classe Graph
     */
    public static final int MAX = 1000;
    /**
     * Liste des cerlces représentant les Nœuds
     */
    //private ArrayList<Node> nodes = new ArrayList<>();
    /**
     * Indice du dernier cercle sélectionné, initialisé à -1
     */
    private Node currentNode = null;
    //private static int countArcClicks = 0;
    /**
     * Couleur courante de la classe, initilisée à bleue
     */
    private Color currentColor = Color.BLUE;
    private JLabel info;
    
    public boolean move;
    // Position précédente avant un déplacement
    private Vector2D prevPos;
    
    /**
     * Dernier algorithme associé à l'onglet
     */
    private Algorithm algo;
    
    /**
     * Active la sélection d'un nœud source et d'un nœud de destination.
     */
    private boolean st;

    /**
     * Valeur du prochain id disponible pour créer un noeud
     */
    private int nextNodeId;
    
    private Node src = null;
    private Node dest = null;
    public boolean oriente;

    private String pathSauvegarde = " ";
    private String fileName;
    private String resultat;

    //Pour les Arcs :
    /**
     * Dernier Nœud sur lequel on a passé la souris
     */
    private Node fromPoint = null;
    /**
     * Liste des Arcs
     */
    //private ArrayList<MyLine> lines = new ArrayList<>();
    /**
     * Arc courant
     */
    private int currentArcIndex = -1;
    /**
     * Initial Line width
     */
    private static final float LINIT = 2;
    /**
     * Line width.
     */
    private float lineWidth = Draw.LINIT;
    /**
     * Définit si le graphe est pondéré ou non
     */
    public boolean pondere = true;

    /**
     * Graph représenté par le Draw
     */
    private Graph G = null;

    //Select many elements
    /**
     * coordonées du rectangle de selection
     */
    private int selectXstart;
    private int selectYstart;
    private int selectXend;
    private int selectYend;
    /**
     * rectangle de selection
     */
    private Rectangle zoneR;
    /**
     *
     */
    private static boolean drawZone = false;
    //Bouton
    private final JSlider zoomSlider;
    private final JLabel zoomLabel;
    //Camera
    private Point currentMousePosition;
    private Point currentCameraPosition;
    private Point camera = new Point(0,0);
    private float zoom = 100f;
    private static final int MAX_ZOOM = 1000;
    private static final int MIN_ZOOM = 50;
    // Icones et images
    private static final ImageIcon fitIco = new ImageIcon("asset/icons/fit.png");
    
    public int getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(int nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    public void setDest(Node n) {
        this.dest = n;
    }

    public void setSrc(Node n) {
        this.src = n;
    }

    public Graph getG() {
        return this.G;
    }

    public void setPondere(boolean bool) {
        this.pondere = bool;
    }

    public boolean getPondere() {
        return this.pondere;
    }

    public float getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(float w) {
        this.lineWidth = w;
    }

    public double getCircleW() {
        return Draw.nodeRadius;
    }

    public void setCircleW(double r) {
        Draw.nodeRadius = r;
    }
    
    public String getPathSauvegarde() {
        return pathSauvegarde;
    }

    public void setPathSauvegarde(String nomSauvegarde) {
        this.pathSauvegarde = nomSauvegarde;
    }

    public boolean getOriente() {
        return oriente;
    }

    public void setOriente(boolean oriente) {
        this.oriente = oriente;
    }
    
    public void setCurrentColor(Color c) {
        this.currentColor = c;
    }

    public ArrayList<Arc> getLines() {
        return G.getLines();
    }

    public ArrayList<Node> getNodes() {
        return G.getNodes();
    }

    public History getTransitions() {
        return this.transitions;
    }

    public void setInterface(Interface inter) {
        this.inter = inter;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Algorithm getAlgo() {
        return algo;
    }

    public void setAlgo(Algorithm algo) {
        this.algo = algo;
    }

    public void setSt(boolean st) {
        this.st = st;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
        inter.refreshResult();
    }
    
    
    

    public Draw(boolean oriente, boolean pondere) {
        resultat = "";
        this.oriente = oriente;
        this.G = new Graph(this);
        this.pondere = pondere;
        this.st = false;
        
        move = false;
        fileName = "";
        nextNodeId = 0;
        // Zoom Toolbar
        this.setLayout(new BorderLayout());
        JToolBar bottomLayout = new JToolBar(JToolBar.HORIZONTAL);
        bottomLayout.setLayout(new BorderLayout());
        JToolBar tools = new JToolBar(null, JToolBar.HORIZONTAL);
        tools.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomLayout.add(tools, BorderLayout.EAST);
        zoomLabel = new JLabel("100%");
        zoomLabel.setPreferredSize(new Dimension(30, 20));
        zoomLabel.setAlignmentX(FlowLayout.RIGHT);
        zoomSlider = new JSlider(MIN_ZOOM, MAX_ZOOM, 100);
        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setPreferredSize(new Dimension(150, 20));
        zoomSlider.addChangeListener((ChangeEvent event) -> {
            move = true;
            zoom = zoomSlider.getValue();
            int value = 10 * (int) (zoomSlider.getValue() / 10);
            zoomLabel.setText(value + "%");
            repaint();
        });
        
        JButton fitToScreen = new JButton(fitIco);
        fitToScreen.setPreferredSize(new Dimension(24, 24));
        fitToScreen.addActionListener((ActionEvent e) -> {
            ArrayList<Node> nodes = G.getNodes();
            if (!nodes.isEmpty()) {
                Double minX = Double.MAX_VALUE;
                Double minY = Double.MAX_VALUE;
                Double maxX = -10000d;
                Double maxY = -10000d;
                for (Node n : nodes) {
                    minX = Double.min(n.getCx(),minX);
                    minY = Double.min(n.getCy(),minY);
                    maxX = Double.max(n.getCx(),maxX);
                    maxY = Double.max(n.getCy(),maxY);    
                }
                float zoomX = (float) (90*Draw.this.getBounds().getWidth()/(maxX-minX+2*Draw.nodeRadius));
                float zoomY = (float) (90*Draw.this.getBounds().getHeight()/(maxY-minY+2*Draw.nodeRadius));
                zoom = (int)Float.min(zoomX,zoomY);
                zoomSlider.setValue((int)zoom);
                zoomLabel.setText((int)zoom+"%");
                camera = new Point((int)(maxX+minX)/2, (int)(maxY+minY)/2);
                repaint();
            }
        });
        info = new JLabel();
        bottomLayout.add(info,BorderLayout.WEST);
        tools.add(fitToScreen);        
        tools.add(zoomSlider);
        tools.add(zoomLabel);
        tools.setFloatable(false);
        tools.setOpaque(false);
        tools.setFocusable(false);
        bottomLayout.setBorderPainted(false);
        bottomLayout.setFloatable(false);
        bottomLayout.setOpaque(false);
        bottomLayout.setFocusable(false);
        bottomLayout.setBorderPainted(false);
        this.add(bottomLayout, BorderLayout.SOUTH);
        // Init camera position
        currentCameraPosition = new Point(camera);
        Draw d = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                
                currentMousePosition = evt.getLocationOnScreen();
                currentCameraPosition = new Point(camera);
                
                switch (evt.getButton()){
                    case MouseEvent.BUTTON1:
                        
                        if (inter.getMode() == inter.EDITION_MODE) {
                            int x = evt.getX();
                            int y = evt.getY();
                            // Vérifie si on clique où non sur un cercle existant
                            currentNode = findEllipse(x, y);
                            currentArcIndex = getArc(x, y);
                            // Si on souhaite ajouter un Nœud :
                            switch (inter.getActiveTool()){
                                case Interface.NOEUD_TOOL:
                                    if (currentNode == null && currentArcIndex < 0) { // not inside a circle
                                        addNode(x, y);
                                        // On ajoute l'action à la pile
                                        transitions.createLog(History.ADD_NODE, G.getNodes().get(G.getNodes().size() - 1));
                                    }
                                    
                                    break;
                                // Si on souhaite ajouter un label à un Nœud :
                                case Interface.LABEL_TOOL:
                                    if (currentNode != null) { // inside a circle

                                        ActionMenu.renameNode(d, currentNode);

                                    } else if (currentArcIndex >= 0) {
                                        if (pondere) {
                                            String text = JOptionPane.showInputDialog("Entrer le nouveau poids de l'Arc (seuls les entiers seront acceptés):");
                                            try {
                                                int pds = Integer.parseInt(text);
                                                int currentPds = G.getLines().get(currentArcIndex).getPoids();
                                                Arc line = G.getLines().get(currentArcIndex);
                                                line.setPoids(pds);
                                                repaint();
                                                // On ajoute l'action à la pile
                                                transitions.createLog(History.LABEL_ARC, line, Integer.toString(currentPds), text);
                                            } catch (Exception e) {
                                                System.out.println("Pas un entier !");
                                            }
                                        }
                                    }
                                    break;
                                
                                case Interface.ARC_TOOL:
                                    if ((currentNode != null) && (fromPoint == null)) {
                                        fromPoint = currentNode;
                                        fromPoint.setSelect(true);
                                    } else if (fromPoint != null && currentNode == null) {
                                        fromPoint.setSelect(false);
                                        fromPoint = null;
                                    }  
                                    else if ((currentNode != null) && (fromPoint != null)) { // inside circle
                                        Node p = currentNode;
                                        p.setSelect(true);
                                        repaint();
                                        if (pondere) {
                                            String text = JOptionPane.showInputDialog("Entrer le poids de l'Arc (seuls les entiers seront acceptés):");
                                            try {
                                                int pds = Integer.parseInt(text);
                                                Arc newLine = new Arc(fromPoint, p, pds, currentColor);
                                                if (!G.lineExist(newLine)) {
                                                    addLine(newLine);
                                                    // On ajoute l'action à la pile
                                                    transitions.createLog(History.ADD_ARC, newLine);
                                                }
                                                fromPoint.setSelect(false);
                                                fromPoint = null;


                                            } catch (Exception e) {
                                                System.out.println("Pas un entier !");
                                                //fromPoint = null;
                                            } finally {
                                                if (fromPoint!=null) {
                                                    fromPoint.setSelect(false);
                                                    fromPoint = null;                                                    
                                                }

                                            }
                                        } else {
                                            Arc newLine = new Arc(fromPoint, p, 1, currentColor);
                                            if (!G.lineExist(newLine)) {
                                                addLine(newLine);
                                                // On ajoute l'action à la pile
                                                transitions.createLog(History.ADD_ARC, newLine);
                                            }
                                            fromPoint.setSelect(false);
                                            fromPoint = null;
                                        }
                                        p.setSelect(false);
                                
                                    }
                            repaint();
                                    break;
                                
                                case Interface.SELECT_TOOL:
                                    if (currentNode == null && currentArcIndex < 0) {//not on circle or arc
                                        for (Node n: G.getNodes()){
                                            n.setMultiSelected(false);
                                        }
                                        for (Arc a: G.getLines()){
                                            a.setSelected(false);
                                        }
                                        selectXstart = x;
                                        selectYstart = y;
                                    }
                                    break;
                            }
                            if (inter.getActiveTool() != Interface.SELECT_TOOL){
                                                               
                                    for (Node n: G.getNodes()){
                                        n.setMultiSelected(false);
                                    }
                                    for (Arc a: G.getLines()){
                                        a.setSelected(false);
                                    }
                            }
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        if ((inter.getMode() == inter.EDITION_MODE)){
                            int x = evt.getX();
                            int y = evt.getY();
                            Node n = findEllipse(x, y);
                            if (n != null){
                                inter.rightClickNode(n, x, y);
                            }
                        } 

                        break;
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                move = false;
                switch (evt.getButton()){
                    case MouseEvent.BUTTON1: // Clic gauche
                        if (inter.getMode() == inter.EDITION_MODE) {
                            int x = evt.getX();
                            int y = evt.getY();
                            // Vérifie si on clique où non sur un cercle existant
                            currentNode = findEllipse(x, y);
                            if (currentNode != null && prevPos!=null) {
                                Node n = currentNode;
                                transitions.createLog(History.MOVE_NODE, n, prevPos.x, prevPos.y, n.getCx(), n.getCy());
                                prevPos = null;
                            }
                            if (currentArcIndex>=0 && prevPos!=null) {
                                Nail n = G.getLines().get(currentArcIndex).getClou();
                                transitions.createLog(History.MOVE_NAIL, n, prevPos.x, prevPos.y, n.getCx(), n.getCy());
                                prevPos = null;
                            }
                        }
                        if (inter.getActiveTool() == inter.SELECT_TOOL) {
                            drawZone = false;
                            for (Node n: G.getNodes()){
                                int x = (int) n.getCenterX();
                                int y = (int) n.getCenterY();
                                if (zoneR.contains(x, y)) {
                                    n.setMultiSelected(true);
                                }
                            }
                            for (Arc a: G.getLines()){
                                int x = a.getClouPoint().x;
                                int y = a.getClouPoint().y;
                                if (zoneR.contains(x, y)) {
                                    a.setSelected(true);
                                }
                            }
                            repaint();
                        }
                        break;
                      
                }
                 
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                move = false;
                if (inter.getMode() == inter.EDITION_MODE) {
                    int x = evt.getX();
                    int y = evt.getY();
                    currentArcIndex = getArc(x, y);
                    currentNode = findEllipse(x, y);
                    // Si on clique deux fois sur un Nœud, on le supprime
                    if (inter.getActiveTool() == Interface.NOEUD_TOOL && currentNode != null) {
                        if (evt.getClickCount() >= 2) {
                            // On ajoute l'action à la pile
                            // On ajoute les arcs qui seront supprimés
                            if (G.getLines().size() > 0) {
                                for (int i = 0; i < G.getLines().size(); i++) {
                                    Arc l = G.getLines().get(i);
                                    if (l.getFrom().equals(currentNode) || l.getTo().equals(currentNode)) {
                                        transitions.createLog(History.REMOVE_ARC, l);
                                    }
                                }
                            }
                            // La reconstruction du noeud sera placée au haut de la pile
                            
                            //
                            ActionMenu.deleteNode(d, currentNode);
                        }
                    }
                    if (inter.getActiveTool() == inter.ARC_TOOL) {
                        if (evt.getClickCount() >= 2 && currentArcIndex >= 0) {
                            // On ajoute l'action à la pile
                            Arc toDelete = G.getLines().get(currentArcIndex);
                            transitions.createLog(History.REMOVE_ARC, toDelete);
                            //
                            removeArc(currentArcIndex);
                        }
                    }
                    if (inter.getActiveTool() == inter.SELECT_TOOL) {
                        if (currentNode == null && currentArcIndex < 0) {//not on circle or arc
                            for (Node n: G.getNodes()){
                                n.setMultiSelected(false);
                            }
                            for (Arc a: G.getLines()){
                                a.setSelected(false);
                            }
                        }
                    }
                }
                if (inter.getMode() == inter.TRAITEMENT_MODE) {
                    if (st) {
                        int x = evt.getX();
                        int y = evt.getY();
                        if (src == null) {
                            src = findEllipse(x, y);
                            if (src != null) {
                               src.setColor(Color.GREEN); 
                            }
                            repaint();
                        } else if (dest == null) {
                            dest = findEllipse(x, y);
                            if (dest != null) {
                                dest.setColor(Color.RED);
                                repaint();
                                ((AlgorithmST) algo).process(d, src, dest);
                                src = null;
                                dest = null;
                                st = false;
                            } else {
                                src.reinit();
                                repaint();
                                src = null;
                            }
                        }
                    }
                }
            }
        });

        addMouseMotionListener(this);

    }

    //Méthode permettant de draw les éléments. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Toolkit.getDefaultToolkit().sync();
        if (!move){
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
        //order : draw line puis draw circles
        for (Arc a: G.getLines()){
            a.paint(this, (Graphics2D) g);
        }
        // Draw circles
        for (Node n : G.getNodes()){
            n.paint(this, (Graphics2D) g);  
        }
        // Multiselect zone
        if (Draw.drawZone) {
            ((Graphics2D) g).draw(this.zoneR);
        }

    }

    /**
     * Permet de convertir des coordonnées globales en coordonnées de la zone de
     * dessin
     *
     * @param x
     * @param y
     */
    public Vector2D toDrawCoordinates(double x, double y) {
        Rectangle r = this.getBounds();
        int W = r.width, H = r.height;
        int w = (int) (W * 100/zoom);
        int h = (int) (H * 100/zoom);
        int newX = (int) (( x - camera.x + w/2) * zoom/100);
        int newY = (int) (( y - camera.y + h/2) * zoom/100);
        return new Vector2D(newX,newY);
    }
    
    /**
     * Renvoie le nœud correspondant à l'id en paramètre dans la liste nodes.
     * @param id Id du nœud recherché
     * @return Nœud correspondant à l'id en paramètre. Renvoie null si non trouvé.
     */
    public Node getNodeFromId(int id){
        for (Node node: G.getNodes()){
            if (node.getId() == id){
                return node;
            }
        }
        return null;
    }

    /**
     * Permet de convertir des coordonnées de la zone de dessin en coordonnées
     * globales
     *
     * @param x
     * @param y
     */
    public Vector2D toGlobalCoordinates(double x, double y) {
        Rectangle r = this.getBounds();
        int W = r.width, H = r.height;
        int w = (int) (W * 100/zoom);
        int h = (int) (H * 100/zoom);
        int newX = (int) (x * 100 / zoom + camera.x - w/2);
        int newY = (int) (y * 100 / zoom + camera.y - h/2);        
        return new Vector2D(newX,newY);
    }

    /**
     * Redimensionne une dimension à l'échelle de la zone de dessin
     * @param h
     * @return
     */
    public double toDrawScale(double h) {
        return h * zoom / 100;
    }

    /**
     * Redimensionne une dimension à l'échelle globale.
     * @param h
     * @return
     */
    public double toGlobalScale(double h) {
        return h * 100 / zoom;
    }

    /**
     * Vérifie que l'on clique sur un cercle et donne son indice dans la liste
     * nodes
     *
     * @param x = coordonnée x du pointeur de la souris
     * @param y = coordonnée y du pointeur de la souris
     * @return -1 si on ne clique pas sur un cercle, l'indice du cercle dans la
     * liste sinon
     */
    public Node findEllipse(int x, int y) {
        for (int i = 0; i < G.getNodes().size(); i++) {
            if (G.getNodes().get(i).contains(x, y)) { // inside a circle
                return G.getNodes().get(i);
            }
        }
        return null;
    }

    public int getArc(int x, int y) {
        for (int i = 0; i < G.getLines().size(); i++) {
            if (G.getLines().get(i).getClou().contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ajoute un cercle dans la liste nodes et actualise l'affichage
     *
     * @param x = abcsisse du cercle à dessiner
     * @param y = ordonnée du cercle à dessiner
     */
    public void addNode(double x, double y) {
        inter.tabSaved(false);
        nextNodeId++;
        //On ajoute un cercle à la liste nodes et on actualise les attributs concernés
        Vector2D v = toGlobalCoordinates((int)x,(int)y);
        //G.addNode(new Node(v.x, v.y, nodeRadius, String.valueOf(G.getNodes().size()), nextNodeId));
        G.addNode(v.x, v.y, nodeRadius);
        currentNode = G.getNodes().get(G.getNodes().size()-1);
        //On actualise l'affichage avec le nouveau cercle
        repaint();
    }
    @Deprecated
    public int find(Ellipse2D.Double circ) {
        boolean trouve = false;
        int n = 0;
        Ellipse2D.Double comp;
        while ((n < G.getNodes().size()) && (!trouve)) {
            comp = this.G.getNodes().get(n);
            if (Double.compare(comp.x, circ.x) == 0
                    && Double.compare(comp.y, circ.y) == 0) {
                trouve = true;
                return n;
            } else {
                n++;
            }
        }
        return -1;
    }

    /**
     * Ajoute une ligne dans la ArrayList lines et actualise l'affichage
     *
     * @param line = ligne à ajouter
     */
    public void addLine(Arc line) {
        inter.tabSaved(false);
        G.addLine(line);
        repaint(); 
    }

    public Arc findLine(int src, int dest) {
        return G.findLine(src, dest);
    }
    
    public Arc findLine(Node from, Node to) {
        return G.findLine(from, to);
    }

   
    /**
     * Permet de changer l'état de l'indicateur de sauvegarde de l'onglet.
     * @param saved 
     */
    public void saveState(boolean saved){
        inter.tabSaved(saved);
    }
    
    public void removeLine(Arc arc) {
        inter.tabSaved(false);
        G.removeLine(arc);
        repaint(); 
    }

    @Deprecated
    public void removeArc(int n) {
        if (n < 0 || n >= G.getLines().size()) {
            return;
        } else {
            Arc l = G.getLines().get(n);
            G.getLines().remove(l);
        }
        repaint();
    }

    /**
     * Modifie le curseur lorsqu'on se trouve sur un cercle
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        Node n = findEllipse(x, y);
        int a = getArc(x, y);
        if (a>=0) {
            info.setText(G.getLines().get(a).toString());
        } else if (n != null) {
            info.setText(n.toString());
        } else {
            info.setText(null);
        }
        
        if (n != null || a >= 0) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Déplace un cercle et les lignes qui lui sont rattachées
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        move = true;
        int mouseX = event.getX();
        int mouseY = event.getY();
        Vector2D v = toGlobalCoordinates(mouseX, mouseY);
        // Global coordinates of the mouse
        int x = (int) v.x;
        int y = (int) v.y;
        if ((inter.getActiveTool() == inter.NOEUD_TOOL || inter.getActiveTool() == inter.SELECT_TOOL) && inter.getMode() == inter.EDITION_MODE) {
            if (currentNode != null) {
                inter.tabSaved(false);
                if (currentNode.isSelected()) {
                    double transx = x - currentNode.getCx();
                    double transy = y - currentNode.getCy();

                    for (Node n: G.getNodes()){
                        if (n.isSelected()){
                            n.addCx(transx);
                            n.addCy(transy);
                        }
                    }
                    for (Arc a: G.getLines()){
                        if (a.isSelected()) {
                            Nail clou = a.getClou();
                            clou.cx += transx;
                            clou.cy += transy;
                        }
                    }
                    repaint();
                } else {
                    // On ajoute l'action à la pile
                    if (prevPos==null) {
                        prevPos = new Vector2D(currentNode.getCx(),currentNode.getCy());
                    }
                    currentNode.updatePos(x, y);
                    zoneR = new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0); //permet d'éviter qu'un ensemble de points soient toujours sélectionner
                    //après les avoir déselectionner en cliquant a cote
                    repaint();
                }
            }
        }
        if ((inter.getActiveTool() == inter.ARC_TOOL || inter.getActiveTool() == inter.SELECT_TOOL) && inter.getMode() == inter.EDITION_MODE) {

            if (currentArcIndex >= 0) {
                inter.tabSaved(false);
                if (G.getLines().get(currentArcIndex).isSelected()) {
                    double transx;
                    Nail transClou = G.getLines().get(currentArcIndex).getClou();
                    transx = x - transClou.cx;
                    double transy;
                    transy = y - transClou.cy;

                    for (Node n: G.getNodes()){
                        if (n.isSelected()){
                            n.addCx(transx);
                            n.addCy(transy);
                        }
                    }
                    for (Arc a: G.getLines()){
                        if (a.isSelected()){
                            Nail clou = a.getClou();
                            clou.cx += transx;
                            clou.cy += transy;
                        }
                    }
                    repaint();
                } else {
                    Arc line = G.getLines().get(currentArcIndex);
                    if (prevPos == null) {
                        prevPos = new Vector2D(line.getClou().cx,line.getClou().cy);
                    }
                    line.getClou().cx = x;
                    line.getClou().cy = y;

                    zoneR = new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0); //permet d'éviter qu'un ensemble de points soient toujours sélectionner
                    //après les avoir déselectionner en cliquant a cote
                    repaint();
                }
            }
        }
        if (inter.getActiveTool() == inter.SELECT_TOOL) {
            selectXend = event.getX();
            selectYend = event.getY();

            if (currentNode == null && currentArcIndex < 0) {
                int px = Math.min(selectXstart, selectXend);
                int py = Math.min(selectYstart, selectYend);
                int pw = Math.abs(selectXstart - selectXend);
                int ph = Math.abs(selectYstart - selectYend);
                this.zoneR = new Rectangle(px, py, pw, ph);
                Draw.drawZone = true;
                repaint();
            }
        }
        
        if (inter.getMode() == inter.DEPLACEMENT_MODE) {
            Point currentScreenLocation = event.getLocationOnScreen();
            camera.x = (int) (currentCameraPosition.x + toGlobalScale(currentMousePosition.x - currentScreenLocation.x));
            camera.y = (int) (currentCameraPosition.y + toGlobalScale(currentMousePosition.y - currentScreenLocation.y));
            Draw.drawZone = false;
                       
            repaint();
        }
        
        
        if (inter.getMode() == inter.TRAITEMENT_MODE) {
            Draw.drawZone = false;
            repaint();
        }
    }

    public void reinit() {
        for (Node n : G.getNodes()) {
            n.reinit();
        }
        for (Arc a: G.getLines()){
            a.setColor(Color.BLUE);
            a.setFlow(null);
        }
    }

    public void doRedraw(){
         getTopLevelAncestor().revalidate();
         getTopLevelAncestor().repaint();
     }
    

    /**
     *
     * @return Taille des cercles
     */
    public double getTailleCirc() {
        return (float) inter.getTaille() / 20;
    }

    /**
     * Méthode permettant de modifier la taille des noeuds
     */
    public void tailleCirc() {
        if (G.getNodes().size() > 0) {
            double factor = getTailleCirc();
            nodeRadius = factor * Draw.RINIT;
            //lineWidth = (float) factor*Draw.LINIT;
            for (Node n: G.getNodes()){
                n.updateSize(nodeRadius);
            }
            repaint();
        }
    }

    /**
     * Méthode permettant de modifier l'épaisseur des arcs et des périmètres des
     * noeuds
     */
    public void epaisseurLines() {
        if (!G.getNodes().isEmpty()) {
            double factor = (float) inter.getEpaisseur() / 5;
            lineWidth = (float) factor * Arc.DEFAULT_LINE_WIDTH;
            System.out.println(lineWidth);
            for (Arc l: G.getLines()){
                l.width = (int)lineWidth;
            }
            repaint();
        }
    }

    /**
     * Méthode permettant d'actualiser le Graph G représenté par le Draw
     */
    public void exportGraphe() {
        G.updateVariable();
    }

    
}
