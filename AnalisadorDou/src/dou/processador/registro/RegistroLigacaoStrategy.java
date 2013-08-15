package dou.processador.registro;

import dou.processador.Ligacao;
import dou.processador1modo.Entidade;
import dou.processador1modo.Portaria;

public interface RegistroLigacaoStrategy
{

	public void registrar(Ligacao l);

	public void registrar1Modo(Entidade entidadeA, Entidade entidadeB, Portaria portaria);

}
