package DouPDFPagesToTextDayConverter;

import gate.util.GateException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

public class DouPagesToDayConverter
{

	public static void main(String[] args) throws Exception
	{

		File input = new File("C:\\Users\\Rafael\\Desktop\\MestradoSandBox4\\pdf\\Dou-26042013-1-61.pdf");

		convertBasedateFiles(input);
	}

	/**
	 * @param args
	 * @throws GateException
	 * @throws IOException
	 */
	public static void convertBasedateFiles(final File firstPage) throws GateException, IOException
	{
		String path = firstPage.getParent();

		File dir = new File(path);

		File[] subDir = dir.listFiles();

		// todos os ministeros (sub diretrios)
		for (final File sub : subDir)
		{
			if (sub.isDirectory())
			{
				File[] currentfiles = sub.listFiles(new FilenameFilter()
				{
					@Override
					// arquivos da mesma data e jornal
					public boolean accept(File dir, String name)
					{
						return name.endsWith(".pdf")
								&& name.substring(0, Util.Util.FILENAME_DATE_JOR_SIZE).equals(
										firstPage.getName().substring(0, Util.Util.FILENAME_DATE_JOR_SIZE));
					}
				});

				if (currentfiles.length > 0)
				{
					try
					{
						// ordena os arquivos de acordo com a pagina
						Arrays.sort(currentfiles, new Comparator<File>()
						{
							public int compare(File o1, File o2)
							{
								int ext1 = o1.getName().indexOf(".pdf");
								int dash1 = o1.getName().lastIndexOf("-");
								int ext2 = o2.getName().indexOf(".pdf");
								int dash2 = o2.getName().lastIndexOf("-");

								String p1 = o1.getName().substring(dash1 + 1, ext1);
								String p2 = o2.getName().substring(dash2 + 1, ext2);

								return new Integer(Integer.parseInt(p1)).compareTo(new Integer(Integer.parseInt(p2)));
							}
						});
					} catch (Exception e)
					{
						System.out
								.println("*ERRO ****** Exceção ao ordenar lista de arquivos. Processamento continuará normalmente");
						e.printStackTrace();
					}
					// joga o conteudo de todos os pdf em um unico txt
					StringBuilder sbFinalContent = new StringBuilder();
					for (File currentFileChild : currentfiles)
					{
						// System.out.println("convert: " + currentFileChild.getName());
						sbFinalContent.append(DouPDFtoTextConverter.convertFile(currentFileChild));
					}

					String novoNome = sub.getAbsolutePath().replace("pdf", "txt") + "\\"
							+ firstPage.getName().substring(0, Util.Util.FILENAME_DATE_JOR_SIZE) + ".txt";

					File novo = new File(novoNome);

					System.out.println("gravar: " + novo.getAbsolutePath());

					FileUtils.writeStringToFile(novo, sbFinalContent.toString());
				}
			}
		}

	}
}