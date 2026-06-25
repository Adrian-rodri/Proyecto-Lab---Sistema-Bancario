        package GUI;

        import javax.swing.*;
        import javax.swing.border.*;
        import java.awt.*;


        public class InteresesPanel extends JPanel {

        private static final Color ROJO_BA    = new Color(180, 20, 20);
        private static final Color GRIS_CLARO = new Color(248, 248, 248);
        private static final Color GRIS_BORDE = new Color(225, 225, 225);
        private static final Color GRIS_TEXTO = new Color(110, 110, 110);
        private static final Color BLANCO     = Color.WHITE;
        private static final Color VERDE      = new Color(34, 139, 34);

        private JPanel panelResultado;
        private final sistema.bancario.GestorBancario gestor;

        public InteresesPanel(sistema.bancario.GestorBancario gestor) {
        this.gestor = gestor;
        setLayout(new GridBagLayout());
        setBackground(GRIS_CLARO);
        construir();
        }

        private void construir() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(BLANCO);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(GRIS_BORDE, 1, true),
            new EmptyBorder(35, 50, 35, 50)
        ));
        tarjeta.setPreferredSize(new Dimension(480, 320));

        JLabel titulo = new JLabel("Aplicar Intereses Mensuales");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ROJO_BA);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(6));

        JSeparator sep = new JSeparator();
        sep.setForeground(GRIS_BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(sep);
        tarjeta.add(Box.createVerticalStrut(20));

        JLabel lblInfo = new JLabel("<html>Esta acción recorrerá todas las cuentas del<br>"
            + "sistema y acreditará el interés mensual<br>correspondiente a cada una.</html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfo.setForeground(GRIS_TEXTO);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblInfo);
        tarjeta.add(Box.createVerticalStrut(28));

        JButton btnAplicar = new JButton("Aplicar Intereses Mensuales");
        btnAplicar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAplicar.setForeground(BLANCO);
        btnAplicar.setBackground(ROJO_BA);
        btnAplicar.setBorder(new EmptyBorder(10, 28, 10, 28));
        btnAplicar.setFocusPainted(false);
        btnAplicar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAplicar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAplicar.addActionListener(e -> accionAplicar());
        tarjeta.add(btnAplicar);
        tarjeta.add(Box.createVerticalStrut(16));

        panelResultado = new JPanel();
        panelResultado.setLayout(new BoxLayout(panelResultado, BoxLayout.Y_AXIS));
        panelResultado.setOpaque(false);
        panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(panelResultado);

        add(tarjeta);
        }

        private void accionAplicar() {
        gestor.aplicarInteresesMensuales();
        panelResultado.removeAll();
        JLabel lbl = new JLabel("APLICADOS");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(VERDE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResultado.add(lbl);

        panelResultado.revalidate();
        panelResultado.repaint();
        }
        }