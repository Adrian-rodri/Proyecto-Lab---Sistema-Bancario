package sistema.bancario;

public class CuentaAhorros extends CuentaBancaria {

    private static final long serialVersionUID = 1L;

    private double tasaInteresAnual;

    public CuentaAhorros(String numeroCuenta, String titular, String dpi,
            String fechaApertura, double saldoInicial,
            double tasaInteresAnual) {

        super(numeroCuenta, titular, dpi, fechaApertura, saldoInicial);

        if (tasaInteresAnual < 0) {
            throw new IllegalArgumentException("La tasa de interés no puede ser negativa");
        }

        this.tasaInteresAnual = tasaInteresAnual;
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

        if (monto > getSaldo()) {
            registrarTransaccion(
                    TipoTransaccion.RETIRO,
                    monto,
                    "Retiro rechazado: saldo insuficiente"
            );

            return false;
        }

        double nuevoSaldo = getSaldo() - monto;
        actualizarSaldo(nuevoSaldo);

        registrarTransaccion(
                TipoTransaccion.RETIRO,
                monto,
                "Retiro realizado desde cuenta de ahorros"
        );

        return true;
    }

    @Override
    public double calcularInteresMensual() {
        return getSaldo() * tasaInteresAnual / 12;
    }

    @Override
    public String getEstadoCuenta() {
        return super.getEstadoCuenta()
                + "\nTipo de cuenta: Ahorros"
                + "\nTasa de interés anual: " + (tasaInteresAnual * 100) + "%"
                + "\nInterés proyectado del mes: L. "
                + String.format("%.2f", calcularInteresMensual());
    }

    public double getTasaInteresAnual() {
        return tasaInteresAnual;
    }
}