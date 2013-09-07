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
	P.Texto like '%Ciência%' OR
P.Texto like '%Tecnologia%' OR
P.Texto like '%Inovação%' OR
P.Texto like '%CT&I%' OR
P.Texto like '%política industrial%' OR
P.Texto like '%comércio exterior.%' OR
P.Texto like '%políticas industriais%' OR
P.Texto like '%Ministério da Ciência e Tecnologia%' OR
P.Texto like '%Pesquisa e Desenvolvimento%' OR
P.Texto like '%desenvolvimento científico%' OR
--P.Texto like '%desenvolvimento%' OR
P.Texto like '%tecnológico%' OR
P.Texto like '%inovação%' OR
P.Texto like '%produção científica%' OR
P.Texto like '%produção tecnológica%' OR
P.Texto like '%periódicos científicos%' OR
P.Texto like '%patentes%' OR
P.Texto like '%mercado externo%' OR
P.Texto like '%política de Ciência, Tecnologia e Inovação%' OR
P.Texto like '%política industrial%' OR
P.Texto like '%mundo acadêmico%' OR
P.Texto like '%pesquisa científica%' OR
P.Texto like '%Espacial%' OR
P.Texto like '%Nuclear%' OR
P.Texto like '%tecnologias sensíveis%' OR
P.Texto like '%Inovações para a Agropecuária.%' OR
P.Texto like '%produção científica%'

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