package dou.processador.registro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import dou.processador.Ligacao;

public class RegistroSQLServer implements RegistroLigacaoStrategy
{

	Connection conn;

	public RegistroSQLServer()
	{
		// Cria conexao com o banco para guardar os dados apenas uma vez
		try
		{
			conn = DriverManager
					.getConnection("jdbc:jtds:sqlserver://RAFAEL-PC:1433/dou;instance=SQLEXPRESS;user=douuser;password=1234");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void registrar(Ligacao l)
	{
		// if (l.tipoEntidade == "Orgao")
		inserirNoBanco(l.entidade, l.identificacaoPortaria, l.textoPortaria, l.inicioPortaria, l.fimPortaria, l.inicioEntidade,
				l.fimEntidade, l.tipoEntidade, l.tipoPortaria, l.nomeArquivo, l.data);
	}

	private void inserirNoBanco(String entidade, String identificacaoPortaria, String textoPortaria, Long offsetPortIni,
			Long offsetPortEnd, Long offsetEntIni, Long offsetEntEnd, String tipo, String tipoPortaria, String nomeArquivo,
			Date data)
	{

		try
		{

			long insertStart = System.currentTimeMillis();

			String SPsql = "EXEC [processaRegistro] ?,?,?,?,?,?,?,?,?,?,?";

			PreparedStatement ps = conn.prepareStatement(SPsql);
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(1000);

			ps.setString(1, entidade);
			ps.setString(2, identificacaoPortaria);
			ps.setString(3, textoPortaria);
			ps.setLong(4, offsetPortIni);
			ps.setLong(5, offsetPortEnd);
			ps.setLong(6, offsetEntIni);
			ps.setLong(7, offsetEntEnd);
			ps.setString(8, tipo);
			ps.setString(9, tipoPortaria);
			ps.setString(10, nomeArquivo);
			ps.setDate(11, new java.sql.Date(data.getTime()));

			ps.executeQuery();

			long insertEnd = System.currentTimeMillis();

			if ((insertEnd - insertStart) > 200)
				System.out.println("Insert: " + (insertEnd - insertStart) + " ms");

		} catch (Exception e)
		{
			System.out.println("Parametros: " + entidade + ", " + identificacaoPortaria + ", " + "textoPortaria" + ", "
					+ offsetPortIni + ", " + offsetPortEnd + ", " + offsetEntIni + ", " + offsetEntEnd + ", " + tipo + ", "
					+ tipoPortaria + ", " + data);
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
