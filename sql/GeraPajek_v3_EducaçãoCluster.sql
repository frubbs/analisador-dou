use dou

IF OBJECT_ID('tempdb..#PortariaEntidadeFiltrada') IS NOT NULL
  DROP TABLE #PortariaEntidadeFiltrada
  GO
IF OBJECT_ID('tempdb..#EntidadeFiltrada') IS NOT NULL
  DROP TABLE #EntidadeFiltrada
  GO

SELECT 
	E.*
INTO
	#EntidadeFiltrada
FROM
	TbEntidade E
WHERE
	E.TipoEntidade = 'Orgao'
	AND (E.Texto like '%Universidade%' OR E.Texto like 'INSTITUTO FEDERAL%')


SELECT 
	distinct PE.IdPortaria, PE.Identidade, PE.Tempo
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
declare @portarias bigint


select @entidades = count(*) from VwEntidade WHERE IdEntidade in (SELECT IdEntidade FROM #PortariaEntidadeFiltrada)

select @portarias = count(*) from TbPortaria WHERE IdPortaria in (SELECT IdPortaria FROM #PortariaEntidadeFiltrada)

select '*Vertices ' + cast((@entidades + @portarias) as nvarchar) + ' ' + cast(@entidades as nvarchar)


DECLARE @Vertice TABLE (
    ID INT IDENTITY(1,1),
	IdOriginal BIGINT,
	Sisu INT,
	Entidade NVARCHAR(MAX)
)



INSERT INTO 
	@Vertice
SELECT 
	TbEntidade.IdEntidade as IdOriginal,
	ISNULL(TbSisu.sisu,0) as Sisu,
	'"' + Texto + '"                                      0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbEntidade
	LEFT JOIN TbSisu on (TbEntidade.IdEntidade = TbSisu.IdEntidade)
WHERE 
	TbEntidade.IdEntidade in (SELECT IdEntidade FROM #PortariaEntidadeFiltrada)


INSERT INTO 
	@Vertice
SELECT
	IdPortaria as IdOriginal,
	0 as Sisu,
--	 '"' + SUBSTRING(cast(Identificacao as nvarchar),0,130) + '"                    0.0000    0.0000    0.5000 ' AS Entidade
	 '"' + cast(Identificacao as nvarchar) + '"                    0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbPortaria
WHERE 
	IdPortaria in (SELECT IdPortaria FROM #PortariaEntidadeFiltrada)


Select 
CAST(ID as VARCHAR(MAX)) 
+ '  ' + Entidade from @Vertice
ORDER BY ID





select '*Arcs' 

SELECT 
/*	v.*,
	IdEntidade,
	IdPortaria,
	'[' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']',
	v2.*  */
	cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']' 
FROM
	#PortariaEntidadeFiltrada pe 
JOIN 
	@Vertice v on pe.IDEntidade = v.IdOriginal
JOIN 
	@Vertice v2 on pe.IdPortaria = v2.IdOriginal
	

	


-----------------------------------------------
select '######  particao do sisu ##########'

Select 
CAST(Sisu as VARCHAR(MAX)) 
from @Vertice
ORDER BY ID