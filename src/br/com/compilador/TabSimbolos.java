package br.com.compilador;

import java.util.HashMap;
import java.util.Map;

import br.com.compilador.token.Token;
import br.com.compilador.token.TokenType;

public class TabSimbolos {

	private static TabSimbolos instance = new TabSimbolos();
	private Map<String, Token> tab;
	
	private TabSimbolos() {
		tab = new HashMap<String, Token>();

		tab.put("true", new Token(TokenType.BOOLEAN, "true"));
		tab.put("false", new Token(TokenType.BOOLEAN, "false"));
		tab.put("not", new Token(TokenType.OPLOG, "not"));
		tab.put("and", new Token(TokenType.OPLOG, "and"));
		tab.put("or", new Token(TokenType.OPLOG, "or"));
		tab.put("bool", new Token(TokenType.TYPE, "bool"));
		tab.put("text", new Token(TokenType.TYPE, "text"));
		tab.put("int", new Token(TokenType.TYPE, "int"));
		tab.put("float", new Token(TokenType.TYPE, "float"));
		tab.put("PROGRAM", new Token(TokenType.PROGRAM, "PROGRAM"));
		tab.put("end_prog", new Token(TokenType.END_PROG, "end_prog"));
		tab.put("BEGIN", new Token(TokenType.BEGIN, "BEGIN"));
		tab.put("END", new Token(TokenType.END, "END"));
		tab.put("if", new Token(TokenType.IF, "if"));
		tab.put("then", new Token(TokenType.THEN, "then"));
		tab.put("else", new Token(TokenType.ELSE, "else"));
		tab.put("for", new Token(TokenType.FOR, "for"));
		tab.put("WHILE", new Token(TokenType.WHILE, "WHILE"));
		tab.put("declare", new Token(TokenType.DECLARE, "declare"));
		tab.put("to", new Token(TokenType.TO, "to"));
		tab.put("INTEGER", new Token(TokenType.INTEGER, "INTEGER"));
		tab.put("DO", new Token(TokenType.DO, "DO"));
		tab.put("READ", new Token(TokenType.READ, "READ"));
		tab.put("VAR", new Token(TokenType.VAR, "VAR"));
		tab.put("WRITE", new Token(TokenType.WRITE, "WRITE"));
		
	}
	
	public static TabSimbolos getInstance() {
		return instance;
	}
	
	public Token addToken(String lexema, long linha, long coluna) {
		Token token = null;

		if (tab.containsKey(lexema)) {
			token = tab.get(lexema);
			token.setLinha(linha);
			token.setColuna(coluna);
		} else {
			token = new Token(TokenType.ID, lexema, linha, coluna);
			tab.put(lexema, token);
		}

		return token;
	}
	
	public void printReport()
	{
		cabecalhoTabela();
		String formatTab = "| %-15s || %-16s |\n";
		
		tab.forEach((Lexema, token) -> {
			   System.out.printf(formatTab, token.getTokenType(), Lexema );
		});
		System.out.println("---------------------------------------");
	}

	private void cabecalhoTabela()
	{
		System.out.println("\n---------------------------------------");
		System.out.printf("|%27s %10s","Tabela de Simbolos","|");
		System.out.println("\n---------------------------------------");
		System.out.println("|      Token      ||      Lexema      |");
		System.out.println("---------------------------------------");
	}

}
