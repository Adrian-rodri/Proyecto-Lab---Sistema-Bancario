package GUI;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
public class MainFrame extends JFrame{
    
    public MainFrame(){
        setTitle("Banco Atlantida - Inciar Sesion");
        setSize(900, 620);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        this.add(new LoginFrame());
        this.setBackground(new Color(180, 20, 20));
        this.setVisible(true);
    }
}
