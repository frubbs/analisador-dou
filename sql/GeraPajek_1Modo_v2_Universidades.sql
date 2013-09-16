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
	#EntidadeEntidadeFiltrada_aux0
FROM 
	TbEntidadeEntidade EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
    TipoPortaria = 'IniporInt'
	--P.TipoPortaria <> 'IniAcordao' AND P.TipoPortaria <> 'IniTC'



SELECT 
	EE.*
INTO
	#EntidadeEntidadeFiltrada_aux1
FROM 
	#EntidadeEntidadeFiltrada_aux0 EE
JOIN
	 TbPortaria P ON P.IdPortaria = EE.IdPortaria
WHERE
P.Texto Like '%consumo de massa%' OR
P.Texto Like '%mercado interno%' OR
P.Texto Like '%distribuição de renda%' OR
P.Texto Like '%vulnerabilidade externa%' OR
P.Texto Like '%infraestrutura%' OR
P.Texto Like '%mercado mundial%' OR
P.Texto Like '%diferenças sociais%' OR
P.Texto Like '%diferenças regionais%' OR
P.Texto Like '%salário mínimo%' OR
P.Texto Like '%Programa Bolsa Família%' OR
P.Texto Like '%Plano de Desenvolvimento da Educação%' OR
P.Texto Like '%PAC%' OR
P.Texto Like '%desigualdades sociais%' OR
P.Texto Like '%desigualdades regionais%' OR
P.Texto Like '%Política Nacional de Desenvolvimento Regional%' OR
P.Texto Like '%Territórios da Cidadania%' OR
P.Texto Like '%meio rural%' OR
P.Texto Like '%meio urbano%' OR
P.Texto Like '%desigualdades inter-regionais%' OR
P.Texto Like '%desigualdades intrarregionais%' OR
P.Texto Like '%Arranjos Produtivos Locais%' OR
P.Texto Like '%Economia Solidária%' OR
P.Texto Like '%Brasil Sem Miséria%' OR
P.Texto Like '%Programa Brasil Maior%' OR
P.Texto Like '%PAC 2%' OR
P.Texto Like '%articulação intersetorial%' OR
P.Texto Like '%eficiência energética%' OR
P.Texto Like '%exploração sustentável%' OR
P.Texto Like '%potencial florestal%' OR
P.Texto Like '%recursos hídricos%' OR
P.Texto Like '%mudanças climáticas%' OR
P.Texto Like '%controle ambiental%' OR
P.Texto Like '%Plano Brasil Maior%' OR
P.Texto Like '%Assistência técnica e extensão rural%' OR
P.Texto Like '%agricultura familiar%' OR
P.Texto Like '%Programa Trabalho, Emprego e Renda%' OR
P.Texto Like '%crédito produtivo%' OR
P.Texto Like '%microcrédito%' OR
P.Texto Like '%indutoras do trabalho%'



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