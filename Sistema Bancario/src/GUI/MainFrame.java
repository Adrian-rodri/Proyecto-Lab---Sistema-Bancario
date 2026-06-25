package GUI;

import sistema.bancario.GestorBancario;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private static JPanel paneles;
    private static CardLayout cardLayout;

    private static GestorBancario gestor;
    private static MainFrame instancia;

    public MainFrame() {
        instancia = this;
        setTitle("Banco Atlantida - Inciar Sesion");
        setSize(900, 620);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        gestor = new GestorBancario();

        cardLayout = new CardLayout();
        paneles = new JPanel(cardLayout);
        paneles.add(new LoginScreen(), "Inicio");
        add(paneles);
        this.setVisible(true);
    }

    public static void cambiarPantalla(JPanel newPanel, String name) {
        paneles.add(newPanel, name);
        cardLayout.show(paneles, name);
    }

    public static GestorBancario getGestor() {
        return gestor;
    }
}
