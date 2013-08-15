using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public DataSet ds = null;
        public Form1()
        {
            InitializeComponent();
            backgroundWorker1.WorkerReportsProgress = true;
            backgroundWorker1.WorkerSupportsCancellation = true;
        }
      
        private void startAsyncButton_Click(object sender, EventArgs e)
        {
            if (backgroundWorker1.IsBusy != true)
            {
                // Start the asynchronous operation.
                backgroundWorker1.RunWorkerAsync();
            }
        }

        private void cancelAsyncButton_Click(object sender, EventArgs e)
        {
            if (backgroundWorker1.WorkerSupportsCancellation == true)
            {
                // Cancel the asynchronous operation.
                backgroundWorker1.CancelAsync();
            }
        }

        // This event handler is where the time-consuming work is done. 
        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;
             

                if (worker.CancellationPending == true)
                {
                    e.Cancel = true;
                    return;
                }
                else
                {
                    NetworkGenerator ng = new NetworkGenerator();
                    
                    String filtroEntidades = TxtFiltroEntidades.Text;
                    String tipoEntidades = TxtTipoEntidade.Text;
                    String filtroPortarias = TxtFiltroPortarias.Text;

                    ds = ds = ng.geraRede(filtroPortarias, filtroEntidades, tipoEntidades, worker); 

                }

                

            


        }

        // This event handler updates the progress. 
        private void backgroundWorker1_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            textBox1.Text = textBox1.Text + Environment.NewLine + Environment.NewLine + DateTime.Now.ToString() + " " + (e.UserState.ToString());
            textBox1.ScrollToCaret();
            textBox1.Refresh();
        }

        // This event handler deals with the results of the background operation. 
        private void backgroundWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if (e.Cancelled == true)
            {
                textBox1.Text = "Canceled!";
            }
            else if (e.Error != null)
            {
                textBox1.Text = "Error: " + e.Error.Message;
            }
            else
            {
                textBox1.Text += Environment.NewLine + DateTime.Now.ToString() + " Done!";
                textBox1.ScrollToCaret();
                textBox1.Refresh();
                GVRelacionamentos.DataSource = ds;
                GVRelacionamentos.DataMember = "Arcos";
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            SqlConnection connection = new SqlConnection();
            connection.ConnectionString = "Data Source=RAFAEL-PC\\SQLEXPRESS;Initial Catalog=dou;Integrated Security=True";

            if (backgroundWorker1.IsBusy != true)
            {
                backgroundWorker1.RunWorkerAsync();
            }
        }

        private void GVRelacionamentos_CellClick(object sender, DataGridViewCellEventArgs e)
        {

            String IdE1 = GVRelacionamentos.Rows[e.RowIndex].Cells[1].Value.ToString();
            String IdE2 = GVRelacionamentos.Rows[e.RowIndex].Cells[2].Value.ToString();
            String IdP = GVRelacionamentos.Rows[e.RowIndex].Cells[4].Value.ToString();


            String sql = string.Format("SELECT Texto, TipoEntidade FROM TbEntidade where IdEntidade in ({0},{1})", IdE1, IdE2);
            DataSet dsDetalhes = buscaRegistros("dummy", sql);

            GVENtidadePortariaDetalhes.DataSource = dsDetalhes;
            GVENtidadePortariaDetalhes.DataMember = "dummy";


            sql = string.Format("SELECT Texto, TipoPortaria, Data, NomeArquivo  FROM TbPortaria where IdPortaria = {0}", IdP);
            DataSet dsDetalhesP = buscaRegistros("dummy", sql);

            GVPortaria.DataSource = dsDetalhesP;
            GVPortaria.DataMember = "dummy";


            DataTable dt = new DataTable();
            dt = dsDetalhesP.Tables[0];

            TxtPortaria.Text = dt.Rows[0][0].ToString();

        }

        private DataSet buscaRegistros(String tabela, String sql)
        {

            using (SqlConnection conn = new SqlConnection()) //With this, you don't have to close the connection
            {

                conn.ConnectionString = "Data Source=RAFAEL-PC\\SQLEXPRESS;Initial Catalog=dou;Integrated Security=True"; 
                conn.Open();
                SqlDataAdapter adapter = new SqlDataAdapter(sql, conn);
                DataSet ds = new DataSet();
                adapter.SelectCommand.CommandTimeout = 0;
                adapter.Fill(ds, tabela);

                return ds;
            }
        }

    }
}
