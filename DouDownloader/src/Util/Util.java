package Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util
{

	public static String FILEPATERN = "@FILEDATEJOR@-@PAG@.pdf";

	public static final int ESTIMATED_LAST_SECTION_PAGES = 200;

	public static final int FILENAME_DATE_JOR_SIZE = 14; // Tamanho do nome do arquivo até uma posiçao antes da pagina

	public static String getDate(String baseDate)
	{

		String dt = baseDate;// "2008-01-01"; // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(sdf.parse(dt));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			return "fim";
		}
		return sdf.format(c.getTime());
	}

	public static String getnextDate(String baseDate, String dtlimite)
	{

		String dt = baseDate;// "2008-01-01"; // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(sdf.parse(dt));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			return "fim";
		}

		c.add(Calendar.DATE, -1); // number of days to add

		Calendar climite = Calendar.getInstance();
		try
		{
			climite.setTime(sdf.parse(dtlimite));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			return "fim";
		}

		if (!c.before(climite))
			dt = sdf.format(c.getTime()); // dt is now the new date
		else
			dt = "fim";
		return dt;
	}
}
