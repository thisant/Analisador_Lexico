package br.com.compilador.utils;

import java.io.*;

public class FileLoader extends BufferedReader {
    
    private long line;
    private long column;
    private long lastLineSize;
    
    public FileLoader(String fileName) 
    throws FileNotFoundException {
        this(new File(fileName));
    }
    
    public FileLoader(File file) 
    throws FileNotFoundException {
        super((new FileReader(file)));
        line = 1;
        column = 0;
        lastLineSize = 0;
    }
    
    public char getNextChar() throws EOFException, IOException {
        this.mark(1);
        int charValue = this.read();
        if (charValue == -1) throw new EOFException(); 
        column++;
        if (charValue == '\n') { 
            line++;
            lastLineSize = column;
            column = 0;
        } 
        
        return (char) charValue; 
    }
    
    public void resetLastChar() throws IOException {
        this.reset();
        column--;
        if (column < 0) {
            column = lastLineSize;
            line--;
        }
    }

    public long getLine() {
        return line;
    }

    public long getColumn() {
        return column;
    }

}
