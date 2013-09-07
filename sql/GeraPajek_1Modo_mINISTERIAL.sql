use dou

IF OBJECT_ID('tempdb..#EntidadeEntidadeFiltrada') IS NOT NULL
  DROP TABLE #EntidadeEntidadeFiltrada
  GO

IF OBJECT_ID('tempdb..#EntidadeEntidadeFiltrada_aux0') IS NOT NULL
  DROP TABLE #EntidadeEntidadeFiltrada_aux0
  GO

IF OBJECT_ID('tempdb..#EntidadeEntidadeFiltrada_aux1') IS NOT NULL
  DROP TABLE #EntidadeEntidadeFiltrada_aux1
  GO

IF OBJECT_ID('tempdb..#EntidadeEntidadeFiltrada_aux2') IS NOT NULL
  DROP TABLE #EntidadeEntidadeFiltrada_aux2
  GO

IF OBJECT_ID('tempdb..#EntidadeEntidadeFiltrada_aux3') IS NOT NULL
  DROP TABLE #EntidadeEntidadeFiltrada_aux3
  GO

IF OBJECT_ID('tempdb..#EntidadeFiltrada') IS NOT NULL
  DROP TABLE #EntidadeFiltrada
  GO

IF OBJECT_ID('tempdb..#finalResult') IS NOT NULL
  DROP TABLE #finalResult
  GO
  

SELECT 
	E.*
INTO
	#EntidadeFiltrada
FROM
	TbEntidade E
WHERE
	--E.Texto like '%%' 
	--AND
	E.TipoEntidade = 'Orgao'




SELECT 
	EE.*
INTO
SELECT * FROM	#EntidadeEntidadeFiltrada_aux1
FROM 
	TbEntidadeEntidade EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
    TipoPortaria = 'IniporInt'
	--P.TipoPortaria <> 'IniAcordao' AND P.TipoPortaria <> 'IniTC'




/*


SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada_aux1
FROM 
	#EntidadeEntidadeFiltrada_aux0 EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
	P.Texto like '%Ci�ncia%' OR
P.Texto like '%Tecnologia%' OR
P.Texto like '%Inova��o%' OR
P.Texto like '%CT&I%' OR
P.Texto like '%pol�tica industrial%' OR
P.Texto like '%com�rcio exterior.%' OR
P.Texto like '%pol�ticas industriais%' OR
P.Texto like '%Minist�rio da Ci�ncia e Tecnologia%' OR
P.Texto like '%Pesquisa e Desenvolvimento%' OR
P.Texto like '%desenvolvimento cient�fico%' OR
--P.Texto like '%desenvolvimento%' OR
P.Texto like '%tecnol�gico%' OR
P.Texto like '%inova��o%' OR
P.Texto like '%produ��o cient�fica%' OR
P.Texto like '%produ��o tecnol�gica%' OR
P.Texto like '%peri�dicos cient�ficos%' OR
P.Texto like '%patentes%' OR
P.Texto like '%mercado externo%' OR
P.Texto like '%pol�tica de Ci�ncia, Tecnologia e Inova��o%' OR
P.Texto like '%pol�tica industrial%' OR
P.Texto like '%mundo acad�mico%' OR
P.Texto like '%pesquisa cient�fica%' OR
P.Texto like '%Espacial%' OR
P.Texto like '%Nuclear%' OR
P.Texto like '%tecnologias sens�veis%' OR
P.Texto like '%Inova��es para a Agropecu�ria.%' OR
P.Texto like '%produ��o cient�fica%'

*/


SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada_aux2
FROM 
	#EntidadeEntidadeFiltrada_aux1 EE
WHERE
	EE.IdEntidadeA in (SELECT IdEntidade FROM #EntidadeFiltrada) --(244455 row(s) affected)

SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada_aux3
FROM 
	#EntidadeEntidadeFiltrada_aux2 EE
WHERE
	EE.IdEntidadeB in (SELECT IdEntidade FROM #EntidadeFiltrada) 


SELECT * INTO #EntidadeEntidadeFiltrada
FROM
(
SELECT IdEntidadeA as IdEntidade FROM 	#EntidadeEntidadeFiltrada_aux3
UNION
SELECT IdEntidadeB as IdEntidade FROM 	#EntidadeEntidadeFiltrada_aux3
) AS tmp


declare @entidades bigint
select @entidades = count(*) from TbEntidade WHERE IdEntidade in (SELECT IdEntidade FROM #EntidadeFiltrada)

select '*Vertices ' + cast(@entidades as nvarchar)


DECLARE @Vertice TABLE (
    ID INT IDENTITY(1,1),
	IdOriginal BIGINT,
	Entidade NVARCHAR(MAX)
)



INSERT INTO 
	@Vertice
SELECT 
	IdEntidade as IdOriginal,
	-- '"' + SUBSTRING(Texto,0,130) + '"                                      0.0000    0.0000    0.5000 ' AS Entidade
	'"' + Texto + '"                                      0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbEntidade
WHERE 
	IdEntidade in (SELECT IdEntidade FROM #EntidadeFiltrada)


Select CAST(ID as NVARCHAR) + '  ' + Entidade from @Vertice
Order by ID







select '*Edges' 

SELECT
/*	v.*,
	IdEntidade,
	IdPortaria,
	'[' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']',
	v2.*  
	*/
	distinct cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY ee.Tempo DESC) as nvarchar) +']' as ligacao, ee.Tempo
INTO 
	#finalResult
FROM
	#EntidadeEntidadeFiltrada_aux3 ee 
LEFT JOIN 
	@Vertice v on ee.IDEntidadeA = v.IdOriginal
LEFT JOIN 
	@Vertice v2 on ee.IDEntidadeB = v2.IdOriginal



SELECT ligacao from
	#finalResult
Order by Tempo

/*

SELECT 'xxxxxxxxxxxxxxxxxxxxxxx PARTICAO MINISTERIOS (so faz sentido se foi escolhido apenas orgaos xxxxxxxxxxxxxxxxxxx'	

SELECT ISNULL(Particao,9999998)
  FROM 
		@Vertice E
LEFT JOIN
		[SiorgSQL].[dbo].[VwEntidadeParticao] vE on UPPER(LTRIM(RTRIM(E.Entidade))) = UPPER(LTRIM(RTRIM(vE.Nome)))


		*/