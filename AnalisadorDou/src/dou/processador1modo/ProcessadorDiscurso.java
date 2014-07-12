package dou.processador1modo;

import gate.AnnotationSet;
import gate.Document;
import gate.SimpleFeatureMap;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import dou.processador.ProcessadorAnotacoes;
import dou.processador.registro.RegistroLigacaoStrategy;
import dou.util.Util;

public class ProcessadorDiscurso implements ProcessadorAnotacoes
{
	protected final Logger log = Logger.getLogger(ProcessadorDiscurso.class);

	@Override
	public void process(File docFile, Document doc, RegistroLigacaoStrategy strategy)
	{
		try
		{

			// Listas de anotações detectadas pelo gate já na ordem em que
			// aparecem no documento
			long annotStart = System.currentTimeMillis();
			AnnotationSet todasAnnots = doc.getAnnotations();
			List<gate.Annotation> entidadeList = gate.Utils.inDocumentOrder(todasAnnots.get("EntidadeIdentificada"));
			long annotEnd = System.currentTimeMillis();

			long entidadeStart = System.currentTimeMillis();

			log.warn("Entidades no arquivo: " + entidadeList.size());

			HashMap<String, Entidade> entidadesEncontradasNome = new HashMap<String, Entidade>();

			String textoPortaria = doc.getContent().toString();
			String identificacaoPortaria = Util.gerarIdentificacaoUnicaDiscurso(docFile.getAbsolutePath());
			String tipoPortaria = "Discurso";
			String nomeArquivo = docFile.getAbsolutePath();

			Date data = Util.extrairDataDoNomeDoArquivoDiscurso(docFile.getName());

			Portaria portaria = new Portaria(identificacaoPortaria, textoPortaria, 0, 0, tipoPortaria, nomeArquivo, data);

			for (int j = 0; j < entidadeList.size(); j++)
			{
				System.out.print('.');
				gate.Annotation annEnt = entidadeList.get(j);

				long inicioEntidade = annEnt.getStartNode().getOffset();
				long fimEntidade = annEnt.getEndNode().getOffset();

				String entidadeText = doc.getContent().getContent(inicioEntidade, fimEntidade).toString();

				SimpleFeatureMap featureMap = annEnt.getFeatures();
				String particao = "";// extraiPartidoDoArquivo(nomeArquivo);
				// Se houver sinonimo, usa
				entidadeText = featureMap.get("Sinonimo") != null ? featureMap.get("Sinonimo").toString() : entidadeText;

				Entidade entidade = new Entidade(entidadeText, null, particao, inicioEntidade, fimEntidade, featureMap
						.get("kind").toString());

				entidadesEncontradasNome.put(entidade.entidade, entidade);// .add(entidade);

			}

			geraLigacoesEntreEntidades(strategy, entidadesEncontradasNome, portaria);

		} catch (Exception e)
		{
			log.warn("Exception: " + e.getMessage());
			e.printStackTrace();

		}

	}

	private String extraiPartidoDoArquivo(String nomeArquivo)
	{
		// 02-08-2012.NELSON MARQUEZELLI.PTB.SP.sessao[208.2.54.O]

		String aux = nomeArquivo.substring(nomeArquivo.indexOf('.') + 1); // NELSON MARQUEZELLI.PTB.SP.sessao[208.2.54.O]
		aux = aux.substring(aux.indexOf('.') + 1, aux.indexOf(".sessao"));
		return aux;
	}

	private void geraLigacoesEntreEntidades(RegistroLigacaoStrategy strategy, HashMap<String, Entidade> mapentidadesEncontradas,
			Portaria portaria)
	{

		Entidade[] entidadesEncontradas = mapentidadesEncontradas.values().toArray(new Entidade[0]);
		int countProcessed = 0;
		int countTotal = calculaLigacoes(entidadesEncontradas.length);

		log.warn("entidadesEncontradas na portaria: " + entidadesEncontradas.length + "| ligacoes: " + countTotal);

		if (countTotal > 10000)
		{
			log.warn("Total(" + countTotal + ") maior que 10000.");
			String novoNome = portaria.identificacaoPortaria + ".txt";

			File novo = new File(novoNome);

			StringBuilder sb = new StringBuilder();

			sb.append("Portaria: ");
			sb.append(portaria.identificacaoPortaria + "|" + portaria.nomeArquivo + "|" + portaria.textoPortaria.substring(0, 50));
			sb.append('\n');

			for (Entidade entidade : entidadesEncontradas)
			{
				sb.append('\n');
				sb.append(entidade.entidade + " | " + entidade.tipoEntidade);
			}
			sb.append('\n');
			sb.append("Fim");

			try
			{
				FileUtils.writeStringToFile(novo, sb.toString());
				log.warn("Arquivo gravado: " + novo.getAbsolutePath());
			} catch (IOException e)
			{
				log.warn("Exceção ao gravar portaria: " + portaria.identificacaoPortaria + "|" + portaria.nomeArquivo + "|"
						+ portaria.textoPortaria.substring(0, 50));
				log.warn(e.getMessage());
			}
		} else
		{

			for (int k = 0; k < entidadesEncontradas.length; k++)
			{
				for (int j = k + 1; j < entidadesEncontradas.length; j++)
				{

					Entidade entidadeA = entidadesEncontradas[k];
					Entidade entidadeB = entidadesEncontradas[j];

					// log.warn("L: " + entidadeA.entidade + " | " + entidadeB.entidade + " | " +
					// portaria.identificacaoPortaria);
					if (!entidadeA.entidade.equals(entidadeB.entidade))
					{
						strategy.registrar1Modo(entidadeA, entidadeB, portaria);
					}
					if ((countProcessed++ % 100) == 0)
						log.warn("Processadas ligacoes[" + entidadeA.tipoEntidade + "]: " + countProcessed + "|" + countTotal);

				}
			}
		}
	}

	private int calculaLigacoes(int size)
	{
		int count = 0;
		for (int k = 0; k < size; k++)
		{
			for (int j = k + 1; j < size; j++)
			{
				count++;
			}
		}
		return count;
	}

}
