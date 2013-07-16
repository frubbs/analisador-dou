use dou

declare @entidades bigint
declare @portarias bigint

select @entidades = count(*) from TbEntidade

select @portarias = count(*) from TbPortaria

select '*Vertices ' + cast((@entidades + @portarias) as nvarchar) + ' ' + cast(@entidades as nvarchar)

select cast(IdEntidade as nvarchar) + '  "' + SUBSTRING(Texto,0,30) + '"                                                               0.0000    0.0000    0.5000 ' from TbEntidade
select cast(IdPortaria as nvarchar) + '  "' + SUBSTRING(cast(Identificacao as nvarchar),0,30) + '"                                                               0.0000    0.0000    0.5000 ' from TbPortaria

select '*Arcs' 

select cast(IdEntidade as nvarchar) + ' ' + cast(IdPortaria as nvarchar) + ' [' + cast(DENSE_RANK() OVER (ORDER BY Tempo DESC) as nvarchar) +']'  from tbportariaentidade 




