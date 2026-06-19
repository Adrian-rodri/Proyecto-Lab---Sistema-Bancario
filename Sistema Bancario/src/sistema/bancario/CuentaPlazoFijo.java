package banco.dominio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CuentaPlazoFijo extends CuentaAhorros {

    private static final long serialVersionUID = 1L;

    private int plazoMeses;
    private String fechaVencimiento;
    private double penalizacionPct;

    public CuentaPlazoFijo(String numeroCuenta, String titular, String dpi,
            String fechaApertura, double saldoInicial,
            double tasaInteresEspecial, int plazoMeses,
            String fechaVencimiento, double penalizacionPct) {

        super(
                numeroCuenta,
                titular,
                dpi,
                fechaApertura,
                validarSaldoInicial(saldoInicial),
                tasaInteresEspecial
        );

        if (plazoMeses <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor que cero");
        }

        if (penalizacionPct < 0) {
            throw new IllegalArgumentException("La penalización no puede ser negativa");
        }

        validarFecha(fechaVencimiento);

        this.plazoMeses = plazoMeses;
        this.fechaVencimiento = fechaVencimiento;
        this.penalizacionPct = penalizacionPct;
    }

    @Override
    public boolean retirar(double monto) {

        if (monto <= 0) {
            registrarTransaccion(
                    TipoTransaccion.RETIRO,
                    monto,
                    "Retiro rechazado: el monto debe ser mayor que cero"
            );

            return false;
        }

        double penalizacion = calcularPenalizacion(monto);
        double totalDescontar = monto + penalizacion;

        if (totalDescontar > getSaldo()) {
            registrarTransaccion(
                    TipoTransaccion.RETIRO,
                    monto,
                    "Retiro rechazado: saldo insuficiente para cubrir el retiro y la penalización"
            );

            return false;
        }

        actualizarSaldo(getSaldo() - monto);

        registrarTransaccion(
                TipoTransaccion.RETIRO,
                monto,
                "Retiro realizado desde cuenta a plazo fijo"
        );

        if (penalizacion > 0) {
            actualizarSaldo(getSaldo() - penalizacion);

            registrarTransaccion(
                    TipoTransaccion.PENALIZACION,
                    penalizacion,
                    "Penalización aplicada por retiro anticipado"
            );
        }

        return true;
    }

    @Override
    public double calcularInteresMensual() {
        return getSaldo() * getTasaInteresAnual() / 12;
    }

    @Override
    public String getEstadoCuenta() {
        return super.getEstadoCuenta()
                + "\nTipo de cuenta: Plazo fijo"
                + "\nPlazo contratado: " + plazoMeses + " meses"
                + "\nFecha de vencimiento: " + fechaVencimiento
                + "\nPenalización por retiro anticipado: "
                + (penalizacionPct * 100) + "%";
    }

    private double calcularPenalizacion(double monto) {

        if (esRetiroAnticipado()) {
            return monto * penalizacionPct;
        }

        return 0;
    }

    private boolean esRetiroAnticipado() {
        LocalDate vencimiento = LocalDate.parse(fechaVencimiento);
        return LocalDate.now().isBefore(vencimiento);
    }

    private static double validarSaldoInicial(double saldoInicial) {

        if (saldoInicial <= 5000) {
            throw new IllegalArgumentException(
                    "La cuenta a plazo fijo debe abrirse con más de L. 5,000"
            );
        }

        return saldoInicial;
    }

    private static void validarFecha(String fechaVencimiento) {

        try {
            LocalDate.parse(fechaVencimiento);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento debe usar el formato AAAA-MM-DD"
            );
        }
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public double getPenalizacionPct() {
        return penalizacionPct;
    }
}