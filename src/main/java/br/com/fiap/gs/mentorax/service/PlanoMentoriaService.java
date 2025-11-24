package br.com.fiap.gs.mentorax.service;

import br.com.fiap.gs.mentorax.model.Mentoria;
import br.com.fiap.gs.mentorax.model.PerfilProfissional;
import br.com.fiap.gs.mentorax.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanoMentoriaService {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private PerfilRepository perfilRepository;

    /**
     * Gera um plano de mentoria completo usando IA
     */
    public String gerarPlanoComIA(Mentoria mentoria) {
        // Buscar perfis de mentor e mentorado
        PerfilProfissional perfilMentor = perfilRepository.findByUsuarioIdUsuario(mentoria.getMentor().getIdUsuario())
                .orElse(null);

        PerfilProfissional perfilMentorado = perfilRepository.findByUsuarioIdUsuario(mentoria.getMentorado().getIdUsuario())
                .orElse(null);

        String infoMentor = perfilMentor != null
                ? String.format("Mentor - Área: %s, Experiência: %s",
                        perfilMentor.getAreaInteresse(), perfilMentor.getExperienciaResumida())
                : "Mentor: " + mentoria.getMentor().getNome();

        String infoMentorado = perfilMentorado != null
                ? String.format("Mentorado - Área: %s, Objetivos: %s, Experiência: %s, Disponibilidade: %d horas/semana",
                        perfilMentorado.getAreaInteresse(),
                        perfilMentorado.getObjetivosProfissionais(),
                        perfilMentorado.getExperienciaResumida(),
                        perfilMentorado.getDisponibilidadeHoras())
                : "Mentorado: " + mentoria.getMentorado().getNome();

        String prompt = String.format("""
            Você é um especialista em planejamento de mentorias profissionais.
            
            Crie um plano de mentoria completo e estruturado para:
            
            %s
            %s
            
            IMPORTANTE: Formate sua resposta EXATAMENTE com os seguintes marcadores para cada seção:
            
            [INICIO_CRONOGRAMA]
            Aqui deve conter o cronograma detalhado de 6 meses dividido em 3 fases:
            - Fase 1 (Mês 1-2): Fundamentos e Diagnóstico
            - Fase 2 (Mês 3-4): Desenvolvimento e Prática
            - Fase 3 (Mês 5-6): Consolidação e Projeto Final
            [FIM_CRONOGRAMA]
            
            [INICIO_DESCRICAO]
            Aqui deve conter a descrição geral incluindo:
            - Visão geral do programa
            - Metodologia de acompanhamento
            - Frequência de sessões sugerida
            [FIM_DESCRICAO]
            
            [INICIO_METAS]
            Aqui devem estar as metas OKR:
            - 3 Objetivos principais
            - 2-3 Key Results mensuráveis para cada objetivo
            [FIM_METAS]
            
            [INICIO_MENSAGEM]
            Aqui deve conter a mensagem motivacional personalizada focando nos benefícios e crescimento
            [FIM_MENSAGEM]
            
            USE EXATAMENTE esses marcadores ([INICIO_xxx] e [FIM_xxx]) para delimitar cada seção.
            Seja específico, prático e realista no conteúdo entre os marcadores.
            """, infoMentor, infoMentorado);

        return openAIService.gerarConteudo(prompt);
    }

    /**
     * Extrai seções específicas do plano gerado pela IA
     */
    public String[] extrairSecoes(String planoCompleto) {
        // Retorna: [cronograma, descricao, metas, mensagem]
        String[] secoes = new String[4];

        // Tentar extrair usando os marcadores específicos
        secoes[0] = extrairSecao(planoCompleto, "[INICIO_CRONOGRAMA]", "[FIM_CRONOGRAMA]");
        secoes[1] = extrairSecao(planoCompleto, "[INICIO_DESCRICAO]", "[FIM_DESCRICAO]");
        secoes[2] = extrairSecao(planoCompleto, "[INICIO_METAS]", "[FIM_METAS]");
        secoes[3] = extrairSecao(planoCompleto, "[INICIO_MENSAGEM]", "[FIM_MENSAGEM]");

        // Fallback: se não conseguiu extrair com marcadores, usa estratégia de palavras-chave
        if (secoes[0] == null || secoes[0].trim().isEmpty()) {
            return extrairSecoesLegado(planoCompleto);
        }

        return secoes;
    }

    /**
     * Extrai uma seção específica entre marcadores
     */
    private String extrairSecao(String texto, String marcadorInicio, String marcadorFim) {
        try {
            int inicio = texto.indexOf(marcadorInicio);
            int fim = texto.indexOf(marcadorFim);

            if (inicio != -1 && fim != -1 && fim > inicio) {
                return texto.substring(inicio + marcadorInicio.length(), fim).trim();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Método legado de extração por palavras-chave (fallback)
     */
    private String[] extrairSecoesLegado(String planoCompleto) {
        String[] secoes = new String[4];

        // Estratégia simples: dividir por palavras-chave
        String[] linhas = planoCompleto.split("\n");
        StringBuilder cronograma = new StringBuilder();
        StringBuilder descricao = new StringBuilder();
        StringBuilder metas = new StringBuilder();
        StringBuilder mensagem = new StringBuilder();

        int secaoAtual = -1; // -1=nenhuma, 0=cronograma, 1=descricao, 2=metas, 3=mensagem

        for (String linha : linhas) {
            String linhaUpper = linha.toUpperCase();

            if (linhaUpper.contains("CRONOGRAMA") || linhaUpper.contains("FASE")) {
                secaoAtual = 0;
                continue;
            } else if (linhaUpper.contains("DESCRIÇÃO") || linhaUpper.contains("DESCRIÇAO") || linhaUpper.contains("VISÃO GERAL")) {
                secaoAtual = 1;
                continue;
            } else if (linhaUpper.contains("METAS") || linhaUpper.contains("OKR") || linhaUpper.contains("OBJECTIVES")) {
                secaoAtual = 2;
                continue;
            } else if (linhaUpper.contains("MENSAGEM") || linhaUpper.contains("MOTIVACIONAL")) {
                secaoAtual = 3;
                continue;
            }

            switch (secaoAtual) {
                case 0 -> cronograma.append(linha).append("\n");
                case 1 -> descricao.append(linha).append("\n");
                case 2 -> metas.append(linha).append("\n");
                case 3 -> mensagem.append(linha).append("\n");
            }
        }

        // Se não conseguiu extrair, usa o plano completo para todos
        secoes[0] = cronograma.length() > 0 ? cronograma.toString() : planoCompleto;
        secoes[1] = descricao.length() > 0 ? descricao.toString() : "Ver plano completo";
        secoes[2] = metas.length() > 0 ? metas.toString() : "Ver plano completo";
        secoes[3] = mensagem.length() > 0 ? mensagem.toString() : "Continue focado nos seus objetivos!";

        return secoes;
    }
}
