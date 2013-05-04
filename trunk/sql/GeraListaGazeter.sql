use SiorgSQL


SELECT  --Orgaos subordinados a orgaos subordinados a presidencia
	N3.No_Orgao + '@IdOrgao=' + CAST(N3.Co_Orgao as varchar) + '@Particao=' + CAST(N3.Co_Orgao_Pai as varchar)
FROM 
	[dbo].[Vw_Nomes_Orgaos] N 
LEFT JOIN 
	[dbo].[Vw_Nomes_Orgaos] N2 ON N.Co_Orgao = N2.Co_Orgao_Pai
LEFT JOIN 
	[dbo].[Vw_Nomes_Orgaos] N3 ON N2.Co_Orgao = N3.Co_Orgao_Pai
WHERE 
	N.Co_Orgao = 26
	AND LEN(N3.No_Orgao) > 15
	AND N3.No_Orgao not in ('Comitês Técnicos', 'Comissão Técnica', 'Comissão de Ética', 'Secretaria-Geral', 'Assessoria Militar',
							'Assessoria Técnica', 'Comitê Executivo', 'Assessoria Especial', 'Assessoria Jurídica', 'Secretaria-Executiva',
							'Consultoria Jurídica', 'Secretaria Executiva', 'Assessoria Econômica', 'Gabinete do Ministro',
							'Secretaria de Imprensa', 'Assessoria Parlamentar', 'Assessoria Administrativa', 'Diretoria de Gestão Interna')
ORDER BY LEN(N3.No_Orgao)


SELECT --Orgaos subordinados a presidencia
	N.No_Orgao + '@IdOrgao=' + CAST(N.Co_Orgao as varchar) + + '@Particao=' + CAST(N.Co_Orgao as varchar)
FROM
	[dbo].[Vw_Nomes_Orgaos] N
WHERE 
	N.Co_Orgao_Pai = 26



SELECT --Orgaos que nao tem pais (raiz)
	No_Orgao + '@IdOrgao=' + CAST(Co_Orgao as varchar) + '@Particao=' + CAST(Co_Orgao as varchar)
FROM 
	[dbo].[Vw_Nomes_Orgaos] N 
WHERE N.Co_Orgao in (62823,
61501,
62855,
62854,
62853,
62852,
62851,
62881,
62880,
62879,
62878,
62877,
62876,
62875,
62874,
62873,
62872,
62871,
62870,
62869,
62868,
62867,
62866,
62865,
62864,
62863,
62862,
62858,
62861,
62857,
62859,
62860,
62856,
62904,
62903,
62902,
62901,
62900,
62899,
62848,
62849,
62918,
62917,
62916,
62915,
62914,
62847,
62913,
62912,
62911,
62910,
63243,
62909,
62908,
62907,
62906,
62905,
62841,
62882,
67471,
62842,
62824,
62850,
67490,
63550,
63555,
63546,
61438,
62840,
67536) 

