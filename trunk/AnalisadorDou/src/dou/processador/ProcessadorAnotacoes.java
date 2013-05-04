package dou.processador;

import gate.Document;
import dou.processador.registro.RegistroLigacaoStrategy;

public interface ProcessadorAnotacoes {

	public void process(String docFile, Document doc,
			RegistroLigacaoStrategy strategy);

}
