use dou


DECLARE @TbPortariaOrgaoSiorgFiltrada TABLE(
	[IdPortaria] [bigint] NOT NULL,
	[IdEntidade] [bigint] NOT NULL,
	[TipoLigacao] [int] NULL,
	[Tempo] [date] NULL
)



--Filtra as portarias que serão consideradas
INSERT INTO
	@TbPortariaOrgaoSiorgFiltrada
SELECT 
	IdPortaria, IdEntidade, TipoLigacao, Tempo
FROM
	TbPortariaOrgaoSiorg
WHERE
	IdPortaria in (SELECT IdPortaria FROM TbPortaria WHERE Texto like '%segurança%' OR Texto like '%defesa%')


DECLARE @Vertice TABLE (
    ID INT IDENTITY(1,1),
	IdOriginal BIGINT,
	Entidade NVARCHAR(MAX)
)


--Filtra os orgaos que figuram na lista de portarias filtradas.
INSERT INTO 
	@Vertice
SELECT 
	Co_Orgao as IdOriginal,
	'"' + SUBSTRING(No_Orgao,0,100) + '"                                      0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	[SiorgSQL].[dbo].Ta_Nomes_Orgaos WHERE Co_Orgao in (SELECT IdEntidade FROM @TbPortariaOrgaoSiorgFiltrada)
	

declare @entidades bigint
select @entidades = count(*) from @Vertice 


INSERT INTO 
	@Vertice
SELECT
	IdPortaria as IdOriginal,
	 '"' + SUBSTRING(cast(Identificacao as nvarchar),0,30) + '"                    0.0000    0.0000    0.5000 ' AS Entidade
FROM 
	TbPortaria

declare @total bigint
select @total = count(*) from @Vertice 

select '*Vertices ' + cast(@total as nvarchar) + ' ' + cast(@entidades as nvarchar)




Select CAST(ID as NVARCHAR) + '  ' + Entidade from @Vertice







select '*Arcs' 

SELECT
	/*v.*,
	IdEntidade,
	IdPortaria,
	'[' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']',
	v2.*,  
	*/
	cast(v.ID as nvarchar) + ' ' + cast(v2.id as nvarchar) + ' 1 [' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']' 
FROM
	@TbPortariaOrgaoSiorgFiltrada pe 
JOIN 
	@Vertice v on pe.IDEntidade = v.IdOriginal
JOIN 
	@Vertice v2 on pe.IdPortaria = v2.IdOriginal



SELECT 'XXXXXXXXXX  FIM DO ARQUIVO NET. INICIO DO ARQUIVO DE PARTIÇÕES XXXXXXXXXXXXXX'

SELECT 
	ep.Particao  
FROM 
	@Vertice v
LEFT JOIN
	 [SiorgSQL].[dbo].VwEntidadeParticao ep on ep.Co_Orgao = v.IdOriginal
ORDER By v.ID




