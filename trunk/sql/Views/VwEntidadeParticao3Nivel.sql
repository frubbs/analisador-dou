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
	AND N3.No_Orgao not in ('Gabinete da Presidência'
,'Coordenação'
,'Juiz-Auditor e Juiz-Auditor Substituto'
,'Assessoria da Presidência'
,'Corregedoria Regional Eleitoral'
,'Assessoria da Presidência'
,'Secretaria do Tribunal Pleno'
,'Secretaria do Tribunal Pleno'
,'Secretaria Geral da Presidência'
,'Secretaria-Geral da Presidência'
,'Comissão Permanente de Licitação'
,'Coordenadoria da Primeira Turma'
,'Secretaria de Orçamento e Finanças'
,'Secretaria de Administração e Orçamento'
,'Coordenadoria de Produção e Suporte'
,'Coordenadoria de Material e Patrimônio'
,'Coordenadoria da Quarta Turma'
,'Coordenadoria da Quinta Turma'
,'Coordenadoria da Segunda Turma'
,'Coordenadoria da Segunda Seção'
,'Coordenadoria da Sexta Turma'
,'Coordenadoria da Terceira Seção'
,'Coordenadoria da Terceira Turma'
,'Coordenadoria da Corte Especial'
,'Coordenadoria da Primeira Seção'
,'Assessoria de Assuntos Internacionais'
,'Coordenadoria de Orçamento e Finanças'
,'Assessoria da Corregedoria Regional Eleitoral'
,'Assessoria da Corregedoria Regional Eleitoral'
,'Coordenadoria de Registros e Informações Processuais '
,'Secretaria Geral da Mesa'
,'Secretaria Especial de Editoração e Publicações'
,'Serviço'
,'Secretaria de Fiscalização de Pessoal'
,'Serviço de Assistência Médico-social'
,'Coordenadoria de Jurisprudência e Documentação'
,'Coordenadoria de Desenvolvimento de  Recursos Humanos '
,'Coordenadoria de Documentação e Informação '
,'Gabinete dos Juízes de 2ª Instância'
,'Coordenação de Orçamento e Finanças'
,'Coordenação de Produção e Suporte'
,'Diretoria-Geral de Coordenação Administrativa'
,'Diretoria-Geral de Coordenação Judiciária',
'Presidência','Comitês Técnicos', 'Comissão Técnica', 'Comissão de Ética', 'Secretaria-Geral', 'Assessoria Militar',
							'Assessoria Técnica', 'Comitê Executivo', 'Assessoria Especial', 'Assessoria Jurídica', 'Secretaria-Executiva',
							'Consultoria Jurídica', 'Secretaria Executiva', 'Assessoria Econômica', 'Gabinete do Ministro',
							'Secretaria de Imprensa', 'Assessoria Parlamentar', 'Assessoria Administrativa', 'Diretoria de Gestão Interna',
							'Seção','Juízes','Revista','Gabinete', 'Plenário','Ouvidoria', 'Porta-Voz', 'Gabinetes', 'Promotores',
							'Assessoria', 'Secretaria', 'Presidente', 'Cerimonial','Presidência', 'Ematra - Rj', 'Distribuição',
							'Corregedoria', 'Direção Geral','Segunda Seção', 'Direção-Geral', 'Direção-Geral', 'Primeira Seção',
							'Comitê Técnico', 'Ouvidoria-Geral', 'Câmara Temática', 'Câmara Temática', 'Escola Judicial',
							'Diretoria Geral','Diretoria-Geral', 'Vice-Presidente', 'Direção do Foro', 'Seção Judiciária',
							'Vice-Presidência', 'Secretaria Geral','Zonas Eleitorais', 'Diretoria do Foro', 'Seção de Pesquisa',
							'Auditoria Interna', 'Setor de Imprensa', 'Seção de Orçamento', 'Setor de Licitação', 'Diretor de Serviço',
							'Secretaria da Vara', 'Setor de Atermação', 'Gabinete de Juízos', 'Serviço de Compras', 'Departamento Médico',
							'Oficial de Gabinete', 'Seção de Biblioteca', 'Divisão de Gabinete', 'Secretaria do Pleno', 'Seção Administrativa',
							'Seção de Estatística', 'Diretoria de Pessoal', 'Serviço de Contratos', 'Secretaria Judiciária', 'Diretoria Judiciária',
							'Assistência Jurídica', 'Comissão de Promoções', 'Serviço de Transporte', 'Observatório Nacional',
							'Comando do Exército', 'Setor de Distribuição', 'Secretaria de Pessoal', 'Gabinete da Secretaria', 'Secretaria das Sessões',
							'Divisão de Precatórios', 'Assessoria do Gabinete', 'Assessoria de Recursos', 'Secretarias das Turmas', 
							'Supervisor de Gabinete', 'Assistente de Gabinete', 'Auxiliar Especializado', 'Diretoria de Finanças',
							'Secretaria de Recursos', 'Coordenação de Pessoal', 'Supervisão de Gabinete', 'Seção de Apoio Jurídico',
							'Gabinetes dos Ministros', 'Diretoria Administrativa', 'Divisão de Processamento', 'Secretaria de Informática',
							'Secretaria Administrativa', 'Secretaria do Audiovisual', 'Conselho de Administração', 'Subsecretaria de Recursos',
							'Gabinete do Diretor-Geral', 'Assessoria Diplomática', 'Coordenação de Eleição','Diretoria Financeira',
							'Seção de Jurisprudência','Coordenadoria de Suporte','Coordenadoria de Pessoal','Gabinete da Corregedoria',
							'Seção de Protocolo Geral','Serviço de Administração','Coordenadoria de Eleições ','Coordenadoria de Produção',
							'Coordenação de Divulgação ','Secretaria de Distribuição','Juizes Titulares das Varas','Diretoria de Administração',
							'Coordenadoria de Orçamento','Diretoria-Geral Judiciária','Circunscrições Judiciárias','Cartório de Registro Civil',
							'Comissão Técnica Executiva','Comissão de Jurisprudência','Diretoria de Informática','Assessoria da Corregedoria',
							'Comissão de Biossegurança','Secretaria da Informática', 'Gabinete da Diretoria-Geral','Serviço de Controle Interno',
							'Secretaria de Administração','Diretor Geral da Secretaria','Gabinete da Diretoria Geral','Pregoeiro e Equipe de Apoio',
							'Gabinete do Juiz Presidente','Subsecretaria de Expediente','Direção Geral da Secretaria','Serviço de Controle Interno',
							'Coordenadoria de Informática','Gabinete da Vice-Presidência','Diretoria de Recursos Humanos ','Seção de Apoio Administrativo',
							'Assessoria da Diretoria-Geral','Diretoria de Recursos Humanos','Secretaria de Recuros Humanos','Secretaria de Serviços Gerais',
							'Assessoria da Diretoria Geral','Seção de Apoio Administrativo','Coordenadoria de Comunicações','Diretoria-Geral de Secretaria',
							'Secretaria de Controle Interno','Seção de Divulgação e Imprensa','Comissão Especial de Licitação', 'Setor de Acórdão',
							'Secretarias das Varas','Secretária de Informática','Secretaria da Corregedoria','Secretaria da Corregedoria ',
							'Secretaria de Gestão Pública','Secretaria Adjunta de Contas','Comissão Especial de Recursos','Secretaria de Controle Externo ',
							'Secretaria de Controle Externo','Secretaria de Recursos Humanos ','Vice-Presidência e Corregedoria','Coordenação de Controle Interno',
							'Coordenação de Recursos Humanos','Coordenadoria de Serviços Gerais','Assessoria de Gestão Estratégica','Assessoria da Corregedoria-Geral',
							'Assessoria de Comunicação Social','Subchefia para Assuntos Jurídicos','Coordenadoria de Controle Interno',
							'Secretaria-Geral de Administração','Da Secretaria de Controle Interno','Coordenadoria de Controle Interno',
							'Divisão de Procedimentos Diversos','Secretaria de Apoio Administrativo','Divisão de Coordenação e Julgamentos',
							'Central de Mandados','Edital','Membros','Acórdãos','Despachos','Coordenação','Segunda Turma',
							'Nona Turma','Sexta Turma','Oitava Turma','Quarta Turma','Quinta Turma','Sétima Turma','Juizes do Tre',
							'Juízes do TRT','Segunda Turma','Coordenadoria','Tribunal Pleno','Terceira Turma','Primeira Turma','Diretor do Forum',
							'Juízes - 2º Grau','Juízes - 1º Grau','Vice-Predidência','Gerência Setorial','Varas do Trabalho','Coordenação-Geral',
							'Diretora de Fórum','Gerência Setorial','Vice-Corregedoria','indice de Pesquisa','Corregedoria Geral','Ouvidoria Regional',
							'Serviço do Pessoal','Corregedoria-Geral','Juízes do Tribunal','Varas de Trabalho','Indice de Advogados','Gabinete da Revista',
							'Gabinetes de Juízes','Corregedoria Regional','Secretaria do Tribunal','Notas e Avisos Diversos','Gabinete da Corregedoria:',
							'Diretoria-Geral da Secretaria','Secretaria Geral das Sessões','Presidência do Tribunal'
														)



--ORDER BY LEN(N2.No_Orgao)






GO


