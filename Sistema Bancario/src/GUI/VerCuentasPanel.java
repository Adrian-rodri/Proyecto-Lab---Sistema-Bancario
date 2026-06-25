        package GUI;

        import sistema.bancario.GestorBancario;

        import javax.swing.*;
        import javax.swing.border.*;
        import java.awt.*;


        public class VerCuentasPanel extends JPanel {

        private static final Color ROJO_BA     = new Color(180, 20, 20);
        private static final Color ROJO_OSCURO = new Color(140, 12, 12);
        private static final Color GRIS_CLARO  = new Color(248, 248, 248);
        private static final Color GRIS_BORDE  = new Color(225, 225, 225);
        private static final Color GRIS_TEXTO  = new Color(110, 110, 110);
        private static final Color BLANCO      = Color.WHITE;

        private final GestorBancario gestor;
        private JTextArea areaResultado;
        private JButton btnAhorros, btnCorriente, btnPlazoFijo;
        private String tipoActivo = "AHO";

        public VerCuentasPanel(GestorBancario gestor) {
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
        tarjeta.setPreferredSize(new Dimension(560, 460));

        JLabel titulo = new JLabel("Ver Cuentas");
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

        JPanel filaBotones = new JPanel(new GridLayout(1, 3, 10, 0));
        filaBotones.setOpaque(false);
        filaBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnAhorros    = crearBotonTipo("Ahorros");
        btnCorriente  = crearBotonTipo("Corriente");
        btnPlazoFijo  = crearBotonTipo("Plazo Fijo");

        btnAhorros.addActionListener(e -> seleccionarTipo("AHO", btnAhorros));
        btnCorriente.addActionListener(e -> seleccionarTipo("CTE", btnCorriente));
        btnPlazoFijo.addActionListener(e -> seleccionarTipo("PF", btnPlazoFijo));

        filaBotones.add(btnAhorros);
        filaBotones.add(btnCorriente);
        filaBotones.add(btnPlazoFijo);

        tarjeta.add(filaBotones);
        tarjeta.add(Box.createVerticalStrut(20));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaResultado.setBackground(GRIS_CLARO);
        areaResultado.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(new LineBorder(GRIS_BORDE, 1, true));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(460, 260));
        tarjeta.add(scroll);

        add(tarjeta);

        seleccionarTipo("AHO", btnAhorros);
        }

        private JButton crearBotonTipo(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(ROJO_BA);
        btn.setBackground(BLANCO);
        btn.setBorder(new LineBorder(ROJO_BA, 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
        }

        private void marcarBotonActivo(JButton activo) {
        for (JButton btn : new JButton[]{btnAhorros, btnCorriente, btnPlazoFijo}) {
            boolean esElActivo = (btn == activo);
            btn.setBackground(esElActivo ? ROJO_BA : BLANCO);
            btn.setForeground(esElActivo ? BLANCO : ROJO_BA);
        }
        }

        private void seleccionarTipo(String prefijo, JButton botonClickeado) {
        tipoActivo = prefijo;
        marcarBotonActivo(botonClickeado);
        cargarListado();
        }

        private void cargarListado() {
        String listado = gestor.listarCuentasPorTipo(tipoActivo);

        if (listado == null || listado.isBlank()) {
            areaResultado.setText("No hay cuentas registradas de este tipo.");
        } else {
            areaResultado.setText(listado);
        }
        }
        }