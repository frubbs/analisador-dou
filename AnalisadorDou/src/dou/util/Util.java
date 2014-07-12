package dou.util;

import gate.Annotation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class Util
{

	protected final static Logger log = Logger.getLogger(Util.class);

	public static Date extrairDataDoNomeDoArquivo(String name)
	{

		// Formato do nome: Dou-<ddMMyyyy>-<tipojornal>.txt exemplo: Dou23031983-2.txt

		int i = name.indexOf("Dou") + "Dou".length() + 1;
		int f = i + "ddMMyyyy".length();
		String data = name.substring(i, f);

		// log.warn("Data: " + data);

		Date date;
		try
		{
			date = new SimpleDateFormat("ddMMyyyy").parse(data);
		} catch (ParseException e)
		{
			log.warn("Nome de arquivo Invalido:" + name);
			date = null;
		}

		log.warn("data: " + new SimpleDateFormat("yyyy-MM-dd").format(date));

		return date;
		// return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String gerarIdentificacaoUnicaPortaria(Annotation annIni, String arquivo)
	{

		String somenteNumeros = arquivo.replaceAll("[^0-9]", "");
		String offsetInicial = annIni.getStartNode().getOffset().toString();
		String offsetFinal = annIni.getEndNode().getOffset().toString();

		String result = somenteNumeros + offsetInicial + offsetFinal;

		if (result.length() > 20)
			return result.substring(0, 19);
		else
			return result;
	}

	public static String gerarIdentificacaoUnicaDiscurso(String absolutePath)
	{
		String somenteNumeros = absolutePath.replaceAll("[^0-9]", "");

		if (somenteNumeros.length() > 20)
			return somenteNumeros.substring(0, 19);
		else
			return somenteNumeros;
	}

	public static Date extrairDataDoNomeDoArquivoDiscurso(String name)
	{
		// 03-09-2012.GONZAGA PATRIOTA (PRESIDENTE).PSB.PE.sessao[234.2.54.O].txt
		String data = name.substring(0, name.indexOf('.'));

		// log.warn("Data: " + data);

		Date date;
		try
		{
			date = new SimpleDateFormat("dd-MM-yyyy").parse(data);
		} catch (ParseException e)
		{
			log.warn("Nome de arquivo Invalido:" + name);
			date = null;
		}

		log.warn("data: " + new SimpleDateFormat("yyyy-MM-dd").format(date));

		return date;
		// return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

}
