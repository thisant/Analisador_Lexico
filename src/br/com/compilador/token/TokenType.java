package br.com.compilador.token;

public enum TokenType {
	FIM(0), 		// Fim do arquivo
	STRING(3), 	// "Thiago", "Santana"
	ID(4), 			// val, _salario, i__
	RELOP(5), 		// $df, $gt, $le
	OPAD(6), 		// +, -
	OPMULT(7), 		// *, /
	ATRIB(8), 		// :=
	PVIG(9), 		// ;
	ABPAR(10), 		// (
	FPAR(11), 		// )
	BOOLEAN(12), 	// true, false
	OPLOG(13), 		// and, not, or
	TYPE(14), 		// bool, text, int
	PROGRAM(15), 	// program
	BEGIN(17), 		// begin
	END(18), 		// end
	IF(19), 		// if
	THEN(20), 		// then
	ELSE(21), 		// else
	FOR(22), 		// for
	WHILE(23), 		// while
	TO(25), 		// to
	INTEGER(26),	// INTEGER
	DO(27),			// Do 
	READ(28),		// READ 
	VAR(29),		// VAR
	WRITE(30),		// WRITE
	OPREL(31),		// <, <=, >, =>, ==, <>
	VIG(32),		// ,
	PONTO(33),		//
	OPNEG(34),		// ~
	COM(35);		// # aqui #
	
	private int codToken;

	private TokenType(int codToken){
		this.codToken = codToken;
	}

	public int getCodToken(){
		return codToken;
	}
	
	public static TokenType toEnum(int codToken){
		
		for (TokenType tokenType : TokenType.values()){
			if(codToken == tokenType.getCodToken()) { return tokenType; }
		}
		
		throw new IllegalArgumentException("codigo invalido "+codToken);
	}
}
