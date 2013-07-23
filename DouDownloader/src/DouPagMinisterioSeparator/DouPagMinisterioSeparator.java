package DouPagMinisterioSeparator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import DouPDFPagesToTextDayConverter.DouPagesToDayConverter;

public class DouPagMinisterioSeparator
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		if (args.length != 1)
			throw new Exception("Argumentos invalidos.");

		String path = args[0];

		File dir = new File(path);

		File[] files = dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				// Busca todas as primeiras paginas, que possuem sumario
				return name.endsWith("-1.pdf");
			}
		});

		// para cada primeira pagina extrai o summario, move os arquivos e compila as paginas para texto
		for (final File firstPage : files)
		{
			System.out.println("file: " + firstPage.getName());
			HashMap<String, String> summ = readSummary(firstPage.getAbsolutePath());

			File[] currentfiles = dir.listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String name)
				{
					// Processa os arquivos do mesmo dia e jornal do 'firstPage' encontrado
					return name.endsWith(".pdf")
							&& name.substring(0, Util.Util.FILENAME_DATE_JOR_SIZE).equals(
									firstPage.getName().substring(0, Util.Util.FILENAME_DATE_JOR_SIZE));
				}
			});

			// move os arquivos
			for (File currentFileChild : currentfiles)
			{
				moveFileToMinisterioFolder(currentFileChild, summ.get(currentFileChild.getName().trim()));
			}

			// compila os textos dos ministerios em um unico arquivo do dia para cada jornal.
			DouPagesToDayConverter.convertBasedateFiles(firstPage);

		}
	}

	private static void moveFileToMinisterioFolder(File currentFileChild, String destDir)
	{

		if (destDir != null)
		{

			destDir = destDir.trim().replace(" ", "_");
			destDir = destDir.replaceAll("[^a-zA-Z0-9]+", "");

			File theDir = new File(currentFileChild.getParent() + "\\" + destDir.trim());
			if (!theDir.exists())
				if (!theDir.mkdir())
					return;

			try
			{
				System.out.println("Movendo '" + currentFileChild + "' para '" + destDir + "'");

				FileUtils.moveFileToDirectory(currentFileChild, theDir, true);

			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/* Dicionario de arquivo-ministerio. Dado o arquivo (jornal/data/pagina) fala qual ministerio (pasta) ele vai */
	private static HashMap<String, String> readSummary(String fileName)
	{

		HashMap<String, String> dic;
		dic = new HashMap<String, String>();

		try
		{
			File file = new File(fileName);
			String fileNamePattern = Util.Util.FILEPATERN.replace("@FILEDATEJOR@",
					file.getName().substring(0, Util.Util.FILENAME_DATE_JOR_SIZE));

			String sumario = SummaryExtractor.extractRawSummaryFromFile(file);

			Pattern p = Pattern.compile("(^|\\s)([0-9]+)($|\\s)");
			Matcher m = p.matcher(sumario);

			// Pattern p2 = Pattern.compile("(\\p{L}+\\s?)+");
			Pattern p2 = Pattern.compile("([\\p{L}|[\\,]]+\\s?)+");
			Matcher m2 = p2.matcher(sumario);

			String ministerio = "";
			String pagina = "";
			String ministerioAnterior = "";
			String paginaAnterior = "";

			do
			{
				ministerioAnterior = ministerio;
				paginaAnterior = pagina;
				ministerio = "";
				pagina = "";

				if (m.find())
				{
					// System.out.println(m.group(2));
					pagina = m.group(2);
				}

				if (m2.find())
				{
					// System.out.println(m2.group());
					ministerio = m2.group();
				}

				dic.put(fileNamePattern.replace("@PAG@", pagina), ministerio);

				if (!ministerioAnterior.equals("") && !paginaAnterior.equals("") && !ministerio.equals("") && !pagina.equals(""))
				{
					for (int i = Integer.parseInt(paginaAnterior); i < Integer.parseInt(pagina); i++)
					{
						String tempPagina = String.valueOf(i);
						dic.put(fileNamePattern.replace("@PAG@", tempPagina), ministerioAnterior);
					}

				}

				System.out.println("m: " + ministerio + "| mA: " + ministerioAnterior);
				System.out.println("p: " + pagina + "| pA: " + paginaAnterior);

				// System.out.println("min: " + ministerio + " | pag: " + pagina);

			} while (!ministerio.equals("") && !pagina.equals(""));

			// Preenche os arquivos da ultima sessao estimando o numero de paginas que pod ter. nao ha problema ter mais do que
			// existe de fato
			for (int i = Integer.parseInt(paginaAnterior); i < Integer.parseInt(paginaAnterior)
					+ Util.Util.ESTIMATED_LAST_SECTION_PAGES; i++)
			{
				String tempPagina = String.valueOf(i);
				dic.put(fileNamePattern.replace("@PAG@", tempPagina), ministerioAnterior);
			}

			// System.out.println(sumario);
			System.out.println("FIM");

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for (Entry<String, String> entry : dic.entrySet())
		// {
		// // System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
		// }

		return dic;

	}

}
