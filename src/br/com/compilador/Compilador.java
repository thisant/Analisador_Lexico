package br.com.compilador;

import java.io.IOException;

import br.com.compilador.analisadores.Sintatico;

import br.com.compilador.analisadores.Lexico;

public class Compilador {

	public static void main(String[] args) {

		//String filename = "src/file.txt";
		
		String filename = "src/GramME.txt";
		Sintatico sintatico;
		
		try {
			sintatico = new Sintatico(filename);
			sintatico.processar();
		} catch (IOException e) {
			System.out.println("Arquivo não encontrado!");
			System.out.println(e.getMessage());
		}
	}
}
