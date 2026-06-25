package GUI;

import sistema.bancario.CuentaBancaria;
import sistema.bancario.GestorBancario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.function.Consumer;


public class SeleccionarCuentaPanel extends JPanel {

    private static final Color ROJO_BA    = new Color(180, 20, 20);
    private static final Color GRIS_CLARO = new Color(248, 248, 248);
    private static final Color GRIS_CAJA  = new Color(240, 240, 240);
    private static final Color GRIS_BORDE = new Color(225, 225, 225);
    private static final Color GRIS_TEXTO = new Color(110, 110, 110);
    private static final Color BLANCO     = Color.WHITE;

    private final GestorBancario gestor;
    private final Consumer<CuentaBancaria> onSeleccionar;

    private JButton btnAhorros, btnCorriente, btnPlazoFijo;
    private JPanel panelLista;
    private String tipoActivo = "AHO";

    public SeleccionarCuentaPanel(GestorBancario gestor, Consumer<CuentaBancaria> onSeleccionar) {
        this.gestor = gestor;
        this.onSeleccionar = onSeleccionar;
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

        JLabel titulo = new JLabel("Seleccionar Cuenta Destino");
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

        btnAhorros   = crearBotonTipo("Ahorros");
        btnCorriente = crearBotonTipo("Corriente");
        btnPlazoFijo = crearBotonTipo("Plazo Fijo");

        btnAhorros.addActionListener(e -> seleccionarTipo("AHO", btnAhorros));
        btnCorriente.addActionListener(e -> seleccionarTipo("CTE", btnCorriente));
        btnPlazoFijo.addActionListener(e -> seleccionarTipo("PF", btnPlazoFijo));

        filaBotones.add(btnAhorros);
        filaBotones.add(btnCorriente);
        filaBotones.add(btnPlazoFijo);

        tarjeta.add(filaBotones);
        tarjeta.add(Box.createVerticalStrut(20));

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelLista);
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
        panelLista.removeAll();

        String listado = gestor.listarCuentasPorTipo(tipoActivo);

        if (listado == null || listado.isBlank()) {
            JLabel lbl = new JLabel("No hay cuentas registradas de este tipo.");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(GRIS_TEXTO);
            lbl.setBorder(new EmptyBorder(10, 10, 10, 10));
            panelLista.add(lbl);
        } else {
            String[] lineas = listado.split("\n");
            for (String linea : lineas) {
                if (linea.isBlank()) continue;
                panelLista.add(crearFilaCuenta(linea.trim()));
                panelLista.add(Box.createVerticalStrut(6));
            }
        }

        panelLista.revalidate();
        panelLista.repaint();
    }

    private JButton crearFilaCuenta(String lineaTexto) {
        JButton fila = new JButton(lineaTexto);
        fila.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fila.setForeground(Color.DARK_GRAY);
        fila.setBackground(GRIS_CAJA);
        fila.setHorizontalAlignment(SwingConstants.LEFT);
        fila.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        fila.setFocusPainted(false);
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        fila.addChangeListener(e -> {
            if (fila.getModel().isRollover()) {
                fila.setBackground(new Color(230, 230, 230));
            } else {
                fila.setBackground(GRIS_CAJA);
            }
        });

        String numeroCuenta = lineaTexto.split("\\|")[0].trim();
        fila.addActionListener(e -> {
            CuentaBancaria cuenta = gestor.buscarPorNumero(numeroCuenta);
            if (cuenta != null) {
                onSeleccionar.accept(cuenta);
            }
        });

        return fila;
    }
}