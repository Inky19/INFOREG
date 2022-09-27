/*=============================================
Classe MyLine permettant de stocker les informations
d'une ligne à tracer
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 11/03/2022
=============================================*/

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class MyLine {
    /** Cercle/Nœud de départ */
    private final Ellipse2D.Double from;
    /** Cercle/Nœeud d'arrivée */
    private final Ellipse2D.Double  to;
    /** Poids de l'Arc */
    private int poids;
    /** Couleur */
    private Color c;
    /** Clou */
    private Ellipse2D.Double clou;
    /** Rayon des clous */
    public static final int RCLOU=10;

    /** 
     * Constructeur
     * @param fromPoint = cercle de départ
     * @param toPoint = cercle d'arrivée
     */
    public MyLine(Ellipse2D.Double fromPoint, Ellipse2D.Double toPoint, int pds, Color c) {
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.c = c;
        int x = (this.getToPoint().x + this.getFromPoint().x)/2;
        int y = (this.getToPoint().y + this.getFromPoint().y)/2;
        this.clou = new Ellipse2D.Double(x,y,RCLOU,RCLOU);
    }

    public Ellipse2D.Double getClou(){
        return this.clou;
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

    public void setClou(Ellipse2D.Double nouv){
        this.clou = nouv;
    }

}
