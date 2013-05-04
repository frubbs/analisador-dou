USE [dou]
GO

/****** Object:  Table [dbo].[TbPortariaEntidade]    Script Date: 04/05/2013 12:48:37 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TbPortariaEntidade](
	[IdPortaria] [bigint] NOT NULL,
	[IdEntidade] [bigint] NOT NULL,
	[TipoLigacao] [int] NULL,
	[Tempo] [date] NULL
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[TbPortariaEntidade]  WITH CHECK ADD  CONSTRAINT [FK_TbPortariaEntidade_TbEntidade] FOREIGN KEY([IdEntidade])
REFERENCES [dbo].[TbEntidade] ([IdEntidade])
GO

ALTER TABLE [dbo].[TbPortariaEntidade] CHECK CONSTRAINT [FK_TbPortariaEntidade_TbEntidade]
GO

ALTER TABLE [dbo].[TbPortariaEntidade]  WITH CHECK ADD  CONSTRAINT [FK_TbPortariaEntidade_TbPortaria] FOREIGN KEY([IdPortaria])
REFERENCES [dbo].[TbPortaria] ([IdPortaria])
GO

ALTER TABLE [dbo].[TbPortariaEntidade] CHECK CONSTRAINT [FK_TbPortariaEntidade_TbPortaria]
GO

ALTER TABLE [dbo].[TbPortariaEntidade]  WITH CHECK ADD  CONSTRAINT [FK_TbPortariaOrgaoSiorge_TbPortaria] FOREIGN KEY([IdPortaria])
REFERENCES [dbo].[TbPortaria] ([IdPortaria])
GO

ALTER TABLE [dbo].[TbPortariaEntidade] CHECK CONSTRAINT [FK_TbPortariaOrgaoSiorge_TbPortaria]
GO


