package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import NetworkGenerator.NetworkGenerator;

public class NetworkGeneratorGui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		criaForm();

	}

	private static void criaForm() {
		Frame frm = new Frame("Extrator de redes");
		frm.setSize(500, 550);
		frm.setVisible(true);
		frm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		Panel p = new Panel();
		Panel p1 = new Panel();
		Panel p2 = new Panel();

		Label jFirstName = new Label("Filtro Entidades: ");
		final TextField lFirstName = new TextField(20);
		lFirstName
				.setText("(Instituto AND Federal) OR (Universidade AND Minas)");

		Label jLastName = new Label("Filtro Portarias [Separe por virgula]:");
		final TextField lLastName = new TextField(20);
		lLastName.setText("Defesa AND Segurança");

		final TextArea output = new TextArea(30, 50);

		p.setLayout(new GridLayout(3, 0));
		p.add(jFirstName);
		p.add(lFirstName);
		p.add(jLastName);
		p.add(lLastName);
		Button Submit = new Button("Extrair");

		Submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				output.append("Gerando Rede. \n");

				NetworkGenerator ng;

				try {
					String DB = "database\\dou.db";
					output.append("Iniciando gerador com banco local: [" + DB
							+ "].\n");
					ng = new NetworkGenerator(DB);
				} catch (FileNotFoundException e) {
					output.append("Erro ao iniciar gerador:" + e.getMessage());
					return;
				}

				output.append("Gerador iniciado com sucesso. Connection string: ["
						+ ng.getConnectionString() + "]\n");

				String TipoEntidade = "Orgao";
				String filtroEntidades = lFirstName.getText();
				String filtroPortarias = lLastName.getText();

				output.append("Gerando redes com: \n");
				output.append("Entidades: "
						+ ng.preparaFiltro(TipoEntidade, filtroEntidades)
						+ "\n");
				output.append("Portarias: " + ng.preparaFiltro(filtroPortarias)
						+ "\n");

				StringBuilder result = null;
				try {
					output.append("Aguarde, o processo pode levar algum tempo...\n");
					result = ng.geraRede(filtroEntidades, filtroPortarias,
							TipoEntidade);
				} catch (Exception e) {
					output.append("Erro ao gerar Rede:" + e.getMessage());
				}

				output.append("Rede gerada com sucesso. Gravando arquivo... \n");

				File out = new File("network\\rede.net");
				try {
					FileUtils.writeStringToFile(out, result.toString());
				} catch (IOException e) {
					output.append("Erro ao gravar arquivo:" + e.getMessage());
				}
				output.append("Fim do processamento. Arquivo gerado: ["
						+ out.getAbsolutePath() + "]");

			}
		});

		p.add(Submit);

		// p1.setLayout(new FlowLayout());
		p1.add(output);

		p2.add(p);
		p2.add(p1);

		frm.add(p2, BorderLayout.NORTH);
	}
}

class MyActionListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();
		if (s.equals("Exit")) {
			System.exit(0);
		} else if (s.equals("Bonjour")) {
			System.out.println("Good Morning");
		} else {
			System.out.println(s + " clicked");
		}
	}
}
