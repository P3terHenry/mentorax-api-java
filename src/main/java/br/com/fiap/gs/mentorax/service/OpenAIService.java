package br.com.fiap.gs.mentorax.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    @Autowired
    private ChatModel chatModel;

    /**
     * Gera conteúdo usando o modelo GPT-4o-mini da OpenAI
     * @param prompt O texto do prompt
     * @return A resposta gerada pela IA
     */
    public String gerarConteudo(String prompt) {
        try {
            // Configurar opções para usar gpt-4o-mini e economizar créditos
            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .model("gpt-4o-mini")
                    .temperature(0.7)
                    .maxTokens(1500)
                    .build();

            Prompt promptObj = new Prompt(prompt, options);

            return chatModel.call(promptObj).getResult().getOutput().getText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar a API da OpenAI: " + e.getMessage(), e);
        }
    }

    /**
     * Gera recomendações personalizadas baseadas no perfil do usuário
     */
    public String gerarRecomendacoesPersonalizadas(String areaInteresse, String objetivos, String experiencia, String softSkills, String hardSkills) {
        String prompt = String.format("""
            Você é um assistente de mentoria profissional especializado em desenvolvimento de carreira.
            
            Baseado no seguinte perfil profissional, gere recomendações práticas e personalizadas:
            
            Área de Interesse: %s
            Objetivos Profissionais: %s
            Experiência: %s
            Soft Skills: %s
            Hard Skills: %s
            
            Por favor, forneça:
            1. 3 recomendações práticas de aprendizado
            2. 2 sugestões de projetos para portfólio
            3. 1 dica sobre networking na área
            
            Seja específico, prático e motivador. Mantenha a resposta concisa.
            """, areaInteresse, objetivos, experiencia, softSkills, hardSkills);

        return gerarConteudo(prompt);
    }

    /**
     * Gera um plano de estudos personalizado
     */
    public String gerarPlanoEstudos(String areaInteresse, String objetivos, int horasSemanais) {
        String prompt = String.format("""
            Você é um especialista em planejamento de carreira e educação continuada.
            
            Crie um plano de estudos estruturado para:
            
            Área de Interesse: %s
            Objetivos: %s
            Disponibilidade: %d horas por semana
            
            O plano deve incluir:
            1. Cronograma semanal dividido em módulos
            2. Recursos de aprendizado recomendados (cursos, livros, documentação)
            3. Marcos/milestones de progresso
            4. Projeto prático final
            
            Seja realista considerando a disponibilidade de tempo. Mantenha a resposta organizada e clara.
            """, areaInteresse, objetivos, horasSemanais);

        return gerarConteudo(prompt);
    }

    /**
     * Analisa o progresso e gera insights
     */
    public String analisarProgresso(String resumoSessoes, String feedbacks) {
        String prompt = String.format("""
            Você é um analista de desenvolvimento profissional especializado em mentorias.
            
            Baseado nos seguintes dados de uma mentoria:
            
            Resumo das Sessões:
            %s
            
            Feedbacks:
            %s
            
            Forneça:
            1. Análise do progresso (pontos fortes e áreas de melhoria)
            2. Recomendações para as próximas sessões
            3. Ajustes sugeridos no plano de mentoria
            
            Seja objetivo, construtivo e focado em ações concretas.
            """, resumoSessoes, feedbacks);

        return gerarConteudo(prompt);
    }
}
