/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.StepByStep;

import Inforeg.Draw.Draw;

/**
 *
 * @author remir
 */
interface StepAction {
    /**
     * Action à executer dans le sens direct
     * @param d Zone de dessin
     */
    public void execute(Draw d);
    /**
     * Action à executer dans le sens inverse
     * @param d 
     */
    public void reverse(Draw d);
} 
