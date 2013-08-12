package commandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import NetworkGenerator.DatabaseProvider;
import NetworkGenerator.NetworkGenerator;

public class generator {

	private static StringBuilder redeGerada;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		menuInicial();

	}

	private static void menuInicial() {
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (menu1()) {
			case 1 :
				System.out.println("Bases locais:");
				File[] files = new DatabaseProvider()
						.getLocallyAvaliableDatabaseFiles();
				for (File file : files) {
					System.out.println(file.getName());
				}
				menuInicial();
				break;

			case 2 :
				System.out
						.println("Bases disponiveis no servidor (Baixe e salve no caminho configurado ["
								+ new DatabaseProvider().getBasePath()
								+ "]):\n");
				System.out.println(new DatabaseProvider()
						.getRemoteAvaliableDatabaseFiles());

				menuInicial();
				break;

			case 3 :
				System.out.println("Bases Locais: ");
				File[] files1 = new DatabaseProvider()
						.getLocallyAvaliableDatabaseFiles();
				for (File file : files1) {
					if (new DatabaseProvider().checkForUpdates(file)) {
						System.out.println(file.getName()
								+ " (nenhuma atualização disponivel)");
					} else {
						System.out.println(file.getName() + " (atualizado!)");
					}
				}
				System.out
						.println("Use a opçao 2 para ver os links para download dos arquivos atualizados.");
				break;
			case 4 :
				System.out.println("Gerar Redes.");
				gerarRedesMenu();
				break;
		}
	}

	private static void gerarRedesMenu() {

		String dbname = escolheBaseMenu();

		try {

			System.out.println("\nInforme o filtro para portarias.");
			System.out
					.println("Ex: (Defesa AND Segurança) OR (Nomeação OR Nomear).");
			String filtroPortarias = new BufferedReader(new InputStreamReader(
					System.in)).readLine();

			System.out.println("\nInforme o filtro para entidades.");
			System.out
					.println("Ex: (Universidade AND Federal) OR (Ministerio AND Educação).");
			String filtroEntidades = new BufferedReader(new InputStreamReader(
					System.in)).readLine();

			System.out.println("\nInforme o tipo de entidade.");
			System.out.println("0) Orgao");
			System.out.println("1) Nome");
			System.out.println("2) Todos");

			String[] tipo = {"Orgao", "Nome", ""};

			String tipoEntidade = tipo[getMenuOption()];

			System.out
					.println("Escreva o nome do arquivo de rede a ser gerado:");
			String novoNome = new BufferedReader(new InputStreamReader(
					System.in)).readLine();

			System.out
					.println("Aguarde. Gerando rede. Essa operação pode levar bastante tempo.");

			// Gera Rede

			Thread t = new Thread(new GeneratorRunner(dbname, filtroPortarias,
					filtroEntidades, tipoEntidade));
			t.start();

			String slash[] = {"|", "/", "-", "\\"};

			int i = 0;
			while (t.isAlive()) {
				t.join(500);
				if (t.isAlive()) {
					System.out.print("\r" + slash[i]);
					i = ++i >= slash.length ? 0 : i;
				} else {
					System.out.println("A thread morreu!");
				}
			}

			File novo = new File(novoNome);
			System.out.println("\nRede gerada! Gravando arquivo: "
					+ novo.getAbsolutePath());

			FileUtils.writeStringToFile(novo, redeGerada.toString());

			System.out.println("Arquivo gerado!");
			menuInicial();

		} catch (Exception e) {
			System.out.println("Erro ao executar o gerador: " + e.getMessage());
		}

	}
	private static class GeneratorRunner implements Runnable {
		String dbname;
		String filtroPortarias;
		String filtroEntidades;
		String tipoEntidade;

		public GeneratorRunner(String dbname, String filtroPortarias,
				String filtroEntidades, String tipoEntidade) {

			this.dbname = dbname;
			this.filtroPortarias = filtroPortarias;
			this.filtroEntidades = filtroEntidades;
			this.tipoEntidade = tipoEntidade;
		}

		public void run() {
			try {
				NetworkGenerator ng = new NetworkGenerator(dbname);
				redeGerada = ng.geraRede(filtroPortarias, filtroEntidades,
						tipoEntidade);
			} catch (Exception e) {
				System.out.println("Erro ao executar a thread do gerador: "
						+ e.getMessage());
			}
		}
	}
	private static String escolheBaseMenu() {

		File[] files = new DatabaseProvider()
				.getLocallyAvaliableDatabaseFiles();
		System.out.println("Escolha a base a ser usada:");
		for (int i = 0; i < files.length; i++) {
			System.out.println(i + ") " + files[i].getName());
		}

		return files[getMenuOption()].getAbsolutePath();
	}

	public static int getMenuOption() {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			return Integer.parseInt(br.readLine());
		} catch (Exception e) {
			System.out.println("Erro ao selecionar opção: " + e.getMessage());
			return 0;
		}
	}
	public static int menu1() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append("Gerador de redes sociais extraidas do Diário oficial da uniao. \n\n");
		sb.append("Digite: \n");
		sb.append("1) Listar todas as bases disponiveis locamlmente. \n");
		sb.append("2) Listar todas as bases disponiveis no servidor e o link para download. \n");
		sb.append("3) Verificar se há atualizações para as bases locais. \n");
		sb.append("4) Gerar Redes. \n");

		System.out.println(sb.toString());

		return getMenuOption();
	}

}
