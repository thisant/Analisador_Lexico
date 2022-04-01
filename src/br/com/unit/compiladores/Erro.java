package br.com.unit.compiladores;

public class Erro
{
	private TipoErro tipoErro;
	private String lexema;
	private String mensagem;
	private long linha;
	private long coluna;
	
	public Erro(TipoErro tipoErro, String lexema, String msg, long linha, long coluna)
	{
		this.tipoErro = tipoErro;
		this.lexema = lexema;
		this.linha = linha;
		this.coluna = coluna;
		this.mensagem = msg;
	}

	@Override
	public String toString()
	{
		return "Erro [tipo=" + tipoErro + ", lexema=" + lexema + ", (" + linha + ", " + coluna + ")]: " + mensagem;
	}

	public TipoErro getTipoErro() { 
		return tipoErro; 
	}
	
	public void setErrorType(TipoErro tipoErro) 	{
		this.tipoErro = tipoErro; 
	}

	public String getMensagem() 	{ 
		return mensagem; 
	}

	public String getLexema() { 
		return lexema; 
	}

	public long getLinha() 	{ 
		return linha; 
	}

	public long getColuna(){ 
		return coluna; 
	}
}
