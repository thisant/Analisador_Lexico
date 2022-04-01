package br.com.unit.compiladores;

import java.io.IOException;

public class AnalisadorSintatico
{
	private AnalisadorLexico analisadorLexico;
	private Token token = null;

	public AnalisadorSintatico(String nomeArquivo) throws IOException
	{
		analisadorLexico = new AnalisadorLexico(nomeArquivo);
	}

	public void compilando()
	{
		System.out.println("\t     Análise Sintática");

		System.out.println("[ X , Y ]|   Token    |          Lexema       |");
		System.out.println("-----------------------------------------------"); 
		
		do  { 
			token = analisadorLexico.proximoToken(); 
			token.print(); 
		} while (token.getTipoToken() != TipoToken.FIM);
		
		System.out.println("-----------------------------------------------");

		Simbologia.getInstancia().printTabela(); 
		ErrorAssist.getInstancia().gerarTabela();
	}
}
