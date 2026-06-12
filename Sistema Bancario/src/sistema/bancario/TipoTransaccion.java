package sistema.bancario;

/**
 *
 * @author adria
 */
public enum TipoTransaccion {
    DEPOSITO("DEP"), RETIRO("RET"), 
    TRANSFERENCIA_ENVIO("TRF"), TRANSFERENCIA_RECIBO("TRFR"), 
    INTERES("INT"), PENALIZACION("PEN");
    private String codigo;
    private TipoTransaccion(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
    
}
