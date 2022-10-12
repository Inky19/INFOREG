/*=============================================
Classe MyLine permettant de stocker les informations
d'une ligne à tracer
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 11/03/2022
=============================================*/

import ObjetsGraph.Clou;
import ObjetsGraph.Noeud;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import utilities.Vector2D;

public class MyLine {
    /** Cercle/Nœud de départ */
    private final Noeud from;
    /** Cercle/Nœeud d'arrivée */
    private final Noeud to;
    /** Poids de l'Arc */
    private int poids;
    /** Couleur */
    private Color c;
    /** Clou */
    private Clou clou;
    /** Rayon des clous */
    public static final int RCLOU=3;
    public static final int LINE_WIDTH = 3;

    /** 
     * Constructeur
     * @param fromPoint = cercle de départ
     * @param toPoint = cercle d'arrivée
     * @param pds
     * @param c
     */
    public MyLine(Noeud fromPoint, Noeud toPoint, int pds, Color c) {
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.c = c;
        int x = (int) (from.cx + to.cx)/2;
        int y = (int) (from.cy + to.cy)/2;
        System.out.println(x + " " + y);
        this.clou = new Clou(x,y,RCLOU,c);
    }

    public Clou getClou(){
        return this.clou;
    }
    
    public void paint(Draw d, Graphics2D g, boolean multiSelected,float zoom,double cameraX, double cameraY, boolean pondere) {
        g.setPaint(c);
        g.setStroke(new BasicStroke(LINE_WIDTH*zoom/100));
        Vector2D v1 = d.toDrawCoordinates(from.cx,from.cy);
        Vector2D v3 = d.toDrawCoordinates(clou.cx,clou.cy);
        int x1 = (int) v1.x;
        int y1 = (int) v1.y;
        int x3 = (int) v3.x;
        int y3 = (int) v3.y;
        
        if (d.pondere){
            g.drawString(""+poids,x3,y3-10);
        }
        if (from == to) {
            //calcArc(x1,y1,x3,y3,g);
        } else {
            Vector2D v2 = d.toDrawCoordinates(to.cx,to.cy);
            int x2 = (int) v2.x;
            int y2 = (int) v2.y;
            g.drawLine(x1,y1,x3,y3);
            g.drawLine(x3,y3,x2,y2);
            
            clou.paint(g, multiSelected, zoom, cameraX, cameraY);

            g.setPaint(c); //reset color pour poids
            if (d.oriente==Draw.ORIENTE){
                int[] t = new int[4];
                int x4 = (x3+x2)/2;
                int y4 = (y3+y2)/2;
                d.fleche(x3,y3,x4,y4,t);
                g.setStroke(new BasicStroke(LINE_WIDTH*zoom/100));
                g.drawLine(x4,y4,t[0],t[1]);
                g.drawLine(x4,y4,t[2],t[3]);     
            }
        }
    }

    
    
    
    
    
    public int getPoids(){
        return poids;
    }

    public void setPoids(int p){
        this.poids = p;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color col){
        this.c = col;
    }

    /** 
     * Getter du cercle de départ 
     * @return from (attribut)
     */
    public Ellipse2D.Double getFrom() {
        return this.from;
    }

    /** 
     * Getter du cercle d'arrivée 
     * @return to (attribut)
     */
    public Ellipse2D.Double getTo() {
        return this.to;
    }

    /** 
     * Getter des coordonnées du centre du cercle de départ
     * @return p = un Point
     */
    public Point getFromPoint() {
        double centerX = this.from.getCenterX();
        double centerY = this.from.getCenterY();
        Point p = new Point((int)centerX, (int)centerY);
        return p;
    }

    /** 
     * Getter des coordonnées du centre du cercle d'arrivée
     * @return p = un Point
     */
    public Point getToPoint() {
        double centerX = this.to.getCenterX();
        double centerY = this.to.getCenterY();
        Point p = new Point((int)centerX, (int)centerY);
        return p;
    }

    public Point getClouPoint() {
        double centerX = this.clou.getCenterX();
        double centerY = this.clou.getCenterY();
        Point p = new Point((int)centerX, (int)centerY);
        return p;
    }

    public void setClou(Clou nouv){
        this.clou = nouv;
    }

}
