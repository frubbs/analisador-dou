use dou

IF OBJECT_ID('tempdb..#EntidadeEntidadeFiltrada') IS NOT NULL
  DROP TABLE #EntidadeEntidadeFiltrada
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
	E.Texto like '%%' 
	--AND
	--E.TipoEntidade = 'Orgao'


SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada
FROM 
	TbEntidadeEntidade EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
	P.Texto like '%%'
	AND (
			EE.IdEntidadeA in (SELECT IdEntidade FROM #EntidadeFiltrada) 
			OR
			EE.IdEntidadeB in (SELECT IdEntidade FROM #EntidadeFiltrada) 
		)


declare @entidades bigint

select @entidades = count(*) from VwEntidade WHERE IdEntidade in (SELECT IdEntidade FROM #EntidadeEntidadeFiltrada)

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
	IdEntidade in (SELECT IdEntidade FROM #EntidadeEntidadeFiltrada)


Select CAST(ID as NVARCHAR) + '  ' + Entidade from @Vertice







select '*Arcs' 

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
	#EntidadeEntidadeFiltrada ee 
LEFT JOIN 
	@Vertice v on ee.IDEntidadeA = v.IdOriginal
LEFT JOIN 
	@Vertice v2 on ee.IDEntidadeB = v2.IdOriginal



SELECT ligacao from
	#finalResult
Order by Tempo


	

