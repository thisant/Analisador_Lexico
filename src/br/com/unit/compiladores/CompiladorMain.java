package br.com.unit.compiladores;

import java.io.IOException;

public class CompiladorMain {

	public static void main(String[] args) {
        
		//String arquivo = "src/GramaticaTesteErro.txt";
		String arquivo = "src/GramaticaTeste.txt";
		
		AnalisadorSintatico analisadorSintatico;
		
		try {
			analisadorSintatico = new AnalisadorSintatico(arquivo);
			analisadorSintatico.compilando();
		} catch (IOException e) {
			System.out.println("Falha ao encontrar o arquivo!");
			System.out.println(e.getMessage());
		}
	}
}
