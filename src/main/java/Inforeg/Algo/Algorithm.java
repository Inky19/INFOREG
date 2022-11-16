package Inforeg.Algo;

import Inforeg.Draw.Draw;

/**
 *
 * @author inky19
 */
public abstract class Algorithm {

    private String name;
    
    public abstract boolean process(Draw d);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    
}
