package br.com.unit.compiladores;

public enum TipoErro {
    LEXICO(1),
    SINTATICO(2),
    COMPILACAO(3);

    private int codigo;

    private TipoErro(int codigoErro) {
        this.codigo = codigoErro;
    }

    public int getCodigo(){
        return codigo;
    }

    public static TipoErro tipoEnum(int codigoToken) {

        for (TipoErro tipoToken : TipoErro.values()){
            if(codigoToken == tipoToken.getCodigo()) { return tipoToken; }
        }

        throw new IllegalArgumentException("Falha no código: " + codigoToken);
    }
}
