USE [dou]
GO

/****** Object:  StoredProcedure [dbo].[processaRegistro1Modov2]    Script Date: 11/07/2014 22:12:19 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO








CREATE procedure [dbo].[processaRegistro1Modov2] -- v2 = com coluna particao
(
	@EntidadeA varchar(100),
	@EntidadeAStart int,
	@EntidadeAEnd int,
	@TipoEntidadeA nvarchar(10),
	@ParticaoA nvarchar(50),
	
	@EntidadeB varchar(100),
	@EntidadeBStart int,
	@EntidadeBEnd int,
	@TipoEntidadeB nvarchar(10),
	@ParticaoB nvarchar(50),
	
	
	@Identificacao varchar(MAX),
	@TextoPortaria varchar(MAX),
	@PortariaStart int,
	@PortariaEnd int,
	@TipoPortaria nvarchar(50),
	@NomeArquivo nvarchar(MAX),
	@Data date
	
)
AS
	DECLARE @UpdateVariable bit
	DECLARE @ChangeResultEntidadeA TABLE (Id BIGINT)
	DECLARE @ChangeResultEntidadeB TABLE (Id BIGINT)
	DECLARE @ChangeResultPortaria TABLE (Id BIGINT)
	

	/* Insere a Entidade se for nova, preenche o Id para uso a seguir*/
	MERGE dbo.TbEntidade AS target   -- http://stackoverflow.com/questions/10650776/how-do-you-get-the-identity-value-after-using-merge-when-there-is-a-match
    USING (SELECT @EntidadeA) AS source (texto)
    ON (target.Texto = source.texto)
    WHEN MATCHED THEN 
        UPDATE SET @UpdateVariable = 1
    WHEN NOT MATCHED THEN    
        INSERT (Texto, TipoEntidade, DtProcessado, Particao)
        VALUES (source.Texto, @TipoEntidadeA, GETDATE(), @ParticaoA)
	OUTPUT  inserted.IdEntidade INTO @ChangeResultEntidadeA;
	
	DECLARE @IdEntidadeA int
	Select @IdEntidadeA = Id from @ChangeResultEntidadeA


	/* Insere a Entidade se for nova, preenche o Id para uso a seguir*/
	MERGE dbo.TbEntidade AS target   -- http://stackoverflow.com/questions/10650776/how-do-you-get-the-identity-value-after-using-merge-when-there-is-a-match
    USING (SELECT @EntidadeB) AS source (texto)
    ON (target.Texto = source.texto)
    WHEN MATCHED THEN 
        UPDATE SET @UpdateVariable = 1
    WHEN NOT MATCHED THEN    
        INSERT (Texto, TipoEntidade, DtProcessado, Particao)
        VALUES (source.Texto, @TipoEntidadeB, GETDATE(), @ParticaoB)
	OUTPUT  inserted.IdEntidade INTO @ChangeResultEntidadeB;
	
	DECLARE @IdEntidadeB int
	Select @IdEntidadeB = Id from @ChangeResultEntidadeB



	/* Insere a Portaria se ainda nao existir */
	MERGE dbo.TbPortaria AS target  
    USING (SELECT @Identificacao) AS source (Identificacao)
    ON (target.Identificacao = source.Identificacao)
    WHEN MATCHED THEN 
        UPDATE SET @UpdateVariable = 1
    WHEN NOT MATCHED THEN    
        INSERT (Identificacao, Texto, StartOffset, EndOffset, NomeArquivo, TipoPortaria, Data, DtProcessado)
        VALUES (source.Identificacao, @TextoPortaria, @PortariaStart,@PortariaEnd, @NomeArquivo, @TipoPortaria, @Data, GETDATE())
	OUTPUT  inserted.IdPortaria INTO @ChangeResultPortaria;
	
	DECLARE @IdPortaria int
	Select @Idportaria = Id from @ChangeResultPortaria
	
	/*cria a ligacao. em nome do desempenho nao vamos fazer merge aqui. se ocorrer inconsistencias teremos que fazer.*/
	INSERT INTO TbEntidadeEntidade (IdEntidadeA, IdEntidadeB, TipoLigacao ,Tempo, DtProcessado, IdPortaria)
	VALUES (@IdEntidadeA, @IdEntidadeB, 2, @Data,GETDATE(), @Idportaria); -- tipo de ligacao 1 = relacao no dou. 2 - contrato


	select 1
 /*
 insert into tbPortaria values (@param1)
 select 2

 --grant all on dbo.tbPortaria to public


 select * from tbPortaria

 */




GO

