package Inforeg.ObjetGraph;

/*=============================================
Classe Arc permettant de stocker les informations
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Arc implements Comparable<Arc> {

    
    
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
    //private Nail clou;
    
    private ArrayList<Nail> nails;
    /**
     * Rayon des clous
     */
    public static final int RCLOU = 3;
    public static final int DEFAULT_LINE_WIDTH = 3;
    public static final int SELF_ARC_RADIUS = 25;
    /**
     * Constructeur
     *
     * @param fromPoint = cercle de départ
     * @param toPoint = cercle d'arrivée
     * @param pds
     * @param c
     */
    public Arc(Node fromPoint, Node toPoint, int pds, Color c) {
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        this.width = DEFAULT_LINE_WIDTH;
        this.nails = new ArrayList<>();
    }
    
    public Arc(Node fromPoint, Node toPoint, int pds, Color c, Nail nail){
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        this.nails = new ArrayList<>();
        this.addNail(nail);
        this.width = DEFAULT_LINE_WIDTH;
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
    
    private void paintLine(Draw d, Graphics2D g, Vector2D pos1, Vector2D pos2) {
        Vector2D p1 = d.toDrawCoordinates(pos1.x, pos1.y);
        Vector2D p2 = d.toDrawCoordinates(pos2.x, pos2.y);
        g.drawLine((int) p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
    }
    
    public void paint(Draw d, Graphics2D g) {
        g.setPaint(color);
        g.setStroke(new BasicStroke((float) d.toDrawScale(DEFAULT_LINE_WIDTH)));
        Vector2D v1 = d.toDrawCoordinates(from.getCx(), from.getCy());
        
        int x1 = (int) v1.x;
        int y1 = (int) v1.y;

        // Painting of lines
        if (from == to) {
            Vector2D v3 = d.toDrawCoordinates(from.cx+SELF_ARC_RADIUS,from.cy+SELF_ARC_RADIUS);
            int x3 = (int) v3.x;
            int y3 = (int) v3.y;
            g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
            double radius = sqrt((x1-x3)*(x1-x3) + (y1-y3)*(y1-y3))/2;
            g.draw(new Ellipse2D.Double((x1+x3)/2-radius,(y1+y3)/2-radius, 2*radius,  2*radius));
        } else {
            List<Line> lines = getNailLines(this.width,this.color);
            for (Line line : lines) {
                line.arrow = d.oriente;
                line.paint(d, g); 
            }
        }
        // Painting of nails
        for (Nail n : nails) {
            n.paint(d,g);
        }

        // Painting of labels
        if (flow != null) {
            String label = Integer.toString(flow);
            if (nails.isEmpty()) {
                Point middle = new Point((int)(from.cx+to.cx)/2,(int)(from.cy+to.cy)/2);
                paintLabel(d, g, new Point(middle.x,middle.y+(int) d.toDrawScale(20)), label, color, Color.CYAN);
            } else {
                Nail midNail = nails.get(nails.size()/2);
                paintLabel(d, g, new Point((int)midNail.cx,(int)midNail.cy+(int) d.toDrawScale(20)), label, color, Color.CYAN);
            }
        }
        if (d.pondere) {
            String label = Integer.toString(poids);
            Vector2D pos;
            if (nails.isEmpty()) {
                pos = d.toDrawCoordinates((from.cx+to.cx)/2,(from.cy+to.cy)/2);
            } else {
                Nail midNail = nails.get(nails.size()/2);
                pos = d.toDrawCoordinates(midNail.cx, midNail.cy);
            }
            paintLabel(d, g, new Point((int)pos.x,(int)pos.y-(int) d.toDrawScale(12)), label, color, Color.WHITE);
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
        for (Nail n : nails) {
          n.color = col;
        }
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

    public Color getColor() {
        return color;
    }

    public void setSelected(boolean selected) {
        for (Nail nail : nails) {
            nail.selected = false;
        }
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
    public int compareTo(Arc compareEdge) {
        return this.poids - compareEdge.poids;
    }
    
    public ArrayList<Nail> getNails() {
        return nails;
    }
    
    public void addNail(Nail nail) {
        List<Line> hitbox = getNailLines(width+5,Color.RED);
        int i = 0;
        while (i < hitbox.size() && !hitbox.get(i).contains(nail.cx, nail.cy)) {
            i++;
        }
        nails.add(i,nail);
        nail.arc = this;
    }

    private List<Line> getNailLines(int width, Color color) {
        List<Line> lines = new ArrayList<>();
        if (from == to) {
            
        } else {
            if (nails.isEmpty()) {
                lines.add(new Line(from,to,width,color)); 
            } else {
                lines.add(new Line(from,nails.get(0),width,color));
            }
  
            for (int i=0; i < nails.size() - 1; i++) {
                lines.add(new Line(nails.get(i),nails.get(i+1),width,color));
            }
            
            if (!nails.isEmpty()) {
                lines.add(new Line(nails.get(nails.size()-1),to,width,color));
            }
        }
        return lines;
    }
    
    /**
     * 
     * @param x global coordinate x of a point
     * @param y global coordinate y of a point
     * @param d
     * @return true if this point is inside this arc
     */
    public boolean contains(int x, int y) {
        List<Line> hitbox = getNailLines(width+5,Color.RED);
        for (Line line : hitbox) {
            if (line.contains(x,y)) {
                return true;
            }
        }
        return false;
    }
    
    public Nail getClou() {
        if (nails.isEmpty()) {
            return null;
        } else {
            return nails.get(0);
        }
    }
}
