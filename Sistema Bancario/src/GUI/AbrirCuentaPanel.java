package GUI;

import sistema.bancario.CuentaAhorros;
import sistema.bancario.CuentaCorriente;
import sistema.bancario.CuentaPlazoFijo;
import sistema.bancario.GestorBancario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AbrirCuentaPanel extends JPanel {

    private static final Color ROJO_BA     = new Color(180, 20, 20);
    private static final Color GRIS_CLARO  = new Color(248, 248, 248);
    private static final Color GRIS_BORDE  = new Color(225, 225, 225);
    private static final Color GRIS_TEXTO  = new Color(110, 110, 110);
    private static final Color BLANCO      = Color.WHITE;
    private final GestorBancario gestor;
    private final String tipo;


    private JTextField txtNumero, txtTitular, txtDpi, txtFechaApertura, txtSaldo;

    private JTextField txtTasa;

    private JTextField txtSobregiro;

    private JTextField txtPlazo, txtFechaVencimiento, txtPenalizacion;

    public AbrirCuentaPanel(GestorBancario gestor, String tipo) {
        this.gestor = gestor;
        this.tipo = tipo;
        setLayout(new GridBagLayout());
        setBackground(GRIS_CLARO);
        mostrarFormulario();
    }

    private void mostrarFormulario() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(BLANCO);
        tarjeta.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                new EmptyBorder(35, 50, 35, 50)
        ));
        tarjeta.setMaximumSize(new Dimension(520, 9999));

        switch (tipo) {
            case "ahorros"    -> construirFormAhorros(tarjeta);
            case "corriente"  -> construirFormCorriente(tarjeta);
            case "plazofijo"  -> construirFormPlazoFijo(tarjeta);
        }

        add(tarjeta);
    }

    public void construirFormAhorros(JPanel tarjeta) {
        agregarTitulo(tarjeta, "Abrir Cuenta de Ahorros");

        txtNumero        = agregarCampo(tarjeta, "Número de cuenta");
        txtTitular       = agregarCampo(tarjeta, "Titular");
        txtDpi           = agregarCampo(tarjeta, "DPI");
        txtFechaApertura = agregarCampo(tarjeta, "Fecha de apertura (AAAA-MM-DD)");
        txtSaldo         = agregarCampo(tarjeta, "Saldo inicial (L.)");
        txtTasa          = agregarCampo(tarjeta, "Tasa de interés anual (ej. 0.04 = 4%)");

        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(crearBotonAbrir(() -> accionAbrirAhorros()));
    }

    public void construirFormCorriente(JPanel tarjeta) {
        agregarTitulo(tarjeta, "Abrir Cuenta Corriente");

        txtNumero        = agregarCampo(tarjeta, "Número de cuenta");
        txtTitular       = agregarCampo(tarjeta, "Titular");
        txtDpi           = agregarCampo(tarjeta, "DPI");
        txtFechaApertura = agregarCampo(tarjeta, "Fecha de apertura (AAAA-MM-DD)");
        txtSaldo         = agregarCampo(tarjeta, "Saldo inicial (L.)");
        txtSobregiro     = agregarCampo(tarjeta, "Límite de sobregiro (L.)");

        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(crearBotonAbrir(() -> accionAbrirCorriente()));
    }

    public void construirFormPlazoFijo(JPanel tarjeta) {
        agregarTitulo(tarjeta, "Abrir Cuenta a Plazo Fijo");

        txtNumero           = agregarCampo(tarjeta, "Número de cuenta");
        txtTitular          = agregarCampo(tarjeta, "Titular");
        txtDpi              = agregarCampo(tarjeta, "DPI");
        txtFechaApertura    = agregarCampo(tarjeta, "Fecha de apertura (AAAA-MM-DD)");
        txtSaldo            = agregarCampo(tarjeta, "Saldo inicial (L. mínimo 5,000)");
        txtTasa             = agregarCampo(tarjeta, "Tasa de interés especial (ej. 0.07)");
        txtPlazo            = agregarCampo(tarjeta, "Plazo en meses");
        txtFechaVencimiento = agregarCampo(tarjeta, "Fecha de vencimiento (AAAA-MM-DD)");
        txtPenalizacion     = agregarCampo(tarjeta, "Penalización por retiro anticipado (ej. 0.05)");

        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(crearBotonAbrir(() -> accionAbrirPlazoFijo()));
    }

    private void accionAbrirAhorros() {
        try {
            String titular       = txtTitular.getText().trim();
            String dpi           = txtDpi.getText().trim();
            String fechaApertura = txtFechaApertura.getText().trim();
            double saldo         = Double.parseDouble(txtSaldo.getText().trim());
            double tasa          = Double.parseDouble(txtTasa.getText().trim());
            gestor.agregarCuentaAhorros(titular, dpi, saldo, tasa);

            mostrarExito("Cuenta de Ahorros creada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarError("Saldo y tasa deben ser valores numéricos.");
        }
    }

    private void accionAbrirCorriente() {
        try {
            String numero        = txtNumero.getText().trim();
            String titular       = txtTitular.getText().trim();
            String dpi           = txtDpi.getText().trim();
            String fechaApertura = txtFechaApertura.getText().trim();
            double saldo         = Double.parseDouble(txtSaldo.getText().trim());
            double sobregiro     = Double.parseDouble(txtSobregiro.getText().trim());
            gestor.agregarCuentaCorriente(titular, dpi, saldo, sobregiro);
            mostrarExito("Cuenta Corriente creada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarError("Saldo y límite de sobregiro deben ser valores numéricos.");
        }
    }

    private void accionAbrirPlazoFijo() {
        try {
            String numero           = txtNumero.getText().trim();
            String titular          = txtTitular.getText().trim();
            String dpi              = txtDpi.getText().trim();
            String fechaApertura    = txtFechaApertura.getText().trim();
            double saldo            = Double.parseDouble(txtSaldo.getText().trim());
            double tasa             = Double.parseDouble(txtTasa.getText().trim());
            int    plazo            = Integer.parseInt(txtPlazo.getText().trim());
            String fechaVencimiento = txtFechaVencimiento.getText().trim();
            double penalizacion     = Double.parseDouble(txtPenalizacion.getText().trim());
            gestor.agregarCuentaPlazoFijo(titular, dpi, saldo, tasa, plazo, fechaVencimiento, penalizacion);
            mostrarExito("Cuenta a Plazo Fijo creada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarError("Revisa que los campos numéricos tengan el formato correcto.");
        }
    }

    private void agregarTitulo(JPanel panel, String texto) {
        JLabel lbl = new JLabel(texto);
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

    private JButton crearBotonAbrir(Runnable accion) {
        JButton btn = new JButton("Abrir Cuenta");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BLANCO);
        btn.setBackground(ROJO_BA);
        btn.setBorder(new EmptyBorder(10, 28, 10, 28));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void limpiarCampos() {
        for (Component c : getComponents()) {
            if (c instanceof JTextField) ((JTextField) c).setText("");
        }
    }
}