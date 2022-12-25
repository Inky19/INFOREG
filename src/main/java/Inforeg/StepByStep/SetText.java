package Inforeg.StepByStep;

import Inforeg.Draw.Draw;

/**
 * Action de modifier du texte
 *
 * @author Rémi RAVELLI
 * @author François MARIE
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
