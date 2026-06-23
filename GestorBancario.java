/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinal;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GestorBancario {

    private CuentaBancaria[] cuentas;
    private int cantidadCuentas;

    public GestorBancario() {
        cuentas = new CuentaBancaria[500];
        cantidadCuentas = 0;
    }

    public void abrirCuenta(CuentaBancaria cuenta) {
        if (cantidadCuentas < cuentas.length) {
            cuentas[cantidadCuentas] = cuenta;
            cantidadCuentas++;

            escribirHistorial(cuenta.getNumeroCuenta(),
                    "APERTURA | Saldo inicial: L. " + cuenta.getSaldo());

            System.out.println("Cuenta abierta correctamente.");
        } else {
            System.out.println("No hay espacio para más cuentas.");
        }
    }

    public CuentaBancaria buscarCuenta(String numeroCuenta) {
        for (int i = 0; i < cantidadCuentas; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return cuentas[i];
            }
        }

        return null;
    }

    public void realizarDeposito(String numeroCuenta, double monto) {
        CuentaBancaria cuenta = buscarCuenta(numeroCuenta);

        if (cuenta != null) {
            if (cuenta.depositar(monto)) {
                escribirHistorial(numeroCuenta,
                        "DEPOSITO | Monto: L. " + monto
                        + " | Saldo resultante: L. " + cuenta.getSaldo());

                System.out.println("Depósito realizado correctamente.");
            } else {
                System.out.println("No se pudo realizar el depósito.");
            }
        } else {
            System.out.println("Cuenta no encontrada.");
        }
    }

    public void realizarRetiro(String numeroCuenta, double monto) {
        CuentaBancaria cuenta = buscarCuenta(numeroCuenta);

        if (cuenta != null) {
            if (cuenta.retirar(monto)) {
                escribirHistorial(numeroCuenta,
                        "RETIRO | Monto: L. " + monto
                        + " | Saldo resultante: L. " + cuenta.getSaldo());

                System.out.println("Retiro realizado correctamente.");
            } else {
                escribirHistorial(numeroCuenta,
                        "RETIRO RECHAZADO | Monto: L. " + monto
                        + " | Saldo actual: L. " + cuenta.getSaldo());

                System.out.println("Retiro rechazado.");
            }
        } else {
            System.out.println("Cuenta no encontrada.");
        }
    }

    public void realizarTransferencia(String numeroOrigen, String numeroDestino, double monto) {
        CuentaBancaria cuentaOrigen = buscarCuenta(numeroOrigen);
        CuentaBancaria cuentaDestino = buscarCuenta(numeroDestino);

        if (cuentaOrigen != null && cuentaDestino != null) {
            if (cuentaOrigen.retirar(monto)) {
                cuentaDestino.depositar(monto);

                escribirHistorial(numeroOrigen,
                        "TRANSFERENCIA ENVIADA | Destino: " + numeroDestino
                        + " | Monto: L. " + monto
                        + " | Saldo resultante: L. " + cuentaOrigen.getSaldo());

                escribirHistorial(numeroDestino,
                        "TRANSFERENCIA RECIBIDA | Origen: " + numeroOrigen
                        + " | Monto: L. " + monto
                        + " | Saldo resultante: L. " + cuentaDestino.getSaldo());

                System.out.println("Transferencia realizada correctamente.");
            } else {
                escribirHistorial(numeroOrigen,
                        "TRANSFERENCIA RECHAZADA | Destino: " + numeroDestino
                        + " | Monto: L. " + monto
                        + " | Saldo actual: L. " + cuentaOrigen.getSaldo());

                System.out.println("Transferencia rechazada.");
            }
        } else {
            System.out.println("Cuenta origen o destino no encontrada.");
        }
    }

    public void aplicarInteresesMensuales() {
        for (int i = 0; i < cantidadCuentas; i++) {
            double interes = cuentas[i].calcularInteresMensual();

            if (interes > 0) {
                cuentas[i].depositar(interes);

                escribirHistorial(cuentas[i].getNumeroCuenta(),
                        "INTERES MENSUAL | Monto: L. " + interes
                        + " | Saldo resultante: L. " + cuentas[i].getSaldo());
            }
        }

        System.out.println("Intereses mensuales aplicados correctamente.");
    }

    public void reporteGeneral() {
        double totalGeneral = 0;

        System.out.println("===== REPORTE GENERAL DEL BANCO =====");

        for (int i = 0; i < cantidadCuentas; i++) {
            System.out.println(cuentas[i].getEstadoCuenta());
            totalGeneral += cuentas[i].getSaldo();
        }

        System.out.println("Total de cuentas registradas: " + cantidadCuentas);
        System.out.println("Saldo total del banco: L. " + totalGeneral);
    }

    public void escribirHistorial(String numeroCuenta, String texto) {
        try {
            FileWriter fw = new FileWriter("historial_" + numeroCuenta + ".txt", true);

            fw.write(LocalDateTime.now() + " | " + texto + "\n");

            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir historial.");
        }
    }
}