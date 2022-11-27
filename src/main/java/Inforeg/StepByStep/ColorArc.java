/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.StepByStep;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;

/**
 *
 * @author remir
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
