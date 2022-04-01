package br.com.unit.compiladores;

import java.io.EOFException;
import java.io.IOException;

public class AnalisadorLexico {
	private Executor carregarArquivo;
	private StringBuilder lexema = null;
	private char caracter;
	private long tokenLinha;
	private long tokenColuna;


	public AnalisadorLexico(String nomeArquivo) throws IOException {
		carregarArquivo = new Executor(nomeArquivo);
	}

	public Token proximoToken() {
		Token token = null;

		try {
			while (token == null) {
				do {
					caracter = carregarArquivo.getProximoCaractere();
				} while (Character.isWhitespace(caracter));

				lexema = new StringBuilder();
				tokenLinha = carregarArquivo.getLinha();
				tokenColuna = carregarArquivo.getColuna();

				lexema.append(caracter);

				switch (caracter) {
				case '+':
					token = new Token(TipoToken.OPAD, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '-':
					token = new Token(TipoToken.OPAD, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '*':
					token = new Token(TipoToken.OPMULT, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '/':
					token = new Token(TipoToken.OPMULT, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case ';':
					token = new Token(TipoToken.PVIG, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '<':
					token = new Token(TipoToken.OPREL, lexema.toString(), tokenLinha, tokenColuna);
					simboloMenorQue();
					break;
				case '>':
					token = new Token(TipoToken.OPREL, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '=':
					simboloIgual();
					break;
				case ',':
					token = new Token(TipoToken.VIG, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '(':
					token = new Token(TipoToken.ABPAR, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case ')':
					token = new Token(TipoToken.FPAR, lexema.toString(), tokenLinha, tokenColuna);
					break;
				case '#':
					comentario();
					break;
				case '"':
					token = string();
					break;
				case '$':
					token = relop();
					break;
				case ':':
					token = assign();
					break;
				case '.':
					token = new Token(TipoToken.PONTO, lexema.toString(), tokenLinha, tokenColuna);
					break;
				default:
					if (Character.isLetter(caracter) || caracter == '_') {
						token = id();
						break;
					}
					if (Character.isDigit(caracter) || caracter == '-') {
						token = numero();
						break;
					}

					ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
							"Caracter inválido", tokenLinha, tokenColuna);
				}
			}

		} catch (EOFException eof) {
			token = new Token(TipoToken.FIM);
		} catch (IOException io) {
			// Registrando erro (ao compilar)
			ErrorAssist.getInstancia().erroCompilador(TipoErro.COMPILACAO, "", "Falha ao acessar o arquivo",
					tokenLinha, tokenColuna);
			token = new Token(TipoToken.FIM, "Erro de compilação");
		}
		return token;
	}

	private char proximoCaractere() throws IOException {
		char c = carregarArquivo.getProximoCaractere();
		lexema.append(c);
		return c;
	}

	private void ultimoCaractereReiniciar() throws IOException {
		carregarArquivo.ultimoCaractereReiniciar();
		lexema.deleteCharAt(lexema.length() - 1);
	}

	
	private void comentario() throws IOException {
		try {
			char c = proximoCaractere();
			
			do {
				c = proximoCaractere();
			} while (c != '#');
			c = proximoCaractere();
		
		} catch (EOFException e) {
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
					"Erro no comentário não finalizado.", tokenLinha, tokenColuna);
			carregarArquivo.ultimoCaractereReiniciar();
		}
	}

	private Token string() throws IOException {
		char c = proximoCaractere();
		try {
			while (c != '"' && c != '\n') {
				c = proximoCaractere();
			}
		} catch (EOFException eof) {
			lexema.append(" ");
		}

		if (c != '"') {
			ultimoCaractereReiniciar();
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(), "Erro no Literal",
					tokenLinha, tokenColuna);
			return null;
		}

		return new Token(TipoToken.STRING, lexema.toString(), tokenLinha, tokenColuna);
	}

	private Token relop() throws IOException {
		char c = proximoCaractere();

		try {
			switch (c) {
			case 'l':
				c = proximoCaractere();

				if (c != 't' && c != 'e') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tokenLinha, tokenColuna);
				}
				break;

			case 'g':
				c = proximoCaractere();

				if (c != 't' && c != 'e') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tokenLinha, tokenColuna);
				}
				break;

			case 'e':
				c = proximoCaractere();

				if (c != 'q') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tokenLinha, tokenColuna);
				}
				break;

			case 'd':
				c = proximoCaractere();

				if (c != 'f') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tokenLinha, tokenColuna);
				}
				break;

			default:

				if (Character.isWhitespace(c)) {
					ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
							"O Relop está incorreto. Valores válidos: $lt|$gt|$ge|$le|$eq|$df", tokenLinha, tokenColuna);
				}

				ultimoCaractereReiniciar();
				return new Token(TipoToken.RELOP, lexema.toString(), tokenLinha, tokenColuna);
			}
		} catch (EOFException eofError) {
			carregarArquivo.ultimoCaractereReiniciar();
			lexema.append(" ");
		}

		return new Token(TipoToken.RELOP, lexema.toString(), tokenLinha, tokenColuna);
	}

	private Token assign() throws IOException {
		char c = proximoCaractere();
		if (c != '=') {
			// Registra Erro
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(), "Operador inválido",
					tokenLinha, tokenColuna);
			return null;
		}
		return new Token(TipoToken.ATRIB, lexema.toString(), tokenLinha, tokenColuna);
	}
	
	private Token simboloIgual() throws IOException {
		char c = proximoCaractere();
		if (c != '=') {
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(), "Operador inválido", tokenLinha, tokenColuna);
			return null;
		}
		
		return new Token(TipoToken.ATRIB, lexema.toString(), tokenLinha, tokenColuna);
	}
	
	private Token simboloMenorQue() throws IOException {
		char c = proximoCaractere();
		
		if (c == '=') {
			return new Token(TipoToken.ATRIB, lexema.toString(), tokenLinha, tokenColuna);
		}
		
		return null;
	}

	private Token numero() throws IOException {
		char c = proximoCaractere();

		try {
			while (Character.isDigit(c)) {
				c = proximoCaractere();
			}

			
			if (c != 'E') {
				ultimoCaractereReiniciar();
				return new Token(TipoToken.INTEGER, lexema.toString(), tokenLinha, tokenColuna);
			}

			c = proximoCaractere();

			if (c != '+') {
				ultimoCaractereReiniciar();
				ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
						"Número inteiro inválido. `+` é válido após:  `" + lexema.toString() + '`', tokenLinha, tokenColuna);
				return null;
			}

			do {
				c = proximoCaractere();
			} while (Character.isDigit(c));

			ultimoCaractereReiniciar();

			return new Token(TipoToken.INTEGER, lexema.toString(), tokenLinha, tokenColuna);
		} catch (EOFException eofError) {
			lexema.append(" ");
		}

		return null;
	}

	private Token id() throws IOException {
		Token token = null;
		try {
			char c = proximoCaractere();
			while (Character.isLetter(c) || c == '_' || Character.isDigit(c)) {
				c = proximoCaractere();
			}
			ultimoCaractereReiniciar();
		} catch (EOFException e) {
			carregarArquivo.ultimoCaractereReiniciar();
		}

		token = Simbologia.getInstancia().adicionarToken(lexema.toString(), tokenLinha, tokenColuna);
		return token;
	}
}
