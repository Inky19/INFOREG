/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.UI;

import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author Rémi
 */
public class CustomButton extends JButton {
    public Color color;
    public Color focusColor;
    public Color selectedColor;
    private boolean selected = false;
    private boolean enabled = true;
    
    public CustomButton(String text,Color color, Color focusColor, Color selectedColor) {
        super(text);
        
        this.color = color;
        this.focusColor = focusColor;
        this.selectedColor = selectedColor;
        setFocusPainted(false);
        setBorderPainted(false);
        
        focus();
        
        if (color == null) {
            setOpaque(false);
            setBackground(Color.BLACK);
        } else {
            setBackground(color); 
        }
        updateUI();
        
    }
    
    public CustomButton(Icon icon,Color color, Color focusColor, Color selectedColor) {
        super(icon);
        this.color = color;
        this.focusColor = focusColor;
        this.selectedColor = selectedColor;
        setFocusPainted(false);
        setBorderPainted(false);
        
        
        focus();
        if (color==null) {
            setOpaque(false);
            setBackground(Color.BLUE);
        } else {
            setBackground(color);
        }
        updateUI();
    }
    
    public CustomButton(String text,Icon icon,Color color, Color focusColor, Color selectedColor) {
        super(text,icon);
        this.color = color;
        this.focusColor = focusColor;
        this.selectedColor = selectedColor;
        setFocusPainted(false);
        setBorderPainted(false);
        focus();
        if (color==null) {
            setOpaque(false);
            setBackground(Color.BLACK);
        } else {
            setBackground(color);
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
    
    @Override
    public void setEnabled(boolean b) {
        if (color!= null) {
            setBackground(color);
        }
        super.setEnabled(b);
        enabled = b;
        
    }
    
    private void focus() {
        
        addMouseListener(new MouseInputListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected && enabled) {
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
                if (!selected && enabled) {
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
