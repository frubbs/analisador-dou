USE [dou]
GO

/****** Object:  View [dbo].[VwLigacao]    Script Date: 31/07/2013 17:30:04 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [dbo].[VwLigacao] AS
select 
	SUBSTRING(P.Texto,0,70) InicioPortaria, E.Texto, P.Data 
from 
	TbPortariaEntidade PE
JOIN 
	TbPortaria P on PE.IdPortaria = P.IdPortaria
JOIN
	TbEntidade E on PE.IdEntidade = E.IdEntidade
GO


