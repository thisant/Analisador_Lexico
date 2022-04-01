package br.com.unit.compiladores;

import java.io.IOException;

public class CompiladorMain {

	public static void main(String[] args) {
        
		String arquivo = "src/GramaticaTeste.txt";
		//String arquivo = "src/GramaticaTeste.txt";
		AnalisadorSintatico analisadorSintatico;
		
		try {
			analisadorSintatico = new AnalisadorSintatico(arquivo);
			analisadorSintatico.processar();
		} catch (IOException e) {
			System.out.println("Arquivo não encontrado!");
			System.out.println(e.getMessage());
		}
	}
}
