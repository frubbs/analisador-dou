USE [dou]
GO

/****** Object:  StoredProcedure [dbo].[processaOrgaoSiorg]    Script Date: 04/05/2013 14:07:51 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


ALTER procedure [dbo].[processaOrgaoSiorg]
(
	@NomeOrgao varchar(50),
	@CodigoOrgaoSiorg bigint,
	@Particao int,
	@Identificacao bigint,
	@TextoPortaria nvarchar(MAX),
	@PortariaStart int,
	@PortariaEnd int,
	@EntidadeStart int,
	@EntidadeEnd int,
	@TipoEntidade nvarchar(10),
	@Data date
	
)
AS
	DECLARE @UpdateVariable bit
	DECLARE @ChangeResultPortaria TABLE (Id BIGINT)
	
	/* Insere a Portaria se ainda nao existir preenche o Id para uso a seguir*/
	MERGE dbo.TbPortaria AS target								-- http://stackoverflow.com/questions/10650776/how-do-you-get-the-identity-value-after-using-merge-when-there-is-a-match
    USING (SELECT @Identificacao) AS source (Identificacao)
    ON (target.Identificacao = source.Identificacao)
    WHEN MATCHED THEN 
        UPDATE SET @UpdateVariable = 1
    WHEN NOT MATCHED THEN    
        INSERT (Identificacao, Texto, StartOffset, EndOffset, Data)
        VALUES (source.Identificacao, @TextoPortaria, @PortariaStart,@PortariaEnd, @Data)
	OUTPUT  inserted.IdPortaria INTO @ChangeResultPortaria;
	
	DECLARE @IdPortaria int
	Select @Idportaria = Id from @ChangeResultPortaria
	
	/*cria a ligacao. em nome do desempenho nao vamos fazer merge aqui. se ocorrer inconsistencias teremos que fazer.*/
	INSERT INTO TbPortariaOrgaoSiorg (IdPortaria, IdEntidade, TipoLigacao,Tempo)
	VALUES (@IdPortaria, @CodigoOrgaoSiorg, 1, @Data); -- tipo de ligacao 1 = relacao no dou. 2 - contrato

	select 1

GO


