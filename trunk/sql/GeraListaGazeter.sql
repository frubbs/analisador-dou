use SiorgSQL


SELECT  --Orgaos subordinados a orgaos subordinados a presidencia
	Nome + '@IdOrgao=' + CAST(Co_Orgao as varchar) + '@Particao=' + CAST(Particao as varchar)
FROM 
	VwEntidadeParticao
ORDER BY Co_Orgao
