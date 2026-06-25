        package GUI;

        import sistema.bancario.GestorBancario;

        import javax.swing.*;
        import javax.swing.border.*;
        import java.awt.*;


        public class ReportePanel extends JPanel {

        private static final Color ROJO_BA    = new Color(180, 20, 20);
        private static final Color GRIS_CLARO = new Color(248, 248, 248);
        private static final Color GRIS_BORDE = new Color(225, 225, 225);
        private static final Color GRIS_TEXTO = new Color(110, 110, 110);
        private static final Color BLANCO     = Color.WHITE;
        private static final Color VERDE      = new Color(34, 139, 34);

        private final GestorBancario gestor;
        private JPanel panelResultado;

        public ReportePanel(GestorBancario gestor) {
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
        tarjeta.setPreferredSize(new Dimension(560, 420));agregarTitulo(tarjeta);
        tarjeta.add(Box.createVerticalStrut(16));

        JPanel filaBotones = new JPanel();
        filaBotones.setLayout(new BoxLayout(filaBotones, BoxLayout.X_AXIS));
        filaBotones.setOpaque(false);
        filaBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaBotones.add(crearBotonGenerar());
        filaBotones.add(Box.createHorizontalStrut(10));
        filaBotones.add(crearBotonHistorialSaldos());
        tarjeta.add(filaBotones);

        tarjeta.add(Box.createVerticalStrut(16));

        panelResultado = new JPanel();
        panelResultado.setLayout(new BorderLayout());
        panelResultado.setOpaque(false);
        tarjeta.add(panelResultado);

        add(tarjeta);
        }

        private JButton crearBotonHistorialSaldos() {
        JButton btn = new JButton("Historial de Saldos Consolidados");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(ROJO_BA);
        btn.setBackground(BLANCO);
        btn.setBorder(new LineBorder(ROJO_BA, 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> accionVerHistorialSaldos());
        return btn;
        }

        private void accionVerHistorialSaldos() {
        panelResultado.removeAll();

        String historial = gestor.cargarSaldosBinario();

        JTextArea area = new JTextArea(
            historial == null || historial.isBlank()
                    ? "No hay historial de saldos consolidados guardado."
                    : historial
        );
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(GRIS_CLARO);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(new LineBorder(GRIS_BORDE, 1, true));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(440, 220));

        panelResultado.add(scroll, BorderLayout.CENTER);
        panelResultado.revalidate();
        panelResultado.repaint();
        }

        private void agregarTitulo(JPanel panel) {
        JLabel lbl = new JLabel("Reporte General del Banco");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(ROJO_BA);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));

        JSeparator sep = new JSeparator();
        sep.setForeground(GRIS_BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sep);
        }

        private JButton crearBotonGenerar() {
        JButton btn = new JButton("Generar Reporte General");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BLANCO);
        btn.setBackground(ROJO_BA);
        btn.setBorder(new EmptyBorder(10, 28, 10, 28));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> accionGenerar());
        return btn;
        }

        private void accionGenerar() {
        panelResultado.removeAll();

        StringBuilder sb = new StringBuilder();

        sb.append("=== TOTALES DEL BANCO ===\n\n");
        sb.append(String.format("Total de cuentas:        %d%n", gestor.getTotalCuentas()));
        sb.append(String.format("Total en Ahorros:         L. %,.2f%n", gestor.calcularTotalAhorros()));
        sb.append(String.format("Total en Corriente:       L. %,.2f%n", gestor.calcularTotalCorriente()));
        sb.append(String.format("Total en Plazo Fijo:      L. %,.2f%n", gestor.calcularTotalPlazoFijo()));
        sb.append(String.format("TOTAL GENERAL DEL BANCO:  L. %,.2f%n", gestor.calcularTotalGeneral()));
        sb.append("\n");

        String listado = gestor.listarCuentas();
        sb.append("=== LISTADO DE CUENTAS ===\n\n");
        if (listado == null || listado.isBlank()) {
        sb.append("No hay cuentas registradas.\n");
        } else {
        sb.append(listado);
        }
        sb.append("\n");

        sb.append("=== HISTORIAL DE TRANSACCIONES (TODAS LAS CUENTAS) ===\n\n");
        if (listado == null || listado.isBlank()) {
        sb.append("No hay transacciones para mostrar.\n");
        } else {
        String[] lineas = listado.split("\n");
        for (String linea : lineas) {
            if (linea.isBlank()) continue;
            String[] partes = linea.split("\\|");
            if (partes.length == 0) continue;
            String numeroCuenta = partes[0].trim();

            sb.append("--- Cuenta ").append(numeroCuenta).append(" ---\n");
            String historial = gestor.leerHistorialCuenta(numeroCuenta);
            sb.append(historial == null || historial.isBlank()
                    ? "Sin movimientos.\n"
                    : historial);
            sb.append("\n");
        }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(GRIS_CLARO);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(new LineBorder(GRIS_BORDE, 1, true));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(440, 220));
        panelResultado.add(scroll, BorderLayout.CENTER);
        panelResultado.revalidate();
        panelResultado.repaint();
        }
        }