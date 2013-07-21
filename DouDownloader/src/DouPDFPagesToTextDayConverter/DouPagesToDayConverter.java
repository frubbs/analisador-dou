package DouPDFPagesToTextDayConverter;

import gate.util.GateException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

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