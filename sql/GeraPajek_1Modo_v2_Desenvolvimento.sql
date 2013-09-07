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

/*

SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada_aux0
FROM 
	TbEntidadeEntidade EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
    TipoPortaria = 'IniporInt'
	--P.TipoPortaria <> 'IniAcordao' AND P.TipoPortaria <> 'IniTC'

	*/

SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada_aux1
FROM 
	TbEntidadeEntidade EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
P.Texto Like '%CONHECIMENTO, EDUCA��O E CULTURA%' OR
P.Texto Like '%Conhecimento, educa��o e cultura%' OR
P.Texto Like '%Educa��o e cultura%' OR
P.Texto Like '%educa��o b�sica%' OR
P.Texto Like '%piso nacional do magist�rio%' OR
P.Texto Like '%PROUNI%' OR
P.Texto Like '%educa��o superior%' OR
P.Texto Like '%mestres e doutores%' OR
P.Texto Like '%gest�o da escola%' OR
P.Texto Like '%Educa��o B�sica%' OR
P.Texto Like '%Educa��o Profissional e Tecnol�gica%' OR
P.Texto Like '%Educa��o Superior%' OR
P.Texto Like '%Pesquisa e Extens�o%' OR
P.Texto Like '%Quadras esportivas escolares%' OR
P.Texto Like '%Educa��o infantil%' OR
P.Texto Like '%educa��o profissional%' OR
P.Texto Like '%PRONATEC%' OR
P.Texto Like '%rede federal de educa��o%' OR
P.Texto Like '%Rede Federal de Educa��o Superior%' OR
P.Texto Like '% Universidades Federais%' OR
P.Texto Like '%Arte e cultura%' OR
P.Texto Like '%pol�tica de cultura%' OR
P.Texto Like '%Sistema Nacional de Cultura%' OR
P.Texto Like '%Pra�as dos Esportes e da Cultura%' OR
P.Texto Like '%economia criativa%' OR
P.Texto Like '%esporte na inclus�o social%' OR
P.Texto Like '%potencial econ�mico do esporte%' OR
P.Texto Like '%eventos esportivos%' OR
P.Texto Like '%Copa 2014%' OR
P.Texto Like '%Olimp�adas%' OR
P.Texto Like '%legado esportivo%' OR
P.Texto Like '%esporte de alto rendimento%' OR
P.Texto Like '%ol�mpico%' OR
P.Texto Like '%legado social%' OR
P.Texto Like '%esporte educativo%' 



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