USE [dou]
GO

/****** Object:  Table [dbo].[TbPortaria]    Script Date: 11/07/2014 22:11:46 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[TbPortaria](
	[IdPortaria] [bigint] IDENTITY(99000000,1) NOT NULL,
	[Identificacao] [varchar](max) NOT NULL,
	[Texto] [varchar](max) NOT NULL,
	[StartOffset] [int] NOT NULL,
	[EndOffset] [int] NOT NULL,
	[NomeArquivo] [nvarchar](max) NULL,
	[TipoPortaria] [nvarchar](50) NULL,
	[Data] [date] NOT NULL,
	[DtProcessado] [datetime] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

