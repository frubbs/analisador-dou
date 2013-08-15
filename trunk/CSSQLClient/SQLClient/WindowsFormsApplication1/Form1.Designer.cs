namespace WindowsFormsApplication1
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.douDataSet = new WindowsFormsApplication1.douDataSet();
            this.douDataSetBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.button1 = new System.Windows.Forms.Button();
            this.GVRelacionamentos = new System.Windows.Forms.DataGridView();
            this.TxtFiltroEntidades = new System.Windows.Forms.TextBox();
            this.TxtFiltroPortarias = new System.Windows.Forms.TextBox();
            this.TxtTipoEntidade = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.backgroundWorker1 = new System.ComponentModel.BackgroundWorker();
            this.GVENtidadePortariaDetalhes = new System.Windows.Forms.DataGridView();
            this.GVPortaria = new System.Windows.Forms.DataGridView();
            this.TxtPortaria = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.douDataSet)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.douDataSetBindingSource)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.GVRelacionamentos)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.GVENtidadePortariaDetalhes)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.GVPortaria)).BeginInit();
            this.SuspendLayout();
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(12, 428);
            this.textBox1.Multiline = true;
            this.textBox1.Name = "textBox1";
            this.textBox1.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.textBox1.Size = new System.Drawing.Size(780, 201);
            this.textBox1.TabIndex = 0;
            // 
            // douDataSet
            // 
            this.douDataSet.DataSetName = "douDataSet";
            this.douDataSet.SchemaSerializationMode = System.Data.SchemaSerializationMode.IncludeSchema;
            // 
            // douDataSetBindingSource
            // 
            this.douDataSetBindingSource.DataSource = this.douDataSet;
            this.douDataSetBindingSource.Position = 0;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(364, 139);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 2;
            this.button1.Text = "Gerar!";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // GVRelacionamentos
            // 
            this.GVRelacionamentos.AllowUserToAddRows = false;
            this.GVRelacionamentos.AllowUserToDeleteRows = false;
            this.GVRelacionamentos.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.GVRelacionamentos.Location = new System.Drawing.Point(457, 25);
            this.GVRelacionamentos.Name = "GVRelacionamentos";
            this.GVRelacionamentos.ReadOnly = true;
            this.GVRelacionamentos.Size = new System.Drawing.Size(363, 397);
            this.GVRelacionamentos.TabIndex = 3;
            this.GVRelacionamentos.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.GVRelacionamentos_CellClick);
            // 
            // TxtFiltroEntidades
            // 
            this.TxtFiltroEntidades.Location = new System.Drawing.Point(8, 28);
            this.TxtFiltroEntidades.Name = "TxtFiltroEntidades";
            this.TxtFiltroEntidades.Size = new System.Drawing.Size(431, 20);
            this.TxtFiltroEntidades.TabIndex = 4;
            this.TxtFiltroEntidades.Text = "(Universidade AND Federal)";
            // 
            // TxtFiltroPortarias
            // 
            this.TxtFiltroPortarias.Location = new System.Drawing.Point(8, 113);
            this.TxtFiltroPortarias.Name = "TxtFiltroPortarias";
            this.TxtFiltroPortarias.Size = new System.Drawing.Size(431, 20);
            this.TxtFiltroPortarias.TabIndex = 5;
            this.TxtFiltroPortarias.Text = "(Seleção OR Vestibular OR Enem OR Sisu)";
            // 
            // TxtTipoEntidade
            // 
            this.TxtTipoEntidade.Location = new System.Drawing.Point(8, 70);
            this.TxtTipoEntidade.Name = "TxtTipoEntidade";
            this.TxtTipoEntidade.Size = new System.Drawing.Size(79, 20);
            this.TxtTipoEntidade.TabIndex = 6;
            this.TxtTipoEntidade.Text = "Orgao";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(8, 12);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(79, 13);
            this.label1.TabIndex = 7;
            this.label1.Text = "Filtro Entidades";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(8, 54);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(78, 13);
            this.label2.TabIndex = 8;
            this.label2.Text = "Tipo Entidades";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(9, 97);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(73, 13);
            this.label3.TabIndex = 9;
            this.label3.Text = "Filtro Portarias";
            // 
            // backgroundWorker1
            // 
            this.backgroundWorker1.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker1_DoWork);
            this.backgroundWorker1.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorker1_ProgressChanged);
            this.backgroundWorker1.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorker1_RunWorkerCompleted);
            // 
            // GVENtidadePortariaDetalhes
            // 
            this.GVENtidadePortariaDetalhes.AllowUserToAddRows = false;
            this.GVENtidadePortariaDetalhes.AllowUserToDeleteRows = false;
            this.GVENtidadePortariaDetalhes.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.GVENtidadePortariaDetalhes.Location = new System.Drawing.Point(12, 301);
            this.GVENtidadePortariaDetalhes.Name = "GVENtidadePortariaDetalhes";
            this.GVENtidadePortariaDetalhes.ReadOnly = true;
            this.GVENtidadePortariaDetalhes.Size = new System.Drawing.Size(427, 121);
            this.GVENtidadePortariaDetalhes.TabIndex = 10;
            // 
            // GVPortaria
            // 
            this.GVPortaria.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.GVPortaria.Location = new System.Drawing.Point(12, 190);
            this.GVPortaria.Name = "GVPortaria";
            this.GVPortaria.Size = new System.Drawing.Size(427, 87);
            this.GVPortaria.TabIndex = 11;
            // 
            // TxtPortaria
            // 
            this.TxtPortaria.Location = new System.Drawing.Point(826, 25);
            this.TxtPortaria.Multiline = true;
            this.TxtPortaria.Name = "TxtPortaria";
            this.TxtPortaria.Size = new System.Drawing.Size(405, 604);
            this.TxtPortaria.TabIndex = 12;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(12, 174);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(43, 13);
            this.label4.TabIndex = 13;
            this.label4.Text = "Portaria";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(12, 285);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(54, 13);
            this.label5.TabIndex = 14;
            this.label5.Text = "Entidades";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(454, 9);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(50, 13);
            this.label6.TabIndex = 15;
            this.label6.Text = "Ligações";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(823, 9);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(73, 13);
            this.label7.TabIndex = 16;
            this.label7.Text = "Texto Portaria";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1243, 641);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.TxtPortaria);
            this.Controls.Add(this.GVPortaria);
            this.Controls.Add(this.GVENtidadePortariaDetalhes);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.TxtTipoEntidade);
            this.Controls.Add(this.TxtFiltroPortarias);
            this.Controls.Add(this.TxtFiltroEntidades);
            this.Controls.Add(this.GVRelacionamentos);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.textBox1);
            this.Name = "Form1";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this.douDataSet)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.douDataSetBindingSource)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.GVRelacionamentos)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.GVENtidadePortariaDetalhes)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.GVPortaria)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.BindingSource douDataSetBindingSource;
        private douDataSet douDataSet;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.DataGridView GVRelacionamentos;
        private System.Windows.Forms.TextBox TxtFiltroEntidades;
        private System.Windows.Forms.TextBox TxtFiltroPortarias;
        private System.Windows.Forms.TextBox TxtTipoEntidade;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.ComponentModel.BackgroundWorker backgroundWorker1;
        private System.Windows.Forms.DataGridView GVENtidadePortariaDetalhes;
        private System.Windows.Forms.DataGridView GVPortaria;
        private System.Windows.Forms.TextBox TxtPortaria;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label7;
    }
}

