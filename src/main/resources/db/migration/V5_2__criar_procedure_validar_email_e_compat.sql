CREATE OR ALTER PROCEDURE PRC_VALIDAR_EMAIL_E_COMPAT
    @P_EMAIL NVARCHAR(150),
    @P_HARD_SKILLS NVARCHAR(MAX),   -- ex: 'JAVA,SQL,SPRING,REACT'
    @P_REQUISITOS NVARCHAR(MAX),    -- ex: 'JAVA,ORACLE,SPRING,DOCKER'
    @O_JSON NVARCHAR(MAX) OUTPUT
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE
@V_VALIDO CHAR(1) = 'N',
        @V_MATCHES INT = 0,
        @V_TOTAL_REQ INT = 0,
        @V_SCORE DECIMAL(5,2) = 0,
        @V_COMUM NVARCHAR(MAX) = '',
        @V_FALTANTES NVARCHAR(MAX) = '',
        @REQ NVARCHAR(MAX),
        @HAVE NVARCHAR(MAX),
        @TOKEN NVARCHAR(200),
        @POS INT,
        @COMMA INT;

BEGIN TRY
        -- Validação de e-mail usando LIKE (substituto de REGEXP_LIKE)
IF @P_EMAIL LIKE '%_@_%_.%' SET @V_VALIDO = 'S';
ELSE SET @V_VALIDO = 'N';

        -- Verifica se requisitos foram informados
        IF @P_REQUISITOS IS NULL OR LTRIM(RTRIM(@P_REQUISITOS)) = ''
BEGIN
            SET @O_JSON = '{"erro":"REQUISITOS_NAO_INFORMADOS"}';
            RETURN;
END

        -- Normaliza strings
        SET @REQ = UPPER(REPLACE(ISNULL(@P_REQUISITOS,''),' ',''));
        SET @HAVE = UPPER(REPLACE(ISNULL(@P_HARD_SKILLS,''),' ',''));
        SET @POS = 1;

        WHILE CHARINDEX(',', @REQ) > 0
BEGIN
            SET @COMMA = CHARINDEX(',', @REQ);
            SET @TOKEN = LEFT(@REQ, @COMMA - 1);
            SET @REQ = SUBSTRING(@REQ, @COMMA + 1, LEN(@REQ));

            IF LEN(@TOKEN) > 0
BEGIN
                SET @V_TOTAL_REQ += 1;
                IF CHARINDEX(@TOKEN, @HAVE) > 0
BEGIN
                    SET @V_MATCHES += 1;
                    SET @V_COMUM += CASE WHEN @V_COMUM = '' THEN '' ELSE ',' END + @TOKEN;
END
ELSE
                    SET @V_FALTANTES += CASE WHEN @V_FALTANTES = '' THEN '' ELSE ',' END + @TOKEN;
END
END

        -- Último token (sem vírgula final)
        IF LEN(@REQ) > 0
BEGIN
            SET @V_TOTAL_REQ += 1;
            IF CHARINDEX(@REQ, @HAVE) > 0
BEGIN
                SET @V_MATCHES += 1;
                SET @V_COMUM += CASE WHEN @V_COMUM = '' THEN '' ELSE ',' END + @REQ;
END
ELSE
                SET @V_FALTANTES += CASE WHEN @V_FALTANTES = '' THEN '' ELSE ',' END + @REQ;
END

        IF @V_TOTAL_REQ = 0
BEGIN
            SET @O_JSON = '{"erro":"REQUISITOS_INVALIDOS"}';
            RETURN;
END

        SET @V_SCORE = ROUND((CAST(@V_MATCHES AS FLOAT) / @V_TOTAL_REQ) * 100, 2);

        SET @O_JSON = '{' +
            '"email_valido":"' + @V_VALIDO + '",' +
            '"score_compatibilidade":' + CAST(@V_SCORE AS NVARCHAR(10)) + ',' +
            '"skills_em_comum":"' + ISNULL(@V_COMUM,'') + '",' +
            '"skills_faltantes":"' + ISNULL(@V_FALTANTES,'') + '"' +
        '}';

END TRY
BEGIN CATCH
INSERT INTO T_MENTORAX_LOG_ERRO (PROCEDURE_NOME, MENSAGEM_ERRO)
        VALUES ('PRC_VALIDAR_EMAIL_E_COMPAT', ERROR_MESSAGE());
        SET @O_JSON = '{"erro":"falha_validacao_ou_calculo"}';
END CATCH
END;
GO
