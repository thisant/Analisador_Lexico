package br.com.unit.compiladores;

import java.util.HashMap;
import java.util.Map;

public class Simbologia {

	private static Simbologia instancia = new Simbologia();
	private Map<String, Token> tabela;
	
	private Simbologia() {
		tabela = new HashMap<String, Token>();

		tabela.put("true", new Token(TipoToken.BOOLEAN, "true"));
		tabela.put("false", new Token(TipoToken.BOOLEAN, "false"));
		tabela.put("not", new Token(TipoToken.OPLOG, "not"));
		tabela.put("and", new Token(TipoToken.OPLOG, "and"));
		tabela.put("or", new Token(TipoToken.OPLOG, "or"));
		tabela.put("bool", new Token(TipoToken.TYPE, "bool"));
		tabela.put("PROGRAM", new Token(TipoToken.PROGRAM, "PROGRAM"));
		tabela.put("BEGIN", new Token(TipoToken.BEGIN, "BEGIN"));
		tabela.put("END", new Token(TipoToken.END, "END"));
		tabela.put("if", new Token(TipoToken.IF, "if"));
		tabela.put("then", new Token(TipoToken.THEN, "then"));
		tabela.put("else", new Token(TipoToken.ELSE, "else"));
		tabela.put("for", new Token(TipoToken.FOR, "for"));
		tabela.put("WHILE", new Token(TipoToken.WHILE, "WHILE"));
		tabela.put("to", new Token(TipoToken.TO, "to"));
		tabela.put("INTEGER", new Token(TipoToken.INTEGER, "INTEGER"));
		tabela.put("DO", new Token(TipoToken.DO, "DO"));
		tabela.put("READ", new Token(TipoToken.READ, "READ"));
		tabela.put("VAR", new Token(TipoToken.VAR, "VAR"));
		tabela.put("WRITE", new Token(TipoToken.WRITE, "WRITE"));
		tabela.put("STRING", new Token(TipoToken.STRING, "STRING"));
		
	}
	
	public static Simbologia getInstancia() {
		return instancia;
	}
	
	public Token addToken(String lexema, long linha, long coluna) {
		Token token = null;

		if (tabela.containsKey(lexema)) {
			token = tabela.get(lexema);
			token.setLinha(linha);
			token.setColuna(coluna);
		} else {
			token = new Token(TipoToken.ID, lexema, linha, coluna);
			tabela.put(lexema, token);
		}

		return token;
	}
	
	public void printRelatorio()
	{
		tituloTabela();
		String tabelaForma = "| %-15s | %-16s |\n";
		
		tabela.forEach((Lexema, token) -> {
			   System.out.printf(tabelaForma, token.getTipoToken(), Lexema );
		});
		System.out.println("--------------------------------------");
	}

	private void tituloTabela()
	{
		System.out.printf("\n\t       Simbólos\n\n");
		System.out.println("|      Token      |       Lexema     |");
		System.out.println("--------------------------------------");
	}

}
