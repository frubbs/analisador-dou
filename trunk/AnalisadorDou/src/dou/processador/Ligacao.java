package dou.processador;

import java.util.Date;

public class Ligacao
{

	public Ligacao(String _entidade, String _idEntidade, String _identificacaoPortaria, String _textoPortaria,
			long _inicioPortaria, long _fimPortaria, long _inicioEntidade, long _fimEntidade, String _tipoEntidade,
			String _nomeArquivo, Date _data)
	{
		entidade = _entidade.trim().replace('\n', ' ');
		try
		{
			idEntidade = new Long(_idEntidade);
		} catch (Exception e)
		{
			idEntidade = 0L;
		}
		identificacaoPortaria = _identificacaoPortaria;
		textoPortaria = _textoPortaria;
		inicioPortaria = _inicioPortaria;
		fimPortaria = _fimPortaria;
		inicioEntidade = _inicioEntidade;
		fimEntidade = _fimEntidade;
		tipoEntidade = _tipoEntidade;
		nomeArquivo = _nomeArquivo;
		data = _data;
	}

	public String entidade;
	public long idEntidade;
	public String identificacaoPortaria;
	public String textoPortaria;
	public long inicioPortaria;
	public long fimPortaria;
	public long inicioEntidade;
	public long fimEntidade;
	public String tipoEntidade;
	public String nomeArquivo;
	public Date data;
}
