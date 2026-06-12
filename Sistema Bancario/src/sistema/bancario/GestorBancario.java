package sistema.bancario;

import java.io.*;
import javax.swing.JOptionPane;

public class GestorBancario {
    private File saldosbin;
    private File sistemabin;
    private CuentaBancaria[] cuentas;

    public GestorBancario() {
        cuentas=new CuentaBancaria[500];
        saldosbin=new File("saldos_consolidados.bin");
        sistemabin=new File("sistema_backup.bin");
    }
    
    public void inicializarArchivos(){
        try{
            if(!saldosbin.exists())
                saldosbin.createNewFile();
            if(!sistemabin.exists())
                sistemabin.createNewFile();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al crear archivos: "+e.getMessage());
        }
    }
    
    public void guardarSaldosBinario(String fecha, int numCuentas, Double totalAhorros, Double totalCorriente, Double totalPlazoFijo, Double totalGeneral){
        try(RandomAccessFile ram=new RandomAccessFile(saldosbin,"rw")){
            ram.seek(saldosbin.length());
            ram.writeUTF(fecha);
            ram.writeInt(numCuentas);
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
                salida+=ram.readUTF();
                salida+=ram.readInt();
                salida+=ram.readDouble();
                salida+=ram.readDouble();
                salida+=ram.readDouble();
            }
            ram.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al cargar saldos: "+e.getMessage());
        }
        return salida;
    }
    
    public void serializarSistema(){
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(sistemabin))){
            for(CuentaBancaria cuenta: cuentas){
                if(cuenta==null)
                    break;
                oos.writeObject(cuenta);
            }
            oos.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al serializar sistema: "+e.getMessage());
        }
        
    }
    
    public void restaurarSistema(){
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(sistemabin))){
            CuentaBancaria cuenta;
            for(int i=0;i<500;i++){
                if((cuenta=(CuentaBancaria)ois.readObject())==null)
                    break;
                cuentas[i]=cuenta;
            }
            ois.close();
        }catch(IOException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error al restaurar sistema: "+e.getMessage());
        }
    }
}
