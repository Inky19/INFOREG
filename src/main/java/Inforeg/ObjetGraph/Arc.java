package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe Arc
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 * @author Samy AMAL
 */
public class Arc implements Comparable<Arc>, Clickable, GraphObject {

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

    private AttachedLabel label;
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
        this.label = new AttachedLabel();
        this.color = c;
        this.colorDisplayed = c;
        this.width = DEFAULT_LINE_WIDTH;
        this.nails = new ArrayList<>();
        if (from == to && from != null) {
            nails.add(new Nail(fromPoint.getCx() + 2 * Line.CIRCLE_RADIUS, fromPoint.getCy(), this));
        }
    }

    public Arc(Node fromPoint, Node toPoint, int pds, Color c, Nail nail) {
        this.from = fromPoint;
        this.to = toPoint;
        this.poids = pds;
        this.color = c;
        this.colorDisplayed = c;
        this.nails = new ArrayList<>();
        this.label = new AttachedLabel(Integer.toString(poids),Vector2D.middle(fromPoint.getCenterPos(), toPoint.getCenterPos()), c);
        this.addNailWhereSelected(nail);
        this.width = DEFAULT_LINE_WIDTH;
    }

    @Override
    public void paint(Draw d, Graphics2D g) {

        // Painting of lines
        List<Line> lines = getNailLines(this.width, this.colorDisplayed);
        int i = 0, size = lines.size();
        for (Line line : lines) {
            line.arrow = (i == size - 1 && d.oriente); // affichage de la flèche sur la dernière ligne
            line.paint(d, g);
            i++;
        }
        // Painting of nails
        for (Nail nail : nails) {
            nail.paint(d, g);
        }
        // Painting of labels
        if (d.pondere) {
            // text
            String text;
            if (flow == null) {
                text = Integer.toString(poids);
            } else {
                text = flow + " / " + poids;
            }
            label.text = text;
            // position
            Vector2D pos;
            if (from == to) {
                pos = nails.get(0).getCenterPos();
            } else {
                if (nails.isEmpty()) {
                    // on affiche le label au milieu des deux noeuds
                    pos = Vector2D.middle(from.getCenterPos(), to.getCenterPos());
                } else if (nails.size() % 2 == 0) {
                    // on affiche le label au milieu d'une ligne
                    pos = Vector2D.middle(nails.get(nails.size() / 2 - 1).getCenterPos(), nails.get(nails.size() / 2).getCenterPos());
                } else {
                    // on affiche le label sur le clou du milieu
                    pos = nails.get(nails.size() / 2).getCenterPos().minus(new Vector2D(0, 15));
                }
            }
            label.pos = pos;
            // Color
            label.textColor = colorDisplayed;

            label.paint(d, g);
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
    @Override
    public int compareTo(Arc compareEdge) {
        return this.poids - compareEdge.poids;
    }

    public ArrayList<Nail> getNails() {
        return nails;
    }

    /**
     * Permet l'ajout des clous lors du chargement d'un fichier
     *
     * @param nail Clou à ajouter
     */
    public void addNail(Nail nail){
        nail.arc = this;
        nail.color = colorDisplayed;
        if (from == to) {
            nails.set(0, nail);
        } else {
            nails.add(nail);
        }
    }
    
    public void addNail(Nail nail, int index){
        nail.arc = this;
        nail.color = colorDisplayed;
        if (from == to){
            nails.set(0, nail);
        } else {
            nails.add(index, nail);
        }
    }
    
    /**
     * Permet d'ajouter un clou sur l'arc.
     * Attention ! Ne fonctionne pas si le clou ne se trouve pas sur l'arc. Utiliser loadNail à la place.
     * @param nail Clou à ajouter
     */
    public void addNailWhereSelected(Nail nail) {
        if (from == to) {
            return; // Can't add nail to loop
        }
        List<Line> hitbox = getNailLines(width + 5, Color.RED);
        int i = 0;
        while (i < hitbox.size() && !hitbox.get(i).contains(nail.cx, nail.cy)) {
            i++;
        }
        nails.add(i, nail);
        nail.color = colorDisplayed;
        nail.arc = this;
    }

    private List<Line> getNailLines(int width, Color color) {
        List<Line> lines = new ArrayList<>();
        if (from == to) {
            lines.add(new Line(from, nails.get(0), width, color, true));
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
    @Override
    public boolean contains(double x, double y) {
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

    public void reinit() {
        setColorDisplayed(color);
        flow = null;
    }
}
