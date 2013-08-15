use dou

IF OBJECT_ID('tempdb..#PortariaEntidadeFiltrada') IS NOT NULL
  DROP TABLE #PortariaEntidadeFiltrada
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
	E.Texto like '%%' AND
	E.TipoEntidade = 'Orgao'


SELECT 
	PE.*
INTO
	#PortariaEntidadeFiltrada
FROM 
	TbPortaria P
JOIN
	TbPortariaEntidade PE ON P.IdPortaria = PE.IdPortaria
WHERE
	P.Texto like '%%'
	AND PE.IdEntidade in (SELECT IdEntidade FROM #EntidadeFiltrada)


declare @entidades bigint

select @entidades = count(*) from VwEntidade WHERE IdEntidade in (SELECT IdEntidade FROM #PortariaEntidadeFiltrada)

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
	VwEntidade
WHERE 
	IdEntidade in (SELECT IdEntidade FROM #PortariaEntidadeFiltrada)

/*
INSERT INTO 
	@Vertice
SELECT
	IdPortaria as IdOriginal,
--	 '"' + SUBSTRING(cast(Identificacao as nvarchar),0,130) + '"                    0.0000    0.0000    0.5000 ' AS Entidade
	 '"' + cast(Identificacao as nvarchar) + '"                    0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbPortaria
WHERE 
	IdPortaria in (SELECT IdPortaria FROM #PortariaEntidadeFiltrada)
*/

Select 'vertices ok' --CAST(ID as NVARCHAR) + '  ' + Entidade from @Vertice







select '*Arcs' 

SELECT
/*	v.*,
	IdEntidade,
	IdPortaria,
	'[' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']',
	v2.*  
	*/
	distinct cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY pe.Tempo DESC) as nvarchar) +']' as ligacao, 
	pe.Tempo,
	pe.IdPortaria
INTO 
	#finalResult
FROM
	#PortariaEntidadeFiltrada pe 
JOIN 
	#PortariaEntidadeFiltrada pe2 on (pe.idPortaria = pe2.idPortaria) AND (pe.IDEntidade > pe2.IDEntidade)
LEFT JOIN 
	@Vertice v on pe.IDEntidade = v.IdOriginal
LEFT JOIN 
	@Vertice v2 on pe2.IDEntidade = v2.IdOriginal

Select 'arcos ok'

SELECT * from
	#finalResult
Order by Tempo


	

