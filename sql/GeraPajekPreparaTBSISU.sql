SELECT 
	ISNULL(e.identidade,999999) as identidade, 
	ISNULL(s.sisu,99) as sisu, 
	ISNULL(e.texto, 'null') as entdade, 
	ISNULL(s.ie,'null') as entidade2
INTO
	TbSisu
FROM 
	VwSisu s
	LEFT JOIN TbEntidade e 
		on 
			UPPER(e.Texto) = UPPER(s.ie) OR
			'FUNDAÇÃO ' + UPPER(e.Texto) = UPPER(s.ie)	OR
			'FUNDAÇÃO ' + UPPER(s.ie) = UPPER(e.Texto)
--ORDER BY 3 desc

