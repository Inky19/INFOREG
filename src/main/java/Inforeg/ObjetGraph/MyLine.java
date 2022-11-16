package Inforeg.ObjetGraph;

/*=============================================
Classe MyLine permettant de stocker les informations
d'une ligne à tracer
Auteur : Samy AMAL
Date de création : 03/03/2022
Date de dernière modification : 11/03/2022
=============================================*/
import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.sqrt;

public class MyLine implements Comparable<MyLine> {

    private boolean selected;
    
    
    public int width;
    /**
     * Cercle/Nœud de départ
     */
    private Node from;
    /**
     * Cercle/Nœeud d'arrivée
     */
    private Node to;
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
    public static final int DEFAULT_LINE_WIDTH = 3;

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
        this.width = DEFAULT_LINE_WIDTH;
        int x, y;
        if (from != null && to != null){
            if (from!=to) {
                x = (int) (from.getCx() + to.getCx()) / 2;
                y = (int) (from.getCy() + to.getCy()) / 2;          
            } else {
                x = (int) (from.getCx() + from.getHeight());
                y = (int) (from.getCy() + from.getHeight());              
            }
            this.clou = new Nail(x, y, RCLOU, c);
        } else {
            this.clou = null;
        }
        

    }
    
    public MyLine(Node fromPoint, Node toPoint, int pds, Color c, Nail nail){
        selected = false;
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        this.clou = nail;
        this.width = DEFAULT_LINE_WIDTH;
    }

    public Nail getClou() {
        return this.clou;
    }
    
    private void paintLabel(Draw d, Graphics2D g, Point pos, String label, Color textColor, Color bgColor) {
            Font font = new Font("Arial", Font.BOLD, (int) d.toDrawScale(15));
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            Rectangle2D bg = metrics.getStringBounds(label, g);
            
            // Determine the X coordinate for the text
            int fontX = (int) (pos.x - metrics.stringWidth(label) / 2);
            int fontY = pos.y;
            g.setPaint(bgColor);
            g.fillRect(fontX, fontY-(int)bg.getHeight() + metrics.getDescent(), (int)bg.getWidth(), (int)bg.getHeight());
            g.setPaint(textColor);
            g.drawString(label, fontX, fontY);
    }
    
    public void paint(Draw d, Graphics2D g) {
        g.setPaint(color);
        g.setStroke(new BasicStroke((float) d.toDrawScale(DEFAULT_LINE_WIDTH)));
        Vector2D v1 = d.toDrawCoordinates(from.getCx(), from.getCy());
        Vector2D v3 = d.toDrawCoordinates(clou.cx, clou.cy);
        int x1 = (int) v1.x;
        int y1 = (int) v1.y;
        int x3 = (int) v3.x;
        int y3 = (int) v3.y;
        // Painting of lines
        if (from == to) {
            g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
            double radius = sqrt((x1-x3)*(x1-x3) + (y1-y3)*(y1-y3))/2;
            g.draw(new Ellipse2D.Double((x1+x3)/2-radius,(y1+y3)/2-radius, 2*radius, 2*radius));
            //d.calcArc(x1,y1,x3,y3,g);
        } else {
            Vector2D v2 = d.toDrawCoordinates(to.getCx(), to.getCy());
            int x2 = (int) v2.x;
            int y2 = (int) v2.y;
            g.drawLine(x1, y1, x3, y3);
            g.drawLine(x3, y3, x2, y2);

            g.setPaint(color); //reset color pour poids
            if (d.oriente) {
                int[] t = new int[4];
                int x4 = (x3 + x2) / 2;
                int y4 = (y3 + y2) / 2;
                d.fleche(x3, y3, x4, y4, t);
                g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
                g.drawLine(x4, y4, t[0], t[1]);
                g.drawLine(x4, y4, t[2], t[3]);
            } 
        }
        // Painting of nails
        clou.paint(d, g, selected);
        // Painting of labels
        if (flow != null) {
            String label = Integer.toString(flow);
            paintLabel(d, g, new Point(x3,y3+(int) d.toDrawScale(20)), label, color, Color.CYAN);
        }
        if (d.pondere) {
            String label = Integer.toString(poids);
            paintLabel(d, g, new Point(x3,y3-(int) d.toDrawScale(12)), label, color, Color.WHITE);
        }
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

    public void setColor(Color col) {
        this.color = col;
        this.clou.color = col;
    }

    /**
     * Getter du cercle de départ
     *
     * @return from (attribut)
     */
    public Node getFrom() {
        return this.from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public void setTo(Node to) {
        this.to = to;
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

    public Integer getFlow() {
        return flow;
    }

    public void setFlow(Integer flow) {
        this.flow = flow;
    }
    
    @Override
    public String toString() {
        return "Arc | poids: " + poids + ", " + from.toString() + " -> " + to.toString() + " |";
    }
    /**
     * @param compareEdge = Arc à comparer
     * @return la différence entre les deux poids des arcs
     */
    public int compareTo(MyLine compareEdge) {
        return this.poids - compareEdge.poids;
    }
}
