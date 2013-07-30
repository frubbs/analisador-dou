package dou.processador;

import gate.AnnotationSet;
import gate.Document;
import gate.SimpleFeatureMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dou.processador.registro.RegistroLigacaoStrategy;
import dou.util.Util;

public class ProcessadorInicioInicio implements ProcessadorAnotacoes
{

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
			// System.out.println("Extrair as anotações: " + (annotEnd - annotStart) + " ms");

			// Para cada inicio procura a proxima assinatura, que indica o fim
			// da portaria
			System.out.println("Quantidade portarias: " + inicioList.size());
			// for (gate.Annotation annIni : inicioList) {
			for (int i = 0; i < inicioList.size(); i++)
			{
				gate.Annotation annIni = inicioList.get(i);
				long inicioPortaria = annIni.getStartNode().getOffset();

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

				// System.out.println("#### Processando : " + "port: " +
				// annIni.getStartNode().getOffset());

				// long ordemStart = System.currentTimeMillis();
				/*
				 * long ordemEnd = System.currentTimeMillis(); System.out.println("Ordenar: " + (ordemEnd - ordemStart) + " ms");
				 */

				String identificacaoPortaria = Util.gerarIdentificacaoUnicaPortaria(annIni, docFile.getName());

				// se chegamos aqui temos uma portaria identificada. vamos
				// anotar as entidades nela presentes.
				long entidadeStart = System.currentTimeMillis();

				// long offsetIni = System.currentTimeMillis();

				// long offsetEnd = System.currentTimeMillis();
				// if ((offsetEnd - offsetIni)>0)
				// System.out.println("offset iiniport: " + (offsetEnd -
				// offsetIni) + " ms");

				// offsetIni = System.currentTimeMillis();

				// offsetEnd = System.currentTimeMillis();
				// if ((offsetEnd - offsetIni)>0)
				// System.out.println("offset fimport: " + (offsetEnd -
				// offsetIni) + " ms");

				System.out.println("Entidades size: " + entidadeList.size());

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
					// System.out.println("offset ini ent: " + (offsetEnd -
					// offsetIni) + " ms");

					if (inicioEntidade >= inicioPortaria)
					{
						if (inicioEntidade <= fimPortaria) // Se esta entre o
															// inicio e o final,
															// pertence à
															// portaria
						{

							// offsetIni = System.currentTimeMillis();

							long fimEntidade = annEnt.getEndNode().getOffset();

							// offsetEnd = System.currentTimeMillis();
							// if ((offsetEnd - offsetIni)>0)
							// System.out.println("offset fim ent: " +
							// (offsetEnd - offsetIni) + " ms");

							// offsetIni = System.currentTimeMillis();
							String entidade = doc.getContent().getContent(inicioEntidade, fimEntidade).toString();
							// offsetEnd = System.currentTimeMillis();
							// if ((offsetEnd - offsetIni)>0)
							// System.out.println("get content: " + (offsetEnd -
							// offsetIni) + " ms");

							SimpleFeatureMap featureMap = annEnt.getFeatures();

							long textStart = System.currentTimeMillis();
							String textoPortaria = doc.getContent().getContent(inicioPortaria, fimPortaria).toString();
							long textEnd = System.currentTimeMillis();
							// System.out.println("ExtraiTextoPortaria (" + identificacaoPortaria + ") : " + (textEnd - textStart)
							// + " ms");

							int particao = featureMap.get("Particao") != null ? Integer.parseInt(featureMap.get("Particao")
									.toString()) : 0;

							strategy.registrar(new Ligacao(entidade, null, particao, identificacaoPortaria, textoPortaria,
									inicioPortaria, fimPortaria, // annFim.getEndNode().getOffset(),
									inicioEntidade, fimEntidade, featureMap.get("kind").toString(), // TODO
																									// resolver
																									// o
																									// kind
									docFile.getAbsolutePath(), Util.extrairDataDoNomeDoArquivo(docFile.getName())));

							entidadesEncontradas.add(annEnt);
						}// TODO se falhar aqui ja pode sair fora. a lista esta
							// ordenada.
						else
						{
							// System.out.println("saind pois inicioEntidade (" + inicioEntidade + ") > fimportaria (" +
							// fimPortaria
							// + ")");
							break;
						}
					}
					/*
					 * else { System.out.println("saind pois inicioEntidade (" + inicioEntidade + ") < inicioportaria (" +
					 * fimPortaria + ")" ); break; }
					 */

				}
				System.out.println("Encontradas: " + entidadesEncontradas.size());
				for (gate.Annotation e : entidadesEncontradas)
				{
					entidadeList.remove(e);
				}

				/*
				 * if(entidadesEncontradas.size() == 0) { String textoPortaria = doc
				 * .getContent().getContent(inicioPortaria,fimPortaria).toString ();
				 * System.out.println("Nao encontrei entidades em : " + textoPortaria); }
				 */

				long entidadeEnd = System.currentTimeMillis();
				// System.out.println("entidades: " + (entidadeEnd - entidadeStart) + "ms");
				// System.in.read();
			}// fim. vamos para o proximo inicio de portaria

		} catch (Exception e)
		{
			e.printStackTrace();

		}

	}

}
