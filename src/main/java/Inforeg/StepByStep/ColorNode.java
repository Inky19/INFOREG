package Inforeg.StepByStep;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;

/**
 * Couleur d'un nœud
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public class ColorNode implements StepAction {

    Node n;
    Color previousColor;
    Color nextColor;
    Boolean outline;

    public ColorNode(Node n, Color nextColor, Boolean outline) {
        this.n = n;
        this.nextColor = nextColor;
        this.outline = outline;
    }

    @Override
    public void execute(Draw d) {
        previousColor = n.getColorDisplayed();
        if (outline) {
            previousColor = n.getOutlineColor();
            n.setOutlineColor(nextColor);
        } else {
            previousColor = n.getColorDisplayed();
            n.setColorDisplayed(nextColor);
        }
        d.repaint();
    }

    @Override
    public void reverse(Draw d) {
        if (outline) {
            n.setOutlineColor(previousColor);
        } else {
            n.setColorDisplayed(previousColor);
        }
        d.repaint();
    }
}
