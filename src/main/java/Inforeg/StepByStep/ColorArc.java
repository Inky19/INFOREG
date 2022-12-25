package Inforeg.StepByStep;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import java.awt.Color;

/**
 * Couleur d'un arc
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public class ColorArc implements StepAction {

    Arc a;
    Color previousColor;
    Color nextColor;

    public ColorArc(Arc a, Color nextColor) {
        this.a = a;
        this.nextColor = nextColor;
    }

    @Override
    public void execute(Draw d) {
        previousColor = a.getColorDisplayed();
        a.setColorDisplayed(nextColor);
        d.repaint();
    }

    @Override
    public void reverse(Draw d) {
        a.setColorDisplayed(previousColor);
        d.repaint();
    }
}
