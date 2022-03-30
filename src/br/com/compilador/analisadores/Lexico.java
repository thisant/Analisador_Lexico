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
			// Trata entrada até encontrar um token
			while (token == null) {
				// Remove espaços em branco no início do reconhecimento
				do {
					caracter = fileLoader.getNextChar();
				} while (Character.isWhitespace(caracter));

				lexema = new StringBuilder();
				// Posição inicial do possível token
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
				case '/':
					token = new Token(TokenType.OPMULT, lexema.toString(), tk_lin, tk_col);
					break;
				case ';':
					token = new Token(TokenType.PVIG, lexema.toString(), tk_lin, tk_col);
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
				case '{':
					processaComentario();
					break;
				case '"':
					token = processaLiteral();
					break;
				case '$':
					token = processaRelop();
					break;
				case ':':
					token = processaAssign();
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

					// Registra erro (Léxico)
					ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
							"Caracter inválido", tk_lin, tk_col);
				}
			}

		} catch (EOFException eof) {
			// Cada método deve tratar a possibilidade de um fim de arquivo apropriadamente
			// Se o fim de arquivo ocorre no início do processamento do token, então, isso
			// significa
			// que devemos encerrar retornando um 'Token EOF'
			token = new Token(TokenType.EOF);
		} catch (IOException io) {
			// Registra erro (Processamento)
			ErrorHandler.getInstance().addCompilerError(ErrorType.PROCESSAMENTO, "", "Erro ao acessar o arquivo",
					tk_lin, tk_col);
			token = new Token(TokenType.EOF, "Erro de processamento");
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

	/**
	 * metodo responsavel por ignorar o comentario no codigo
	 */
	private void processaComentario() throws IOException {
		try {
			char c = getNextChar();
			if (c != '#') {
				// Registra erro, reseta lexema e reinicia
				ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
						"Comentário mal formatado", tk_lin, tk_col);
			}

			do {
				do {
					c = getNextChar();
				} while (c != '#');
				c = getNextChar();
			} while (c != '}');
		} catch (EOFException e) {
			// Gera Erro, pois se um EOF ocorre, significa que o comentário não foi fechado
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
					"Comentário não finalizado.", tk_lin, tk_col);
			fileLoader.resetLastChar();
		}
	}

	private Token processaLiteral() throws IOException {
		char c = getNextChar();
		try {
			while (c != '"' && c != '\n') {
				c = getNextChar();
			}
		} catch (EOFException eof) {
			// Adiciona espaço para tornar o tratamento homogêneo
			lexema.append(" ");
		}

		if (c != '"') {
			resetLastChar();
			// Registra Erro Léxico
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(), "Literal não finalizado",
					tk_lin, tk_col);
			return null;
		}

		return new Token(TokenType.LITERAL, lexema.toString(), tk_lin, tk_col);
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
							"Relop Inválido. Valores esperados: $lt|$gt|$ge|$le|$eq|$df", tk_lin, tk_col);
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
			// Registra Erro Léxico
			ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(), "Operador inv�lido",
					tk_lin, tk_col);
			return null;
		}
		return new Token(TokenType.ATRIB, lexema.toString(), tk_lin, tk_col);
	}

	private Token processaNum() throws IOException {
		char c = getNextChar();

		/**
		 * NUM_FLOAT: 3.10E+10|4.8 NUM_INT: 3E+10|123|48
		 */

		try {
			while (Character.isDigit(c)) {
				c = getNextChar();
			}

			if (c == '.') {
				c = getNextChar();

				if (!Character.isDigit(c)) {
					resetLastChar();
					ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
							"Número Float inválido. Esperado finalizar o número", tk_lin, tk_col);
					return null;
				}

				do {
					c = getNextChar();
				} while (Character.isDigit(c));

				if (c != 'E') {
					resetLastChar();
					return new Token(TokenType.NUM_FLOAT, lexema.toString(), tk_lin, tk_col);
				}

				c = getNextChar();

				if (c != '+') {
					resetLastChar();
					ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
							"Número Float inválido. `+` é esperado após E", tk_lin, tk_col);
					return null;
				}

				c = getNextChar();

				do {
					c = getNextChar();
				} while (Character.isDigit(c));

				resetLastChar();

				return new Token(TokenType.NUM_FLOAT, lexema.toString(), tk_lin, tk_col);
			}

			if (c != 'E') {
				resetLastChar();
				return new Token(TokenType.NUM_INT, lexema.toString(), tk_lin, tk_col);
			}

			c = getNextChar();

			if (c != '+') {
				resetLastChar();
				ErrorHandler.getInstance().addCompilerError(ErrorType.LEXICO, lexema.toString(),
						"Número Inteiro inválido. `+` é esperado após `" + lexema.toString() + '`', tk_lin, tk_col);
				return null;
			}

			do {
				c = getNextChar();
			} while (Character.isDigit(c));

			resetLastChar();

			return new Token(TokenType.NUM_INT, lexema.toString(), tk_lin, tk_col);
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
			// Quebra de padrão provocado pelo fim do arquivo
			// Ainda retornaremos o token
			fileLoader.resetLastChar();
		}

		token = TabSimbolos.getInstance().addToken(lexema.toString(), tk_lin, tk_col);
		return token;
	}
}
