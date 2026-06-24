package sistema.bancario;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class CuentaBancaria implements Transaccionable, Serializable {

    private static final long serialVersionUID = 1L;

    protected String numeroCuenta, titular, dpi, fechaApertura;
    protected double saldo;
    protected File archivoHistorial;

    public CuentaBancaria(String numeroCuenta, String titular, String dpi, String fechaApertura, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.dpi = dpi;
        this.fechaApertura = fechaApertura;
        this.saldo = saldoInicial;
        this.archivoHistorial = new File("datos/historiales/historial_" + numeroCuenta + ".txt");
    }

    @Override
    public boolean depositar(double monto) {
        if (monto <= 0) {
            System.err.println("El monto debe ser positivo.");
            return false;
        }
        saldo += monto;
        registrarTransaccion(TipoTransaccion.DEPOSITO,monto,"Deposito realizado");
        return true;
    }

    @Override
    public String getEstadoCuenta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cuenta:  ").append(numeroCuenta).append("\n");
        sb.append("Titular: ").append(titular).append("\n");
        sb.append("DPI:     ").append(dpi).append("\n");
        sb.append("Apertura: ").append(fechaApertura).append("\n");
        sb.append("Saldo:   L. ").append(String.format("%,.2f", saldo)).append("\n");
        return sb.toString();
    }

    @Override
    public boolean transferirA(CuentaBancaria destino, double monto) {
        if (monto <= 0) {
            JOptionPane.showMessageDialog(new JFrame(),"El monto debe ser positivo.");
            return false;
        }
        if (destino == null) {
            JOptionPane.showMessageDialog(new JFrame(),"Cuenta destino no válida.");
            return false;
        }
        if (this == destino) {
            JOptionPane.showMessageDialog(new JFrame(),"No se puede transferir a la misma cuenta.");
            return false;
        }
        

        if (this.retirar(monto)) {
            destino.depositar(monto);
            
            escribirHistorial("RESUMEN TRANSFERENCIA: L. "+String.format("%,.2f", monto)+" enviados a cta "+destino.numeroCuenta);
            
            destino.escribirHistorial("RESUMEN TRANSFERENCIA: L. "+String.format("%,.2f", monto)+" recibidos de cta " + this.numeroCuenta);
            
            JOptionPane.showMessageDialog(new JFrame(),"Transferencia exitosa: L. " + String.format("%,.2f", monto));
            return true;
        }
        return false;
    }
    public void registrarTransaccion(TipoTransaccion tipo, double monto, String descripcion){
        Transaccion transaccion= new Transaccion(tipo,monto,getSaldo(),descripcion,numeroCuenta);
        escribirHistorial(transaccion.toLinea());
    }
    public void escribirHistorial(String linea){
        try(FileWriter writer= new FileWriter(archivoHistorial,true)){
            writer.write(linea+"\n");
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }
    public void actualizarSaldo(double monto){
        setSaldo(monto);
    }
    

    @Override
    public abstract boolean retirar(double monto);
    
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public String getDpi() {
        return dpi;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public File getArchivoHistorial() {
        return archivoHistorial;
    }

    @Override
    public String toString() {
        return numeroCuenta + " | " + titular + " | L. " + String.format("%,.2f", saldo);
    }
}
