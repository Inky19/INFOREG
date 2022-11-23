package Inforeg.Algo;

import Inforeg.Draw.Draw;

/**
 *
 * @author inky19
 */
public abstract class Algorithm {
    
    public final boolean autoStart;

    public Algorithm() {
        this.autoStart = true;
    }
    
    public Algorithm(boolean autoStart) {
        this.autoStart = autoStart;
    }
    
    private String name;
    
    public abstract void process(Draw d);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAutoStart() {
        return autoStart;
    }
    
    
    
    
    
}
