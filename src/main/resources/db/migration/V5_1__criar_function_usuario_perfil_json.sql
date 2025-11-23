CREATE OR ALTER PROCEDURE PRC_USUARIO_PERFIL_JSON
    @P_ID_USUARIO INT,
    @O_JSON NVARCHAR(MAX) OUTPUT
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE
@V_NOME NVARCHAR(100),
        @V_EMAIL NVARCHAR(150),
        @V_CARGO NVARCHAR(100),
        @V_TIPO NVARCHAR(20),
        @V_AREA NVARCHAR(100),
        @V_OBJ NVARCHAR(500),
        @V_SOFT NVARCHAR(300),
        @V_HARD NVARCHAR(300),
        @V_QTD_MENTORIAS INT,
        @V_JSON NVARCHAR(MAX);

BEGIN TRY
SELECT @V_NOME = NOME, @V_EMAIL = EMAIL, @V_CARGO = CARGO, @V_TIPO = TIPO_USUARIO
FROM T_MENTORAX_USUARIO
WHERE ID_USUARIO = @P_ID_USUARIO;

SELECT TOP 1
            @V_AREA = AREA_INTERESSE,
    @V_OBJ = OBJETIVOS_PROFISSIONAIS,
       @V_SOFT = SOFT_SKILLS,
       @V_HARD = HARD_SKILLS
FROM T_MENTORAX_PERFIL_PROFISSIONAL
WHERE ID_USUARIO = @P_ID_USUARIO;

SELECT @V_QTD_MENTORIAS = COUNT(*)
FROM T_MENTORAX_MENTORIA
WHERE (ID_MENTOR = @P_ID_USUARIO OR ID_MENTORADO = @P_ID_USUARIO)
  AND ISNULL(STATUS, 'ATIVA') = 'ATIVA';

SET @V_JSON =
        '{' +
          '"id_usuario":' + CAST(@P_ID_USUARIO AS NVARCHAR) + ',' +
          '"nome":"' + ISNULL(@V_NOME,'') + '",' +
          '"email":"' + ISNULL(@V_EMAIL,'') + '",' +
          '"cargo":"' + ISNULL(@V_CARGO,'') + '",' +
          '"tipo_usuario":"' + ISNULL(@V_TIPO,'') + '",' +
          '"perfil":{' +
              '"area_interesse":"' + ISNULL(@V_AREA,'') + '",' +
              '"objetivos":"' + ISNULL(@V_OBJ,'') + '",' +
              '"soft_skills":"' + ISNULL(@V_SOFT,'') + '",' +
              '"hard_skills":"' + ISNULL(@V_HARD,'') + '"' +
          '},' +
          '"mentorias_ativas":' + CAST(ISNULL(@V_QTD_MENTORIAS,0) AS NVARCHAR) +
        '}';

        SET @O_JSON = @V_JSON;
END TRY
BEGIN CATCH
INSERT INTO T_MENTORAX_LOG_ERRO (PROCEDURE_NOME, MENSAGEM_ERRO)
        VALUES ('PRC_USUARIO_PERFIL_JSON', ERROR_MESSAGE());
        SET @O_JSON = '{"erro":"falha_interna"}';
END CATCH
END;
GO
