package br.com.compilador.token;

public enum TokenType {
	EOF(0), 		// Fim do arquivo
	NUM_INT(1), 	// 123,48, 3E+10
	NUM_FLOAT(2), 	// 4.8, 3.10E+10
	LITERAL(3), 	// "Thiago", "Santana"
	ID(4), 			// val, _salario, i__
	RELOP(5), 		// $df, $gt, $le
	OPAD(6), 		// +, -
	OPMULT(7), 		// *, /
	ATRIB(8), 		// <-
	PVIG(9), 		// ;
	ABPAR(10), 		// (
	FPAR(11), 		// )
	BOOLEAN(12), 	// true, false
	OPLOG(13), 		// and, not, or
	TYPE(14), 		// bool, text, int
	PROGRAM(15), 	// program
	END_PROG(16), 	// end_prog
	BEGIN(17), 		// begin
	END(18), 		// end
	IF(19), 		// if
	THEN(20), 		// then
	ELSE(21), 		// else
	FOR(22), 		// for
	WHILE(23), 		// while
	DECLARE(24), 	// declare 
	TO(25), 		// to
	INTEGER(26),	// INTEGER
	DO(27),			// Do 
	READ(28),		// READ 
	VAR(29),		// VAR
	WRITE(30),		// WRITE
	OPREL(31),		// <, <=, >, =>, ==, <>
	VIG(32);		// ,
	
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
