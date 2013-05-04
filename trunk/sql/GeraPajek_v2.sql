use dou

declare @entidades bigint
declare @portarias bigint


select @entidades = count(*) from TbEntidade 
select @portarias = count(*) from TbPortaria 

select '*Vertices ' + cast((@entidades + @portarias) as nvarchar) + ' ' + cast(@entidades as nvarchar)


DECLARE @Vertice TABLE (
    ID INT IDENTITY(1,1),
	IdOriginal BIGINT,
	Entidade NVARCHAR(MAX)
)



INSERT INTO 
	@Vertice
SELECT 
	IdEntidade as IdOriginal,
	'"' + SUBSTRING(Texto,0,50) + '"                                      0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbEntidade

	


INSERT INTO 
	@Vertice
SELECT
	IdPortaria as IdOriginal,
	 '"' + SUBSTRING(cast(Identificacao as nvarchar),0,30) + '"                    0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbPortaria


Select CAST(ID as NVARCHAR) + '  ' + Entidade from @Vertice







select '*Arcs' 

SELECT
/*	v.*,
	IdEntidade,
	IdPortaria,
	'[' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']',
	v2.*  
	*/
	cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']' 
FROM
	TbPortariaEntidade pe 
JOIN 
	@Vertice v on pe.IDEntidade = v.IdOriginal
JOIN 
	@Vertice v2 on pe.IdPortaria = v2.IdOriginal


	

