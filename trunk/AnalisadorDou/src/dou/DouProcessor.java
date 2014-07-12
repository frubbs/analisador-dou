/*
 *  BatchProcessApp.java
 *
 *
 * Copyright (c) 2006, The University of Sheffield.
 *
 * This file is part of GATE (see http://gate.ac.uk/), and is free
 * software, licenced under the GNU Library General Public License,
 * Version 2, June1991.
 *
 * A copy of this licence is included in the distribution in the file
 * licence.html, and is also available at http://gate.ac.uk/gate/licence.html.
 *
 *  Ian Roberts, March 2006
 *
 *  $Id: BatchProcessApp.java,v 1.5 2006/06/11 19:17:57 ian Exp $
 */
package dou;

import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;

import dou.processador.ProcessadorAnotacoes;
import dou.processador.registro.RegistroLigacaoStrategy;

/**
 * Essa classe le o arquivo, chama a aplicação do gate para gerar anotações e chama o processador de anotações passando a
 * estrategia de gravação fornecida.
 */
public class DouProcessor
{

	protected final Logger log = Logger.getLogger(DouProcessor.class);

	public void processFile(File gappFile, File docFile, ProcessadorAnotacoes processador,
			RegistroLigacaoStrategy registroStrategy) throws Exception
	{

		long startTime = System.currentTimeMillis();

		// Configura variaveis de ambiente para o GATE
		configureGateProps();

		// initialise GATE - this must be done before calling any GATE APIs
		Gate.init();

		// load the saved application
		CorpusController application = (CorpusController) PersistenceManager.loadObjectFromFile(gappFile);

		// Create a Corpus to use. We recycle the same Corpus object for each
		// iteration. The string parameter to newCorpus() is simply the
		// GATE-internal name to use for the corpus. It has no particular
		// significance.
		Corpus corpus = Factory.newCorpus("DouProcessor Corpus");
		application.setCorpus(corpus);

		long arquivoStart = System.currentTimeMillis();

		// load the document (using the specified encoding if one was given)
		System.out.print("Processing document " + docFile + "...");

		@SuppressWarnings("deprecation")
		Document doc = Factory.newDocument(docFile.toURL(), encoding);

		// put the document in the corpus
		corpus.add(doc);

		// run the application
		long appExecStart = System.currentTimeMillis();
		application.execute();
		long appExecEnd = System.currentTimeMillis();
		log.warn("appExecute: " + (appExecEnd - appExecStart) + " ms");

		// remove the document from the corpus again
		corpus.clear();

		long portariaStart = System.currentTimeMillis();

		processador.process(docFile, doc, registroStrategy);

		long portariaEnd = System.currentTimeMillis();
		log.warn("Portaria: " + (portariaEnd - portariaStart) + " ms");

		// Release the document, as it is no longer needed
		Factory.deleteResource(doc);

		log.warn("done");
		long arquivoEnd = System.currentTimeMillis();
		log.warn("Arquivo: " + (arquivoEnd - arquivoStart) + "ms");

		long stopTime = System.currentTimeMillis();
		log.warn("Fim do programa: " + (stopTime - startTime) + " ms");

	}

	private void configureGateProps()
	{
		Properties props = System.getProperties();
		// props.setProperty("gate.plugins.home", ".\\plugins\\ANNIE");
		props.setProperty("gate.home",
				"C:\\Users\\Rafael\\workspace\\analisador-dou\\AnalisadorDou\\lib\\GateAPI\\gate-7.1-build4485-ALL");

		// props.setProperty("gate.site.config", ".\\gate.xml");

		/*
		 * Properties props = System.getProperties(); props.setProperty("gate.plugins.home", ".\\plugins\\ANNIE");
		 * props.setProperty("gate.home", ".\\bin\\gate.jar"); props.setProperty("gate.site.config", ".\\gate.xml");
		 * 
		 * String gateHomeStr = System.getProperty(Gate.GATE_HOME_PROPERTY_NAME);
		 * 
		 * // gateHomeStr = // Thread.currentThread().getContextClassLoader().getResource ("gate/Gate.class").toString();
		 * 
		 * log.warn("#########8gfah: " + gateHomeStr);
		 */

		//
		//
		String gateHomeStr = System.getProperty(Gate.GATE_HOME_PROPERTY_NAME);

		// //gateHomeStr =
		// Thread.currentThread().getContextClassLoader().getResource("gate/Gate.class").toString();
		//
		log.warn("## Gate home: " + gateHomeStr);

	}

	/** Diretorion onde estao os arquivos a serem processados. */
	private String diretorio = null;

	/**
	 * The character encoding to use when loading the docments. If null, the platform default encoding is used.
	 */
	private String encoding = "UTF-8";
}