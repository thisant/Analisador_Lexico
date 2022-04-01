package br.com.unit.compiladores;

public enum TipoToken {
	FIM(0), 		
	STRING(3), 	    
	ID(4), 			
	RELOP(5), 		
	OPAD(6), 		
	OPMULT(7), 		
	ATRIB(8), 		
	PVIG(9), 		
	ABPAR(10), 		
	FPAR(11), 		
	BOOLEAN(12), 	
	OPLOG(13), 		
	TYPE(14), 		
	PROGRAM(15), 	
	BEGIN(17), 		
	END(18), 		
	IF(19), 		
	THEN(20), 		
	ELSE(21), 		
	FOR(22), 		
	WHILE(23), 		
	TO(25), 		
	INTEGER(26),	
	DO(27),			
	READ(28),		
	VAR(29),		
	WRITE(30),		
	OPREL(31),		
	VIG(32),		
	PONTO(33),		
	OPNEG(34),		
	COM(35);		
	
	private int codigoToken;

	private TipoToken(int codigoToken){
		this.codigoToken = codigoToken;
	}

	public int getCodigoToken(){
		return codigoToken;
	}
	
	public static TipoToken tipoEnum(int codigoToken){
		
		for (TipoToken tipoToken : TipoToken.values()){
			if(codigoToken == tipoToken.getCodigoToken()) { return tipoToken; }
		}
		
		throw new IllegalArgumentException("Falha no código: " + codigoToken);
	}
}
