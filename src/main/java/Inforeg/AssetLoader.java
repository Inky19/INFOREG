package Inforeg;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Classe qui charge l'ensemble des ressources du logiciel.
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public abstract class AssetLoader {

    private static final Toolkit tkit = Toolkit.getDefaultToolkit();
    // Images
    private static final Image pinImg = tkit.createImage(getURL("asset/icons/cursor/pinCursor.png"));
    private static final Image paintImg = tkit.createImage(getURL("asset/icons/cursor/paintCursor.png"));
    // Icons
    public static final ImageIcon appIco = new ImageIcon(getURL("asset/icon.png"));
    public static final ImageIcon tabIco = new ImageIcon(getURL("asset/icons/tab.png"));
    public static final ImageIcon unsavedTabIco = new ImageIcon(getURL("asset/icons/unsaved_tab.png"));
    public static final ImageIcon moveCursor = new ImageIcon(getURL("asset/icons/move.png"));
    public static final ImageIcon selectCursor = new ImageIcon(getURL("asset/icons/select.png"));
    public static final ImageIcon arcIco = new ImageIcon(getURL("asset/icons/arc.png"));
    public static final ImageIcon nodeIco = new ImageIcon(getURL("asset/icons/node.png"));
    public static final ImageIcon labelIco = new ImageIcon(getURL("asset/icons/label.png"));
    public static final ImageIcon backIco = new ImageIcon(getURL("asset/icons/back.png"));
    public static final ImageIcon forwardIco = new ImageIcon(getURL("asset/icons/forward.png"));
    public static final ImageIcon fitIco = new ImageIcon(getURL("asset/icons/fit.png"));
    public static final ImageIcon downArrow = new ImageIcon(getURL("asset/icons/arrow-down.png"));
    public static final ImageIcon upArrow = new ImageIcon(getURL("asset/icons/arrow-up.png"));
    public static final ImageIcon colorIco = new ImageIcon(getURL("asset/icons/color.png"));
    public static final ImageIcon pinIco = new ImageIcon(getURL("asset/icons/pin.png"));
    public static final ImageIcon previousIco = new ImageIcon(getURL("asset/icons/previous.png"));
    public static final ImageIcon nextIco = new ImageIcon(getURL("asset/icons/next.png"));
    public static final ImageIcon resetIco = new ImageIcon(getURL("asset/icons/reset.png"));
    public static final ImageIcon checkBox0 = new ImageIcon(getURL("asset/icons/checkbox0.png"));
    public static final ImageIcon checkBox1 = new ImageIcon(getURL("asset/icons/checkbox1.png"));
    public static final ImageIcon playIco = new ImageIcon(getURL("asset/icons/play.png"));
    public static final ImageIcon githubIco = new ImageIcon(getURL("asset/icons/github.png"));
    public static final ImageIcon orienteIco = new ImageIcon(getURL("asset/icons/oriente.png"));
    public static final ImageIcon norienteIco = new ImageIcon(getURL("asset/icons/noriente.png"));
    public static final ImageIcon pondereIco = new ImageIcon(getURL("asset/icons/pondere.png"));
    public static final ImageIcon npondereIco = new ImageIcon(getURL("asset/icons/npondere.png"));
    public static final ImageIcon plusIco = new ImageIcon(getURL("asset/icons/plus.png"));
    public static final ImageIcon copyIco = new ImageIcon(getURL("asset/icons/copy.png"));
    public static final ImageIcon checkIco = new ImageIcon(getURL("asset/icons/check.png"));
    // Cursor
    public static final Cursor pinCursor = tkit.createCustomCursor(pinImg, new Point(3, 24), "pinCursor");
    public static final Cursor paintCursor = tkit.createCustomCursor(paintImg, new Point(3, 29), "paintCursor");

    public static URL getURL(String path) {
        return AssetLoader.class.getClassLoader().getResource(path);
    }
}
