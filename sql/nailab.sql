--
-- script de criaçao da estrutura do nailab
--
-- query consulta para tradução
SELECT id, natural_text, action, MATCH (action,natural_text) AGAINST  ('{texto}'  IN NATURAL LANGUAGE MODE) AS score 
FROM nailab.`natural_text_actions`
WHERE MATCH (action,natural_text) AGAINST ('{texto}' IN NATURAL LANGUAGE MODE)
order by 4 desc  
limit 1
