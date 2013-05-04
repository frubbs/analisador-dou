USE [dou]
GO

/****** Object:  View [dbo].[VwEntidade]    Script Date: 04/05/2013 12:45:26 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [dbo].[VwEntidade] AS
SELECT
 IdEntidade as IdEntidade,
 Texto,
 TipoEntidade
FROM
	dou.dbo.tbentidade
UNION
SELECT
	Co_Orgao+9900000 as IdEntidade, 
	No_Orgao as Texto, 
	'Orgao' as TipoEntidade
FROM
	SiorgSQL.dbo.Ta_Nomes_Orgaos

GO


