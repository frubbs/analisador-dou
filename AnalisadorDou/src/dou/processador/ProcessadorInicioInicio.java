package dou.processador;

import gate.AnnotationSet;
import gate.Document;
import gate.SimpleFeatureMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dou.processador.registro.RegistroLigacaoStrategy;
import dou.util.Util;

public class ProcessadorInicioInicio implements ProcessadorAnotacoes
{
	protected final Logger log = Logger.getLogger(ProcessadorInicioInicio.class);

	@Override
	public void process(File docFile, Document doc, RegistroLigacaoStrategy strategy)
	{

		try
		{

			// Listas de anota��es detectadas pelo gate j� na ordem em que
			// aparecem no documento
			long annotStart = System.currentTimeMillis();
			AnnotationSet todasAnnots = doc.getAnnotations();
			List<gate.Annotation> inicioList = gate.Utils.inDocumentOrder(todasAnnots.get("Inicio"));
			List<gate.Annotation> entidadeList = gate.Utils.inDocumentOrder(todasAnnots.get("EntidadeIdentificada"));
			long annotEnd = System.currentTimeMillis();
			// log.warn("Extrair as anota��es: " + (annotEnd - annotStart) + " ms");

			// Para cada inicio procura a proxima assinatura, que indica o fim
			// da portaria
			log.warn("Quantidade portarias: " + inicioList.size());
			// for (gate.Annotation annIni : inicioList) {
			for (int i = 0; i < inicioList.size(); i++)
			{
				gate.Annotation annIni = inicioList.get(i);
				long inicioPortaria = annIni.getStartNode().getOffset();

				SimpleFeatureMap fMap = annIni.getFeatures();
				String tipoPortaria = fMap.get("kind") != null ? fMap.get("kind").toString() : "";

				long fimPortaria;
				if (i == inicioList.size() - 1) // se estivermos no ultimo
												// inicio, o fim � o fim e nao a
												// proximo inicio
				{
					fimPortaria = doc.getContent().size().longValue();
				} else
				{
					fimPortaria = inicioList.get(i + 1).getStartNode().getOffset();
				}

				// log.warn("#### Processando : " + "port: " +
				// annIni.getStartNode().getOffset());

				// long ordemStart = System.currentTimeMillis();
				/*
				 * long ordemEnd = System.currentTimeMillis(); log.warn("Ordenar: " + (ordemEnd - ordemStart) + " ms");
				 */

				String identificacaoPortaria = Util.gerarIdentificacaoUnicaPortaria(annIni, docFile.getName());

				// se chegamos aqui temos uma portaria identificada. vamos
				// anotar as entidades nela presentes.
				long entidadeStart = System.currentTimeMillis();

				// long offsetIni = System.currentTimeMillis();

				// long offsetEnd = System.currentTimeMillis();
				// if ((offsetEnd - offsetIni)>0)
				// log.warn("offset iiniport: " + (offsetEnd -
				// offsetIni) + " ms");

				// offsetIni = System.currentTimeMillis();

				// offsetEnd = System.currentTimeMillis();
				// if ((offsetEnd - offsetIni)>0)
				// log.warn("offset fimport: " + (offsetEnd -
				// offsetIni) + " ms");

				log.warn("Entidades size: " + entidadeList.size());

				List<gate.Annotation> entidadesEncontradas = new ArrayList<gate.Annotation>();
				// for (gate.Annotation annEnt : entidadeList) {
				for (int j = 0; j < entidadeList.size(); j++)
				{
					System.out.print('.');

					gate.Annotation annEnt = entidadeList.get(j);

					// offsetIni = System.currentTimeMillis();

					long inicioEntidade = annEnt.getStartNode().getOffset();

					// offsetEnd = System.currentTimeMillis();
					// if ((offsetEnd - offsetIni)>0)
					// log.warn("offset ini ent: " + (offsetEnd -
					// offsetIni) + " ms");

					if (inicioEntidade >= inicioPortaria)
					{
						if (inicioEntidade < fimPortaria) // Se esta entre o
															// inicio e o final,
															// pertence �
															// portaria
						{

							// offsetIni = System.currentTimeMillis();

							long fimEntidade = annEnt.getEndNode().getOffset();

							// offsetEnd = System.currentTimeMillis();
							// if ((offsetEnd - offsetIni)>0)
							// log.warn("offset fim ent: " +
							// (offsetEnd - offsetIni) + " ms");

							// offsetIni = System.currentTimeMillis();
							String entidade = doc.getContent().getContent(inicioEntidade, fimEntidade).toString();
							// offsetEnd = System.currentTimeMillis();
							// if ((offsetEnd - offsetIni)>0)
							// log.warn("get content: " + (offsetEnd -
							// offsetIni) + " ms");

							SimpleFeatureMap featureMap = annEnt.getFeatures();

							long textStart = System.currentTimeMillis();
							String textoPortaria = doc.getContent().getContent(inicioPortaria, fimPortaria).toString();
							long textEnd = System.currentTimeMillis();
							// log.warn("ExtraiTextoPortaria (" + identificacaoPortaria + ") : " + (textEnd - textStart)
							// + " ms");

							int particao = featureMap.get("Particao") != null ? Integer.parseInt(featureMap.get("Particao")
									.toString()) : 0;

							// Se houver sinonimo, usa
							entidade = featureMap.get("Sinonimo") != null ? featureMap.get("Sinonimo").toString() : entidade;

							strategy.registrar(new Ligacao(entidade, null, particao, identificacaoPortaria, textoPortaria,
									inicioPortaria, fimPortaria, // annFim.getEndNode().getOffset(),
									inicioEntidade, fimEntidade, featureMap.get("kind").toString(), tipoPortaria, docFile
											.getAbsolutePath(), Util.extrairDataDoNomeDoArquivo(docFile.getName())));

							entidadesEncontradas.add(annEnt);
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
				log.warn("Encontradas: " + entidadesEncontradas.size());
				for (gate.Annotation e : entidadesEncontradas)
				{
					entidadeList.remove(e);
				}

				/*
				 * if(entidadesEncontradas.size() == 0) { String textoPortaria = doc
				 * .getContent().getContent(inicioPortaria,fimPortaria).toString (); log.warn("Nao encontrei entidades em : " +
				 * textoPortaria); }
				 */

				long entidadeEnd = System.currentTimeMillis();
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
