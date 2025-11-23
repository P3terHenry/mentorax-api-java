-- =========================================================
-- V6__popular_dados_iniciais.sql
-- Carga inicial para MentoraX (compatível com SQL Server / Flyway)
-- =========================================================

BEGIN
    DECLARE
@ID_MENTOR_CARLA INT,
        @ID_MENTOR_RAFAEL INT,
        @ID_MENTOR_BEATRIZ INT,
        @ID_MENTORADO_LUCAS INT,
        @ID_MENTORADO_MARIANA INT,
        @V_ID_PERFIL INT,
        @V_ID_MENTORIA INT,
        @V_ID_SESSAO INT,
        @V_ID_PLANO INT,
        @V_ID_HIST INT,
        @V_ID_FEEDBACK INT;

    -- Usuário e Perfil - Carla Menezes
EXEC dbo.PRC_INS_USUARIO
        @P_NOME = 'Carla Menezes',
        @P_EMAIL = 'carla.menezes@techmentor.com',
        @P_SENHA_HASH = 'HASH_CARLA',
        @P_CARGO = 'Tech Lead de IA',
        @P_TIPO_USUARIO = 'MENTOR',
        @P_ATIVO = 'S',
        @O_ID_USUARIO = @ID_MENTOR_CARLA OUTPUT;

EXEC dbo.PRC_INS_PERFIL
        @P_ID_USUARIO = @ID_MENTOR_CARLA,
        @P_AREA = 'Inteligência Artificial',
        @P_OBJ = 'Ajudar novos profissionais a migrar para a área de dados e IA',
        @P_EXP = '12 anos de experiência em projetos de machine learning',
        @P_SOFT = 'Liderança, Comunicação, Empatia',
        @P_HARD = 'Python, TensorFlow, SQL, Docker',
        @P_DISP_HORAS = 5,
        @O_ID_PERFIL = @V_ID_PERFIL OUTPUT;

    -- Usuário e Perfil - Rafael Campos
EXEC dbo.PRC_INS_USUARIO
        @P_NOME = 'Rafael Campos',
        @P_EMAIL = 'rafael.campos@mentorhub.com',
        @P_SENHA_HASH = 'HASH_RAF',
        @P_CARGO = 'Engenheiro de Dados Sênior',
        @P_TIPO_USUARIO = 'MENTOR',
        @P_ATIVO = 'S',
        @O_ID_USUARIO = @ID_MENTOR_RAFAEL OUTPUT;

EXEC dbo.PRC_INS_PERFIL
        @P_ID_USUARIO = @ID_MENTOR_RAFAEL,
        @P_AREA = 'Engenharia de Dados',
        @P_OBJ = 'Capacitar mentorados a construir pipelines escaláveis',
        @P_EXP = '8 anos em Big Data e cloud',
        @P_SOFT = 'Didática, Planejamento, Escuta ativa',
        @P_HARD = 'Python, Spark, Azure, SQL',
        @P_DISP_HORAS = 4,
        @O_ID_PERFIL = @V_ID_PERFIL OUTPUT;

    -- Usuário e Perfil - Beatriz Lobo
EXEC dbo.PRC_INS_USUARIO
        @P_NOME = 'Beatriz Lobo',
        @P_EMAIL = 'beatriz.lobo@mentora.com',
        @P_SENHA_HASH = 'HASH_BEATRIZ',
        @P_CARGO = 'Especialista em UX Design',
        @P_TIPO_USUARIO = 'MENTOR',
        @P_ATIVO = 'S',
        @O_ID_USUARIO = @ID_MENTOR_BEATRIZ OUTPUT;

EXEC dbo.PRC_INS_PERFIL
        @P_ID_USUARIO = @ID_MENTOR_BEATRIZ,
        @P_AREA = 'UX e Experiência do Usuário',
        @P_OBJ = 'Guiar transição de carreira para design de produtos digitais',
        @P_EXP = '10 anos em UX Research e UI Design',
        @P_SOFT = 'Empatia, Comunicação Visual',
        @P_HARD = 'Figma, Design Thinking, HTML, CSS',
        @P_DISP_HORAS = 3,
        @O_ID_PERFIL = @V_ID_PERFIL OUTPUT;

    -- Usuário e Perfil - Lucas Freitas
EXEC dbo.PRC_INS_USUARIO
        @P_NOME = 'Lucas Freitas',
        @P_EMAIL = 'lucas.freitas@empresa.com',
        @P_SENHA_HASH = 'HASH_LUCAS',
        @P_CARGO = 'Analista Administrativo',
        @P_TIPO_USUARIO = 'MENTORADO',
        @P_ATIVO = 'S',
        @O_ID_USUARIO = @ID_MENTORADO_LUCAS OUTPUT;

EXEC dbo.PRC_INS_PERFIL
        @P_ID_USUARIO = @ID_MENTORADO_LUCAS,
        @P_AREA = 'Dados e IA',
        @P_OBJ = 'Migrar da área administrativa para análise de dados',
        @P_EXP = '3 anos em controle financeiro',
        @P_SOFT = 'Organização, Comunicação',
        @P_HARD = 'Excel, SQL, Power BI',
        @P_DISP_HORAS = 6,
        @O_ID_PERFIL = @V_ID_PERFIL OUTPUT;

    -- Usuário e Perfil - Mariana Silva
