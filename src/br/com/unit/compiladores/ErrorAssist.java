package br.com.unit.compiladores;

import java.util.ArrayList;
import java.util.List;

public class ErrorAssist
{

	private static ErrorAssist instancia = new ErrorAssist();
	private List<Erro> erros = new ArrayList<Erro>();

	public void erroCompilador(Erro error)
	{
		erros.add(error);
	}
	
	public void erroCompilador(TipoErro errorType, String lexema, String mensagem, long linha, long coluna) {
		Erro e =  new Erro(errorType, lexema, mensagem, linha, coluna);
		erroCompilador(e);
	}

	public void gerarTabela()
	{
		if (erros.size() != 0) tituloTabelaLexico();

		for (Erro erro : erros)
		{
			System.out.println(erro.toString());
		}
	}

	private void tituloTabelaLexico()
	{
		System.out.printf("\nErros");
		System.out.printf("\n");
	}


	public static ErrorAssist getInstancia()
	{
		return instancia;
	}
}
