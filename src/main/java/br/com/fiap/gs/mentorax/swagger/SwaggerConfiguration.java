package br.com.fiap.gs.mentorax.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfiguration {

    @Bean
    OpenAPI configurarSwagger() {

        return new OpenAPI().info(new Info()
                .title("MentoraX - Plataforma de Mentorias Inteligentes")
                .description("A MentoraX é uma plataforma voltada à gestão e acompanhamento de mentorias profissionais, "
                        + "conectando mentores experientes a mentorados em desenvolvimento de carreira. "
                        + "O sistema integra funcionalidades de IA para geração de planos personalizados, "
                        + "acompanhamento de progresso e análise de desempenho.")
                .summary("API REST desenvolvida em Java com Spring Boot, integrando controle de usuários, "
                        + "mentorias, sessões, planos, feedbacks e histórico de IA, com versionamento de banco via Flyway.")
                .version("v1.0.0"));
    }


}
