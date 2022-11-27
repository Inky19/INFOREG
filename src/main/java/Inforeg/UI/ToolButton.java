/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.UI;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author remir
 */
public class ToolButton extends JButton {
    public Color color;
    public Color focusColor;
    public Color selectedColor;
    private boolean selected = false;
    
    public ToolButton(String text,Color color, Color focusColor, Color selectedColor) {
        super(text);
        this.color = color;
        this.focusColor = focusColor;
        this.selectedColor = selectedColor;
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(color); 
        focus();
        if (color == null) {
            setOpaque(false);
        }
        updateUI();
    }
    
    public ToolButton(Icon icon,Color color, Color focusColor, Color selectedColor) {
        super(icon);
        this.color = color;
        this.focusColor = focusColor;
        this.selectedColor = selectedColor;
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(color);
        focus();
        if (color==null) {
            setOpaque(false);
        }
        updateUI();
    }
    
    public ToolButton(String text,Icon icon,Color color, Color focusColor, Color selectedColor) {
        super(text,icon);
        this.color = color;
        this.focusColor = focusColor;
        this.selectedColor = selectedColor;
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(color);
        focus();
        if (color==null) {
            setOpaque(false);
        }
        updateUI();
        
    }
    
    public void select() {
        selected = true;
        if (selectedColor==null) {
            setOpaque(false);
        } else {
            setOpaque(true);
            setBackground(selectedColor);
        }
    }
    
    public void unselect() {
        selected = false;
        if (color==null) {
            setOpaque(false);
            updateUI();
        } else {
            setOpaque(true);
            setBackground(color);
        }
    }
    
    public void focus() {
        
        addMouseListener(new MouseInputListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    if (focusColor==null) {
                        setOpaque(false);
                    } else {
                        setOpaque(true);
                        setBackground(focusColor);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    if (color==null) {
                        setOpaque(false);
                    } else {
                        setOpaque(true);
                        setBackground(color);
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }
    
    
}
