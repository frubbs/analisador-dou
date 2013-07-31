USE [SiorgSQL]
GO

/****** Object:  View [dbo].[VwEntidadeParticao3Nivel]    Script Date: 31/07/2013 17:31:57 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO




/*
verificacao 1 - deve ter o mesmo tamanho - 69445
  select count(*) from [Vw_Nomes_Orgaos]
  select count(*) from [Ta_Nomes_Orgaos]


*/


CREATE VIEW [dbo].[VwEntidadeParticao3Nivel] AS
SELECT 
	 N3.No_Orgao as Nome, N3.Co_Orgao as Co_Orgao, N3.Co_Orgao_Pai as Particao
FROM 
	[dbo].[Vw_Nomes_Orgaos] N 
LEFT JOIN 
	[dbo].[Vw_Nomes_Orgaos] N2 ON N.Co_Orgao = N2.Co_Orgao_Pai
LEFT JOIN 
	[dbo].[Vw_Nomes_Orgaos] N3 ON N2.Co_Orgao = N3.Co_Orgao_Pai
WHERE 
	N.Co_Orgao in (
		select 
			Co_Orgao
		from 
			[Ta_Nomes_Orgaos]
		where 
			Co_Orgao not in (select Co_Orgao_Filho from [Ta_Hierarquia_Orgaos] )
		)
	AND N3.No_Orgao not in ('Gabinete da Presid�ncia'
,'Coordena��o'
,'Juiz-Auditor e Juiz-Auditor Substituto'
,'Assessoria da Presid�ncia'
,'Corregedoria Regional Eleitoral'
,'Assessoria da Presid�ncia'
,'Secretaria do Tribunal Pleno'
,'Secretaria do Tribunal Pleno'
,'Secretaria Geral da Presid�ncia'
,'Secretaria-Geral da Presid�ncia'
,'Comiss�o Permanente de Licita��o'
,'Coordenadoria da Primeira Turma'
,'Secretaria de Or�amento e Finan�as'
,'Secretaria de Administra��o e Or�amento'
,'Coordenadoria de Produ��o e Suporte'
,'Coordenadoria de Material e Patrim�nio'
,'Coordenadoria da Quarta Turma'
,'Coordenadoria da Quinta Turma'
,'Coordenadoria da Segunda Turma'
,'Coordenadoria da Segunda Se��o'
,'Coordenadoria da Sexta Turma'
,'Coordenadoria da Terceira Se��o'
,'Coordenadoria da Terceira Turma'
,'Coordenadoria da Corte Especial'
,'Coordenadoria da Primeira Se��o'
,'Assessoria de Assuntos Internacionais'
,'Coordenadoria de Or�amento e Finan�as'
,'Assessoria da Corregedoria Regional Eleitoral'
,'Assessoria da Corregedoria Regional Eleitoral'
,'Coordenadoria de Registros e Informa��es Processuais '
,'Secretaria Geral da Mesa'
,'Secretaria Especial de Editora��o e Publica��es'
,'Servi�o'
,'Secretaria de Fiscaliza��o de Pessoal'
,'Servi�o de Assist�ncia M�dico-social'
,'Coordenadoria de Jurisprud�ncia e Documenta��o'
,'Coordenadoria de Desenvolvimento de  Recursos Humanos '
,'Coordenadoria de Documenta��o e Informa��o '
,'Gabinete dos Ju�zes de 2� Inst�ncia'
,'Coordena��o de Or�amento e Finan�as'
,'Coordena��o de Produ��o e Suporte'
,'Diretoria-Geral de Coordena��o Administrativa'
,'Diretoria-Geral de Coordena��o Judici�ria',
'Presid�ncia','Comit�s T�cnicos', 'Comiss�o T�cnica', 'Comiss�o de �tica', 'Secretaria-Geral', 'Assessoria Militar',
							'Assessoria T�cnica', 'Comit� Executivo', 'Assessoria Especial', 'Assessoria Jur�dica', 'Secretaria-Executiva',
							'Consultoria Jur�dica', 'Secretaria Executiva', 'Assessoria Econ�mica', 'Gabinete do Ministro',
							'Secretaria de Imprensa', 'Assessoria Parlamentar', 'Assessoria Administrativa', 'Diretoria de Gest�o Interna',
							'Se��o','Ju�zes','Revista','Gabinete', 'Plen�rio','Ouvidoria', 'Porta-Voz', 'Gabinetes', 'Promotores',
							'Assessoria', 'Secretaria', 'Presidente', 'Cerimonial','Presid�ncia', 'Ematra - Rj', 'Distribui��o',
							'Corregedoria', 'Dire��o Geral','Segunda Se��o', 'Dire��o-Geral', 'Dire��o-Geral', 'Primeira Se��o',
							'Comit� T�cnico', 'Ouvidoria-Geral', 'C�mara Tem�tica', 'C�mara Tem�tica', 'Escola Judicial',
							'Diretoria Geral','Diretoria-Geral', 'Vice-Presidente', 'Dire��o do Foro', 'Se��o Judici�ria',
							'Vice-Presid�ncia', 'Secretaria Geral','Zonas Eleitorais', 'Diretoria do Foro', 'Se��o de Pesquisa',
							'Auditoria Interna', 'Setor de Imprensa', 'Se��o de Or�amento', 'Setor de Licita��o', 'Diretor de Servi�o',
							'Secretaria da Vara', 'Setor de Aterma��o', 'Gabinete de Ju�zos', 'Servi�o de Compras', 'Departamento M�dico',
							'Oficial de Gabinete', 'Se��o de Biblioteca', 'Divis�o de Gabinete', 'Secretaria do Pleno', 'Se��o Administrativa',
							'Se��o de Estat�stica', 'Diretoria de Pessoal', 'Servi�o de Contratos', 'Secretaria Judici�ria', 'Diretoria Judici�ria',
							'Assist�ncia Jur�dica', 'Comiss�o de Promo��es', 'Servi�o de Transporte', 'Observat�rio Nacional',
							'Comando do Ex�rcito', 'Setor de Distribui��o', 'Secretaria de Pessoal', 'Gabinete da Secretaria', 'Secretaria das Sess�es',
							'Divis�o de Precat�rios', 'Assessoria do Gabinete', 'Assessoria de Recursos', 'Secretarias das Turmas', 
							'Supervisor de Gabinete', 'Assistente de Gabinete', 'Auxiliar Especializado', 'Diretoria de Finan�as',
							'Secretaria de Recursos', 'Coordena��o de Pessoal', 'Supervis�o de Gabinete', 'Se��o de Apoio Jur�dico',
							'Gabinetes dos Ministros', 'Diretoria Administrativa', 'Divis�o de Processamento', 'Secretaria de Inform�tica',
							'Secretaria Administrativa', 'Secretaria do Audiovisual', 'Conselho de Administra��o', 'Subsecretaria de Recursos',
							'Gabinete do Diretor-Geral', 'Assessoria Diplom�tica', 'Coordena��o de Elei��o','Diretoria Financeira',
							'Se��o de Jurisprud�ncia','Coordenadoria de Suporte','Coordenadoria de Pessoal','Gabinete da Corregedoria',
							'Se��o de Protocolo Geral','Servi�o de Administra��o','Coordenadoria de Elei��es ','Coordenadoria de Produ��o',
							'Coordena��o de Divulga��o ','Secretaria de Distribui��o','Juizes Titulares das Varas','Diretoria de Administra��o',
							'Coordenadoria de Or�amento','Diretoria-Geral Judici�ria','Circunscri��es Judici�rias','Cart�rio de Registro Civil',
							'Comiss�o T�cnica Executiva','Comiss�o de Jurisprud�ncia','Diretoria de Inform�tica','Assessoria da Corregedoria',
							'Comiss�o de Biosseguran�a','Secretaria da Inform�tica', 'Gabinete da Diretoria-Geral','Servi�o de Controle Interno',
							'Secretaria de Administra��o','Diretor Geral da Secretaria','Gabinete da Diretoria Geral','Pregoeiro e Equipe de Apoio',
							'Gabinete do Juiz Presidente','Subsecretaria de Expediente','Dire��o Geral da Secretaria','Servi�o de Controle Interno',
							'Coordenadoria de Inform�tica','Gabinete da Vice-Presid�ncia','Diretoria de Recursos Humanos ','Se��o de Apoio Administrativo',
							'Assessoria da Diretoria-Geral','Diretoria de Recursos Humanos','Secretaria de Recuros Humanos','Secretaria de Servi�os Gerais',
							'Assessoria da Diretoria Geral','Se��o de Apoio Administrativo','Coordenadoria de Comunica��es','Diretoria-Geral de Secretaria',
							'Secretaria de Controle Interno','Se��o de Divulga��o e Imprensa','Comiss�o Especial de Licita��o', 'Setor de Ac�rd�o',
							'Secretarias das Varas','Secret�ria de Inform�tica','Secretaria da Corregedoria','Secretaria da Corregedoria ',
							'Secretaria de Gest�o P�blica','Secretaria Adjunta de Contas','Comiss�o Especial de Recursos','Secretaria de Controle Externo ',
							'Secretaria de Controle Externo','Secretaria de Recursos Humanos ','Vice-Presid�ncia e Corregedoria','Coordena��o de Controle Interno',
							'Coordena��o de Recursos Humanos','Coordenadoria de Servi�os Gerais','Assessoria de Gest�o Estrat�gica','Assessoria da Corregedoria-Geral',
							'Assessoria de Comunica��o Social','Subchefia para Assuntos Jur�dicos','Coordenadoria de Controle Interno',
							'Secretaria-Geral de Administra��o','Da Secretaria de Controle Interno','Coordenadoria de Controle Interno',
							'Divis�o de Procedimentos Diversos','Secretaria de Apoio Administrativo','Divis�o de Coordena��o e Julgamentos',
							'Central de Mandados','Edital','Membros','Ac�rd�os','Despachos','Coordena��o','Segunda Turma',
							'Nona Turma','Sexta Turma','Oitava Turma','Quarta Turma','Quinta Turma','S�tima Turma','Juizes do Tre',
							'Ju�zes do TRT','Segunda Turma','Coordenadoria','Tribunal Pleno','Terceira Turma','Primeira Turma','Diretor do Forum',
							'Ju�zes - 2� Grau','Ju�zes - 1� Grau','Vice-Predid�ncia','Ger�ncia Setorial','Varas do Trabalho','Coordena��o-Geral',
							'Diretora de F�rum','Ger�ncia Setorial','Vice-Corregedoria','indice de Pesquisa','Corregedoria Geral','Ouvidoria Regional',
							'Servi�o do Pessoal','Corregedoria-Geral','Ju�zes do Tribunal','Varas de Trabalho','Indice de Advogados','Gabinete da Revista',
							'Gabinetes de Ju�zes','Corregedoria Regional','Secretaria do Tribunal','Notas e Avisos Diversos','Gabinete da Corregedoria:',
							'Diretoria-Geral da Secretaria','Secretaria Geral das Sess�es','Presid�ncia do Tribunal'
														)



--ORDER BY LEN(N2.No_Orgao)






GO


