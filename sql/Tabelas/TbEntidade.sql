USE [dou]
GO

/****** Object:  Table [dbo].[TbEntidade]    Script Date: 31/07/2013 17:22:52 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TbEntidade](
	[IdEntidade] [bigint] IDENTITY(1,1) NOT NULL,
	[Texto] [nvarchar](100) NOT NULL,
	[TipoEntidade] [nvarchar](10) NULL,
 CONSTRAINT [PK_TbEntidade] PRIMARY KEY CLUSTERED 
(
	[IdEntidade] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO


