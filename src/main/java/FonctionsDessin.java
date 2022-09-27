/*=============================================
Interface FonctionsDessin contenant les méthodes
permettants d'afficher à l'écran certaines formes
Auteur : Béryl CASSEL
Date de création : 18/03/2022
Date de dernière modification : 18/03/2022
=============================================*/

import java.awt.Graphics;
import java.awt.Point;

public interface FonctionsDessin {

    /**
     * @return x, solution of (a+bx)²+(c+dx)²=e²
     */
    private static double resolve(double a, double b, double c, double d, double e) {
        return (-Math.sqrt(Math.abs(2*a*b*c*d - a*a*d*d - b*b*c*c + b*b*e*e + d*d*e*e)) - (a*b+c*d)) / (b*b+d*d);
    }

    public default Point sym(int x, int y, double a, double b,int x1, int y1, int x2, int y2){
        double alpha = a*((double) x) - ((double) y)+2*b;
        double beta = (double) (x2-x1);
        double gamma = (double) (y2-y1);
        int xM = (int) Math.round((gamma*(((double) y-alpha))+beta*((double) x))/(a*gamma+beta));
        int yM = (int) Math.round(alpha+a*((double) xM));
        Point p = new Point(xM,yM);
        return p;
    }

    public default void calcArc(int x1, int y1, int x2, int y2, Graphics g) {

        // Parameters
        //------------
        double h = 50; // >0 and <=p
        
        // Computation
        //------------
        
        double x3 = (x1+x2)/2;
        double y3 = (y1+y2)/2; // (x3,y3) middle of (x1,y1) and (x2,y2)
        double p = Math.hypot(x3-x1,y3-y1); // distance between (x1,y1) and (x3,y3) solve (x3-x1)²+(y3-y1)²=p²
        double r = (p*p+h*h)/(2*h); // on right triangle, solve p²+(r-h)²=r²
        double a = (((double) y1)-((double)y2)) / (((double) x1)-((double)x2));
        double b = ((double) y1)-a*((double) x1); // y=ax+b for (x1,y1) and (x2,y2)
        double c = -1/a;
        double d = y3-c*x3; // line y=cx+d is perpendicular with line y=ax+b
        
        
        // y0=c.x0+d so distance between (x0,y0) and (x3,y3) solve (x3-x0)²+(y3-(c.x0+d))² = (r-h)²
        // pbm ici!!!
        double x0 = resolve(x3, -1, y3-d, -c, r-h);
        double y0 = c*x0+d;
        Point m = sym((int) x0,(int) y0,a,b,x1,y1,x2,y2);
        
        double alpha1 = Math.atan2(y1-y0,x1-x0);
        double alpha2 = Math.atan2(y2-y0,x2-x0);
        //double alphabis1 = Math.atan2(y1-m.y,x1-m.x);
        //double alphabis2 = Math.atan2(y2-m.y,x2-m.x);
        
        // Display
        //------------
        
        int x = (int) Math.round(x0 - r);
        int y = (int) Math.round(y0 - r);
        int xbis = (int) Math.round(m.x - r);
        int ybis = (int) Math.round(m.y - r);
        int width = (int) Math.round(2*r);
        int height = width;
        // angles are negative because Swing origin is on top-left corner with descending ordinates
        int startAngle = (int) -Math.round(Math.toDegrees(alpha1));
        int arcAngle = (int) -Math.round(Math.toDegrees(alpha2 - alpha1));
        //int startAnglebis = (int) -Math.round(Math.toDegrees(alphabis1));
        //int arcAnglebis = (int) -Math.round(Math.toDegrees(alphabis2 - alphabis1));
        g.drawArc(x, y, width, height, startAngle, arcAngle);
        g.drawArc(xbis,ybis,width,height,180+startAngle,arcAngle);
    }

    public default void fleche(int x1, int x2, int x3, int x4,int[] tab){
        int nb = 10;
        double norme = Math.sqrt(Math.pow((x1-x3),2)+Math.pow((x2-x4),2));
        double alpha = Math.acos((x3-x1)/norme);
        if (x4>=x2){
            double a1 = 3*Math.PI/4 - alpha;
            tab[0] = (int) Math.round(x3+nb*Math.cos(a1));
            tab[1] = (int) Math.round(x4-nb*Math.sin(a1));
            tab[2] = (int) Math.round(x3-nb*Math.sin(a1));
            tab[3] = (int) Math.round(x4-nb*Math.cos(a1));
        } else {
            double a1 = 3*Math.PI/4 + alpha;
            tab[0] = (int) Math.round(x3+nb*Math.cos(a1));
            tab[1] = (int) Math.round(x4-nb*Math.sin(a1));
            tab[2] = (int) Math.round(x3-nb*Math.sin(a1));
            tab[3] = (int) Math.round(x4-nb*Math.cos(a1));
        }
    }
    
}
