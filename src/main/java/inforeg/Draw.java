package inforeg;

/*=============================================
Classe Draw permettant de dessiner les noeuds et arcs
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 08/03/2022
=============================================*/

import inforeg.ObjetGraph.Noeud;
import inforeg.ObjetGraph.Clou;
import java.awt.BasicStroke;
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
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import utilities.Vector2D;
 
public class Draw extends JPanel implements MouseMotionListener, FonctionsDessin {
    
    private Interface Interface;
    /** Piles Ctrl+Z et Ctrl+Y **/
    private Transitions transitions = new Transitions();
    
    //Pour les Nœuds :
    /** Rayon intial des cercles représentants les Nœuds */
    private static final double RINIT = 17;
    /** Rayon des cercles représentant les Nœuds, initialisé au rayon initial */
    private static double circleW = Draw.RINIT;
    /** Nombre maximum de nœuds d'un graphe, défini dans la classe Graphe */
    public static final int MAX = Graphe.nbmax;
    /** Liste des cerlces représentant les Nœuds*/
    private ArrayList<Noeud> nodes = new ArrayList<>();
    /** Nombre courant de cercle (i.e de Nœud du Graphe) */
    private int numOfCircles = 0;
    /** Indice du dernier cercle sélectionné, initialisé à -1 */
    private int currentCircleIndex = -1;
    //private static int countArcClicks = 0;
    /** Couleur courante de la classe, initilisée à bleue */
    private Color currentColor = Color.BLUE;
    
    /** Valeur du prochain id disponible pour créer un noeud */
    private int nextNodeId;

    private int src = -1;
    private int dest = -1;
    public int oriente = -1;
    public static final int ORIENTE = 0;
    public static final int NONORIENTE = 1;
    private String pathSauvegarde = " ";
    
    //Pour les Arcs :
    /** Dernier Nœud sur lequel on a passé la souris */
    private Noeud fromPoint = null ;
    /** Liste des Arcs */
    private ArrayList<MyLine> lines = new ArrayList<>();
    /** Nombre d'Arcs dessinés */
    private int numOfLines = 0;
    /** Arc courant */
    private int currentArcIndex = -1;
    /** Initial Line width */
    private static final float LINIT = 2;
    /** Line width. */
    private float lineWidth = Draw.LINIT;
    /** Définit si le graphe est pondéré ou non */
    public boolean pondere = true;
    
    /** Graphe représenté par le Draw */
    private Graphe G = null;
    
    //Select many elements
    /** coordonées du rectangle de selection */
    private int selectXstart ;
    private int selectYstart ;
    private int selectXend ;
    private int selectYend ;
    /** rectangle de selection */
    private Rectangle zoneR ;
    /**  */
    private static boolean drawZone = false ;
    /** listes de booléens - éléments à déplacer en même temps */
    private boolean[] multiSelecCirc = new boolean[MAX];
    private boolean[] multiSelecArc = new boolean[MAX];
    //Bouton
    private final JSlider zoomSlider;
    private final JLabel zoomLabel;
    //Camera
    private int cameraX = 0;
    private int cameraY = 0;
    private float zoom = 100f;
    private static final int MAX_ZOOM = 500;
    private static final int MIN_ZOOM = 50;

    public int getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(int nextNodeId) {
        this.nextNodeId = nextNodeId;
    }
    
    public void setDest(int i) {
        this.dest = i;
    }

    public void setSrc(int i) {
        this.src = i;
    }

    public Graphe getG(){
        return this.G;
    }

    public void setPondere(boolean bool){
        this.pondere = bool;
    }

    public boolean getPondere(){
        return this.pondere;
    }

    public float getLineWidth(){
        return this.lineWidth;
    }

    public void setLineWidth(float w){
        this.lineWidth = w;
    }

    public double getCircleW(){
        return Draw.circleW;
    }

    public void setCircleW(double r){
        Draw.circleW = r;
    }

    public int getNumOfCircles() {
		return numOfCircles;
	}

    public String getPathSauvegarde() {
        return pathSauvegarde;
    }

    public void setPathSauvegarde(String nomSauvegarde) {
        this.pathSauvegarde = nomSauvegarde;
    }

    public int getOriente() {
        return oriente;
    }

    public void setOriente(int oriente) {
        this.oriente = oriente;
    }

    public int getNumOfLines(){
        return numOfLines;
    }

    public void setCurrentColor(Color c){
        this.currentColor = c;
    }

