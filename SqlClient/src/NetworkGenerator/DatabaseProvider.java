package NetworkGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class DatabaseProvider {

	private String basePath = "\\";
	private boolean checkUpdatesOnline = false;
	private String updateUrl = "rafaelhenrique.net\\dou\\dbList.txt";

	public DatabaseProvider() {
		readConfig();
	}

	public DatabaseProvider(String basePath) {
		readConfig();
		this.basePath = basePath;
	}

	private void readConfig() {
		Properties prop = new Properties();

		try {
			// load a properties file
			prop.load(new FileInputStream("DatabaseProvider.properties"));

			// get the property value and print it out
			basePath = prop.getProperty("basePath");
			checkUpdatesOnline = Boolean.parseBoolean(prop
					.getProperty("checkUpdatesOnline"));
			updateUrl = prop.getProperty("updateUrl");

		} catch (Exception ex) {
			System.out.println("Erro ao carregar propriedades: "
					+ ex.getMessage());
		}

	}

	public String getRemoteAvaliableDatabaseFiles() {
		return getUpdateList();
	}

	public File[] getLocallyAvaliableDatabaseFiles() {

		try {
			// String basePath =
			// "C:\\Users\\Rafael\\workspace\\analisador-dou\\SqlClient\\database\\";
			File dir = new File(basePath);

			File[] files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".db");
				}
			});

			// TODO Checar se o arquivo db tem a estrutura de tabelas necessária

			if (files == null)
				throw new Exception("Nenhum arquivo db encontrado em "
						+ basePath);
			return files;
		} catch (Exception e) {
			System.out
					.println("Erro ao listar bases. verifique o arquivo DatabaseProvider.properties. exceção: "
							+ e.getMessage());
			return null;
		}

	}

	public boolean checkForUpdates(File file) {
		if (checkUpdatesOnline) {
			String body = getUpdateList();

			String fileName = getFileName(file.getName());
			Date fileDate = getFileDate(file.getName());

			String[] updateFiles = body.split("\n");

			for (String Info : updateFiles) {

				// System.out.println("Info: " + Info);

				String[] fileInfo = Info.split("=");

				String updateFileName = getFileName(fileInfo[0]);

				if (updateFileName.equals(fileName)) {
					Date updateFiledate = getFileDate(fileInfo[0]);
					// System.out.println("data con: " + updateFiledate);

					if (updateFiledate.after(fileDate)) {
						return true;
					}

				}
			}
		}
		return false;
	}

	private String getFileName(String fileName) {
		// dou.23031983.db
		return fileName.substring(0, fileName.indexOf("."));
	}

	private Date getFileDate(String name) {
		// dou.23031983.db
		String aux = name.substring(0, name.indexOf(".db"));
		String data = aux.substring(aux.lastIndexOf(".") + ".".length());

		Date date = null;
		try {
			date = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH).parse(data);
		} catch (ParseException e) {
			System.out.println("Erro ao converter data: " + e.getMessage());
		}
		return date;
	}

	private String getUpdateList() {
		URL url;
		String body = "";
		try {
			url = new URL(updateUrl);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			body = IOUtils.toString(in, encoding);
		} catch (Exception e) {
			System.out.println("Erro ao buscar update: " + e.getMessage());
		}
		return body;
	}

	public String getBasePath() {
		return basePath;

	}
}
