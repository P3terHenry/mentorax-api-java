-- =========================================================
-- Procedure – Exportação de Dataset em JSON (manual)
-- Adaptada para SQL Server / Azure SQL
-- =========================================================
CREATE OR ALTER PROCEDURE dbo.PRC_EXPORT_DATASET_JSON
    @O_JSON NVARCHAR(MAX) OUTPUT
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE
@V_JSON NVARCHAR(MAX) = '{"usuarios":[',
        @V_FIRST BIT = 1,
        @ID_USUARIO INT,
        @NOME NVARCHAR(100),
        @EMAIL NVARCHAR(150),
        @CARGO NVARCHAR(100),
        @TIPO_USUARIO NVARCHAR(20),
        @AREA NVARCHAR(100),
        @OBJ NVARCHAR(500),
        @SOFT NVARCHAR(300),
        @HARD NVARCHAR(300),
        @DISP INT,
        @QTD INT,
        @TMP NVARCHAR(MAX);

BEGIN TRY
        DECLARE C_U CURSOR FOR
SELECT ID_USUARIO, NOME, EMAIL, CARGO, TIPO_USUARIO
FROM T_MENTORAX_USUARIO
WHERE ATIVO = 'S';

OPEN C_U;
FETCH NEXT FROM C_U INTO @ID_USUARIO, @NOME, @EMAIL, @CARGO, @TIPO_USUARIO;

WHILE @@FETCH_STATUS = 0
BEGIN
            -- Pega perfil (se houver)
SELECT TOP 1
                @AREA = AREA_INTERESSE,
    @OBJ = OBJETIVOS_PROFISSIONAIS,
       @SOFT = SOFT_SKILLS,
       @HARD = HARD_SKILLS,
       @DISP = DISPONIBILIDADE_HORAS
FROM T_MENTORAX_PERFIL_PROFISSIONAL
WHERE ID_USUARIO = @ID_USUARIO;

IF @@ROWCOUNT = 0
SELECT @AREA = NULL, @OBJ = NULL, @SOFT = NULL, @HARD = NULL, @DISP = 0;

-- Conta mentorias associadas
SELECT @QTD = COUNT(*)
FROM T_MENTORAX_MENTORIA
WHERE (ID_MENTOR = @ID_USUARIO OR ID_MENTORADO = @ID_USUARIO);

-- Monta JSON incremental
IF @V_FIRST = 0 SET @V_JSON += ',';
            SET @V_FIRST = 0;

            SET @TMP =
                '{"id_usuario":' + CAST(@ID_USUARIO AS NVARCHAR) + ',' +
                '"nome":"' + ISNULL(@NOME,'') + '",' +
                '"email":"' + ISNULL(@EMAIL,'') + '",' +
                '"cargo":"' + ISNULL(@CARGO,'') + '",' +
                '"tipo_usuario":"' + ISNULL(@TIPO_USUARIO,'') + '",' +
                '"perfil":{' +
                    '"area_interesse":"' + ISNULL(@AREA,'') + '",' +
                    '"objetivos":"' + ISNULL(@OBJ,'') + '",' +
                    '"soft_skills":"' + ISNULL(@SOFT,'') + '",' +
                    '"hard_skills":"' + ISNULL(@HARD,'') + '",' +
                    '"disponibilidade_horas":' + CAST(ISNULL(@DISP,0) AS NVARCHAR) +
                '},' +
                '"qtd_mentorias":' + CAST(ISNULL(@QTD,0) AS NVARCHAR) +
                '}';

            SET @V_JSON += @TMP;

FETCH NEXT FROM C_U INTO @ID_USUARIO, @NOME, @EMAIL, @CARGO, @TIPO_USUARIO;
END

CLOSE C_U;
DEALLOCATE C_U;

        SET @V_JSON += ']}';
        SET @O_JSON = @V_JSON;
END TRY
BEGIN CATCH
INSERT INTO T_MENTORAX_LOG_ERRO (PROCEDURE_NOME, MENSAGEM_ERRO)
        VALUES ('PRC_EXPORT_DATASET_JSON', ERROR_MESSAGE());
        SET @O_JSON = '{"erro":"falha_exportacao"}';
END CATCH
END;
GO