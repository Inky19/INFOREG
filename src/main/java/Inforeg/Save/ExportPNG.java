package Inforeg.Save;

import Inforeg.Draw.Draw;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 * Export en image PNG
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public abstract class ExportPNG {

    public static void export(Draw d) {
        JFileChooser fileExplorer = new JFileChooser();
        int res = fileExplorer.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            d.prepareExport();
            File file = fileExplorer.getSelectedFile();
            String[] filePath = Utils.formatPath(file, "png");
            File formatFile = new File(filePath[1]);
            BufferedImage im = new BufferedImage(d.getWidth(), d.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D gfx = im.createGraphics();
            d.paint(gfx);
            try {
                ImageIO.write(im, "PNG", formatFile);
            } catch (IOException ex) {
                Logger.getLogger(ExportPNG.class.getName()).log(Level.SEVERE, null, ex);
            }
            d.restoreAfterExport();
        }
    }
}
