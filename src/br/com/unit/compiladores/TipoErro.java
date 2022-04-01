package br.com.unit.compiladores;

public enum TipoErro {
    LEXICO(1),
    SINTATICO(2),
    SEMANTICO(3),
    PROCESSAMENTO(4);

    private int codigo;

    private TipoErro(int codErro) {
        this.codigo = codErro;
    }

    public int getCodigo(){
        return codigo;
    }

    public static TipoErro tipoEnum(int codigoToken) {

        for (TipoErro tipoToken : TipoErro.values()){
            if(codigoToken == tipoToken.getCodigo()) { return tipoToken; }
        }

        throw new IllegalArgumentException("codigo invalido " + codigoToken);
    }
}
