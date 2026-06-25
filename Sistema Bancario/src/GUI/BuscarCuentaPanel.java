        package GUI;

        import sistema.bancario.CuentaBancaria;
        import sistema.bancario.GestorBancario;

        import javax.swing.*;
        import javax.swing.border.*;
        import java.awt.*;
        import java.util.function.Consumer;
        import java.util.ArrayList;

        public class BuscarCuentaPanel extends JPanel {

        private static final Color ROJO_BA    = new Color(180, 20, 20);
        private static final Color GRIS_CLARO = new Color(248, 248, 248);
        private static final Color GRIS_BORDE = new Color(225, 225, 225);
        private static final Color GRIS_TEXTO = new Color(110, 110, 110);
        private static final Color BLANCO     = Color.WHITE;
        private static final Color VERDE      = new Color(34, 139, 34);

        private JTextField txtNumero;
        private JPanel panelResultado;
        private JComboBox<String> comboCriterio;

        private final Consumer<CuentaBancaria> onCuentaSeleccionada;
        private final sistema.bancario.GestorBancario gestor;
        public BuscarCuentaPanel(GestorBancario gestor, Consumer<CuentaBancaria> onCuentaSeleccionada) {
        this.onCuentaSeleccionada = onCuentaSeleccionada;
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
        tarjeta.setPreferredSize(new Dimension(520, 420));

        JLabel titulo = new JLabel("Buscar Cuenta");
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
        tarjeta.add(Box.createVerticalStrut(24));
        JLabel lbl = new JLabel("Buscar por:");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(GRIS_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lbl);
        tarjeta.add(Box.createVerticalStrut(4));

        comboCriterio = new JComboBox<>(new String[]{"Número de cuenta", "DPI", "Titular (nombre)"});
        comboCriterio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboCriterio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        comboCriterio.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(comboCriterio);
        tarjeta.add(Box.createVerticalStrut(14));

        JPanel filaBusqueda = new JPanel();
        filaBusqueda.setLayout(new BoxLayout(filaBusqueda, BoxLayout.X_AXIS));
        filaBusqueda.setOpaque(false);
        filaBusqueda.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaBusqueda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        txtNumero = new JTextField();
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNumero.setBorder(new CompoundBorder(
        new LineBorder(GRIS_BORDE, 1, true),
        new EmptyBorder(6, 10, 6, 10)
        ));
        filaBusqueda.add(txtNumero);
        filaBusqueda.add(Box.createHorizontalStrut(10));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setForeground(BLANCO);
        btnBuscar.setBackground(ROJO_BA);
        btnBuscar.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> accionBuscar());
        filaBusqueda.add(btnBuscar);

        tarjeta.add(filaBusqueda);
        tarjeta.add(Box.createVerticalStrut(24));

        panelResultado = new JPanel();
        panelResultado.setLayout(new BoxLayout(panelResultado, BoxLayout.Y_AXIS));
        panelResultado.setOpaque(false);
        panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollResultado = new JScrollPane(panelResultado);
        scrollResultado.setBorder(null);
        scrollResultado.setOpaque(false);
        scrollResultado.getViewport().setOpaque(false);
        scrollResultado.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollResultado.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollResultado.setPreferredSize(new Dimension(460, 220));
        tarjeta.add(scrollResultado);

        add(tarjeta);
        }

        private void accionBuscar() {
        panelResultado.removeAll();

        String valor = txtNumero.getText().trim();
        if (valor.isEmpty()) {
        mostrarMensaje("Ingrese un valor para buscar.", Color.RED);
        panelResultado.revalidate();
        panelResultado.repaint();
        return;
        }

        String criterio = (String) comboCriterio.getSelectedItem();

        if ("Número de cuenta".equals(criterio)) {
        CuentaBancaria cuenta = gestor.buscarPorNumero(valor);
        if (cuenta == null) {
        mostrarMensaje("No se encontró ninguna cuenta con ese número.", GRIS_TEXTO);
        } else {
        mostrarResultado(cuenta);
        }

        } else if ("DPI".equals(criterio)) {
        ArrayList<CuentaBancaria> resultados = gestor.buscarPorDPI(valor);
        if (resultados == null || resultados.isEmpty()) {
        mostrarMensaje("No se encontraron cuentas con ese DPI.", GRIS_TEXTO);
        } else {
        for (CuentaBancaria c : resultados) {
            mostrarResultado(c);
        }
        }

        } else {
        ArrayList<CuentaBancaria> resultados = gestor.buscarPorTitular(valor);
        if (resultados == null || resultados.isEmpty()) {
        mostrarMensaje("No se encontraron cuentas con ese nombre.", GRIS_TEXTO);
        } else {
        for (CuentaBancaria c : resultados) {
            mostrarResultado(c);
        }
        }
        }

        panelResultado.revalidate();
        panelResultado.repaint();
        }

        private void mostrarResultado(CuentaBancaria cuenta) {
        JPanel caja = new JPanel();
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        caja.setBackground(new Color(245, 255, 245));
        caja.setBorder(new CompoundBorder(
        new LineBorder(new Color(180, 220, 180), 1, true),
        new EmptyBorder(16, 20, 16, 20)
        ));
        caja.setAlignmentX(Component.LEFT_ALIGNMENT);
        caja.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        agregarFila(caja, "Número de cuenta:", cuenta.getNumeroCuenta());
        agregarFila(caja, "Titular:",          cuenta.getTitular());
        agregarFila(caja, "DPI:",              cuenta.getDpi());
        agregarFila(caja, "Fecha apertura:",   cuenta.getFechaApertura());
        agregarFila(caja, "Saldo:",            "L. " + String.format("%,.2f", cuenta.getSaldo()));

        caja.add(Box.createVerticalStrut(14));

        JButton btnSeleccionar = new JButton("Seleccionar esta cuenta");
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSeleccionar.setForeground(BLANCO);
        btnSeleccionar.setBackground(VERDE);
        btnSeleccionar.setBorder(new EmptyBorder(8, 16, 8, 16));
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSeleccionar.addActionListener(e -> {
        onCuentaSeleccionada.accept(cuenta);
        JOptionPane.showMessageDialog(this,
            "Cuenta " + cuenta.getNumeroCuenta() + " seleccionada.",
            "Cuenta seleccionada", JOptionPane.INFORMATION_MESSAGE);
        });
        caja.add(btnSeleccionar);

        panelResultado.add(caja);
        panelResultado.add(Box.createVerticalStrut(10));
        }
        private void agregarFila(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEtiqueta = new JLabel(etiqueta + "  ");
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEtiqueta.setForeground(GRIS_TEXTO);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblValor.setForeground(Color.DARK_GRAY);

        fila.add(lblEtiqueta);
        fila.add(lblValor);
        panel.add(fila);
        }

        private void mostrarMensaje(String msg, Color color) {
        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResultado.add(lbl);
        panelResultado.revalidate();
        panelResultado.repaint();
        }
        }