package exec;

import java.io.File;

import dou.pre.processador.Separador;

public class Separa {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Lista os arquivos a serem processados

		File theDir = new File(args[0]);

		File[] arquivos = theDir.listFiles();

		String Temp_Dir = args[1];

		for (File file : arquivos) {
			System.out.println("Separando: " + file);
			new Separador().separaOuMove(file, Temp_Dir);
		}
		System.out.println("Fim separação");

	}

}