EXEC dbo.PRC_INS_USUARIO
        @P_NOME = 'Mariana Silva',
        @P_EMAIL = 'mariana.silva@empresa.com',
        @P_SENHA_HASH = 'HASH_MARI',
        @P_CARGO = 'Atendente de Suporte',
        @P_TIPO_USUARIO = 'MENTORADO',
        @P_ATIVO = 'S',
        @O_ID_USUARIO = @ID_MENTORADO_MARIANA OUTPUT;

EXEC dbo.PRC_INS_PERFIL
        @P_ID_USUARIO = @ID_MENTORADO_MARIANA,
        @P_AREA = 'UX Design',
        @P_OBJ = 'Transição para design de experiência do usuário',
        @P_EXP = '2 anos de atendimento ao cliente',
        @P_SOFT = 'Empatia, Criatividade',
        @P_HARD = 'Figma, Canva, HTML',
        @P_DISP_HORAS = 5,
        @O_ID_PERFIL = @V_ID_PERFIL OUTPUT;

    -- Mentoria (Carla → Lucas)
EXEC dbo.PRC_INS_MENTORIA
        @P_ID_MENTOR = @ID_MENTOR_CARLA,
        @P_ID_MENTORADO = @ID_MENTORADO_LUCAS,
        @P_DATA_INICIO = '2024-09-15',
        @P_DATA_FIM = NULL,
        @P_STATUS = 'ATIVA',
        @P_NOTA = NULL,
        @O_ID_MENTORIA = @V_ID_MENTORIA OUTPUT;

    -- Sessões e Feedbacks
EXEC dbo.PRC_INS_SESSAO
        @P_ID_MENTORIA = @V_ID_MENTORIA,
        @P_DATA_SESSAO = '2024-09-20',
        @P_ASSUNTO = 'Introdução à Análise de Dados',
        @P_RESUMO = 'Apresentação dos fundamentos de dados e Python básico',
        @P_PROX = 'Realizar mini-projeto no Power BI',
        @P_HUMOR = 'Motivado',
        @O_ID_SESSAO = @V_ID_SESSAO OUTPUT;

EXEC dbo.PRC_INS_FEEDBACK
        @P_ID_SESSAO = @V_ID_SESSAO,
        @P_NOTA = 5,
        @P_COMENTARIO = 'Sessão inspiradora e prática',
        @O_ID_FEEDBACK = @V_ID_FEEDBACK OUTPUT;

EXEC dbo.PRC_INS_SESSAO
        @P_ID_MENTORIA = @V_ID_MENTORIA,
        @P_DATA_SESSAO = '2024-09-25',
        @P_ASSUNTO = 'Modelagem e Visualização de Dados',
        @P_RESUMO = 'Exploração de dados reais e criação de dashboards',
        @P_PROX = 'Aprimorar storytelling visual',
        @P_HUMOR = 'Engajado',
        @O_ID_SESSAO = @V_ID_SESSAO OUTPUT;

EXEC dbo.PRC_INS_FEEDBACK
        @P_ID_SESSAO = @V_ID_SESSAO,
        @P_NOTA = 4,
        @P_COMENTARIO = 'Excelente dinâmica e aplicação prática',
        @O_ID_FEEDBACK = @V_ID_FEEDBACK OUTPUT;

    -- Plano de Mentoria
EXEC dbo.PRC_INS_PLANO
        @P_ID_MENTORIA = @V_ID_MENTORIA,
        @P_CRONOGRAMA = 'Semana 1: Python; Semana 2: Power BI; Semana 3: Projeto prático;',
        @P_DESC = 'Plano de migração para carreira de dados',
        @P_OKRS = 'O: Migrar para área de dados; KR1: Curso Power BI; KR2: Projeto em Python',
        @P_MSG = 'Continue evoluindo! Você está no caminho certo.',
        @P_DATA_GERACAO = '2024-09-15',
        @O_ID_PLANO = @V_ID_PLANO OUTPUT;

    -- Histórico IA
EXEC dbo.PRC_INS_HIST_IA
        @P_ID_USUARIO = @ID_MENTOR_CARLA,
        @P_PROMPT = 'Gerar plano de estudo personalizado para analista de dados',
        @P_RESPOSTA = 'Plano com Python e Power BI criado',
        @P_DATA_EXEC = '2024-09-15',
        @P_TIPO = 'PLANO IA',
        @O_ID_HIST = @V_ID_HIST OUTPUT;

EXEC dbo.PRC_INS_HIST_IA
        @P_ID_USUARIO = @ID_MENTORADO_LUCAS,
        @P_PROMPT = 'Analisar desempenho de aprendizado do mentorado',
        @P_RESPOSTA = 'IA sugeriu novos tópicos de revisão',
        @P_DATA_EXEC = '2024-09-22',
        @P_TIPO = 'AVALIAÇÃO',
        @O_ID_HIST = @V_ID_HIST OUTPUT;

    PRINT 'Carga de dados inicial da MentoraX inserida com sucesso!';
END;
GO
