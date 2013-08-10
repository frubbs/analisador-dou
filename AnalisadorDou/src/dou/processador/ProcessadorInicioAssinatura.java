package dou.processador;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.SimpleFeatureMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dou.processador.registro.RegistroLigacaoStrategy;
import dou.util.Util;

public class ProcessadorInicioAssinatura implements ProcessadorAnotacoes
{

	protected final Logger log = Logger.getLogger(ProcessadorInicioAssinatura.class);

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
			List<gate.Annotation> assinaturaList = gate.Utils.inDocumentOrder(todasAnnots.get("Assinatura"));
			List<gate.Annotation> entidadeList = gate.Utils.inDocumentOrder(todasAnnots.get("EntidadeIdentificada"));
			long annotEnd = System.currentTimeMillis();
			log.warn("Extrair as anotações: " + (annotEnd - annotStart) + " ms");

			// Para cada inicio procura a proxima assinatura, que indica o fim
			// da portaria

			log.warn("Quantidade portarias: " + inicioList.size());
			for (int i = 0; i < inicioList.size(); i++)
			{
				gate.Annotation annInicioPortaria = inicioList.get(i);
				// log.warn("#### Processando : " + "port: " +
				// annIni.getStartNode().getOffset());

				long inicioPortaria = annInicioPortaria.getStartNode().getOffset();
				long fimPortaria = detectaFimPortaria(i, inicioList, assinaturaList);

				SimpleFeatureMap fMap = annInicioPortaria.getFeatures();
				String tipoPortaria = fMap.get("kind") != null ? fMap.get("kind").toString() : "";

				if (fimPortaria == 0)
				{
					log.warn("FimPortaria == 0 ->  nao foi possivel determinar o proximo fim.");
					break; // inconsistencia. fim do processamento.
				}

				String identificacaoPortaria = Util.gerarIdentificacaoUnicaPortaria(annInicioPortaria, docFile.getName());

				// se chegamos aqui temos uma portaria identificada. vamos
				// processar as entidades nela presentes.
				long entidadeStart = System.currentTimeMillis();

				log.warn("Entidades size: " + entidadeList.size());

				List<gate.Annotation> entidadesEncontradas = new ArrayList<gate.Annotation>();
				// for (gate.Annotation annEnt : entidadeList) {
				for (int j = 0; j < entidadeList.size(); j++)
				{
					System.out.print('.');

					gate.Annotation annEntidade = entidadeList.get(j);

					long inicioEntidade = annEntidade.getStartNode().getOffset();

					if (inicioEntidade >= inicioPortaria)
					{
						if (inicioEntidade <= fimPortaria) // Se esta entre o inicio e o final, pertence à portaria
						{
							long fimEntidade = annEntidade.getEndNode().getOffset();

							String entidade = doc.getContent().getContent(inicioEntidade, fimEntidade).toString();

							SimpleFeatureMap featureMap = annEntidade.getFeatures();

							String textoPortaria = doc.getContent().getContent(inicioPortaria, fimPortaria).toString();

							String IdOrgao = featureMap.get("IdOrgao") != null ? featureMap.get("IdOrgao").toString() : "";

							int particao = featureMap.get("Particao") != null ? Integer.parseInt(featureMap.get("Particao")
									.toString()) : 0;

							String tipo = featureMap.get("kind") != null ? featureMap.get("kind").toString() : "";

							// Se houver sinonimo, usa
							entidade = featureMap.get("Sinonimo") != null ? featureMap.get("Sinonimo").toString() : entidade;

							strategy.registrar(new Ligacao(entidade, IdOrgao, particao, identificacaoPortaria, textoPortaria,
									inicioPortaria, fimPortaria, // annFim.getEndNode().getOffset(),
									inicioEntidade, fimEntidade, tipo, tipoPortaria, docFile.getName(), Util
											.extrairDataDoNomeDoArquivo(docFile.getName())));

							entidadesEncontradas.add(annEntidade);
						}// TODO se falhar aqui ja pode sair fora. a lista esta
							// ordenada.
						else
						{
							log.warn("saind pois inicioEntidade (" + inicioEntidade + ") > fimportaria (" + fimPortaria + ")");
							break;
						}
					}
				}

				log.warn("Encontradas: " + entidadesEncontradas.size());
				for (gate.Annotation e : entidadesEncontradas)
				{
					entidadeList.remove(e);
				}

				long entidadeEnd = System.currentTimeMillis();
				log.warn("entidades: " + (entidadeEnd - entidadeStart) + "ms");
				// System.in.read();
			}// fim. vamos para o proximo inicio de portaria

		} catch (Exception e)
		{
			log.warn("Exceção :" + e.getMessage());
			e.printStackTrace();

		}

	}

	private long detectaFimPortaria(int inicioCorrente, List<Annotation> inicioList, List<Annotation> assinaturaList)
	{
		gate.Annotation annAssinaturaFim = assinaturaList.get(0); // A lista já esta ordenada, pegamos o topo, ou seja, a proxima
																	// assinatura.

		Long inicioOffset = inicioList.get(inicioCorrente).getStartNode().getOffset();

		Long proximoInicioOffset = (inicioCorrente < inicioList.size()) ? inicioList.get(++inicioCorrente).getStartNode()
				.getOffset() : 0;

		annAssinaturaFim = validaOrdemInicioAssinatura(assinaturaList, inicioList.get(inicioCorrente), annAssinaturaFim);
		if (annAssinaturaFim == null)
			return 0;

		Long proximaAssinaturaOffset = annAssinaturaFim.getStartNode().getOffset();

		if (proximaAssinaturaOffset < proximoInicioOffset)
		{
			// Detectado caso mais comum, um inicio seguido de uma assinatura
			assinaturaList.remove(0); // remove pra fila andar
			return proximaAssinaturaOffset;
		} else
		{
			// Detectado 2 inicios seguidos, sem uma assinatura separando-os
			return proximoInicioOffset;

		}
	}

	/*
	 * Esse metodo verifica se as anotações de inicio de portaria e assinatura, que indica fim de portaria, estao em ordem. Se a
	 * assinatura estiver antes do inicio, pula pra proxima. isso nao deveria ocorrer, mas se acontecer, estamos preparados.
	 */
	private gate.Annotation validaOrdemInicioAssinatura(List<gate.Annotation> assinaturaList, gate.Annotation annIni,
			gate.Annotation annFim)
	{

		long ordemStart = System.currentTimeMillis();

		while ((annFim.getStartNode().getOffset() < annIni.getEndNode().getOffset()) && !assinaturaList.isEmpty())
		{
			log.warn("annFim menor que inicio: annFim.getStartNode().getOffset():" + annFim.getStartNode().getOffset());

			annFim = null;

			assinaturaList.remove(0);
			if (assinaturaList.isEmpty())
			{
				log.warn("assinaturaList.isEmpty()2");
				break; // Se nao há assinatura correspondente, há
						// inconsistencia. fim do processamento.
			}
			annFim = assinaturaList.get(0);
		}

		long ordemEnd = System.currentTimeMillis();
		log.warn("ValidaOrdemInicioAssinatura: " + (ordemEnd - ordemStart) + " ms");

		return annFim;
	}

}
