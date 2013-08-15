package dou.processador1modo;

import java.util.Date;

public class Portaria
{

	public Portaria(String _identificacaoPortaria, String _textoPortaria, long _inicioPortaria, long _fimPortaria,
			String _tipoPortaria, String _nomeArquivo, Date _data)
	{

		identificacaoPortaria = _identificacaoPortaria;
		textoPortaria = _textoPortaria;
		inicioPortaria = _inicioPortaria;
		fimPortaria = _fimPortaria;
		tipoPortaria = _tipoPortaria;
		nomeArquivo = _nomeArquivo;
		data = _data;
	}

	public String identificacaoPortaria;
	public String textoPortaria;
	public long inicioPortaria;
	public long fimPortaria;
	public String tipoPortaria;
	public String nomeArquivo;
	public Date data;
}
