package sistema.bancario;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GestorBancario {
    private File saldosbin;
    private File sistemabin;
    private CuentaBancaria[] cuentas;
    private int indexCuentas=0;

    public GestorBancario() {
        cuentas=new CuentaBancaria[500];
        saldosbin=new File("datos/saldos_consolidados.bin");
        sistemabin=new File("datos/sistema_backup.bin");
        inicializarArchivos();
    }
    
    public void inicializarArchivos(){
        try{
            File carpeta= new File("datos/historiales");
            if(!carpeta.exists())
                carpeta.mkdirs();
            if(!saldosbin.exists())
                saldosbin.createNewFile();
            if(!sistemabin.exists())
                sistemabin.createNewFile();
            restaurarSistema();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al crear archivos: "+e.getMessage());
        }
    }
    
    public void guardarSaldosBinario(String fecha, int numCuentas, Double totalAhorros, Double totalCorriente, Double totalPlazoFijo, Double totalGeneral){
        try(RandomAccessFile ram=new RandomAccessFile(saldosbin,"rw")){
            ram.seek(saldosbin.length());
            ram.writeUTF(fecha);
            ram.writeInt(numCuentas);
            ram.writeDouble(totalAhorros);
            ram.writeDouble(totalCorriente);
            ram.writeDouble(totalPlazoFijo);
            ram.writeDouble(totalGeneral);
            ram.close();
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Error al guardar saldos: "+e.getMessage());
        } 
    }
    
    public String cargarSaldosBinario(){
        String salida="";
        try(RandomAccessFile ram=new RandomAccessFile(saldosbin, "rw")){
            while(ram.getFilePointer()<saldosbin.length()){
                salida+=ram.readUTF();//fecha
                salida+=ram.readInt();//numCuentas
                salida+=ram.readDouble();//totalAhorros
                salida+=ram.readDouble();//totalCorriente
                salida+=ram.readDouble();//totalPlazoFijo
                salida+=ram.readDouble();//totalGeneral
            }
            ram.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al cargar saldos: "+e.getMessage());
        }
        return salida;
    }
    
    public void serializarSistema(){
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(sistemabin))){
            for(int i=0;i<indexCuentas;i++)
                oos.writeObject(cuentas[i]);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al serializar sistema: "+e.getMessage());
        }
        
    }
    
    public void restaurarSistema(){
        if(!sistemabin.exists())
            return;
        indexCuentas=0;
        cuentas= new CuentaBancaria[500];
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(sistemabin))){
            while(true){
                try{
                    CuentaBancaria cuenta= (CuentaBancaria) ois.readObject();
                    cuentas[indexCuentas++]=cuenta;
                }catch(EOFException e){
                    break;
                }
            }
//            CuentaBancaria cuenta;
//            for(int i=0;i<500;i++){
//                if((cuenta=(CuentaBancaria)ois.readObject())==null)
//                    break;
//                cuentas[i]=cuenta;
//            }
//            ois.close();
        }catch(IOException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error al restaurar sistema: "+e.getMessage());
        }
    }
    /*
    Logica para agregar cuentas
    */
    private void agregarCuenta(CuentaBancaria nueva){
        if(nueva==null)
            return;
        if(!hayEspacioDisponible()){
            JOptionPane.showMessageDialog(new JFrame(),"No hay espacio para mas cuentas");
            return;
        }
        if(existeCuenta(nueva.getNumeroCuenta())){
            JOptionPane.showMessageDialog(new JFrame(),"La cuenta ya existe");
            return;
        }
        cuentas[indexCuentas++]=nueva;
        serializarSistema();
        try{
            File historial= new File("datos/historiales/historial_"+nueva.getNumeroCuenta()+".txt");
            if(!historial.exists())
                historial.createNewFile();
            JOptionPane.showMessageDialog(new JFrame(),"Cuenta creada: "+nueva.getNumeroCuenta());
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }
    
    public void agregarCuentaAhorros(String titular, String dpi, double saldoInicial, double tasaInteres){
        if(!validarDPI(dpi)){
            JOptionPane.showMessageDialog(new JFrame(),"DPI invalido");
            return;
        }
        String numeroCuenta= generarNumeroCuenta("AHO");
        String fecha= LocalDate.now().toString();
        CuentaAhorros nueva= new CuentaAhorros(numeroCuenta,titular,dpi,fecha,saldoInicial,tasaInteres);
        agregarCuenta(nueva);
    }
    
    public void agregarCuentaCorriente(String titular, String dpi, double saldoInicial, double limiteSobregiro){
        if(!validarDPI(dpi)){
            JOptionPane.showMessageDialog(new JFrame(), "DPI invalido");
            return;
        }
        String numeroCuenta= generarNumeroCuenta("CTE");
        String fecha= LocalDate.now().toString();
        CuentaCorriente nueva= new CuentaCorriente(numeroCuenta,titular,dpi,fecha,saldoInicial,limiteSobregiro);
        agregarCuenta(nueva);
    }
    public void agregarCuentaPlazoFijo(String titular, String dpi, double saldoInicial, double tasaInteres, int plazoMeses, String fechaVencimiento, double penalizacionPct){
        if(!validarDPI(dpi)){
            JOptionPane.showMessageDialog(new JFrame(), "DPI invalido");
            return;
        }
        LocalDate fechaVenc= LocalDate.parse(fechaVencimiento);
        if(!fechaVenc.isAfter(LocalDate.now())){
            JOptionPane.showMessageDialog(new JFrame(),"Fecha de vencimiento invalida");
            return;
        }
        String numeroCuenta= generarNumeroCuenta("PF");
        String fecha= LocalDate.now().toString();
        CuentaPlazoFijo nuevo = new CuentaPlazoFijo(numeroCuenta,titular,dpi,fecha,saldoInicial,tasaInteres,plazoMeses,fechaVencimiento,penalizacionPct);
        agregarCuenta(nuevo);
    }
    
    private boolean existeCuenta(String numeroCuenta){
        for(int i=0; i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getNumeroCuenta().equals(numeroCuenta))
                    return true;
            }
        }
        return false;
    }
    
    public boolean hayEspacioDisponible(){
        return indexCuentas<500;
    }
    
    public int obtenerIndiceCuenta(String numeroCuenta){
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getNumeroCuenta().equals(numeroCuenta))
                    return i;
            }
        }
        return -1;
    }
    public String generarNumeroCuenta(String prefijo){
        int contador =0;
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getNumeroCuenta().startsWith(prefijo))
                    contador+=1;
            }
        }
        contador++;
        return String.format("%s-%04d",prefijo, contador);
    }
    
    public boolean validarDPI(String dpi){
        if(dpi==null)
            return false;
        if(dpi.length()!=13)
            return false;
        return dpi.matches("\\d+");
    }
    
    /*
    Logica para busqueda y consulta de las cuentas
    */
    
    public CuentaBancaria buscarPorNumero(String numeroCuenta){
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getNumeroCuenta().equals(numeroCuenta))
                    return cuentas[i];
            }
        }
        return null;
    }
    
    public ArrayList<CuentaBancaria> buscarPorDPI(String dpi){
        ArrayList<CuentaBancaria> results= new ArrayList<>();
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getDpi().equals(dpi))
                    results.add(cuentas[i]);
            }
        }
        return results;
    }
    
    public ArrayList<CuentaBancaria> buscarPorTitular(String nombre){
        ArrayList<CuentaBancaria> results= new ArrayList<>();
        String lowercase=nombre.toLowerCase();
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getTitular().toLowerCase().contains(lowercase))
                    results.add(cuentas[i]);
            }
        }
        return results;
    }
    
    public int getTotalCuentas(){
        return indexCuentas;
    }
    
    public String listarCuentas(){
        String lista= "";
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                lista+=cuentas[i].toString()+"\n";
            }
        }
        return lista;
    }
    
    public String listarCuentasPorTipo(String prefijo){
        String lista= "";
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i]!=null){
                if(cuentas[i].getNumeroCuenta().startsWith(prefijo))
                    lista+=cuentas[i].toString()+"\n";
            }
        }
        return lista;
    }
    
    /*
    Logica para calculos y totales
    */
    public double calcularTotalAhorros(){
        double total=0;
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i] instanceof CuentaAhorros && !(cuentas[i] instanceof CuentaPlazoFijo))
                total+=cuentas[i].getSaldo();
        }
        return total;
    }
    
    public double calcularTotalCorriente(){
        double total=0;
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i] instanceof CuentaCorriente){
                total+=cuentas[i].getSaldo();
            }
        }
        return total;
    }
    
    public double calcularTotalPlazoFijo(){
        double total=0;
        for(int i=0;i<indexCuentas;i++){
            if(cuentas[i] instanceof CuentaPlazoFijo){
                total+=cuentas[i].getSaldo();
            }
        }
        return total;
    }
    
    public double calcularTotalGeneral(){
        double total= calcularTotalAhorros()+ calcularTotalCorriente()+calcularTotalPlazoFijo();
        return total;
    }
    
    public void aplicarInteresesMensuales(){
        for(int i=0; i<indexCuentas;i++){
            if(cuentas[i]!=null){
                double interes= cuentas[i].calcularInteresMensual();
                if(interes>0){
                    cuentas[i].depositar(interes);
                }
            }
        }
    }
}
