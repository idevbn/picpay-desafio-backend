-- CLEAR SEQUENCES
SELECT
    SETVAL(c.oid, 1, FALSE)
FROM
    pg_class c
    JOIN pg_namespace n ON n.oid = c.relnamespace
WHERE
    c.relkind = 'S';

-- Função que remove todos os dados de todas as tabelas dos bancos de dados de um determinado usuário
CREATE OR REPLACE FUNCTION truncate_tables(username IN VARCHAR) RETURNS void AS '
DECLARE
    statements CURSOR FOR
        SELECT tablename, schemaname FROM pg_tables
        WHERE tableowner = username AND tablename ILIKE ''tb_%''; -- Para não pegar as tabelas do flyway
BEGIN
    FOR stmt IN statements LOOP
        EXECUTE ''TRUNCATE TABLE '' || quote_ident(stmt.schemaname) || ''.'' || quote_ident(stmt.tablename) || '' CASCADE;'';
    END LOOP;
END;
' LANGUAGE plpgsql;

-- Chamando a função para remover os dados
SELECT truncate_tables('test');