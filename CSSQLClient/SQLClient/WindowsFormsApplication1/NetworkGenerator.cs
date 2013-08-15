using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WindowsFormsApplication1
{
    class NetworkGenerator
    {
       
        private String ConnectionString = "Data Source=RAFAEL-PC\\SQLEXPRESS;Initial Catalog=dou;Integrated Security=True";
        private SqlConnection conn;
        private BackgroundWorker worker;
        public String statusMsg = "teste";

        public String getConnectionString()
        {
            return ConnectionString;
        }

        public NetworkGenerator()
        {

            conn = new SqlConnection();
            conn.ConnectionString = ConnectionString;
        }

        public DataSet geraRede(String filtroPortarias, String filtroEntidades, String tiposEntidade, BackgroundWorker _worker = null)
        {
            this.worker = _worker;


            StringBuilder sb = new StringBuilder();

            if (worker != null) worker.ReportProgress(0, "Preparando o banco....");

            preparaBanco(conn);

            if (worker != null) worker.ReportProgress(0, "Banco preparado! " + getAARowCount());
            if (worker != null) worker.ReportProgress(0, "Populando tabelas auxiliares..." + getAARowCount());

            populaTabelasAuxiliares(conn, filtroEntidades, filtroPortarias,
                    tiposEntidade);

            if (worker != null) worker.ReportProgress(0, "Tabelas auxiliares populadas!" + getAARowCount());
            if (worker != null) worker.ReportProgress(0, "Populando tabela de vertices...");

            populaTabeladeVertices(conn);

            if (worker != null) worker.ReportProgress(0, "Tabela de vertices populadas!" + getAARowCount());

            //sb.append(geraCabecalho(conn));

            //sb.append(geraListaVertices(conn));

            //sb.append("*Arcs\n");

            if (worker != null) worker.ReportProgress(0, "Gerando lista de arcos..." + getAARowCount());
            return geraListaArcos(conn);

       

        }


        public int getAARowCount()
        {
            
            
            using (conn) //With this, you don't have to close the connection
            {
                conn = new SqlConnection();
                conn.ConnectionString = ConnectionString; conn.Open();
                SqlDataAdapter adapter = new SqlDataAdapter("SELECT @@ROWCOUNT", conn);
                DataSet ds = new DataSet();
                adapter.SelectCommand.CommandTimeout = 0;
                adapter.Fill(ds, "Rowcount");
                
                DataTable dt = new DataTable();
                dt = ds.Tables["Rowcount"];

                return int.Parse(dt.Rows[0][0].ToString());
                
            }

        }

        private DataSet buscaRegistros(SqlConnection conn, String tabela, String sql)
        {
            if (worker != null) worker.ReportProgress(0, "Executando: " + sql);

            using (conn) //With this, you don't have to close the connection
            {
                conn = new SqlConnection();
                conn.ConnectionString = ConnectionString; conn.Open();
                SqlDataAdapter adapter = new SqlDataAdapter(sql, conn);
                DataSet ds = new DataSet();
                adapter.SelectCommand.CommandTimeout = 0;
                adapter.Fill(ds, tabela);
                return ds;
            }
            return null;
        }

        private DataSet geraListaArcos(SqlConnection conn)
        {
            /*
            String sql = " SELECT"
                    + "		cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(pe.Tempo as nvarchar) '1' + ']' AS Arcos"
                    + "	FROM" + "		PortariaEntidadeFiltrada pe" + "	LEFT JOIN"
                    + "		Vertice v on pe.IDEntidade = v.IdOriginal" + "	LEFT JOIN"
                    + "		Vertice v2 on pe.IdPortaria = v2.IdOriginal"
                    + "	ORDER BY pe.Tempo";
            */


            String sql = " SELECT " +
                         " distinct cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY pe.Tempo DESC) as nvarchar) +']' as ligacao, " +
                         " v.IdOriginal, " +
                         " v2.IdOriginal, " +
                         " pe.Tempo, "+
	                     " pe.IdPortaria " +
                        // " INTO #finalResult " +
                         " FROM " +
	                     "   PortariaEntidadeFiltrada pe " +
                         " JOIN " + 
	                     "   PortariaEntidadeFiltrada pe2 on (pe.idPortaria = pe2.idPortaria) AND (pe.IDEntidade > pe2.IDEntidade) " +
                         " LEFT JOIN " +
	                     "   Vertice v on pe.IDEntidade = v.IdOriginal " +
                         " LEFT JOIN " +
	                     "   Vertice v2 on pe2.IDEntidade = v2.IdOriginal ";


            return buscaRegistros(conn, "Arcos", sql);
        }

        private DataSet geraListaVertices(SqlConnection conn)
        {
            String sql = "SELECT"
                    + "				CAST(ID as NVARCHAR) ||  '  ' || Entidade  AS Vertices"
                    + "				FROM" + "				Vertice";

            return buscaRegistros(conn, "Vertices", sql);
        }

        private DataSet geraCabecalho(SqlConnection conn)
        {

            String sql = "SELECT"
                    + "		'*Vertices ' ||"
                    + "		("
                    + "			CAST((SELECT count(*) from TbEntidade WHERE IdEntidade in (SELECT IdEntidade FROM PortariaEntidadeFiltrada)) AS INTEGER)"
                    + "			+ CAST((SELECT count(*) from TbPortaria WHERE IdPortaria in (SELECT IdPortaria FROM PortariaEntidadeFiltrada)) AS INTEGER)"
                    + "		) ||"
                    + "		' ' ||"
                    + "		(SELECT count(*) from TbEntidade WHERE IdEntidade in (SELECT IdEntidade FROM PortariaEntidadeFiltrada)) "
                    + " AS Cabecalho";

            return buscaRegistros(conn, "Cabecalho", sql);
        }

        private void executeCommand(SqlConnection conn, String queryString)
        {
            if (worker != null) worker.ReportProgress(0, "executeCommand: " + queryString);

            using (conn)
            {
                conn = new SqlConnection();
                conn.ConnectionString = ConnectionString;
                SqlCommand command = new SqlCommand(queryString, conn);
                command.CommandTimeout = 0;
                command.Connection.Open();
                command.ExecuteNonQuery();
                command.Connection.Close();
            }
        }

        private void populaTabeladeVertices(SqlConnection conn)
        {

            String queryString = ("INSERT INTO Vertice (IdOriginal, Entidade)"
                    + "		SELECT"
                    + "			IdEntidade as IdOriginal,"
                    + "			\'\"\' + Texto + \'\"                                      0.0000    0.0000    0.5000 \' AS Entidade"
                    + "		FROM"
                    + "			TbEntidade"
                    + "		WHERE"
                    + "			IdEntidade in (SELECT IdEntidade FROM PortariaEntidadeFiltrada)");

            executeCommand(conn, queryString);


            queryString = ("INSERT INTO Vertice (IdOriginal, Entidade)"
            + "		SELECT"
            + "			IdPortaria as IdOriginal,"
            + "			 \'\"\' + cast(Identificacao as nvarchar) + \'\"                    0.0000    0.0000    0.5000 \' AS Entidade"
            + "		FROM"
            + "			TbPortaria"
            + "		WHERE"
            + "			IdPortaria in (SELECT IdPortaria FROM PortariaEntidadeFiltrada)");

            executeCommand(conn, queryString);

        }

        private void populaTabelasAuxiliares(SqlConnection conn,
                String filtroEntidades, String filtroPortarias, String tiposEntidade)
        {



            String sqlEnt = " INSERT INTO EntidadeFiltrada"
                    + " 	SELECT IdEntidade, Texto, TipoEntidade FROM TbEntidade"
                    + "	 @WHERECLAUSE@	";

            executeCommand(conn, sqlEnt.Replace("@WHERECLAUSE@",
                    preparaFiltro(tiposEntidade, filtroEntidades)));

            String sqlPortEnt = "	INSERT INTO PortariaEntidadeFiltrada"
                    + "			SELECT"
                    + "				TbPortariaEntidade.IdPortaria, TbPortariaEntidade.IdEntidade, TbPortariaEntidade.TipoLigacao, TbPortariaEntidade.Tempo "
                    + "			FROM"
                    + "				TbPortaria"
                    + "			JOIN"
                    + "				TbPortariaEntidade  ON TbPortaria.IdPortaria = TbPortariaEntidade.IdPortaria"
                    + "	 @WHERECLAUSE@	";

            executeCommand(conn, sqlPortEnt.Replace("@WHERECLAUSE@",
                    preparaFiltro(filtroPortarias)));
        }

        public String preparaFiltro(String filtroTexto)
        {
            String filtro = preparaFiltro("", filtroTexto);
            if (worker != null) worker.ReportProgress(0, "filtro: " + filtro);

            return filtro;

        }

        public String preparaFiltro(String tiposEntidade, String filtroTexto)
        {
            String FILTRO_TIPO_ENTIDADE = " TipoEntidade in (@TIPO@)";

            String WClause = "";

            // prepara tipo
            if (tiposEntidade.Trim().Length > 0)
            {
                String[] tokens = tiposEntidade.Split(',');
                foreach (String token in tokens)
                {
                    String currentToken = token.Trim();
                    if (currentToken.Length > 0)
                    {
                        if (tiposEntidade.IndexOf("\'" + currentToken + "\'") < 0) // evita
                            // problema
                            // com
                            // palavras
                            // repetidas
                            tiposEntidade = tiposEntidade.Replace(currentToken, "\'"
                                    + currentToken + "\'");
                    }
                }
                tiposEntidade = FILTRO_TIPO_ENTIDADE.Replace("@TIPO@",
                        tiposEntidade);

                WClause = "WHERE " + tiposEntidade;

            }

            // prepara filtro
            if (filtroTexto.Trim().Length > 0)
            {
                String[] separators = { "AND", "OR", "(", "((", "(((", "((((", "))))", ")))", "))", ")" };
                String[] tokens = filtroTexto.Split(separators, StringSplitOptions.RemoveEmptyEntries);

                foreach (String token in tokens)
                {
                    String currentToken = token.Trim();
                    if (currentToken.Length > 0)
                    {
                        if (filtroTexto.IndexOf("Texto like \'%" + currentToken + "%\'") < 0) // evita
                            // problema
                            // com
                            // palavras
                            // repetidas
                            filtroTexto = filtroTexto.Replace(currentToken,
                                    "Texto like \'%" + currentToken + "%\'");
                    }
                }

                if (WClause.Trim().Length > 0)
                {
                    WClause += "AND (" + filtroTexto + ")";
                }
                else
                {
                    WClause = "WHERE " + filtroTexto;
                }

            }
            return WClause;
        }

        // Cria as tabelas temporarias
        public void preparaBanco(SqlConnection conn)
        {
            String comm =  "IF OBJECT_ID('EntidadeFiltrada') IS NOT NULL " +
                                    "  DROP TABLE EntidadeFiltrada ";

            executeCommand(conn, comm);

            executeCommand(conn, " CREATE TABLE EntidadeFiltrada" + "		("
                    + "			IdEntidade INTEGER NOT NULL," + "			Texto NVARCHAR(100),"
                    + "			TipoEntidade NVARCHAR(10),"
                    + "		PRIMARY KEY (IdEntidade )," + "		UNIQUE (IdEntidade )) ");

            executeCommand(conn, "IF OBJECT_ID('PortariaEntidadeFiltrada') IS NOT NULL " +
                                    "  DROP TABLE PortariaEntidadeFiltrada ");
            executeCommand(conn, " CREATE TABLE PortariaEntidadeFiltrada"
                    + "		(" + "			IdPortaria INTEGER NOT NULL,"
                    + "			IdEntidade INTEGER NOT NULL," + "			TipoLigacao INTEGER,"
                    + "			Tempo datetime" + "		)");

            executeCommand(conn, " IF OBJECT_ID('Vertice') IS NOT NULL " +
                                    "  DROP TABLE Vertice ");

            comm = " CREATE TABLE Vertice  ( " +
 			        " ID INTEGER IDENTITY, " + 	
			        " IdOriginal BIGINT," +
			        " Entidade NVARCHAR(300) " +
                    " )"; 


            executeCommand(conn, comm);

        }
    }
}
