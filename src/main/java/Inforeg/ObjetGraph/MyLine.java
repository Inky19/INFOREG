package Inforeg.ObjetGraph;

/*=============================================
Classe MyLine permettant de stocker les informations
d'une ligne à tracer
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 11/03/2022
=============================================*/
import Inforeg.Draw.Draw;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import static java.lang.Math.sqrt;

public class MyLine {

    private boolean selected;
    /**
     * Cercle/Nœud de départ
     */
    private final Node from;
    /**
     * Cercle/Nœeud d'arrivée
     */
    private final Node to;
    /**
     * Poids de l'Arc
     */
    private int poids;
    /**
     * Couleur
     */
    private Color color;
    /**
     * Flux
     */
    private Integer flow = null;
    /**
     * Nail
     */
    private Nail clou;
    /**
     * Rayon des clous
     */
    public static final int RCLOU = 3;
    public static final int LINE_WIDTH = 3;

    /**
     * Constructeur
     *
     * @param fromPoint = cercle de départ
     * @param toPoint = cercle d'arrivée
     * @param pds
     * @param c
     */
    public MyLine(Node fromPoint, Node toPoint, int pds, Color c) {
        selected = false;
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        int x, y;
        if (from!=to) {
            x = (int) (from.getCx() + to.getCx()) / 2;
            y = (int) (from.getCy() + to.getCy()) / 2;          
        } else {
            x = (int) (from.getCx() + from.getHeight());
            y = (int) (from.getCy() + from.getHeight());              
        }
        this.clou = new Nail(x, y, RCLOU, c);
    }
    
    public MyLine(Node fromPoint, Node toPoint, int pds, Color c, Nail nail){
        selected = false;
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        this.clou = nail;
    }

    public Nail getClou() {
        return this.clou;
    }

    public void paint(Draw d, Graphics2D g) {
        g.setPaint(color);
        g.setStroke(new BasicStroke((float) d.toDrawScale(LINE_WIDTH)));
        Point v1 = d.toDrawCoordinates(from.getCx(), from.getCy());
        Point v3 = d.toDrawCoordinates(clou.cx, clou.cy);
        int x1 = (int) v1.x;
        int y1 = (int) v1.y;
        int x3 = (int) v3.x;
        int y3 = (int) v3.y;

        if (d.pondere) {
            Font font = new Font("Arial", Font.BOLD, (int) d.toDrawScale(15));
            g.setFont(font);
            g.drawString("" + poids, x3, y3 - (int) d.toDrawScale(10));
        }
        if (from == to) {
            g.setStroke(new BasicStroke((float) d.toDrawScale(LINE_WIDTH)));
            double radius = sqrt((x1-x3)*(x1-x3) + (y1-y3)*(y1-y3))/2;
            g.draw(new Ellipse2D.Double((x1+x3)/2-radius,(y1+y3)/2-radius, 2*radius, 2*radius));
            //d.calcArc(x1,y1,x3,y3,g);
        } else {
            Point v2 = d.toDrawCoordinates(to.getCx(), to.getCy());
            int x2 = (int) v2.x;
            int y2 = (int) v2.y;
            g.drawLine(x1, y1, x3, y3);
            g.drawLine(x3, y3, x2, y2);

            g.setPaint(color); //reset color pour poids
            if (d.oriente == Draw.ORIENTE) {
                int[] t = new int[4];
                int x4 = (x3 + x2) / 2;
                int y4 = (y3 + y2) / 2;
                d.fleche(x3, y3, x4, y4, t);
                g.setStroke(new BasicStroke((float) d.toDrawScale(LINE_WIDTH)));
                g.drawLine(x4, y4, t[0], t[1]);
                g.drawLine(x4, y4, t[2], t[3]);
            } 
        }
        clou.paint(d, g, selected);
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int p) {
        this.poids = p;
    }

    public Color getC() {
        return color;
    }

    public void setC(Color col) {
        this.color = col;
    }

    /**
     * Getter du cercle de départ
     *
     * @return from (attribut)
     */
    public Node getFrom() {
        return this.from;
    }

    /**
     * Getter du cercle d'arrivée
     *
     * @return to (attribut)
     */
    public Node getTo() {
        return this.to;
    }

    /**
     * Getter des coordonnées du centre du cercle de départ
     *
     * @return p = un Point
     */
    public Point getFromPoint() {
        double centerX = this.from.getCenterX();
        double centerY = this.from.getCenterY();
        Point p = new Point((int) centerX, (int) centerY);
        return p;
    }

    /**
     * Getter des coordonnées du centre du cercle d'arrivée
     *
     * @return p = un Point
     */
    public Point getToPoint() {
        double centerX = this.to.getCenterX();
        double centerY = this.to.getCenterY();
        Point p = new Point((int) centerX, (int) centerY);
        return p;
    }

    public Point getClouPoint() {
        double centerX = this.clou.getCenterX();
        double centerY = this.clou.getCenterY();
        Point p = new Point((int) centerX, (int) centerY);
        return p;
    }

    public void setClou(Nail nouv) {
        this.clou = nouv;
    }

    public Color getColor() {
        return color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    
}
