package GUI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;


public class LoginFrame extends JFrame {

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

    private void configurarVentana(){
        setTitle("Banco Atlantida - Inciar Sesion");
        setSize(900,620);
        setMinimumSize(new Dimension(750,500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

    }

    private JPanel crearPanelFondo() {
        return new JPanel() {
            Image imagenFondo = cargarImagen("/ASSETS/FONDO_LOGIN.png");
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    private JPanel crearTarjetaLogin(){
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(BLANCO);
        tarjeta.setBorder(new CompoundBorder(
                new LineBorder(new Color(220,220,220),1,true),
                new EmptyBorder(35,50,35,50)
        ));

        tarjeta.setPreferredSize(new Dimension(420,460));
        
    }




    private Image cargarImagen(String ruta){
        try{
            URL url = getClass().getResource(ruta);
            if(url!=null) return new ImageIcon(url).getImage();

        }catch (Exception e){
            System.out.println("No se pudo cargar imagen: "+ruta);
        }
        return null;
    }




}
