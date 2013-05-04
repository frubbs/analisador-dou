package exec;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;

import dou.DouProcessor;
import dou.pre.processador.Separador;

@Deprecated
public class ProcessDir {

	private static final String TEMP_DIR = "c:\\TempDir";
	private static ClassLoader cl;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Lista os arquivos a serem processados
		File[] arquivos = new File(args[1]).listFiles();

		File gapp = new File(args[0]);

		for (File file : arquivos) {

			File[] arquivosPequenos = new Separador().separa(file, TEMP_DIR);
			for (File arquivoPequeno : arquivosPequenos) {

				System.out.println(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
						.format(new Date())
						+ "###Process: "
						+ file.getName()
						+ "   size: "
						+ file.length()
						+ "###arquivoPequeno: "
						+ arquivoPequeno.getName()
						+ "   size: "
						+ arquivoPequeno.length());

				cl = new URLClassLoader(
						new URL[]{new URL(
								"file:///C:\\Users\\Rafael\\workspace\\Teste\\src\\dou\\DouProcessor.java")});

				Class<?> theClass = cl.loadClass("dou.DouProcessor");

				DouProcessor dp = (DouProcessor) theClass.newInstance();

				// dp.processFile(gapp, arquivoPequeno);

				dp = null;
				cl = null;

				System.gc();
			}

			System.out.println("");
		}

	}
}