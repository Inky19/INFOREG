package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Classe utilisée pour définir l'apparence des lignes qui constituent un arc. 
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public class Line implements Clickable, GraphObject {

    public final static int DEFAULT_WIDTH = 8;
    public final static Color DEFAULT_COLOR = Color.BLUE;
    public final static int CIRCLE_RADIUS = 15;
    public Attachable from;
    public Attachable to;
    public int width;
    public Color color;
    public boolean arrow = false;
    public boolean circle = false;

    public Line(Attachable from, Attachable to) {
        this.from = from;
        this.to = to;
        this.width = DEFAULT_WIDTH;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, Attachable to, Color color) {
        this.from = from;
        this.to = to;
        this.width = DEFAULT_WIDTH;
        this.color = color;
    }

    public Line(Attachable from, Attachable to, int width) {
        this.from = from;
        this.to = to;
        this.width = width;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, Attachable to, int width, Color color) {
        this.from = from;
        this.to = to;
        this.width = width;
        this.color = color;
    }

    public Line(Attachable from) {
        this.from = from;
        this.to = null;
        this.width = DEFAULT_WIDTH;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, int width) {
        this.from = from;
        this.to = null;
        this.width = width;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, int width, Color color) {
        this.from = from;
        this.to = null;
        this.width = width;
        this.color = color;
    }

    public Line(Attachable from, Attachable to, int width, Color color, boolean circle) {
        this.from = from;
        this.to = to;
        this.width = width;
        this.color = color;
        this.circle = circle;
    }

    /**
     * Renvoie true si le point de coordonnées x et y touche la hitbox de la
     * ligne
     *
     * @param x abscisse globale du point
     * @param y ordonnée globale du point
     * @return
     */
    @Override
    public boolean contains(double x, double y) {
        Vector2D p1 = from.getCenterPos();
        Vector2D p2 = to.getCenterPos();

        if (p1 == null && p2 == null) {
            return false;
        } else if (p1 == null || p2 == null) {
            Vector2D v = new Vector2D(x, y);
            double dist;
            if (p1 == null) {
                dist = v.minus(p2.plus(new Vector2D(CIRCLE_RADIUS, 0))).getNorm();
            } else {
                dist = v.minus(p1.plus(new Vector2D(CIRCLE_RADIUS, 0))).getNorm();
            }
            return (CIRCLE_RADIUS - width / 2 <= dist && dist <= CIRCLE_RADIUS + width / 2);
        } else {
            if (circle) {
                double radius = Vector2D.dist(p1, p2)/2;
                Vector2D center = Vector2D.middle(p1, p2);
                double d = Vector2D.dist(center, new Vector2D(x, y));

                return (Math.abs(d - radius) <= width/2);
            }
            Vector2D v = p2.minus(p1);

            double l = v.getNorm() - from.getRadius() - to.getRadius();
            Vector2D n = new Vector2D(-v.y, v.x);

            v.normalize();
            n.normalize();

            // On prend en compte le rayon des points d'attache
            Vector2D p1bis = p1.plus(v.multiply(from.getRadius()));

            Vector2D p = new Vector2D(x - p1bis.x, y - p1bis.y);

            double dp1 = p.dotProduct(n);
            double dp2 = p.dotProduct(v);

            return (Math.abs(dp1) <= width / 2 && Math.abs(dp2 - l / 2) <= l / 2);
        }
    }

    @Override
    public void paint(Draw d, Graphics2D g) {
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        Vector2D p1 = from.getCenterPos();
        Vector2D p2 = to.getCenterPos();

        if (p1 == null && p2 == null) {
            // nothing to paint
        } else if (p1 == null || p2 == null) {
            paintCircle(d, g, p2 == null ? p1 : p2);
        } else {
            if (circle) {
                paintCircle(d, g, p1, p2);
            } else {
                if (arrow) {
                    paintArrow(d, g);
                    Vector2D v = p2.minus(p1);
                    v.normalize();
                    // Ajustement de p2 pour une flèche plus jolie
                    p2 = p2.minus(v.multiply(to.getRadius() + width));
                }
                paintLine(d, g, p1, p2);
            }
        }
    }

    private void paintLine(Draw d, Graphics2D g, Vector2D a, Vector2D b) {
        Vector2D v1 = d.toDrawCoordinates(a);
        Vector2D v2 = d.toDrawCoordinates(b);
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
    }

    private void paintCircle(Draw d, Graphics2D g, Vector2D a) {
        Vector2D aPos = d.toDrawCoordinates(a);
        Vector2D bPos = d.toDrawCoordinates(a.plus(new Vector2D(2 * CIRCLE_RADIUS, 0)));
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        double radius = d.toDrawScale(CIRCLE_RADIUS);
        g.draw(new Ellipse2D.Double((aPos.x + bPos.x) / 2 - radius, (aPos.y + bPos.y) / 2 - radius, 2 * radius, 2 * radius));
    }

    private void paintCircle(Draw d, Graphics2D g, Vector2D a, Vector2D b) {
        Vector2D aPos = d.toDrawCoordinates(a);
        Vector2D bPos = d.toDrawCoordinates(b);
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        double radius = d.toDrawScale(Vector2D.dist(a, b) / 2);
        g.draw(new Ellipse2D.Double((aPos.x + bPos.x) / 2 - radius, (aPos.y + bPos.y) / 2 - radius, 2 * radius, 2 * radius));
    }

    private void paintArrow(Draw d, Graphics2D g) {
        Vector2D p1 = from.getCenterPos();
        Vector2D p2 = to.getCenterPos();
        Vector2D v = new Vector2D(p2.x - p1.x, p2.y - p1.y); // Direction vector
        Vector2D n = new Vector2D(-v.y, v.x); // Normal vector
        v.normalize();
        n.normalize();

        int h = (int) (10 * Math.sqrt(width));
        int w = (int) (5 * Math.sqrt(width));

        //Vector2D middle = Vector2D.middle(p1, p2);

        Vector2D head = p2.minus(v.multiply(to.getRadius() + width/2 + 1)); // head of the arrow
        Vector2D a = head.minus(v.multiply(w)).plus(n.multiply(h / 2));
        Vector2D b = head.minus(v.multiply(w)).minus(n.multiply(h / 2));

        paintLine(d, g, head, a);
        paintLine(d, g, head, b);
    }
}
