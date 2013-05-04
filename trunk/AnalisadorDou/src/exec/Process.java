package exec;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import dou.DouProcessor;
import dou.processador.ProcessadorInicioAssinatura;
import dou.processador.registro.RegistroSiorgSQLServer;

public class Process
{

	private static final String TEMP_DIR = "c:\\TempDir";
	private static ClassLoader cl;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{

		// Lista os arquivos a serem processados
		File arquivo = new File(args[1]);
		File gapp = new File(args[0]);

		System.out.println("Argumento [0] : " + args[0]);
		System.out.println("Argumento [1] : " + args[1]);

		// File[] arquivosPequenos = new Separador().separa(arquivo, TEMP_DIR);

		// for (File arquivoPequeno : arquivosPequenos) {

		File arquivoPequeno = arquivo;

		System.out.println(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()) + "###Process: " + arquivo.getName()
				+ "   size: " + arquivo.length() + "###arquivoPequeno: " + arquivoPequeno.getName() + "   size: "
				+ arquivoPequeno.length());

		new DouProcessor().processFile(gapp, arquivoPequeno, new ProcessadorInicioAssinatura(), new RegistroSiorgSQLServer());

		System.out.println("");
		// }

	}
}