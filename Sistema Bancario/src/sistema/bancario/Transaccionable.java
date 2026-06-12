package sistema.bancario;


public interface Transaccionable {
    public boolean depositar(double monto);
    public boolean retirar(double monto);
    public boolean transferirA(CuentaBancaria destino, double monto);
    public double calcularInteresMensual();
    public String getEstadoCuenta();
}
