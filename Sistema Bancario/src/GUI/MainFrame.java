package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class MainFrame extends JFrame{
    private static JPanel paneles;
    private static CardLayout cardLayout;
    
    public MainFrame(){
        setTitle("Banco Atlantida - Inciar Sesion");
        setSize(900, 620);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        cardLayout= new CardLayout();
        paneles= new JPanel(cardLayout);
        paneles.add(new LoginFrame(),"Inicio");
        add(paneles);
        this.setVisible(true);
    }
    public static void cambiarPantalla(JPanel newPanel, String name){
        paneles.add(newPanel,name);
        cardLayout.show(paneles, name);
    }
}
