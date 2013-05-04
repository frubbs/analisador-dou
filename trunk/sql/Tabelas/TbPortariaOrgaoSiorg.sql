USE [dou]
GO

/****** Object:  Table [dbo].[TbPortariaOrgaoSiorg]    Script Date: 04/05/2013 12:48:57 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TbPortariaOrgaoSiorg](
	[IdPortaria] [bigint] NOT NULL,
	[IdEntidade] [bigint] NOT NULL,
	[TipoLigacao] [int] NULL,
	[Tempo] [date] NULL
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[TbPortariaOrgaoSiorg]  WITH CHECK ADD  CONSTRAINT [FK_TbPortariaOrgaoSiorg_TbPortaria] FOREIGN KEY([IdPortaria])
REFERENCES [dbo].[TbPortaria] ([IdPortaria])
GO

ALTER TABLE [dbo].[TbPortariaOrgaoSiorg] CHECK CONSTRAINT [FK_TbPortariaOrgaoSiorg_TbPortaria]
GO


