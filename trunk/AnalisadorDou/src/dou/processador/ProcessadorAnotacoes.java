package dou.processador;

import gate.Document;

import java.io.File;

import dou.processador.registro.RegistroLigacaoStrategy;

public interface ProcessadorAnotacoes
{

	public void process(File docFile, Document doc, RegistroLigacaoStrategy strategy);

}
