package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Label sur un objet
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */

public class AttachedLabel implements GraphObject {
    
    private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEFAULT_SIZE = 15;
    public String text;
    public String name;
    public int size = DEFAULT_SIZE;
    public Vector2D pos;
    private Vector2D offset = new Vector2D(0, 0);
    public Color textColor = DEFAULT_FONT_COLOR;
    public Color bgColor = DEFAULT_BACKGROUND_COLOR;

    public AttachedLabel() {
        this.pos = new Vector2D(0, 0);
    }

    public AttachedLabel(String label, Vector2D pos, Vector2D offset) {
        this.text = label;
        this.pos = pos;
        this.offset = offset;
    }

    public AttachedLabel(String label, Vector2D pos) {
        this.text = label;
        this.pos = pos;
    }

    public AttachedLabel(String label, Vector2D pos, Color textColor) {
        this.text = label;
        this.textColor = textColor;
        this.pos = pos;
    }

    public AttachedLabel(String label, Vector2D pos, Vector2D offset, Color textColor, Color backgroundColor) {
        this.text = label;
        this.offset = offset;
        this.pos = pos;
        this.textColor = textColor;
        this.bgColor = backgroundColor;
    }
    
    @Override
    public void paint(Draw d, Graphics2D g) {
        Point dPos = d.toDrawCoordinates(pos.plus(offset)).toPoint();
        Font font = new Font("Arial", Font.BOLD, (int) d.toDrawScale(size));
        FontMetrics metrics = g.getFontMetrics(font);
        Rectangle2D bg = metrics.getStringBounds(text, g);

        int fontX = (int) (dPos.x - metrics.stringWidth(text) / 2);
        int fontY = dPos.y;
        int paddingHorizontal = (int) d.toDrawScale(4); // padding à gauche et à droite
        if (bgColor != null) {
            g.setPaint(bgColor);
            g.fillRect(fontX - paddingHorizontal, fontY - (int) bg.getHeight()/2, (int) (bg.getWidth() + 2 * paddingHorizontal), (int) bg.getHeight());
        }
        g.setPaint(textColor);
        g.setFont(font);
        g.drawString(text, fontX, (int) (fontY - metrics.getHeight() / 2 + metrics.getAscent()));
    }

    public boolean contains(int x, int y) {
        return false;
    }
}
