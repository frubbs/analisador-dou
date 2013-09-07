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

        public DataSet geraRede(String filtroPortarias, String filtroEntidades, String tiposEntidade, String tiposPortaria, BackgroundWorker _worker = null)
        {
            this.worker = _worker;


            StringBuilder sb = new StringBuilder();

            if (worker != null) worker.ReportProgress(0, "Preparando o banco....");

            preparaBanco(conn);

            if (worker != null) worker.ReportProgress(0, "Banco preparado! " + getAARowCount());
            if (worker != null) worker.ReportProgress(0, "Populando tabelas auxiliares..." + getAARowCount());

            populaTabelasAuxiliares(conn, filtroEntidades, filtroPortarias,
                    tiposEntidade, tiposPortaria);

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


            String sql = "   SELECT "+
                    //   "	        distinct cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY ee.Tempo DESC) as nvarchar) +']' as ligacao, ee.Tempo " +
                         "          v.IdOriginal, v2.IdOriginal, ee.tempo, ee.IdPortaria    " +
                         "   FROM " +
	                     "          EntidadeEntidadeFiltrada_aux3 ee  " + 
                         "   LEFT JOIN  " +
	                     "          Vertice v on ee.IDEntidadeA = v.IdOriginal " +
                         "   LEFT JOIN  " +
	                     "          Vertice v2 on ee.IDEntidadeB = v2.IdOriginal";


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

            String queryString =  " INSERT INTO " +
                                  "   Vertice " +
                                  " SELECT " +
                                  "   IdEntidade as IdOriginal, " +
                                  "	'\"\'   + Texto + \'\"                                      0.0000    0.0000    0.5000 \' AS Entidade " + 
                                  " FROM " +
                                  "     TbEntidade " +
                                  " WHERE " +
                                  "     IdEntidade in (SELECT IdEntidade FROM EntidadeFiltrada)";

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
                String filtroEntidades, String filtroPortarias, String tiposEntidade, String tiposPortaria)
        {



            String sqlEnt = "SELECT " +
	                        "       E.* " +
                            "INTO " +
	                        "       EntidadeFiltrada " +
                            "FROM " +
	                        "       TbEntidade E " +
                            " @WHERECLAUSE@	";

            executeCommand(conn, sqlEnt.Replace("@WHERECLAUSE@",
                    "WHERE TipoEntidade = '" + tiposEntidade + "'"));

            
            String sqlPortEnt = "	SELECT " +
                                "       EE.* " +
                                "   INTO " + 
	                            "       EntidadeEntidadeFiltrada_aux1 " + 
                                "   FROM " +
	                            "       TbEntidadeEntidade EE " +
                                "   JOIN " + 
	                            "       TbPortaria P ON P.IdPortaria = EE.IdPortaria " +
                                " @WHERECLAUSE@	";

/*            executeCommand(conn, sqlPortEnt.Replace("@WHERECLAUSE@",
                    preparaFiltro(filtroPortarias)));
*/
              executeCommand(conn, sqlPortEnt.Replace("@WHERECLAUSE@",
                    "WHERE P.TipoPortaria = '" + tiposPortaria + "'"));


            String sqlPortEnt2 = "	SELECT " + 
                                 "  	EE.* " +
                                 "  INTO " +
	                             "     EntidadeEntidadeFiltrada_aux2 " +
                                 "  FROM " + 
	                             "     EntidadeEntidadeFiltrada_aux1 EE " +
                                 "  WHERE " +
	                             "     EE.IdEntidadeA in (SELECT IdEntidade FROM EntidadeFiltrada) "; 

            executeCommand(conn, sqlPortEnt2);

            String sqlPortEnt3 = "	SELECT " +
	                             "      EE.* " +
                                 "  INTO " +
	                             "      EntidadeEntidadeFiltrada_aux3 " +
                                 "  FROM  " +
	                             "      EntidadeEntidadeFiltrada_aux2 EE " + 
                                 "  WHERE " +
	                             "      EE.IdEntidadeB in (SELECT IdEntidade FROM EntidadeFiltrada) ";

            executeCommand(conn, sqlPortEnt3);


            
            String sqlPortEnt4 = "	SELECT * INTO EntidadeEntidadeFiltrada " + 
                                 "  FROM " +
                                 "  ( " +
                                 "      SELECT IdEntidadeA as IdEntidade FROM 	EntidadeEntidadeFiltrada_aux3 " +
                                 "      UNION " +
                                 "      SELECT IdEntidadeB as IdEntidade FROM 	EntidadeEntidadeFiltrada_aux3 " +
                                 "  ) AS tmp ";

            executeCommand(conn, sqlPortEnt4);

            
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


        private void dropTableExists(SqlConnection conn, String table)
        {

            String comm = " IF OBJECT_ID('{0}') IS NOT NULL " +
                          " DROP TABLE {0} ";

            executeCommand(conn, string.Format(comm,table));

        }


        // Cria as tabelas temporarias
        public void preparaBanco(SqlConnection conn)
        {

            dropTableExists(conn, "EntidadeEntidadeFiltrada");
            dropTableExists(conn, "EntidadeEntidadeFiltrada_aux0");
            dropTableExists(conn, "EntidadeEntidadeFiltrada_aux1");
            dropTableExists(conn, "EntidadeEntidadeFiltrada_aux2");
            dropTableExists(conn, "EntidadeEntidadeFiltrada_aux3");
            dropTableExists(conn, "EntidadeFiltrada");
            dropTableExists(conn, "finalResult");
            dropTableExists(conn, "Vertice");


            String comm = " CREATE TABLE Vertice ( " +
                          "  ID INT IDENTITY(1,1), " +
	                      "  IdOriginal BIGINT, " +
	                      "  Entidade NVARCHAR(MAX)) ";

            executeCommand(conn, comm);

 

        }
    }
}
