package GUI;

import sistema.bancario.GestorBancario;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class LoginScreen extends JPanel {

    //PALETA DE COLORES
    private static final Color ROJO_BA = new Color(180, 20, 20);
    private static final Color ROJO_BOTON = new Color(160, 15, 15);
    private static final Color ROJO_HOVER = new Color(200, 30, 30);
    private static final Color GRIS_CAMPO = new Color(245, 245, 245);
    private static final Color GRIS_TEXTO = new Color(120, 120, 120);
    private static final Color BLANCO = Color.WHITE;

    private static final String USUARIO_VALIDO = "admin";
    private static final String CONTRASENA_VALIDA = "1234";
    private static final int MAX_INTENTOS = 3;

    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton btnLogin;
    private JLabel lblErorr;
    private JCheckBox chkMostrar;
    private int intentosFallidos;

    public LoginScreen() {
        setLayout(new BorderLayout());
        JPanel panelFondo = crearPanelFondo();
        JPanel tarjeta = crearTarjetaLogin();

        panelFondo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        panelFondo.add(tarjeta, gbc);
        this.add(panelFondo,BorderLayout.CENTER);
        setVisible(true);
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

    private JPanel crearTarjetaLogin() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(BLANCO);
        tarjeta.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(35, 50, 35, 50)
        ));

        tarjeta.setPreferredSize(new Dimension(420, 460));
        tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };

        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(35, 50, 35, 50));
        tarjeta.setPreferredSize(new Dimension(430, 470));

        JLabel lblLogo = crearLogo();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblLogo);
        tarjeta.add(Box.createVerticalStrut(20));

        tarjeta.add(crearEtiqueta("Usuario"));
        tarjeta.add(Box.createVerticalStrut(5));
        campoUsuario = new JTextField();
        estilizarCampo(campoUsuario, "Ingresa tu usuario");
        tarjeta.add(campoUsuario);
        tarjeta.add(Box.createVerticalStrut(15));

        tarjeta.add(crearEtiqueta("Contraseña"));
        tarjeta.add(Box.createVerticalStrut(5));

        campoPassword = new JPasswordField();
        estilizarCampo(campoPassword, "Ingresa tu contraseña");
        tarjeta.add(campoPassword);

        tarjeta.add(Box.createVerticalStrut(5));

        chkMostrar = new JCheckBox("Mostrar contraseña");
        chkMostrar.setOpaque(false);
        chkMostrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkMostrar.setFocusPainted(false);
        chkMostrar.addActionListener(e -> toggleContrasena());
        tarjeta.add(chkMostrar);

        tarjeta.add(Box.createVerticalStrut(15));

        lblErorr = new JLabel(" ");
        lblErorr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblErorr.setForeground(ROJO_BA);
        lblErorr.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblErorr);
        tarjeta.add(Box.createVerticalStrut(20));

        btnLogin = crearBotonLogin();
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(btnLogin);
        return tarjeta;
    }

    private Image cargarImagen(String ruta) {
        try {
            URL url = getClass().getResource(ruta);
            if (url != null) {
                return new ImageIcon(url).getImage();
            }

        } catch (Exception e) {
            System.out.println("No se pudo cargar imagen: " + ruta);
        }
        return null;
    }

    private JLabel crearLogo() {
        Image img = cargarImagen("/ASSETS/LOGO4.png");
        Image escalada = img.getScaledInstance(280, 150, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(escalada));
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Seogeo UI", Font.PLAIN, 13));
        lbl.setForeground(Color.darkGray);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private void estilizarCampo(JTextField campo, String placeholder) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(GRIS_CAMPO);
        campo.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        campo.setForeground(GRIS_TEXTO);
        campo.setText(placeholder);
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(GRIS_TEXTO);
                    campo.setText(placeholder);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar((char) 0);
                    }
                }
            }
        });

        if (campo instanceof JPasswordField) {
            ((JPasswordField) campo).setEchoChar((char) 0);
        }
    }

    private void toggleContrasena() {
        if (chkMostrar.isSelected()) {
            campoPassword.setEchoChar((char) 0);
        } else {
            campoPassword.setEchoChar('•');
        }
    }

    private JButton crearBotonLogin() {
        JButton btn = new JButton("INICIAR SESIÓN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? ROJO_HOVER
                        : getModel().isRollover() ? ROJO_HOVER : ROJO_BOTON);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BLANCO);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(BLANCO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> validarLogin());

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validarLogin();
                }
            }
        };
        campoUsuario.addKeyListener(enterListener);
        campoPassword.addKeyListener(enterListener);

        return btn;
    }

    private void mostrarError(String mensaje) {
        lblErorr.setText(mensaje);
    }

    private void validarLogin() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoPassword.getPassword()).trim();

        if (usuario.equals("Ingresa tu usuario")) {
            usuario = "";
        }
        if (contrasena.equals("Ingresa tu contraseña")) {
            contrasena = "";
        }

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Por favor completa todos los campos.");
            return;
        }

        if (usuario.equals(USUARIO_VALIDO) && contrasena.equals(CONTRASENA_VALIDA)) {
            loginExitoso();
        } else {
            intentosFallidos++;
            int restantes = MAX_INTENTOS - intentosFallidos;

            if (intentosFallidos >= MAX_INTENTOS) {
                bloquearSistema();
            } else {
                mostrarError("Usuario o contraseña incorrectos. Intentos restantes: " + restantes);
                limpiarCampos();
            }
        }
    }

    private void loginExitoso() {
        lblErorr.setText(" ");
        GestorBancario gestor = MainFrame.getGestor();
        MainFrame.cambiarPantalla(new MenuScreen(gestor), "Menu");
    }

    private void bloquearSistema() {
        btnLogin.setEnabled(false);
        campoUsuario.setEnabled(false);
        campoPassword.setEnabled(false);
        mostrarError("Demasiados intentos fallidos. Sistema bloqueado.");
        JOptionPane.showMessageDialog(this,
                "Has superado el número máximo de intentos.\nPor favor contacta al administrador.",
                "Acceso bloqueado",
                JOptionPane.ERROR_MESSAGE);
    }

    private void limpiarCampos() {
        campoUsuario.setText("");
        campoPassword.setText("");
        campoUsuario.requestFocus();
    }

}
