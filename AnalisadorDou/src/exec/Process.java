package exec;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import dou.DouProcessor;
import dou.processador1modo.ProcessadorInicioInicio1Modo;
import dou.processador1modo.registro.RegistroSQLServer1Modo;

public class Process
{
	protected static final Logger log = Logger.getLogger(Process.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
	{

		try
		{
			// Lista os arquivos a serem processados
			File arquivo = new File(args[1]);
			File gapp = new File(args[0]);

			log.warn("Argumento [0] : " + args[0]);
			log.warn("Argumento [1] : " + args[1]);

			// File[] arquivosPequenos = new Separador().separa(arquivo, TEMP_DIR);

			// for (File arquivoPequeno : arquivosPequenos) {

			File arquivoPequeno = arquivo;

			log.warn(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()) + "IOPA ###Process: " + arquivo.getName()
					+ "   size: " + arquivo.length() + "###arquivoPequeno: " + arquivoPequeno.getName() + "   size: "
					+ arquivoPequeno.length());

			if (arquivoPequeno.length() > -5)
			{
				try
				{

					new DouProcessor().processFile(gapp, arquivoPequeno, new ProcessadorInicioInicio1Modo(),
							new RegistroSQLServer1Modo());

					File newFile = new File(arquivoPequeno.getAbsolutePath().replace("\\txt\\", "\\ProcessedTxt\\"));

					File theDir = new File(newFile.getParent());
					if (!theDir.exists())
						theDir.mkdir();

					log.warn("NewFile: " + newFile.getAbsolutePath());

					if (true)// arquivoPequeno.renameTo(newFile))
					{
						log.warn("File is moved successful!");
					} else
					{
						log.warn("File is failed to move! ok  " + newFile.getAbsolutePath());
					}

				} catch (Exception e)
				{
					log.warn("Exception: " + e.getMessage());
					log.warn("Exceção ao processar arquivo: " + arquivoPequeno.getName());

					try
					{

						if (arquivoPequeno.renameTo(new File("C:\\TempDirError2\\" + arquivoPequeno.getName())))
						{
							log.warn("File is moved successful!");
						} else
						{
							log.warn("File is failed to move! ex " + arquivoPequeno.getAbsolutePath());
						}

					} catch (Exception ex)
					{
						log.warn("Exception ao mover: " + ex.getMessage());

					}

				}
			} else
			{
				log.warn("Size <= 0 :  Nothing to do here...");
			}

			// }
		} catch (Exception e)
		{
			log.warn("Exception: " + e.getMessage());

		}

	}
}