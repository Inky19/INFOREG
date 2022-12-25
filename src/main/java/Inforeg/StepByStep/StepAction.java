package Inforeg.StepByStep;

import Inforeg.Draw.Draw;

/**
 * Interface pour le mode pas à pas
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
interface StepAction {

    /**
     * Action à executer dans le sens direct
     *
     * @param d Zone de dessin
     */
    public void execute(Draw d);

    /**
     * Action à executer dans le sens inverse
     *
     * @param d
     */
    public void reverse(Draw d);
}
