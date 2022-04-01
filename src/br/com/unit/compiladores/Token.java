package br.com.unit.compiladores;

public class Token {
	private TipoToken tipoToken; 
	private String lexema;       
	private long linha;         
	private long coluna;   

	public Token(TipoToken tipoToken) {
		this(tipoToken, "");
	}
	
	public Token(TipoToken tipoToken, String lexema) {
		this(tipoToken, lexema, 0, 0);
	}

	public Token(TipoToken tipoToken, String lexema, long linha, long coluna) {
		this.tipoToken = tipoToken;
		this.lexema = lexema;
		this.linha = linha;
		this.coluna = coluna;
	}
	
	
	public void print() {
		String tokenFormato = "[%3s,%3s]| %-10s | %-21s |\n";
		System.out.printf(tokenFormato,this.coluna,this.linha,tipoToken.toString(), this.lexema);	
	}
	
	public TipoToken getTipoToken() {
		return this.tipoToken;
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
