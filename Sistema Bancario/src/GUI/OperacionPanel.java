package GUI;

import sistema.bancario.CuentaBancaria;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class OperacionPanel extends JPanel {

    private static final Color ROJO_BA    = new Color(180, 20, 20);
    private static final Color GRIS_CLARO = new Color(248, 248, 248);
    private static final Color GRIS_BORDE = new Color(225, 225, 225);
    private static final Color GRIS_TEXTO = new Color(110, 110, 110);
    private static final Color BLANCO     = Color.WHITE;

    private final String tipo;
    private final CuentaBancaria cuentaOrigen;

    private JTextField txtMonto;
    private JTextField txtCuentaDestino;

    public OperacionPanel(String tipo, CuentaBancaria cuentaOrigen) {
        this.tipo          = tipo;
        this.cuentaOrigen  = cuentaOrigen;
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
        tarjeta.setPreferredSize(new Dimension(480, tipo.equals("transferencia") ? 420 : 360));

        agregarTitulo(tarjeta);
        agregarInfoCuenta(tarjeta);
        tarjeta.add(Box.createVerticalStrut(20));
        agregarCampos(tarjeta);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(crearBotonConfirmar());

        add(tarjeta);
    }

    private void agregarTitulo(JPanel panel) {
        String titulo = switch (tipo) {
            case "deposito"      -> "Realizar Depósito";
            case "retiro"        -> "Realizar Retiro";
            case "transferencia" -> "Realizar Transferencia";
            default              -> "Operación";
        };

        JLabel lbl = new JLabel(titulo);
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

        agregarFilaInfo(caja, "Número:", cuentaOrigen.getNumeroCuenta());
        agregarFilaInfo(caja, "Titular:", cuentaOrigen.getTitular());
        agregarFilaInfo(caja, "Saldo actual:", "L. " + String.format("%,.2f", cuentaOrigen.getSaldo()));

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

    private void agregarCampos(JPanel panel) {
        txtMonto = agregarCampo(panel, "Monto (L.)");

        if (tipo.equals("transferencia")) {
            txtCuentaDestino = agregarCampo(panel, "Número de cuenta destino");
        }
    }

    private JTextField agregarCampo(JPanel panel, String etiqueta) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(GRIS_TEXTO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));

        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(campo);
        panel.add(Box.createVerticalStrut(14));

        return campo;
    }

    private JButton crearBotonConfirmar() {
        String label = switch (tipo) {
            case "deposito"      -> "Confirmar Depósito";
            case "retiro"        -> "Confirmar Retiro";
            case "transferencia" -> "Confirmar Transferencia";
            default              -> "Confirmar";
        };

        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BLANCO);
        btn.setBackground(ROJO_BA);
        btn.setBorder(new EmptyBorder(10, 28, 10, 28));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> accionConfirmar());

        return btn;
    }

    private void accionConfirmar() {
        try {
            double monto = Double.parseDouble(txtMonto.getText().trim());

            switch (tipo) {
                case "deposito" -> {
                    mostrarExito("Depósito de L. " + String.format("%,.2f", monto) + " realizado.");
                }
                case "retiro" -> {
                    mostrarExito("Retiro de L. " + String.format("%,.2f", monto) + " realizado.");
                }
                case "transferencia" -> {
                    String destino = txtCuentaDestino.getText().trim();
                    if (destino.isEmpty()) {
                        mostrarError("Ingrese el número de cuenta destino.");
                        return;
                    }
                    mostrarExito("Transferencia de L. " + String.format("%,.2f", monto) + " a cuenta " + destino + " realizada.");
                }
            }

            txtMonto.setText("");
            if (txtCuentaDestino != null) txtCuentaDestino.setText("");

        } catch (NumberFormatException e) {
            mostrarError("El monto debe ser un valor numérico.");
        }
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}