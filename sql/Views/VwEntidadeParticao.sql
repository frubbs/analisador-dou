USE [SiorgSQL]
GO

/****** Object:  View [dbo].[VwEntidadeParticao]    Script Date: 31/07/2013 17:31:16 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



/*
verificacao 1 - deve ter o mesmo tamanho - 69445
  select count(*) from [Vw_Nomes_Orgaos]
  select count(*) from [Ta_Nomes_Orgaos]


*/


CREATE VIEW [dbo].[VwEntidadeParticao] AS
SELECT 
	*
FROM
	VwEntidadeParticao3Nivel

UNION

SELECT 
	*
FROM
	VwEntidadeParticao2Nivel

UNION

SELECT 
	*
FROM
	VwEntidadeParticao1Nivel

GO


