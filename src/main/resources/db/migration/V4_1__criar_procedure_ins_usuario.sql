-- =========================================================
-- PROCEDURE: Inserir Usuário (Atualizada com campos de recuperação)
-- Banco: SQL Server / Azure
-- =========================================================

CREATE OR ALTER PROCEDURE PRC_INS_USUARIO
    @P_NOME NVARCHAR(100),
    @P_EMAIL NVARCHAR(150),
    @P_SENHA_HASH NVARCHAR(255),
    @P_CARGO NVARCHAR(100),
    @P_TIPO_USUARIO NVARCHAR(20),
    @P_ATIVO CHAR(1) = 'S',
    @P_CODIGO_RECUPERACAO NVARCHAR(10) = NULL,
    @P_CODIGO_EXPIRA_EM DATETIME = NULL,
    @P_TENTATIVAS_REC INT = 0,
    @P_ULTIMA_RECUPERACAO DATETIME = NULL,
    @O_ID_USUARIO INT OUTPUT
    AS
BEGIN
    SET NOCOUNT ON;

BEGIN TRY
INSERT INTO T_MENTORAX_USUARIO (
            NOME,
            EMAIL,
            SENHA_HASH,
            CARGO,
            TIPO_USUARIO,
            ATIVO,
            CODIGO_RECUPERACAO,
            CODIGO_RECUPERACAO_EXPIRA_EM,
            CODIGO_RECUPERACAO_TENTATIVAS,
            ULTIMA_RECUPERACAO_EM
        )
        VALUES (
            @P_NOME,
            @P_EMAIL,
            @P_SENHA_HASH,
            @P_CARGO,
            @P_TIPO_USUARIO,
            @P_ATIVO,
            @P_CODIGO_RECUPERACAO,
            @P_CODIGO_EXPIRA_EM,
            @P_TENTATIVAS_REC,
            @P_ULTIMA_RECUPERACAO
        );

        SET @O_ID_USUARIO = SCOPE_IDENTITY();
END TRY
BEGIN CATCH
INSERT INTO T_MENTORAX_LOG_ERRO (PROCEDURE_NOME, MENSAGEM_ERRO)
        VALUES ('PRC_INS_USUARIO', ERROR_MESSAGE());
        THROW;
END CATCH
END;
GO
