/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
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
