package banco.dominio;

public class CuentaCorriente extends CuentaBancaria {

    private static final long serialVersionUID = 1L;

    private static final double COMISION_SOBREGIRO = 150.00;

    private double limiteSobregiro;

    public CuentaCorriente(String numeroCuenta, String titular, String dpi,
            String fechaApertura, double saldoInicial,
            double limiteSobregiro) {

        super(numeroCuenta, titular, dpi, fechaApertura, saldoInicial);

        if (limiteSobregiro < 0) {
            throw new IllegalArgumentException("El límite de sobregiro no puede ser negativo");
        }

        this.limiteSobregiro = limiteSobregiro;
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

        double saldoDespuesDelRetiro = getSaldo() - monto;
        double comision = 0;

        if (saldoDespuesDelRetiro < 0) {
            comision = COMISION_SOBREGIRO;
        }

        double saldoFinal = saldoDespuesDelRetiro - comision;

        if (saldoFinal < -limiteSobregiro) {
            registrarTransaccion(
                    TipoTransaccion.RETIRO,
                    monto,
                    "Retiro rechazado: supera el límite de sobregiro"
            );

            return false;
        }

        actualizarSaldo(saldoFinal);

        if (comision > 0) {
            registrarTransaccion(
                    TipoTransaccion.RETIRO,
                    monto,
                    "Retiro realizado con sobregiro. Comisión aplicada: L. "
                    + String.format("%.2f", comision)
            );
        } else {
            registrarTransaccion(
                    TipoTransaccion.RETIRO,
                    monto,
                    "Retiro realizado desde cuenta corriente"
            );
        }

        return true;
    }

    @Override
    public double calcularInteresMensual() {
        return 0;
    }

    @Override
    public String getEstadoCuenta() {
        return super.getEstadoCuenta()
                + "\nTipo de cuenta: Corriente"
                + "\nLímite de sobregiro: L. "
                + String.format("%.2f", limiteSobregiro);
    }

    public double getLimiteSobregiro() {
        return limiteSobregiro;
    }
}