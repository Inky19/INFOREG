package Inforeg.StepByStep;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Permet l'enregistrement d'une séquence d'action sur le graphe pour visualiser
 * les algorithmes.
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public class StepByStep {

    private ArrayList<LinkedList<StepAction>> steps;
    private LinkedList<StepAction> currentStep;
    private int stepIndex;

    public StepByStep() {
        steps = new ArrayList<>();
        currentStep = new LinkedList<>();
        stepIndex = 0;
    }

    /**
     * Supprime toutes les étapes enregistrées
     */
    public void clear() {
        stepIndex = 0;
        steps = new ArrayList<>();
        currentStep = new LinkedList<>();
    }

    // Actions possibles
    public void colorNode(Node n, Color c, Boolean outline) {
        currentStep.add(new ColorNode(n, c, outline));
    }

    public void colorArc(Arc a, Color c) {
        currentStep.add(new ColorArc(a, c));
    }

    public void setInfoText(String text) {
        currentStep.add(new SetText(text));
    }

    // Fin actions possibles
    /**
     * @return nb le nombre d'étapes enregistrées
     */
    public int getNbStep() {
        return steps.size();
    }

    public int getCurrentStepIndex() {
        return stepIndex;
    }

    /**
     *
     * @return true si l'étape en cours ne contient pas d'action.
     */
    public boolean stepEmpty() {
        return currentStep.isEmpty();
    }

    /**
     * Permet d'enregistrer l'étape courante et de passer à la suivante
     */
    public void nextStep() {
        steps.add(currentStep);
        currentStep = new LinkedList<>();
    }

    /**
     * Affiche l'étape suivante
     *
     * @param d Zone de dessin
     * @return false si il n'y a plus d'étape à afficher
     */
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

    /**
     * Affiche l'étape précédente
     *
     * @param d
     * @return false si il n'y a plus d'étape à afficher
     */
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

    public boolean isFirstStep() {
        return (stepIndex == 0);
    }

    public boolean isLastStep() {
        return (stepIndex == steps.size());
    }
}
