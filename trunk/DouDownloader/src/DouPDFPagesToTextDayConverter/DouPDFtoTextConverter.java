package DouPDFPagesToTextDayConverter;

import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.corpora.DocumentContentImpl;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;

public class DouPDFtoTextConverter
{

	public static String convertFile(File file) throws MalformedURLException, GateException
	{
		String fileContent = getFileContent(file);

		String adjustedFileContent = adjustLineBreaks(fileContent);

		// System.out.println(adjustedFileContent);

		return adjustedFileContent;

	}

	private static String adjustLineBreaks(String fileContent)
	{

		return fileContent.replace("-\n", "");
	}

	private static String getFileContent(File file) throws GateException, MalformedURLException
	{
		Gate.init();

		Document doc = Factory.newDocument(file.toURL(), null);

		DocumentContentImpl docImpl = (DocumentContentImpl) doc.getContent();

		return docImpl.getOriginalContent();
	}
}
