package DouPagMinisterioSeparator;

import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;

import DouPDFPagesToTextDayConverter.DouPDFtoTextConverter;

public class testeGateConvert
{

	/**
	 * @param args
	 * @throws GateException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException, GateException
	{
		String filename = "C:\\Users\\Rafael\\Desktop\\MestradoApresentacao1\\Metodo2b\\pdf\\PresidnciadaRepblica\\Dou-26042013-2-1.pdf";
		File file = new File(filename);

		System.out.println(DouPDFtoTextConverter.convertFile(file));

	}

}
