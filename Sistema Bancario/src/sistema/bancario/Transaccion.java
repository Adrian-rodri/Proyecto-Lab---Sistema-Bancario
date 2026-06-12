package sistema.bancario;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    private final TipoTransaccion tipoTransaccion;
    private final double monto, saldoDespues;
    private final String fechaHora,descripcion, numeroCuenta;

    public Transaccion(TipoTransaccion tipo, double monto,double saldoDespues, String descripcion,String numeroCuenta) {
        this.tipoTransaccion = tipo;
        this.monto = monto;
        this.saldoDespues = saldoDespues;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.descripcion = descripcion;
        this.numeroCuenta = numeroCuenta;
    }
    public String toLinea() {
        return String.format("%s | %s | %s | L. %,.2f | Saldo: L. %,.2f | %s%n",fechaHora, tipoTransaccion.getCodigo(), numeroCuenta, monto, saldoDespues, descripcion);
    }
    public TipoTransaccion getTipo() {
        return tipoTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public double getSaldoDespues() {
        return saldoDespues;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }
}
