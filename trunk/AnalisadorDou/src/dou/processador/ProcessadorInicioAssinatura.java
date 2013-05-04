package dou.processador;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.SimpleFeatureMap;

import java.util.ArrayList;
import java.util.List;

import dou.processador.registro.RegistroLigacaoStrategy;
import dou.util.Util;

public class ProcessadorInicioAssinatura implements ProcessadorAnotacoes
{

	@Override
	public void process(String docFile, Document doc, RegistroLigacaoStrategy strategy)
	{

		try
		{

			// Listas de anota��es detectadas pelo gate j� na ordem em que
			// aparecem no documento
			long annotStart = System.currentTimeMillis();
			AnnotationSet todasAnnots = doc.getAnnotations();
			List<gate.Annotation> inicioList = gate.Utils.inDocumentOrder(todasAnnots.get("Inicio"));
			List<gate.Annotation> assinaturaList = gate.Utils.inDocumentOrder(todasAnnots.get("Assinatura"));
			List<gate.Annotation> entidadeList = gate.Utils.inDocumentOrder(todasAnnots.get("EntidadeIdentificada"));
			long annotEnd = System.currentTimeMillis();
			System.out.println("Extrair as anota��es: " + (annotEnd - annotStart) + " ms");

			// Para cada inicio procura a proxima assinatura, que indica o fim
			// da portaria

			System.out.println("Quantidade portarias: " + inicioList.size());
			for (int i = 0; i < inicioList.size(); i++)
			{
				gate.Annotation annInicioPortaria = inicioList.get(i);
				// System.out.println("#### Processando : " + "port: " +
				// annIni.getStartNode().getOffset());

				long inicioPortaria = annInicioPortaria.getStartNode().getOffset();
				long fimPortaria = detectaFimPortaria(i, inicioList, assinaturaList);

				if (fimPortaria == 0)
				{
					System.out.println("FimPortaria == 0 ->  nao foi possivel determinar o proximo fim.");
					break; // inconsistencia. fim do processamento.
				}

				String identificacaoPortaria = Util.gerarIdentificacaoUnicaPortaria(annInicioPortaria, docFile);

				// se chegamos aqui temos uma portaria identificada. vamos
				// processar as entidades nela presentes.
				long entidadeStart = System.currentTimeMillis();

				System.out.println("Entidades size: " + entidadeList.size());

				List<gate.Annotation> entidadesEncontradas = new ArrayList<gate.Annotation>();
				// for (gate.Annotation annEnt : entidadeList) {
				for (int j = 0; j < entidadeList.size(); j++)
				{
					System.out.print('.');

					gate.Annotation annEntidade = entidadeList.get(j);

					long inicioEntidade = annEntidade.getStartNode().getOffset();

					if (inicioEntidade >= inicioPortaria)
					{
						if (inicioEntidade <= fimPortaria) // Se esta entre o inicio e o final, pertence � portaria
						{
							long fimEntidade = annEntidade.getEndNode().getOffset();

							String entidade = doc.getContent().getContent(inicioEntidade, fimEntidade).toString();

							SimpleFeatureMap featureMap = annEntidade.getFeatures();

							String textoPortaria = doc.getContent().getContent(inicioPortaria, fimPortaria).toString();

							String IdOrgao = featureMap.get("IdOrgao") != null ? featureMap.get("IdOrgao").toString() : "";

							strategy.registrar(new Ligacao(entidade, IdOrgao, identificacaoPortaria, textoPortaria,
									inicioPortaria, fimPortaria, // annFim.getEndNode().getOffset(),
									inicioEntidade, fimEntidade, featureMap.get("kind").toString(), docFile, Util
											.extrairDataDoNomeDoArquivo(docFile)));

							entidadesEncontradas.add(annEntidade);
						}// TODO se falhar aqui ja pode sair fora. a lista esta
							// ordenada.
						else
						{
							System.out.println("saind pois inicioEntidade (" + inicioEntidade + ") > fimportaria (" + fimPortaria
									+ ")");
							break;
						}
					}
				}

				System.out.println("Encontradas: " + entidadesEncontradas.size());
				for (gate.Annotation e : entidadesEncontradas)
				{
					entidadeList.remove(e);
				}

				long entidadeEnd = System.currentTimeMillis();
				System.out.println("entidades: " + (entidadeEnd - entidadeStart) + "ms");
				// System.in.read();
			}// fim. vamos para o proximo inicio de portaria

		} catch (Exception e)
		{
			e.printStackTrace();

		}

	}

	private long detectaFimPortaria(int inicioCorrente, List<Annotation> inicioList, List<Annotation> assinaturaList)
	{
		gate.Annotation annAssinaturaFim = assinaturaList.get(0); // A lista j� esta ordenada, pegamos o topo, ou seja, a proxima
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
	 * Esse metodo verifica se as anota��es de inicio de portaria e assinatura, que indica fim de portaria, estao em ordem. Se a
	 * assinatura estiver antes do inicio, pula pra proxima. isso nao deveria ocorrer, mas se acontecer, estamos preparados.
	 */
	private static gate.Annotation validaOrdemInicioAssinatura(List<gate.Annotation> assinaturaList, gate.Annotation annIni,
			gate.Annotation annFim)
	{

		long ordemStart = System.currentTimeMillis();

		while ((annFim.getStartNode().getOffset() < annIni.getEndNode().getOffset()) && !assinaturaList.isEmpty())
		{
			System.out.println("annFim menor que inicio: annFim.getStartNode().getOffset():" + annFim.getStartNode().getOffset());

			annFim = null;

			assinaturaList.remove(0);
			if (assinaturaList.isEmpty())
			{
				System.out.println("assinaturaList.isEmpty()2");
				break; // Se nao h� assinatura correspondente, h�
						// inconsistencia. fim do processamento.
			}
			annFim = assinaturaList.get(0);
		}

		long ordemEnd = System.currentTimeMillis();
		System.out.println("ValidaOrdemInicioAssinatura: " + (ordemEnd - ordemStart) + " ms");

		return annFim;
	}

}
