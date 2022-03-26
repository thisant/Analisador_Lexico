package br.com.compilador;

import java.io.IOException;

import br.com.compilador.analisadores.Sintatico;

import br.com.compilador.analisadores.Lexico;

public class Compilador {

	public static void main(String[] args) {

		if(args.length < 1) {
			System.out.println("Informe o nome do arquivo a ser compilado");
			System.out.println("USAGE: java Compilador <nome do arquivo de entrada>");
			return;
		}
		//String filename = args[0];
		String filename = "/compiler/file.txt";
		Sintatico sintatico;
		Lexico lexico;
		try {
			sintatico = new Sintatico(filename);
			sintatico.processar();
		} catch (IOException e) {
			System.out.println("Arquivo não encontrado!");
			System.out.println("USAGE: java Compilador <nome do arquivo de entrada>");
		}
	}
}
