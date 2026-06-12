package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginFrame {

    //PALETA DE COLORES
    private static final Color ROJO_BA      = new Color(180, 20, 20);
    private static final Color ROJO_BOTON   = new Color(160, 15, 15);
    private static final Color ROJO_HOVER   = new Color(200, 30, 30);
    private static final Color GRIS_CAMPO   = new Color(245, 245, 245);
    private static final Color GRIS_TEXTO   = new Color(120, 120, 120);
    private static final Color BLANCO       = Color.WHITE;

    private static final String USUARIO="admin";
    private static final String PASSWORD="1234";
    private static final int MAX_INTENTOS = 3;

    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton btnLogin;
    private JLabel lblErorr;
    private JCheckBox chkMostrar;
    private int intentosFallidos;


    public LoginFrame(){
        configurarVentana();
        JPanel panelFondo = crearPanelFondo();
        JPanel tarjeta = crearTarjetaLogin();

        panelFondo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor=GridBagConstraints.CENTER;
        panelFondo.add(tarjeta,gbc);

        setContenPane(panelFondo);
        setVisible(true);
    }

    


}
