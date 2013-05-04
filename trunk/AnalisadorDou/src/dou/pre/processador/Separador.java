package dou.pre.processador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Splitter;

public class Separador {

	private final String NEW_LINE_SEPARATOR = "@@DOUBLE_NW@@";
	private final long MAX_FILE_LENGTH = 1000000L * 2;
	private final int MAX_PIECES_FILE = 1000;

	/**
	 * Separa os arquivos em outros menores para evitar estouro de memoria
	 * 
	 * @param args
	 */
	public File[] separa(File original, String tempDir) {

		List<File> result = new ArrayList<File>();

		if (original.length() > MAX_FILE_LENGTH) {

			File theDir = new File(tempDir);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				// System.out.println("creating directory: " + directoryName);
				boolean res = theDir.mkdir();
				if (res) {
					// System.out.println("DIR created");
				}

			}

			String fileContent = getFileContent(original);

			Iterable<String> pieces = Splitter.on(NEW_LINE_SEPARATOR).split(
					fileContent);

			String newContent = " ";
			int counter = 1;
			for (String string : pieces) {

				newContent += " " + NEW_LINE_SEPARATOR + '\n' + " " + string;
				// if (counter % MAX_PIECES_FILE == 0) {
				if (newContent.length() > MAX_FILE_LENGTH / 2) {
					result.add(saveTempFile(newContent, counter,
							original.getName(), tempDir));
					newContent = "";
				}

				counter++;

			}
		} else {
			result.add(original);
		}

		File[] arrayresult = (File[]) result.toArray(new File[0]);
		return arrayresult;

	}

	/**
	 * Separa os arquivos em outros menores para evitar estouro de memoria
	 * 
	 * @param args
	 */
	public void separaOuMove(File original, String tempDir) {

		if (original.length() > MAX_FILE_LENGTH) {

			File theDir = new File(tempDir);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				// System.out.println("creating directory: " + directoryName);
				boolean res = theDir.mkdir();
				if (res) {
					// System.out.println("DIR created");
				}

			}

			String fileContent = getFileContent(original);

			Iterable<String> pieces = Splitter.on(NEW_LINE_SEPARATOR).split(
					fileContent);

			String newContent = " ";
			int counter = 1;
			for (String string : pieces) {

				newContent += " " + NEW_LINE_SEPARATOR + '\n' + " " + string;
				// if (counter % MAX_PIECES_FILE == 0) {
				if (newContent.length() > MAX_FILE_LENGTH / 2) {
					saveTempFile(newContent, counter, original.getName(),
							tempDir);
					newContent = "";
				}

				counter++;

			}
		} else {
			try {
				FileUtils.copyFileToDirectory(original, new File(tempDir));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// result.add(original);
		}

	}

	private File saveTempFile(String newContent, int counter, String name,
			String tempDir) {
		String filename = tempDir + "\\" + String.valueOf(counter) + name;

		File result = new File(filename);

		try {

			FileUtils.writeStringToFile(result, newContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	private String getFileContent(File file) {

		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));

			String thisLine = "";

			while ((thisLine = br.readLine()) != null) {

				sb.append(thisLine);
			}

			br.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();

	}
}
