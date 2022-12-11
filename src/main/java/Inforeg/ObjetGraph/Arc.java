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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Arc implements Comparable<Arc> {
    /**
     * Epaisseur de l'arc
     */
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
     * Couleur affichée
     */
    private Color colorDisplayed;
    /**
     * Flux
     */
    private Integer flow = null;
    /**
     * Clous
     */
    private ArrayList<Nail> nails;
    /**
     * Rayon des clous
     */
    public static final int RCLOU = 3;
    /**
     * Taille par défaut de l'arc
     */
    public static final int DEFAULT_LINE_WIDTH = 3;

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
        this.colorDisplayed = c;
        this.width = DEFAULT_LINE_WIDTH;
        this.nails = new ArrayList<>();
    }

    public Arc(Node fromPoint, Node toPoint, int pds, Color c, Nail nail) {
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        this.colorDisplayed = c;
        this.nails = new ArrayList<>();
        this.addNail(nail);
        this.width = DEFAULT_LINE_WIDTH;
    }

    protected static void paintLabel(Draw d, Graphics2D g, Vector2D p, String label, Color textColor, Color bgColor) {
        Point pos = d.toDrawCoordinates(p).toPoint();
        Font font = new Font("Arial", Font.BOLD, (int) d.toDrawScale(15));
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        Rectangle2D bg = metrics.getStringBounds(label, g);

        // Determine the X coordinate for the text
        int fontX = (int) (pos.x - metrics.stringWidth(label) / 2);
        int fontY = pos.y;
        int offset = (int) d.toDrawScale(5);
        g.setPaint(bgColor);
        g.fillRect(fontX - offset, fontY - (int) bg.getHeight() + metrics.getDescent(), (int) (bg.getWidth() + 2 * offset), (int) bg.getHeight());
        g.setPaint(textColor);
        g.drawString(label, fontX, fontY);
    }

    public void paint(Draw d, Graphics2D g) {

        // Painting of lines
        List<Line> lines = getNailLines(this.width, this.colorDisplayed);
        int i = 0, size = lines.size();
        for (Line line : lines) {
            line.arrow = (i == size / 2 && d.oriente);
            line.paint(d, g);
            i++;
        }
        // Painting of nails
        for (Nail nail : nails) {
            nail.paint(d, g);
        }

        // Painting of labels
        if (flow != null) {
            String label = Integer.toString(flow);
            Vector2D pos;
            if (from == to) {
                pos = from.getCenterPos().plus(new Vector2D(2*Line.CIRCLE_RADIUS,0));
            } else {
                if (nails.isEmpty()) {
                    pos = Vector2D.middle(from.getCenterPos(), to.getCenterPos());
                } else {
                    Nail midNail = nails.get(nails.size() / 2);
                    pos = midNail.getCenterPos();
                }                
            }
            paintLabel(d, g, pos.plus(new Vector2D(0, 22)), label, colorDisplayed, Color.CYAN);
        }
        if (d.pondere) {
            String label = Integer.toString(poids);
            Vector2D pos;
            if (from == to) {
                pos = from.getCenterPos().plus(new Vector2D(2*Line.CIRCLE_RADIUS,0));
            } else {
                if (nails.isEmpty()) {
                    pos = Vector2D.middle(from.getCenterPos(), to.getCenterPos());
                } else {
                    Nail midNail = nails.get(nails.size() / 2);
                    pos = midNail.getCenterPos();
                }                
            }
            
            paintLabel(d, g, pos.minus(new Vector2D(0, 12)), label, color, Color.WHITE);
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

    public void setColor(Color color) {
        this.color = color;
        this.colorDisplayed = color;
        for (Nail n : nails) {
            n.color = color;
        }
    }

    /**
     * Change the displayed color of this arc. Reset the color by using reinit()
     *
     * @param color new color of this arc
     */
    public void setColorDisplayed(Color color) {
        this.colorDisplayed = color;
        for (Nail n : nails) {
            n.color = color;
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
    
    /**
     * Permet l'ajout des clous lors du chargement d'un fichier
     * @param nail Clou à ajouter
     */
    public void loadNail(Nail nail){
        nails.add(nail);
    }

    public void addNail(Nail nail) {
        List<Line> hitbox = getNailLines(width + 5, Color.RED);
        int i = 0;
        while (i < hitbox.size() && !hitbox.get(i).contains(nail.cx, nail.cy)) {
            i++;
        }
        nails.add(i, nail);
        nail.arc = this;
    }

    private List<Line> getNailLines(int width, Color color) {
        List<Line> lines = new ArrayList<>();
        if (from == to) {
            lines.add(new Line(from, width, color));
        } else {
            if (nails.isEmpty()) {
                lines.add(new Line(from, to, width, color));
            } else {
                lines.add(new Line(from, nails.get(0), width, color));
            }

            for (int i = 0; i < nails.size() - 1; i++) {
                lines.add(new Line(nails.get(i), nails.get(i + 1), width, color));
            }

            if (!nails.isEmpty()) {
                lines.add(new Line(nails.get(nails.size() - 1), to, width, color));
            }
        }
        return lines;
    }

    /**
     *
     * @param x global coordinate x of a point
     * @param y global coordinate y of a point
     * @param d
     * @return true if the point (x ,y) touches this arc
     */
    public boolean contains(int x, int y) {
        List<Line> hitbox = getNailLines(width + 5, Color.RED);
        for (Line line : hitbox) {
            if (line.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public Color getColorDisplayed() {
        return colorDisplayed;
    }
    
    @Deprecated
    /**
     * Will be deleted once saved is supported
     */
    public Nail getClou() {
        if (nails.isEmpty()) {
            return null;
        } else {
            return nails.get(0);
        }
    }

    public void reinit() {
        setColorDisplayed(color);
        flow = null;
    }
}
