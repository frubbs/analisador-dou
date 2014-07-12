USE [dou]
GO

/****** Object:  Table [dbo].[TbEntidade]    Script Date: 11/07/2014 22:11:18 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TbEntidade](
	[IdEntidade] [bigint] IDENTITY(1,1) NOT NULL,
	[Texto] [nvarchar](100) NOT NULL,
	[TipoEntidade] [nvarchar](10) NULL,
	[DtProcessado] [datetime] NULL,
	[Particao] [nvarchar](50) NULL
) ON [PRIMARY]

GO

