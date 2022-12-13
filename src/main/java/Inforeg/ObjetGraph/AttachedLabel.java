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
public class AttachedLabel {
    
    private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    public String text;
    public String name;
    private Attachable object;
    private Vector2D offset;
    public Color textColor = DEFAULT_FONT_COLOR;
    public Color bgColor = DEFAULT_BACKGROUND_COLOR;

    public AttachedLabel(String label, Attachable object, Vector2D offset) {
        this.text = label;
        this.object = object;
        this.offset = offset;
    }
    
    public AttachedLabel(String label, Attachable object) {
        this.text = label;
        this.object = object;
        this.offset = new Vector2D(0, 0);
    }

    public AttachedLabel(String label, Attachable object, Vector2D offset, Color textColor) {
        this.text = label;
        this.offset = offset;
        this.textColor = textColor;
        this.object = object;
    }

    public AttachedLabel(String label, Attachable object, Vector2D offset, Color textColor, Color backgroundColor) {
        this.text = label;
        this.offset = offset;
        this.object = object;
        this.textColor = textColor;
        this.bgColor = backgroundColor;
    }
    
    
    //public boolean contains(int x,int y) {
    //}
    
    
    
    public void paint(Draw d, Graphics2D g) {
        Point dPos = d.toDrawCoordinates(object.getCenterPos()).plus(offset).toPoint();
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
