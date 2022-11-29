/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author remir
 */
public class Label {
    
    private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    public String text;
    public String name;
    public Vector2D pos;
    public Color textColor = DEFAULT_FONT_COLOR;
    public Color bgColor = DEFAULT_BACKGROUND_COLOR;

    public Label(String label, Vector2D pos) {
        this.text = label;
        this.pos = pos;
    }

    public Label(String label, Vector2D pos, Color textColor) {
        this.text = label;
        this.pos = pos;
        this.textColor = textColor;
    }

    public Label(String label, Vector2D pos, Color textColor, Color backgroundColor) {
        this.text = label;
        this.pos = pos;
        this.textColor = textColor;
        this.bgColor = backgroundColor;
    }
    
    public void paint(Draw d, Graphics2D g) {
        Point dPos = d.toDrawCoordinates(pos).toPoint();
        Font font = new Font("Arial", Font.BOLD, (int) d.toDrawScale(15));
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        Rectangle2D bg = metrics.getStringBounds(text, g);

        // Determine the X coordinate for the text
        int fontX = (int) (dPos.x - metrics.stringWidth(text) / 2);
        int fontY = dPos.y;
        int offset = (int) d.toDrawScale(5);
        g.setPaint(bgColor);
        g.fillRect(fontX - offset, fontY - (int) bg.getHeight() + metrics.getDescent(), (int) (bg.getWidth() + 2 * offset), (int) bg.getHeight());
        g.setPaint(textColor);
        g.drawString(text, fontX, fontY);
    }
}
