USE [dou]
GO

/****** Object:  Table [dbo].[TbEntidadeEntidade]    Script Date: 11/07/2014 22:11:34 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TbEntidadeEntidade](
	[IdEntidadeA] [bigint] NOT NULL,
	[IdEntidadeB] [bigint] NOT NULL,
	[TipoLigacao] [int] NULL,
	[Tempo] [date] NULL,
	[DtProcessado] [datetime] NULL,
	[IdPortaria] [bigint] NULL
) ON [PRIMARY]

GO

