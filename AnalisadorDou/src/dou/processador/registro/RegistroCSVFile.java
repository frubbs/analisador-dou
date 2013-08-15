package dou.processador.registro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.commons.lang.NotImplementedException;

import dou.processador.Ligacao;
import dou.processador1modo.Entidade;
import dou.processador1modo.Portaria;

public class RegistroCSVFile implements RegistroLigacaoStrategy
{

	String PATH = ".\\Ligacoes\\";
	String OUTPUT_FILE = PATH + "Ligacoes.txt";

	String SEPARATOR = "@";
	PrintWriter out;
	PrintWriter out2;

	public RegistroCSVFile()
	{

		try
		{

			new File(PATH).mkdir();

			out = new PrintWriter(new FileOutputStream(OUTPUT_FILE, true));
			String txt = "Entidade" + SEPARATOR + "IdENtidade" + SEPARATOR + "Particao" + SEPARATOR + "IdntificacaoPortaria"
					+ SEPARATOR + "iniPortOffset" + SEPARATOR + "fimPortOffset" + SEPARATOR + "iniEntidadeOffset" + SEPARATOR
					+ "fimEntidadeOffset" + SEPARATOR + "Tipo" + SEPARATOR + "Data";
			out.println(txt);
			out.flush();
			out.close();

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void registrar(Ligacao l)
	{
		try
		{

			out = new PrintWriter(new FileOutputStream(OUTPUT_FILE, true));
			String txt = l.entidade + SEPARATOR + l.idEntidade + SEPARATOR + l.particao + SEPARATOR + l.identificacaoPortaria
					+ SEPARATOR + l.inicioPortaria + SEPARATOR + l.fimPortaria + SEPARATOR + l.inicioEntidade + SEPARATOR
					+ l.fimEntidade + SEPARATOR + l.tipoEntidade + SEPARATOR + l.data;

			out.println(txt);
			out.flush();
			out.close();

			out2 = new PrintWriter(new FileOutputStream(PATH + l.identificacaoPortaria, true));
			String txt2 = l.textoPortaria;

			out2.println(txt2);
			out2.flush();
			out2.close();

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void registrar1Modo(Entidade entidadeA, Entidade entidadeB, Portaria portaria)
	{
		throw new NotImplementedException();

	}
}
