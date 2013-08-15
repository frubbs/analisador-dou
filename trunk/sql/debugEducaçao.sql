select * from tbportariaentidade pe

join tbentidade e on e.IdEntidade = pe.IdEntidade
join TbPortaria p on p.IdPortaria = pe.IdPortaria
where pe.idportaria = 99022816