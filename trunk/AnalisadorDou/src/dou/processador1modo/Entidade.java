package dou.processador1modo;

public class Entidade
{
	public Entidade(String _entidade, String _idEntidade, String _particao, long _inicioEntidade, long _fimEntidade,
			String _tipoEntidade)
	{
		entidade = _entidade.trim().replace('\n', ' ');
		try
		{
			idEntidade = new Long(_idEntidade);
		} catch (Exception e)
		{
			idEntidade = 0L;
		}
		particao = _particao;
		inicioEntidade = _inicioEntidade;
		fimEntidade = _fimEntidade;
		tipoEntidade = _tipoEntidade;
	}

	public String entidade;
	public long idEntidade;
	public String particao; // nao usado atualmente
	public long inicioEntidade;
	public long fimEntidade;
	public String tipoEntidade;
}
