USE [SiorgSQL]
GO

/****** Object:  View [dbo].[Vw_Nomes_Orgaos]    Script Date: 31/07/2013 17:31:00 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [dbo].[Vw_Nomes_Orgaos] AS
SELECT H.Co_Orgao as Co_Orgao_Pai, N.Co_Orgao, No_Orgao
  FROM [dbo].[Ta_Nomes_Orgaos] N
  LEFT JOIN [dbo].[Ta_Hierarquia_Orgaos] H ON H.Co_Orgao_Filho = N.Co_Orgao


GO


