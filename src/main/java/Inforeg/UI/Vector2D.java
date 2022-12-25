package Inforeg.UI;

import java.awt.Point;

/**
 * Vecteur 2D
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

    public double dotProduct(Vector2D p) {
        return x * p.x + y * p.y;
    }

    public double getNorm() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double l = getNorm();
        x /= l;
        y /= l;
    }

    public Vector2D plus(Vector2D p) {
        return new Vector2D(this.x + p.x, this.y + p.y);
    }

    public Vector2D minus(Vector2D p) {
        return new Vector2D(this.x - p.x, this.y - p.y);
    }

    public Vector2D multiply(double a) {
        return new Vector2D(a * x, a * y);
    }

    public static Vector2D middle(Vector2D p1, Vector2D p2) {
        return new Vector2D((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    public static double dist(Vector2D a, Vector2D b) {
        return a.minus(b).getNorm();
    }

}
