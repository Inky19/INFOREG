/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.StepByStep;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author remir
 */
public class StepByStep {
    private ArrayList<LinkedList<StepAction>> steps;
    private LinkedList<StepAction> currentAction;
    private int stepIndex;
    

    public StepByStep() {
        steps = new ArrayList<>();
        currentAction = new LinkedList<>();
        stepIndex = 0;
    }
    
    public void init() {
        stepIndex = 0;
        steps = new ArrayList<>();
        currentAction = new LinkedList<>();
    }
    // Actions
    public void colorNode(Node n, Color c, Boolean outline) {
        currentAction.add(new ColorNode(n,c, outline));
    }
    public void colorArc(Arc a, Color c) {
        currentAction.add(new ColorArc(a,c));
    }
    
    public void setInfoText(String text) {
        currentAction.add(new SetText(text));
    }
    
    //
    public void nextStep() {
        steps.add(currentAction);
        currentAction = new LinkedList<>();
    }
    
    public boolean executeNextStep(Draw d) {
        if (stepIndex < steps.size()) {
            for (StepAction action : steps.get(stepIndex)) {
                action.execute(d);
            }
            stepIndex++;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean executePreviousStep(Draw d) {
        if (0 < stepIndex) {
            stepIndex--;
            for (StepAction action : steps.get(stepIndex)) {
                action.reverse(d);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public boolean firstStep() {
        return (stepIndex == 0);
    }
    
    public boolean lastStep() {
        return (stepIndex==steps.size());
    }
}
