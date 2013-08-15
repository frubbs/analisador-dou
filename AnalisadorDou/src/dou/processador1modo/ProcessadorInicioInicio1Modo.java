package dou.processador1modo;

import gate.AnnotationSet;
import gate.Document;
import gate.SimpleFeatureMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dou.processador.ProcessadorAnotacoes;
import dou.processador.registro.RegistroLigacaoStrategy;
import dou.util.Util;

public class ProcessadorInicioInicio1Modo implements ProcessadorAnotacoes
{
	protected final Logger log = Logger.getLogger(ProcessadorInicioInicio1Modo.class);

	@Override
	public void process(File docFile, Document doc, RegistroLigacaoStrategy strategy)
	{

		try
		{

			// Listas de anotações detectadas pelo gate já na ordem em que
			// aparecem no documento
			long annotStart = System.currentTimeMillis();
			AnnotationSet todasAnnots = doc.getAnnotations();
			List<gate.Annotation> inicioList = gate.Utils.inDocumentOrder(todasAnnots.get("Inicio"));
			List<gate.Annotation> entidadeList = gate.Utils.inDocumentOrder(todasAnnots.get("EntidadeIdentificada"));
			long annotEnd = System.currentTimeMillis();
			// log.warn("Extrair as anotações: " + (annotEnd - annotStart) + " ms");

			// Para cada inicio procura a proxima assinatura, que indica o fim
			// da portaria
			log.warn("Quantidade portarias: " + inicioList.size());
			// for (gate.Annotation annIni : inicioList) {
			for (int i = 0; i < inicioList.size(); i++)
			{
				gate.Annotation annIni = inicioList.get(i);
				long inicioPortaria = annIni.getStartNode().getOffset();

				SimpleFeatureMap fMap = annIni.getFeatures();

				long fimPortaria;
				if (i == inicioList.size() - 1) // se estivermos no ultimo
												// inicio, o fim é o fim e nao a
												// proximo inicio
				{
					fimPortaria = doc.getContent().size().longValue();
				} else
				{
					fimPortaria = inicioList.get(i + 1).getStartNode().getOffset();
				}

				// log.warn("#### Processando : " + "port: " +
				// annIni.getStartNode().getOffset());

				// se chegamos aqui temos uma portaria identificada. vamos
				// anotar as entidades nela presentes.
				long entidadeStart = System.currentTimeMillis();

				log.warn("Entidades size: " + entidadeList.size());

				List<gate.Annotation> entidadesEncontradasGate = new ArrayList<gate.Annotation>();

				List<Entidade> entidadesEncontradas = new ArrayList<Entidade>();

				String textoPortaria = doc.getContent().getContent(inicioPortaria, fimPortaria).toString();
				String identificacaoPortaria = Util.gerarIdentificacaoUnicaPortaria(annIni, docFile.getName());
				String tipoPortaria = fMap.get("kind") != null ? fMap.get("kind").toString() : "";
				String nomeArquivo = docFile.getAbsolutePath();
				Date data = Util.extrairDataDoNomeDoArquivo(docFile.getName());

				Portaria portaria = new Portaria(identificacaoPortaria, textoPortaria, inicioPortaria, fimPortaria, tipoPortaria,
						nomeArquivo, data);

				for (int j = 0; j < entidadeList.size(); j++)
				{
					System.out.print('.');

					gate.Annotation annEnt = entidadeList.get(j);

					long inicioEntidade = annEnt.getStartNode().getOffset();

					if (inicioEntidade >= inicioPortaria)
					{
						if (inicioEntidade < fimPortaria) // Se esta entre o
															// inicio e o final,
															// pertence à
															// portaria
						{

							long fimEntidade = annEnt.getEndNode().getOffset();

							String entidadeText = doc.getContent().getContent(inicioEntidade, fimEntidade).toString();

							SimpleFeatureMap featureMap = annEnt.getFeatures();

							int particao = featureMap.get("Particao") != null ? Integer.parseInt(featureMap.get("Particao")
									.toString()) : 0;

							// Se houver sinonimo, usa
							entidadeText = featureMap.get("Sinonimo") != null ? featureMap.get("Sinonimo").toString()
									: entidadeText;

							Entidade entidade = new Entidade(entidadeText, null, particao, inicioEntidade, fimEntidade,
									featureMap.get("kind").toString());

							entidadesEncontradas.add(entidade);
							entidadesEncontradasGate.add(annEnt);
						}// TODO se falhar aqui ja pode sair fora. a lista esta
							// ordenada.
						else
						{
							// log.warn("saind pois inicioEntidade (" + inicioEntidade + ") > fimportaria (" +
							// fimPortaria
							// + ")");
							break;
						}
					}
					/*
					 * else { log.warn("saind pois inicioEntidade (" + inicioEntidade + ") < inicioportaria (" + fimPortaria + ")"
					 * ); break; }
					 */

				}

				for (int k = 0; k < entidadesEncontradas.size(); k++)
				{
					for (int j = k + 1; j < entidadesEncontradas.size(); j++)
					{
						Entidade entidadeA = entidadesEncontradas.get(k);
						Entidade entidadeB = entidadesEncontradas.get(j);

						// log.warn("L: " + entidadeA.entidade + " | " + entidadeB.entidade + " | " +
						// portaria.identificacaoPortaria);
						if ((entidadeA.tipoEntidade.equals(entidadeB.tipoEntidade))
								&& (!entidadeA.entidade.equals(entidadeB.entidade)))
						{
							strategy.registrar1Modo(entidadeA, entidadeB, portaria);
						}
					}
				}

				log.warn("Encontradas: " + entidadesEncontradasGate.size());
				for (gate.Annotation e : entidadesEncontradasGate)
				{
					entidadeList.remove(e);
				}

				/*
				 * if(entidadesEncontradas.size() == 0) { String textoPortaria = doc
				 * .getContent().getContent(inicioPortaria,fimPortaria).toString (); log.warn("Nao encontrei entidades em : " +
				 * textoPortaria); }
				 */

				// long entidadeEnd = System.currentTimeMillis();
				// log.warn("entidades: " + (entidadeEnd - entidadeStart) + "ms");
				// System.in.read();
			}// fim. vamos para o proximo inicio de portaria

		} catch (Exception e)
		{
			log.warn("Exception: " + e.getMessage());
			e.printStackTrace();

		}

	}
}
