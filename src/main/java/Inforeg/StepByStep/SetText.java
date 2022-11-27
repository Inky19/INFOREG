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
public class SetText implements StepAction {
    String nextText;
    String previousText;

    public SetText(String nextText) {
        this.nextText = nextText;
    }

    @Override
    public void execute(Draw d) {
        previousText = d.getInfoText();
        d.setInfoText(nextText);
    }

    @Override
    public void reverse(Draw d) {
        d.setInfoText(previousText);
    }
    
}
