package dou.processador1modo.registro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import dou.processador.Ligacao;
import dou.processador.registro.RegistroLigacaoStrategy;
import dou.processador1modo.Entidade;
import dou.processador1modo.Portaria;

public class RegistroSQLServer1Modo implements RegistroLigacaoStrategy
{

	protected final Logger log = Logger.getLogger(RegistroSQLServer1Modo.class);

	Connection conn;

	public RegistroSQLServer1Modo()
	{
		// Cria conexao com o banco para guardar os dados apenas uma vez
		try
		{
			conn = DriverManager
					.getConnection("jdbc:jtds:sqlserver://RAFAEL-PC:1433/dou;instance=SQLEXPRESS;user=douuser;password=1234");
		} catch (SQLException e)
		{
			log.warn("Exceção :" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void registrar1Modo(Entidade entidadeA, Entidade entidadeB, Portaria portaria)
	{
		try
		{
			long insertStart = System.currentTimeMillis();

			String SPsql = "EXEC [processaRegistro1Modo] ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";

			PreparedStatement ps = conn.prepareStatement(SPsql);
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(2000);

			ps.setString(1, entidadeA.entidade);
			ps.setLong(2, entidadeA.inicioEntidade);
			ps.setLong(3, entidadeA.fimEntidade);
			ps.setString(4, entidadeA.tipoEntidade);

			ps.setString(5, entidadeB.entidade);
			ps.setLong(6, entidadeB.inicioEntidade);
			ps.setLong(7, entidadeB.fimEntidade);
			ps.setString(8, entidadeB.tipoEntidade);

			ps.setString(9, portaria.identificacaoPortaria);
			ps.setString(10, portaria.textoPortaria);
			ps.setLong(11, portaria.inicioPortaria);
			ps.setLong(12, portaria.fimPortaria);
			ps.setString(13, portaria.tipoPortaria);
			ps.setString(14, portaria.nomeArquivo);
			ps.setDate(15, new java.sql.Date(portaria.data.getTime()));

			ps.executeQuery();

			long insertEnd = System.currentTimeMillis();

			if ((insertEnd - insertStart) > 200)
				log.warn("Insert: " + (insertEnd - insertStart) + " ms");

		} catch (Exception e)
		{
			log.warn("Parametros: " + " | " + entidadeA.entidade + " | " + entidadeA.inicioEntidade + " | "
					+ entidadeA.fimEntidade + " | " + entidadeA.tipoEntidade + " | " + entidadeB.entidade + " | "
					+ entidadeB.inicioEntidade + " | " + entidadeB.fimEntidade + " | " + entidadeB.tipoEntidade + " | "
					+ portaria.identificacaoPortaria + " | " + "portaria.textoPortaria" + " | " + portaria.inicioPortaria + " | "
					+ portaria.fimPortaria + " | " + portaria.tipoPortaria + " | " + portaria.nomeArquivo);
			log.warn("Exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void registrar(Ligacao l)
	{
		throw new NotImplementedException();

	}

}
