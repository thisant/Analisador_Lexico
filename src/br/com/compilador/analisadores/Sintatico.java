package br.com.compilador.analisadores;

import java.io.IOException;

import br.com.compilador.TabSimbolos;
import br.com.compilador.token.Token;
import br.com.compilador.token.TokenType;
import br.com.compilador.utils.ErrorHandler;

public class Sintatico
{
	private Lexico lexico;
	private Token token = null;

	public Sintatico(String filename) throws IOException
	{
		lexico = new Lexico(filename);
	}

	public void processar()
	{
		System.out.println("------------------------------------------------");
		System.out.println("( X , Y )|   Token    ||          Lexema       |");
		System.out.println("------------------------------------------------"); 
		
		do  { 
			token = lexico.nextToken(); 
			token.print(); 
		} while (token.getTokenType() != TokenType.FIM);
		
		System.out.println("------------------------------------------------");

		TabSimbolos.getInstance().printReport(); 
		ErrorHandler.getInstance().gerarRelatorio();
	}
}
