USE [dou]
GO

/****** Object:  StoredProcedure [dbo].[processaRegistro]    Script Date: 31/07/2013 17:29:29 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE procedure [dbo].[processaRegistro]
(
	@Entidade varchar(100),
	@Identificacao bigint,
	@TextoPortaria nvarchar(MAX),
	@PortariaStart int,
	@PortariaEnd int,
	@EntidadeStart int,
	@EntidadeEnd int,
	@TipoEntidade nvarchar(10),
	@TipoPortaria nvarchar(50),
	@NomeArquivo nvarchar(MAX),
	@Data date
	
)
AS
	DECLARE @UpdateVariable bit
	DECLARE @ChangeResultEntidade TABLE (Id BIGINT)
	DECLARE @ChangeResultPortaria TABLE (Id BIGINT)
	

	/* Insere a Entidade se for nova, preenche o Id para uso a seguir*/
	MERGE dbo.TbEntidade AS target   -- http://stackoverflow.com/questions/10650776/how-do-you-get-the-identity-value-after-using-merge-when-there-is-a-match
    USING (SELECT @Entidade) AS source (texto)
    ON (target.Texto = source.texto)
    WHEN MATCHED THEN 
        UPDATE SET @UpdateVariable = 1
    WHEN NOT MATCHED THEN    
        INSERT (Texto, TipoEntidade)
        VALUES (source.Texto, @TipoEntidade)
	OUTPUT  inserted.IdEntidade INTO @ChangeResultEntidade;
	
	DECLARE @IdEntidade int
	Select @IdEntidade = Id from @ChangeResultEntidade


	/* Insere a Portaria se ainda nao existir */
	MERGE dbo.TbPortaria AS target  
    USING (SELECT @Identificacao) AS source (Identificacao)
    ON (target.Identificacao = source.Identificacao)
    WHEN MATCHED THEN 
        UPDATE SET @UpdateVariable = 1
    WHEN NOT MATCHED THEN    
        INSERT (Identificacao, Texto, StartOffset, EndOffset, NomeArquivo, TipoPortaria, Data)
        VALUES (source.Identificacao, @TextoPortaria, @PortariaStart,@PortariaEnd, @NomeArquivo, @TipoPortaria, @Data)
	OUTPUT  inserted.IdPortaria INTO @ChangeResultPortaria;
	
	DECLARE @IdPortaria int
	Select @Idportaria = Id from @ChangeResultPortaria
	
	/*cria a ligacao. em nome do desempenho nao vamos fazer merge aqui. se ocorrer inconsistencias teremos que fazer.*/
	INSERT INTO tbPortariaEntidade (IdPortaria, IdEntidade, TipoLigacao,Tempo)
	VALUES (@IdPortaria, @IdEntidade, 2, @Data); -- tipo de ligacao 1 = relacao no dou. 2 - contrato


	select 1
 /*
 insert into tbPortaria values (@param1)
 select 2

 --grant all on dbo.tbPortaria to public


 select * from tbPortaria

 */

GO


