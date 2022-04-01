package br.com.compilador.token;

public class Token {
	private TokenType tokenType; 
	private String lexema;       
	private long linha;          
	private long coluna;         

	public Token(TokenType tokenType) {
		this(tokenType, "");
	}
	
	public Token(TokenType tokenType, String lexema) {
		this(tokenType, lexema, 0, 0);
	}

	public Token(TokenType tokenType, String lexema, long linha, long coluna) {
		this.tokenType = tokenType;
		this.lexema = lexema;
		this.linha = linha;
		this.coluna = coluna;
	}
	
	
	public void print() {
		String formatTokens = "(%3s,%3s)| %-10s || %-21s |\n";
		System.out.printf(formatTokens,this.coluna,this.linha,tokenType.toString(), this.lexema);	
	}
	
	public TokenType getTokenType() {
		return this.tokenType;
	}

	public String getLexema() {
		return this.lexema;
	}

	public long getLinha() {
		return this.linha;
	}

	public long getColuna() {
		return this.coluna;
	}

	public void setLinha(long linha) {
		this.linha = linha;
	}

	public void setColuna(long coluna) {
		this.coluna = coluna;
	}

}
