USE [dou]
GO

/****** Object:  Table [dbo].[TbPortaria]    Script Date: 04/05/2013 12:49:21 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[TbPortaria](
	[IdPortaria] [bigint] IDENTITY(99000000,1) NOT NULL,
	[Identificacao] [bigint] NOT NULL,
	[Texto] [varchar](max) NOT NULL,
	[StartOffset] [int] NOT NULL,
	[EndOffset] [int] NOT NULL,
	[Data] [date] NOT NULL,
 CONSTRAINT [PK_TbPortaria_1] PRIMARY KEY CLUSTERED 
(
	[IdPortaria] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


