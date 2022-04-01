package br.com.unit.compiladores;

import java.io.EOFException;
import java.io.IOException;

public class AnalisadorLexico {
	private Executor carregarArquivo;
	private StringBuilder lexema = null;
	private char caracter;
	private long tk_coluna;
	private long tk_linha;

	public AnalisadorLexico(String nomeArquivo) throws IOException {
		carregarArquivo = new Executor(nomeArquivo);
	}

	public Token proximoToken() {
		Token token = null;

		try {
			// Tratando entrada
			while (token == null) {
				// remove espaços em branco
				do {
					caracter = carregarArquivo.getProximoCaractere();
				} while (Character.isWhitespace(caracter));

				lexema = new StringBuilder();
				// Posição inicial do possível token
				tk_linha = carregarArquivo.getLinha();
				tk_coluna = carregarArquivo.getColuna();

				// Append do caracter do lexema
				lexema.append(caracter);

				switch (caracter) {
				case '+':
					token = new Token(TipoToken.OPAD, lexema.toString(), tk_linha, tk_coluna);
					break;
				case '-':
					token = new Token(TipoToken.OPAD, lexema.toString(), tk_linha, tk_coluna);
					break;
				case '*':
					token = new Token(TipoToken.OPMULT, lexema.toString(), tk_linha, tk_coluna);
					break;
				case '/':
					token = new Token(TipoToken.OPMULT, lexema.toString(), tk_linha, tk_coluna);
					break;
				case ';':
					token = new Token(TipoToken.PVIG, lexema.toString(), tk_linha, tk_coluna);
					break;
				case '<':
					token = new Token(TipoToken.OPREL, lexema.toString(), tk_linha, tk_coluna);
					simboloMenorQue();
					break;
				case '>':
					token = new Token(TipoToken.OPREL, lexema.toString(), tk_linha, tk_coluna);
					break;
				case '=':
					simboloIgual();
					break;
				case ',':
					token = new Token(TipoToken.VIG, lexema.toString(), tk_linha, tk_coluna);
					break;
				case '(':
					token = new Token(TipoToken.ABPAR, lexema.toString(), tk_linha, tk_coluna);
					break;
				case ')':
					token = new Token(TipoToken.FPAR, lexema.toString(), tk_linha, tk_coluna);
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
					token = new Token(TipoToken.PONTO, lexema.toString(), tk_linha, tk_coluna);
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

					// Registra erro 
					ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
							"Caracter inválido", tk_linha, tk_coluna);
				}
			}

		} catch (EOFException eof) {
			token = new Token(TipoToken.FIM);
		} catch (IOException io) {
			// Registra erro (Processamento)
			ErrorAssist.getInstancia().erroCompilador(TipoErro.PROCESSAMENTO, "", "Erro ao acessar o arquivo",
					tk_linha, tk_coluna);
			token = new Token(TipoToken.FIM, "Erro de processamento");
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
			// Gera Erro, pois se um FIM ocorre, significa que o comentário não foi fechado
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
					"Comentário não finalizado.", tk_linha, tk_coluna);
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
			// adiciona espaço para tratamento
			lexema.append(" ");
		}

		if (c != '"') {
			ultimoCaractereReiniciar();
			// Registra Erro
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(), "Literal não finalizado",
					tk_linha, tk_coluna);
			return null;
		}

		return new Token(TipoToken.STRING, lexema.toString(), tk_linha, tk_coluna);
	}

	private Token relop() throws IOException {
		char c = proximoCaractere();

		try {
			switch (c) {
			case 'l':
				c = proximoCaractere();

				if (c != 't' && c != 'e') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tk_linha, tk_coluna);
				}
				break;

			case 'g':
				c = proximoCaractere();

				if (c != 't' && c != 'e') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tk_linha, tk_coluna);
				}
				break;

			case 'e':
				c = proximoCaractere();

				if (c != 'q') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tk_linha, tk_coluna);
				}
				break;

			case 'd':
				c = proximoCaractere();

				if (c != 'f') {
					ultimoCaractereReiniciar();
					return new Token(TipoToken.RELOP, lexema.toString(), tk_linha, tk_coluna);
				}
				break;

			default:

				if (Character.isWhitespace(c)) {
					ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
							"Relop Inválido. Valores esperados: $lt|$gt|$ge|$le|$eq|$df", tk_linha, tk_coluna);
				}

				ultimoCaractereReiniciar();
				return new Token(TipoToken.RELOP, lexema.toString(), tk_linha, tk_coluna);
			}
		} catch (EOFException eofError) {
			carregarArquivo.ultimoCaractereReiniciar();
			lexema.append(" ");
		}

		return new Token(TipoToken.RELOP, lexema.toString(), tk_linha, tk_coluna);
	}

	private Token assign() throws IOException {
		char c = proximoCaractere();
		if (c != '=') {
			// Registra Erro
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(), "Operador inválido",
					tk_linha, tk_coluna);
			return null;
		}
		return new Token(TipoToken.ATRIB, lexema.toString(), tk_linha, tk_coluna);
	}
	
	private Token simboloIgual() throws IOException {
		char c = proximoCaractere();
		if (c != '=') {
			ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(), "Operador inválido", tk_linha, tk_coluna);
			return null;
		}
		
		return new Token(TipoToken.ATRIB, lexema.toString(), tk_linha, tk_coluna);
	}
	
	private Token simboloMenorQue() throws IOException {
		char c = proximoCaractere();
		
		if (c == '=') {
			return new Token(TipoToken.ATRIB, lexema.toString(), tk_linha, tk_coluna);
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
				return new Token(TipoToken.INTEGER, lexema.toString(), tk_linha, tk_coluna);
			}

			c = proximoCaractere();

			if (c != '+') {
				ultimoCaractereReiniciar();
				ErrorAssist.getInstancia().erroCompilador(TipoErro.LEXICO, lexema.toString(),
						"Número Inteiro inválido. `+` É esperado após `" + lexema.toString() + '`', tk_linha, tk_coluna);
				return null;
			}

			do {
				c = proximoCaractere();
			} while (Character.isDigit(c));

			ultimoCaractereReiniciar();

			return new Token(TipoToken.INTEGER, lexema.toString(), tk_linha, tk_coluna);
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
			// Quebra de padrão provocado pelo fim do arquivo
			// Ainda retornaremos o token
			carregarArquivo.ultimoCaractereReiniciar();
		}

		token = Simbologia.getInstancia().addToken(lexema.toString(), tk_linha, tk_coluna);
		return token;
	}
}
