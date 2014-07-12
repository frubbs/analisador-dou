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

			String SPsql = "EXEC [processaRegistro1Modov2] ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";

			PreparedStatement ps = conn.prepareStatement(SPsql);
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(2000);

			ps.setString(1, entidadeA.entidade);
			ps.setLong(2, entidadeA.inicioEntidade);
			ps.setLong(3, entidadeA.fimEntidade);
			ps.setString(4, entidadeA.tipoEntidade);
			ps.setString(5, entidadeA.particao);

			ps.setString(6, entidadeB.entidade);
			ps.setLong(7, entidadeB.inicioEntidade);
			ps.setLong(8, entidadeB.fimEntidade);
			ps.setString(9, entidadeB.tipoEntidade);
			ps.setString(10, entidadeB.particao);

			ps.setString(11, portaria.identificacaoPortaria);
			ps.setString(12, portaria.textoPortaria);
			ps.setLong(13, portaria.inicioPortaria);
			ps.setLong(14, portaria.fimPortaria);
			ps.setString(15, portaria.tipoPortaria);
			ps.setString(16, portaria.nomeArquivo);
			ps.setDate(17, new java.sql.Date(portaria.data.getTime()));

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
