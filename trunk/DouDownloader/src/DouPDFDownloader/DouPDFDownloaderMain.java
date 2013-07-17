package DouPDFDownloader;

import java.io.File;

import Util.Util;

public class DouPDFDownloaderMain
{

	private static final int MAX_PAG = 500;

	private static String URL = "http://www.in.gov.br/servlet/INPDFViewer?jornal=@JOR@&pagina=@PAG@&data=@DATA@&captchafield=firistAccess";

	private static String FILENAME = "@PATH@\\Dou-@DATA@-@JOR@-@PAG@.pdf";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{

		if (args.length != 3)
			throw new Exception("Argumentos invalidos.");

		String iniDate = args[0];
		String endDate = args[1];
		String path = args[2];

		String data = Util.getnextDate(endDate, iniDate);

		while (data != "fim")
		{
			String url = URL.replace("@DATA@", data);
			String file = FILENAME.replace("@DATA@", data.replaceAll("/", "")).replace("@PATH@", path);

			for (int i = 1; i <= 3; i++)
			{

				String urlj = url.replace("@JOR@", String.valueOf(i));
				String filej = file.replace("@JOR@", String.valueOf(i));

				deleteFileIfExists(filej);

				for (int j = 1; j < MAX_PAG; j++)
				{

					String urljp = urlj.replace("@PAG@", String.valueOf(j));
					String filejp = filej.replace("@PAG@", String.valueOf(j));

					if (!PDFDownloader.DownloadPDF(urljp, filejp))
						break;

				}
				// cheguei ao final das paginas. bora pro proximo jornal
				System.out.println("cheguei ao final das paginas. bora pro proximo jornal");

			}
			data = Util.getnextDate(data, iniDate);
		}
		System.out.println("Fim do processamento!");
	}

	private static void deleteFileIfExists(String tempFile)
	{
		// Delete if tempFile exists
		File fileTemp = new File(tempFile);
		if (fileTemp.exists())
		{
			fileTemp.delete();
		}
		fileTemp = null;
	}
}
