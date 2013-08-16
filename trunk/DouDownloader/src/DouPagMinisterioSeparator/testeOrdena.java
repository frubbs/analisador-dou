package DouPagMinisterioSeparator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

public class testeOrdena
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		final String path = "C:\\Users\\Rafael\\Desktop\\MestradoApresentacao1\\PDF2604\\pdf\\TribunaldeContasdaUnio";

		File dir = new File(path);

		File[] currentfiles = dir.listFiles(new FilenameFilter()
		{
			@Override
			// arquivos da mesma data e jornal
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".pdf");
			}
		});

		// ordena os arquivos de acordo com a pagina

		// joga o conteudo de todos os pdf em um unico txt
		for (File currentFileChild : currentfiles)
		{
			System.out.println("convert: " + currentFileChild.getName());
			// sbFinalContent.append(DouPDFtoTextConverter.convertFile(currentFileChild));
		}

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

		System.out.println("\n\n\nORDENA xxxxxxxxxxxxx\n\n\n");
		for (File currentFileChild : currentfiles)
		{
			System.out.println("convert: " + currentFileChild.getName());
			// sbFinalContent.append(DouPDFtoTextConverter.convertFile(currentFileChild));
		}

	}
}
