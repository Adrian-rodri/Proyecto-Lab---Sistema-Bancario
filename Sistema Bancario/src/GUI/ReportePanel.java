package GUI;

import sistema.bancario.CuentaBancaria;

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

    private final CuentaBancaria cuenta;
    private JPanel panelResultado;

    public ReportePanel(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
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

        agregarTitulo(tarjeta);
        agregarInfoCuenta(tarjeta);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(crearBotonGenerar());
        tarjeta.add(Box.createVerticalStrut(16));

        panelResultado = new JPanel();
        panelResultado.setLayout(new BoxLayout(panelResultado, BoxLayout.Y_AXIS));
        panelResultado.setOpaque(false);
        panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(panelResultado);

        add(tarjeta);
    }

    private void agregarTitulo(JPanel panel) {
        JLabel lbl = new JLabel("Reporte de Cuenta");
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
        panel.add(Box.createVerticalStrut(20));
    }

    private void agregarInfoCuenta(JPanel panel) {
        JPanel caja = new JPanel();
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        caja.setBackground(new Color(250, 240, 240));
        caja.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 180, 180), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));
        caja.setAlignmentX(Component.LEFT_ALIGNMENT);
        caja.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        JLabel lblTitulo = new JLabel("Cuenta seleccionada");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitulo.setForeground(ROJO_BA);
        caja.add(lblTitulo);
        caja.add(Box.createVerticalStrut(6));

        agregarFilaInfo(caja, "Número:",       cuenta.getNumeroCuenta());
        agregarFilaInfo(caja, "Titular:",      cuenta.getTitular());
        agregarFilaInfo(caja, "Saldo actual:", "L. " + String.format("%,.2f", cuenta.getSaldo()));

        panel.add(caja);
    }

    private void agregarFilaInfo(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        fila.setOpaque(false);

        JLabel lblE = new JLabel(etiqueta + "  ");
        lblE.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblE.setForeground(GRIS_TEXTO);

        JLabel lblV = new JLabel(valor);
        lblV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblV.setForeground(Color.DARK_GRAY);

        fila.add(lblE);
        fila.add(lblV);
        panel.add(fila);
    }

    private JButton crearBotonGenerar() {
        JButton btn = new JButton("Generar Reporte");
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

        JLabel lbl = new JLabel("Reporte generado para la cuenta "
                + cuenta.getNumeroCuenta() + ". (placeholder)");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(VERDE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResultado.add(lbl);

        panelResultado.revalidate();
        panelResultado.repaint();
    }
}