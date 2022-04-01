package br.com.compilador.analisadores;

import java.io.EOFException;
import java.io.IOException;

import br.com.compilador.TabSimbolos;
import br.com.compilador.token.Token;
import br.com.compilador.token.TokenType;
import br.com.compilador.utils.ErrorHandler;
import br.com.compilador.utils.ErrorType;
import br.com.compilador.utils.FileLoader;

public class Lexico {
	private FileLoader fileLoader;
	private StringBuilder lexema = null;
	private char caracter;
	private long tk_col;
	private long tk_lin;

	public Lexico(String filename) throws IOException {
		fileLoader = new FileLoader(filename);
	}

	public Token nextToken() {
		Token token = null;

		try {
			// tratar entrada
			while (token == null) {
				// remove espaÁos em branco
				do {
					caracter = fileLoader.getNextChar();
				} while (Character.isWhitespace(caracter));

				lexema = new StringBuilder();
				// Posi√ß√£o inicial do poss√≠vel token
				tk_lin = fileLoader.getLine();
				tk_col = fileLoader.getColumn();

				// Apenda 1o caracter do lexema
				lexema.append(caracter);

				switch (caracter) {
				case '+':
					token = new Token(TokenType.OPAD, lexema.toString(), tk_lin, tk_col);
					break;
				case '-':
					token = new Token(TokenType.OPAD, lexema.toString(), tk_lin, tk_col);
					break;
				case '*':
					token = new Token(TokenType.OPMULT, lexema.toString(), tk_lin, tk_col);
					break;
				case '/':
					token = new Token(TokenType.OPMULT, lexema.toString(), tk_lin, tk_col);
					break;
				case ';':
					token = new Token(TokenType.PVIG, lexema.toString(), tk_lin, tk_col);
					break;
				case '<':
					token = new Token(TokenType.OPREL, lexema.toString(), tk_lin, tk_col);
					processaOperadorLogicoMenorQue();
					break;
				case '>':
					token = new Token(TokenType.OPREL, lexema.toString(), tk_lin, tk_col);
					break;
				case '=':
					processaOperadorLogicoIgual();
					break;
				case ',':
					token = new Token(TokenType.VIG, lexema.toString(), tk_lin, tk_col);
					break;
				case '(':
					token = new Token(TokenType.ABPAR, lexema.toString(), tk_lin, tk_col);
					break;
				case ')':
					token = new Token(TokenType.FPAR, lexema.toString(), tk_lin, tk_col);
					break;
				case '#':
					processaComentario();
					break;
				case '"':
					token = processaString();
					break;
				case '$':
					token = processaRelop();
					break;
				case ':':
					token = processaAssign();
					break;
				case '.':
					token = new Token(TokenType.PONTO, lexema.toString(), tk_lin, tk_col);
					break;
				default:
					if (Character.isLetter(caracter) || caracter == '_') {
						token = processaID();
						break;
					}
					if (Character.isDigit(caracter) || caracter == '-') {
						token = processaNum();
						break;
					}

					// Registra erro 
					ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
							"Caracter inv·lido", tk_lin, tk_col);
				}
			}

		} catch (EOFException eof) {
			token = new Token(TokenType.FIM);
		} catch (IOException io) {
			// Registra erro (Processamento)
			ErrorHandler.getInstance().addCompilerError(ErrorType.PROCESSAMENTO, "", "Erro ao acessar o arquivo",
					tk_lin, tk_col);
			token = new Token(TokenType.FIM, "Erro de processamento");
		}
		return token;
	}

	private char getNextChar() throws IOException {
		char c = fileLoader.getNextChar();
		lexema.append(c);
		return c;
	}

	private void resetLastChar() throws IOException {
		fileLoader.resetLastChar();
		lexema.deleteCharAt(lexema.length() - 1);
	}

	
	private void processaComentario() throws IOException {
		try {
			char c = getNextChar();
			
			do {
				c = getNextChar();
			} while (c != '#');
			c = getNextChar();
			
		} catch (EOFException e) {
			// Gera Erro, pois se um FIM ocorre, significa que o coment·rio n„o foi fechado
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
					"Coment·rio n„o finalizado.", tk_lin, tk_col);
			fileLoader.resetLastChar();
		}
	}

	private Token processaString() throws IOException {
		char c = getNextChar();
		try {
			while (c != '"' && c != '\n') {
				c = getNextChar();
			}
		} catch (EOFException eof) {
			// adiciona espaÁo para tratamento
			lexema.append(" ");
		}

		if (c != '"') {
			resetLastChar();
			// Registra Erro
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(), "Literal n„o finalizado",
					tk_lin, tk_col);
			return null;
		}

		return new Token(TokenType.STRING, lexema.toString(), tk_lin, tk_col);
	}

	private Token processaRelop() throws IOException {
		char c = getNextChar();

		try {
			switch (c) {
			case 'l':
				c = getNextChar();

				if (c != 't' && c != 'e') {
					resetLastChar();
					return new Token(TokenType.RELOP, lexema.toString(), tk_lin, tk_col);
				}
				break;

			case 'g':
				c = getNextChar();

				if (c != 't' && c != 'e') {
					resetLastChar();
					return new Token(TokenType.RELOP, lexema.toString(), tk_lin, tk_col);
				}
				break;

			case 'e':
				c = getNextChar();

				if (c != 'q') {
					resetLastChar();
					return new Token(TokenType.RELOP, lexema.toString(), tk_lin, tk_col);
				}
				break;

			case 'd':
				c = getNextChar();

				if (c != 'f') {
					resetLastChar();
					return new Token(TokenType.RELOP, lexema.toString(), tk_lin, tk_col);
				}
				break;

			default:

				if (Character.isWhitespace(c)) {
					ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
							"Relop Inv√°lido. Valores esperados: $lt|$gt|$ge|$le|$eq|$df", tk_lin, tk_col);
				}

				resetLastChar();
				return new Token(TokenType.RELOP, lexema.toString(), tk_lin, tk_col);
			}
		} catch (EOFException eofError) {
			fileLoader.resetLastChar();
			lexema.append(" ");
		}

		return new Token(TokenType.RELOP, lexema.toString(), tk_lin, tk_col);
	}

	private Token processaAssign() throws IOException {
		char c = getNextChar();
		if (c != '=') {
			// Registra Erro
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(), "Operador inv·lido",
					tk_lin, tk_col);
			return null;
		}
		return new Token(TokenType.ATRIB, lexema.toString(), tk_lin, tk_col);
	}
	
	private Token processaOperadorLogicoIgual() throws IOException {
		char c = getNextChar();
		if (c != '=') {
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(), "Operador inv·lido", tk_lin, tk_col);
			return null;
		}
		
		return new Token(TokenType.ATRIB, lexema.toString(), tk_lin, tk_col);
	}
	
	private Token processaOperadorLogicoMenorQue() throws IOException {
		char c = getNextChar();
		
		if (c == '=') {
			return new Token(TokenType.ATRIB, lexema.toString(), tk_lin, tk_col);
		}
		
		return null;
	}

	private Token processaNum() throws IOException {
		char c = getNextChar();

		try {
			while (Character.isDigit(c)) {
				c = getNextChar();
			}

			
			if (c != 'E') {
				resetLastChar();
				return new Token(TokenType.INTEGER, lexema.toString(), tk_lin, tk_col);
			}

			c = getNextChar();

			if (c != '+') {
				resetLastChar();
				ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
						"N√∫mero Inteiro inv√°lido. `+` √© esperado ap√≥s `" + lexema.toString() + '`', tk_lin, tk_col);
				return null;
			}

			do {
				c = getNextChar();
			} while (Character.isDigit(c));

			resetLastChar();

			return new Token(TokenType.INTEGER, lexema.toString(), tk_lin, tk_col);
		} catch (EOFException eofError) {
			lexema.append(" ");
		}

		return null;
	}

	private Token processaID() throws IOException {
		Token token = null;
		try {
			char c = getNextChar();
			while (Character.isLetter(c) || c == '_' || Character.isDigit(c)) {
				c = getNextChar();
			}
			resetLastChar();
		} catch (EOFException e) {
			// Quebra de padr„o provocado pelo fim do arquivo
			// Ainda retornaremos o token
			fileLoader.resetLastChar();
		}

		token = TabSimbolos.getInstance().addToken(lexema.toString(), tk_lin, tk_col);
		return token;
	}
}
