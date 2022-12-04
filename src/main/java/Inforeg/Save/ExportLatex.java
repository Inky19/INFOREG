/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Inforeg.Save;

import Inforeg.Draw.Draw;
import Inforeg.UI.LatexWindow;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author inky19
 */
public abstract class ExportLatex {

    public static void export(JFrame frame, Draw d){
        LatexWindow window = new LatexWindow(frame, d);
        window.setVisible(true);
    }
}