    public ArrayList<MyLine> getLines(){
        return lines;
    }
 
    public ArrayList<Noeud> getNodes(){
        return this.nodes;
    }
    
    public Transitions getTransitions(){
        return this.transitions;
    }
    
    public void setInterface(Interface inter) {
        this.Interface = inter;
    }
    
    public Draw() {
        nextNodeId = 0;
        this.setLayout(new BorderLayout());
        JToolBar tools = new JToolBar(null, JToolBar.HORIZONTAL);
        tools.setLayout(new FlowLayout(FlowLayout.RIGHT));
        zoomLabel = new JLabel("100%"); 
        zoomLabel.setPreferredSize(new Dimension(30,20));
        zoomLabel.setAlignmentX(FlowLayout.RIGHT);
        zoomSlider = new JSlider(MIN_ZOOM, MAX_ZOOM,100);
        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setPreferredSize(new Dimension(150,20));
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                int value = 10*(int) (zoomSlider.getValue()/10);
                zoom = value;
                zoomLabel.setText(value+"%");
                repaint();
            }
        });
        //zoomSlider.setAlignmentX(FlowLayout.RIGHT);
        tools.add(zoomSlider);
        tools.add(zoomLabel);
        tools.setFloatable(false);
        tools.setOpaque(false);
        tools.setBorderPainted(true);
        this.add(tools,BorderLayout.SOUTH);
        
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if (Interface.mode==Interface.EDITION_MODE){
                    int x = evt.getX();
                    int y = evt.getY();
                    // Vérifie si on clique où non sur un cercle existant
                    currentCircleIndex = findEllipse(x, y);
                    currentArcIndex = getArc(x,y);
                    // Si on souhaite ajouter un Nœud :
                    if (Interface.activeTool==Interface.NOEUD_TOOL) {
                        if (currentCircleIndex < 0 && currentArcIndex < 0){ // not inside a circle
                            add(x, y);
                            // On ajoute l'action à la pile
                            transitions.createLog("addCircle", nodes.get(numOfCircles-1));
                        }
                    }
                    // Si on souhaite ajouter un label à un Nœud :
                    if (Interface.activeTool==Interface.LABEL_TOOL) {
                        if (currentCircleIndex >= 0){ // inside a circle
                            try {
                                String lbl = JOptionPane.showInputDialog("Entrer label :");
                                String currentLbl =  nodes.get(currentCircleIndex).getLabel();
                                nodes.get(currentCircleIndex).setLabel(lbl);
                                repaint();
                                // On ajoute l'action à la pile
                                transitions.createLog("updateLbl", getNodes().get(currentCircleIndex),currentLbl,lbl);
                            } catch (Exception NullPointerException){
                                System.out.println("Opération annulée");
                            }
                        } else if (currentArcIndex >= 0){
                            if (pondere){
                                String text = JOptionPane.showInputDialog("Entrer le nouveau poids de l'Arc (seuls les entiers seront acceptés):");
                                try {
                                    int pds = Integer.parseInt(text);
                                    int currentPds = lines.get(currentArcIndex).getPoids();
                                    MyLine line = lines.get(currentArcIndex);
                                    line.setPoids(pds);
                                    repaint();
                                    // On ajoute l'action à la pile
                                    transitions.createLog("updatePds",line,Integer.toString(currentPds),text);
                                } catch (Exception e) {
                                    System.out.println("Pas un entier !");
                                }
                            }
                        } 
                    }
                    if (Interface.activeTool==Interface.ARC_TOOL) {
                        if ((currentCircleIndex >= 0) && (fromPoint==null)){
                            fromPoint = nodes.get(currentCircleIndex);
                        }
                    }
                    if (Interface.activeTool==Interface.SELECT_TOOL){
                        if (currentCircleIndex < 0 && currentArcIndex < 0){//not on circle or arc
                            for(int i = 0; i< getNumOfCircles() ; i++ ){
                                multiSelecCirc[i]=false;
                            } 
                            for(int i = 0; i<getNumOfLines(); i++){
                                multiSelecArc[i]=false;
                            }
                            selectXstart = x;
                            selectYstart = y;
                        }
                    }else{
                        for(int i = 0; i< getNumOfCircles() ; i++ ){
                           multiSelecCirc[i]=false;
                        }                            
                        for(int i = 0; i<getNumOfLines(); i++){
                            multiSelecArc[i]=false;
                        } 
                    }                    
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (Interface.mode==Interface.EDITION_MODE){
                    int x = evt.getX();
                    int y = evt.getY();
                    // Vérifie si on clique où non sur un cercle existant
                    currentCircleIndex = findEllipse(x, y);
                    if (Interface.activeTool==Interface.ARC_TOOL){
                        //ATTENTION : il faudra prendre le compte le cas où on pointe vers le meme cercle
                        if ((currentCircleIndex >= 0) && (fromPoint!=null) && (!fromPoint.equals(nodes.get(currentCircleIndex)))) { // inside circle
                            Noeud p = nodes.get(currentCircleIndex);
                            if (pondere){
                                String text = JOptionPane.showInputDialog("Entrer le poids de l'Arc (seuls les entiers seront acceptés):");
                                try {
                                    int pds = Integer.parseInt(text);
                                    System.out.println(p==null);
                                    MyLine newLine = new MyLine(fromPoint, p, pds, currentColor);
                                    addLine(newLine);
                                    repaint();
                                    //fromPoint = null;
                                    // On ajoute l'action à la pile
                                    transitions.createLog("addLine",newLine);
                                } catch (Exception e) {
                                    System.out.println("Pas un entier !");
                                    //fromPoint = null;
                                } finally {
                                    fromPoint = null;
                                }
                            } else {
                                MyLine newLine = new MyLine(fromPoint,p,1,currentColor);
                                addLine(newLine);
                                // On ajoute l'action à la pile
                                transitions.createLog("addLine",newLine);
                                //
                                fromPoint = null;
                            }    
                        }
                    }
                }if (Interface.activeTool==Interface.SELECT_TOOL){
                    drawZone = false;
                    for(int i = 0; i< getNumOfCircles() ; i++ ){
                        int x = (int) nodes.get(i).getCenterX() ;
                        int y = (int) nodes.get(i).getCenterY() ;
                        if (zoneR.contains(x,y)){
                            multiSelecCirc[i]=true;
                        }
                    }
                    for(int i = 0; i< getNumOfLines() ; i++ ){
                        int x =  lines.get(i).getClouPoint().x;
                        int y =  lines.get(i).getClouPoint().y;
                        if (zoneR.contains(x,y)){
                            multiSelecArc[i]=true;
                        }
                    }
                    repaint();
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (Interface.mode==Interface.EDITION_MODE){
                    int x = evt.getX();
                    int y = evt.getY();
                    currentArcIndex = getArc(x, y);
                    currentCircleIndex = findEllipse(x, y);
                    // Si on clique deux fois sur un Nœud, on le supprime
                    if (Interface.activeTool==Interface.NOEUD_TOOL && currentCircleIndex >= 0) {
                        if (evt.getClickCount() >= 2) {
                            // On ajoute l'action à la pile
                            // On ajoute les arcs qui seront supprimés
                            if(numOfLines > 0){
                                for (int i=0;i<lines.size(); i++){
                                    MyLine l = lines.get(i);
                                    if (l.getFrom().equals(nodes.get(currentCircleIndex)) || l.getTo().equals(nodes.get(currentCircleIndex))){
                                        transitions.createLog("deleteLine",l);
                                    }
                                }
                            }
                            // La reconstruction du noeud sera placée au haut de la pile
                            transitions.createLog("deleteCircle",getNodes().get(currentCircleIndex));
                            //
                            remove(currentCircleIndex);
                        }
                    }
                    if (Interface.activeTool==Interface.ARC_TOOL) {
                        if (evt.getClickCount() >= 2 && currentArcIndex >= 0){
                            // On ajoute l'action à la pile
                            MyLine toDelete = lines.get(currentArcIndex);
                            transitions.createLog("deleteLine",toDelete);
                            //
                            removeArc(currentArcIndex);
                        }
                        if (evt.getClickCount() >= 2 && currentCircleIndex >= 0){
                            if (pondere){
                                String text = JOptionPane.showInputDialog("Entrer le poids de l'Arc (seuls les entiers seront acceptés):");
                                try {
                                    int pds = Integer.parseInt(text);
                                    MyLine arc = new MyLine(nodes.get(currentCircleIndex), nodes.get(currentCircleIndex),pds,currentColor);
                                    Clou clou = new Clou(x-40,y,MyLine.RCLOU);
                                    arc.setClou(clou);
                                    addLine(arc);
                                    repaint();
                                    // On ajoute l'action à la pile
                                    transitions.createLog("addLine",arc);
                                } catch (Exception e) {
                                    System.out.println("Pas un entier !");
                                    
                                } finally {
                                    fromPoint = null;
                                } 
                            } else {
                                MyLine arc = new MyLine(nodes.get(currentCircleIndex), nodes.get(currentCircleIndex),1,currentColor);
                                Clou clou = new Clou(x-40,y,MyLine.RCLOU);
                                arc.setClou(clou);
                                addLine(arc);
                                // On ajoute l'action à la pile
                                transitions.createLog("addLine",arc);
                            }
                        }
                    }
                                        if (Interface.activeTool==Interface.SELECT_TOOL){
                        if (currentCircleIndex < 0 && currentArcIndex < 0){//not on circle or arc
                            for(int i = 0; i< getNumOfCircles() ; i++ ){
                                multiSelecCirc[i]=false;
                            } 
                            for(int i = 0; i< getNumOfLines() ; i++ ){
                                multiSelecArc[i]=false;
                            }  
                        }
                    }
                }
                if (Interface.mode==Interface.TRAITEMENT_MODE) {
                    if ((Interface.activeTraitement==Interface.DIJKSTRA_TRAITEMENT) || (Interface.activeTraitement==Interface.FORD_FULKERSON_TRAITEMENT)){
                        int x = evt.getX();
                        int y = evt.getY();
                        if (src == -1){
                            src = findEllipse(x,y);
                        } else if (dest == -1){
                            dest = findEllipse(x,y);
                            if (dest != -1) {
                                traitement();
                            } else {
                                src = -1;
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
        //order : draw line puis draw circles
        for (int i = 0 ; i < numOfLines; i++){
            lines.get(i).paint(this, (Graphics2D) g, multiSelecArc[i],zoom,this.getBounds().getWidth()/2,this.getBounds().getHeight()/2,pondere);
        }
        // Draw circles
        for (int i = 0; i < numOfCircles; i++) {
            //A CHANGER POUR INCLURE THIS
            nodes.get(i).paint((Graphics2D) g, multiSelecCirc[i],nodes.get(i).getLabel(),zoom,this.getBounds().getWidth()/2,this.getBounds().getHeight()/2);            
        }
        // Multiselect zone
        if(Draw.drawZone){
           ((Graphics2D) g).draw(this.zoneR);
        }
        
    }    
    /**
     * Permet de convertir des coordonnées globales en coordonnées de la zone de dessin
     * @param x
     * @param y
    */
    public Vector2D toDrawCoordinates(double x,double y) {
        Rectangle r = this.getBounds();
        float h = r.height;
        float w = r.width;
        return new Vector2D((x-w/2)*zoom/100+w/2,(y-h/2)*zoom/100+h/2);
    }
    /**
     * Permet de convertir des coordonnées de la zone de dessin en coordonnées globales
     * @param x
     * @param y
    */    
    public Vector2D toGlobalCoordinates(double x,double y) {
        Rectangle r = this.getBounds();
        float h = r.height;
        float w = r.width;
        return new Vector2D((x-w/2)*100/zoom+w/2,(y-h/2)*100/zoom+h/2);
    }
    
    public double toDrawScale(double h) {
        return h*zoom/100;
    }
    
    public double toGlobalScale(double h) {
        return h*100/zoom;
    }
    /**
     * Vérifie que l'on clique sur un cercle et donne son indice dans la liste nodes
     * @param x = coordonnée x du pointeur de la souris
     * @param y = coordonnée y du pointeur de la souris
     * @return -1 si on ne clique pas sur un cercle, l'indice du cercle dans la liste sinon
     */
    public int findEllipse(int x, int y) {
        for (int i = 0; i < numOfCircles; i++) {
            if (nodes.get(i).contains(x, y)) { // inside a circle
                return i;
            }
        }
        return -1;
    }

    public int getArc(int x, int y) {
        for (int i = 0; i < numOfLines; i++) {
            if (lines.get(i).getClou().contains(x,y)){
                return i;
            }
        }
        return -1;
    }
 
    /**
     * Ajoute un cercle dans la liste nodes et actualise l'affichage
     * @param x = abcsisse du cercle à dessiner
     * @param y = ordonnée du cercle à dessiner
     */
    public void add(double x, double y) {
        if (numOfCircles < MAX) {
            nextNodeId++;
            //On ajoute un cercle à la liste nodes et on actualise les attributs concernés
            Vector2D v = toGlobalCoordinates(x, y);
            nodes.add(new Noeud(v.x, v.y, circleW, String.valueOf(numOfCircles), nextNodeId));
            currentCircleIndex = numOfCircles;
            numOfCircles++;
            //On actualise l'affichage avec le nouveau cercle
            repaint();
        }
    }
    
    public int find(Ellipse2D.Double circ){
        boolean trouve = false;
        int n = 0;
        Ellipse2D.Double comp;
        while ((n<this.numOfCircles) && (!trouve)){
            comp = this.nodes.get(n);
            if (
                Double.compare(comp.x, circ.x) == 0 && 
                Double.compare(comp.y, circ.y) == 0
            ){
                trouve = true;
                return n;
            }
            else {
                n++;
            }
        }
        return -1;
    }
    
    /**
     * Ajoute une ligne dans la ArrayList lines et actualise l'affichage
     * @param line = ligne à ajouter
     */
    public void addLine(MyLine line) {
        if (numOfLines < MAX) {
            //On ajoute la ligne à la liste lines
            lines.add(line);
            numOfLines++;
            //On actualise l'affichage avec la nouvelle ligne
            repaint();
        }
    }

    public int findLine(int src, int dest){
        boolean trouve = false;
        int n = 0;
        while ((n<this.numOfLines) && (!trouve)){
            if ((this.lines.get(n).getFrom().equals(nodes.get(src)))
                && (this.lines.get(n).getTo().equals(nodes.get(dest)))){
                    trouve = true;
                    return n;
                }
            else {
                n++;
            }
        }
        return -1;
    }
    
    @Override
    /**
     * Supprime un Nœud sélectionné
     * @param n = indice du Nœud dans la liste circ
     */
    public void remove(int n) {
        if (n < 0 || n >= numOfCircles) {
            return;
        }
        // On supprime toutes les lignes qui sont relié au cercle supprimé
        if(numOfLines > 0){
            ArrayList<MyLine> linesCopy = new ArrayList<>(lines);
            for (MyLine l : linesCopy){
                if (l.getFrom().equals(nodes.get(n)) || l.getTo().equals(nodes.get(n))){
                    lines.remove(l);
                    numOfLines--;
                }
            }
        }
        //On remplace le cercle d'indic n par le dernier cercel ajouté
        //et on supprime le denrier cercle
        numOfCircles--;
        nodes.set(n, nodes.get(numOfCircles));
        nodes.remove(numOfCircles);
        if (currentCircleIndex == n) {
            currentCircleIndex = -1;
        }
        repaint();
    }

    public void removeArc(int n){
        if (n<0 || n>= numOfLines) {
            return;
        } else {
            MyLine l= lines.get(n);
            lines.remove(l);
            numOfLines--;
        }
        repaint();
    }
    
    /** Modifie le curseur lorsqu'on se trouve sur un cercle */
    @Override
    public void mouseMoved(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (findEllipse(x, y) >= 0 || getArc(x, y) >= 0) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }  
    }
 
    /** Déplace un cercle et les lignes qui lui sont rattachées */
    @Override
    public void mouseDragged(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();
        Vector2D v = toGlobalCoordinates(mouseX, mouseY);
        // Global coordinates of the mouse
        int x = (int) v.x;
        int y = (int) v.y;
        if ((Interface.activeTool==Interface.NOEUD_TOOL || Interface.activeTool==Interface.SELECT_TOOL)&& Interface.mode==Interface.EDITION_MODE)  {          
            if (currentCircleIndex >= 0) {
                if(multiSelecCirc[currentCircleIndex]){
                    double transx = x - nodes.get(currentCircleIndex).getCx();
                    double transy = y - nodes.get(currentCircleIndex).getCy();
                    
                    for(int i = 0; i< getNumOfCircles() ; i++){
                        if(multiSelecCirc[i]){
                            Noeud n = nodes.get(i);
                            n.addCx(transx);
                            n.addCy(transy);
                        }
                    }
                    for(int i = 0; i<getNumOfLines() ; i++){
                        if(multiSelecArc[i]){
                            Clou clou = lines.get(i).getClou();
                            clou.cx += transx;
                            clou.cy += transy ;
                        } 
                    }
                    repaint();
                }else{               
                    // On ajoute l'action à la pile
                    nodes.get(currentCircleIndex).updatePos(x, y);
                    zoneR = new Rectangle(Integer.MIN_VALUE,Integer.MIN_VALUE,0,0); //permet d'éviter qu'un ensemble de points soient toujours sélectionner
                                                                                    //après les avoir déselectionner en cliquant a cote
                    repaint();
                }
            }
        } 
        if ((Interface.activeTool==Interface.ARC_TOOL || Interface.activeTool==Interface.SELECT_TOOL)&& Interface.mode==Interface.EDITION_MODE) {

            if (currentArcIndex >= 0) {
                if(multiSelecArc[currentArcIndex]){
                    double transx;
                    Clou transClou = lines.get(currentArcIndex).getClou();
                    transx = x - transClou.cx;
                    double transy;
                    transy = y - transClou.cy;
                    
                    for(int i = 0; i< getNumOfCircles() ; i++){
                        if(multiSelecCirc[i]){
                            nodes.get(i).addCx(transx);
                            nodes.get(i).addCy(transy);
                        }
                    }
                    for(int i = 0; i<getNumOfLines() ; i++){
                        if(multiSelecArc[i]){
                            Clou clou = lines.get(i).getClou();
                            clou.cx += transx ;
                            clou.cy += transy ;
                        } 
                    }
                    repaint();
                }else{
                    MyLine line = lines.get(currentArcIndex);
                    Clou newClou = new Clou(x,y,MyLine.RCLOU);
                    // On ajoute l'action à la pile
                    //Ellipse2D.Double prevClou = new Ellipse2D.Double(line.getClou().x,line.getClou().y,MyLine.RCLOU,MyLine.RCLOU);
                    //transitions.createLog("moveLine",line,prevClou,newClou);
                    //
                    zoneR = new Rectangle(Integer.MIN_VALUE,Integer.MIN_VALUE,0,0); //permet d'éviter qu'un ensemble de points soient toujours sélectionner
                                                                                    //après les avoir déselectionner en cliquant a cote

                    line.setClou(newClou);
                    repaint();
                }
            }
        }    
        if (Interface.activeTool==Interface.SELECT_TOOL) {
            selectXend = event.getX();
            selectYend = event.getY();
            
            if (currentCircleIndex < 0 && currentArcIndex < 0) {
                int px = Math.min(selectXstart,selectXend);
                int py = Math.min(selectYstart,selectYend);
                int pw = Math.abs(selectXstart-selectXend);
                int ph = Math.abs(selectYstart-selectYend);
                this.zoneR = new Rectangle(px, py, pw, ph);
                Draw.drawZone = true;
                repaint();
            }
        }
        if (Interface.mode==Interface.TRAITEMENT_MODE) {
            Draw.drawZone = false;
            repaint();
        }
    }  

    public void reinit(){
        for (int i=0;i<this.numOfLines;i++){
            this.lines.get(i).setC(Color.BLUE);
        }
    }

    public void traitement(){
        reinit();
        repaint();
        if (Interface.activeTraitement == Interface.DIJKSTRA_TRAITEMENT){
            (new Dijkstra()).dijkstra(this, src, dest);
        } else if (Interface.activeTraitement == Interface.FORD_FULKERSON_TRAITEMENT){
            (new FordFulkerson()).fordFulkerson(this, src, dest);
        }
        this.src = -1;
        this.dest = -1;
    }

    
    /**
     * 
     * @return Taille des cercles
     */
    public double getTailleCirc(){
        return (float) Interface.taille/20;
    }
    
    
    /** 
     * Méthode permettant de modifier la taille des noeuds
     */
    public void tailleCirc(){
        if(numOfCircles>0){
            double factor = getTailleCirc();
            circleW = factor*Draw.RINIT ;
            //lineWidth = (float) factor*Draw.LINIT;
            for (int i = 0; i < numOfCircles; i++) {
                nodes.get(i).updateSize(circleW);
            }
            repaint();
        }
    }
    
    /** 
     * Méthode permettant de modifier l'épaisseur des arcs et des périmètres des noeuds
     */
    public void epaisseurLines(){
        if(numOfCircles>0){
            double factor = (float) Interface.epaisseur/20;
            lineWidth = (float) factor*Draw.LINIT;
            repaint();
        }
    }
    
    /**
     * Méthode permettant d'actualiser le Graphe G représenté par le Draw
     */
    public void exportGraphe(){
        switch (this.oriente){
            case Draw.ORIENTE:
                this.G = new GOriente(this);
                break;
            case Draw.NONORIENTE:
                this.G = new GNonOriente(this);
                break;
        }
    }

}
