package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Layout extends JFrame {

    public void guiInitialize(int width, int height) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Tourism Agency");
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
