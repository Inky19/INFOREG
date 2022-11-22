/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Rémi
 */
public abstract class AssetLoader {
    
    // Images
    
    // Icons
    public static final ImageIcon appIco = new ImageIcon(getURL("asset/icon.png"));
    public static final ImageIcon tabIco = new ImageIcon(getURL("asset/icons/tab.png"));
    public static final ImageIcon unsavedTabIco = new ImageIcon(getURL("asset/icons/unsaved_tab.png"));
    public static final ImageIcon moveCursor = new ImageIcon(getURL("asset/icons/move.png"));
    public static final ImageIcon selectCursor = new ImageIcon(getURL("asset/icons/select.png"));
    public static final ImageIcon arcIco = new ImageIcon(getURL("asset/icons/arc.png"));
    public static final ImageIcon nodeIco = new ImageIcon(getURL("asset/icons/node.png"));
    public static final ImageIcon labelIco = new ImageIcon(getURL("asset/icons/label.png"));
    public static final ImageIcon dropIco = new ImageIcon(getURL("asset/icons/dropdown.png"));
    public static final ImageIcon backIco = new ImageIcon(getURL("asset/icons/back.png"));
    public static final ImageIcon forwardIco = new ImageIcon(getURL("asset/icons/forward.png"));
    public static final ImageIcon fitIco = new ImageIcon(getURL("asset/icons/fit.png"));
    
    
    
    public static URL getURL(String path) {
        return AssetLoader.class.getClassLoader().getResource(path);
    }
}
