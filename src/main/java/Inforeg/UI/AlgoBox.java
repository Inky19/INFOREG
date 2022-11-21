/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Inforeg.UI;

import Inforeg.Algo.Algorithm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author inky19
 */
public class AlgoBox extends JPanel{
    
    private static Dimension buttonSize = new Dimension(124,27);

    private String name;
    private List<Algorithm> algos;
    private AlgoWindow window;
    
    public AlgoBox(String name, AlgoWindow window, ImageIcon ico){
        super();
        this.name = name;
        this.window = window;
        algos = new ArrayList<>();
        this.setLayout(new BoxLayout(this, 1));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel icoLabel = new JLabel(ico);
        icoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(nameLabel);
        this.add(icoLabel);
        this.setVisible(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAlgo(Algorithm algo) {
        algos.add(algo);
        JButton algoButton = new JButton(algo.getName());
        algoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        algoButton.setMaximumSize(buttonSize);
        algoButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                window.selectAlgo(algo);
            }
        });
        
        this.add(algoButton);
    }

    public void removeAlgo(Algorithm algo) {
        algos.remove(algo);
    }
    
    public int nbAlgos(){
        return algos.size();
    }
    
}
