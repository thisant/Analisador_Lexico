package br.com.unit.compiladores;

import java.io.*;

public class Executor extends BufferedReader {
    
    private long linha;
    private long coluna;
    private long tamanhoLinhaFinal;
    
    public Executor(String nomeArquivo) 
    throws FileNotFoundException {
        this(new File(nomeArquivo));
    }
    
    public Executor(File arquivo) 
    throws FileNotFoundException {
        super((new FileReader(arquivo)));
        linha = 1;
        coluna = 0;
        tamanhoLinhaFinal = 0;
    }
    
    public char getProximoCaractere() throws EOFException, IOException {
        this.mark(1);
        int valorCaractere = this.read();
        if (valorCaractere == -1) throw new EOFException(); 
        coluna++;
        if (valorCaractere == '\n') { 
            linha++;
            tamanhoLinhaFinal = coluna;
            coluna = 0;
        } 
        
        return (char) valorCaractere; 
    }
    
    public void ultimoCaractereReiniciar() throws IOException {
        this.reset();
        coluna--;
        if (coluna < 0) {
            coluna = tamanhoLinhaFinal;
            linha--;
        }
    }

    public long getLinha() {
        return linha;
    }

    public long getColuna() {
        return coluna;
    }

}
