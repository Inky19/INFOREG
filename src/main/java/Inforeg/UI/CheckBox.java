package Inforeg.UI;

import Inforeg.AssetLoader;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/**
 * Checkbox
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 */
public class CheckBox extends JCheckBox {

    boolean checked = false;

    public CheckBox(String s) {
        super(s);
        this.setIcon(AssetLoader.checkBox0);
        this.setSelectedIcon(AssetLoader.checkBox1);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checked = !checked;
                if (checked) {
                    CheckBox.this.setForeground(Color.BLACK);
                } else {
                    CheckBox.this.setForeground(Color.GRAY);
                }

            }
        });
        this.setForeground(Color.GRAY);
    }
}
