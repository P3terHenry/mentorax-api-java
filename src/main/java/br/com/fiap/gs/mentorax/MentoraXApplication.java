package br.com.fiap.gs.mentorax;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class MentoraXApplication {

    public static void main(String[] args) {
        // Carrega .env apenas se existir, sem quebrar em produção
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // <--- evita exception se .env não existir
                .load();

        // Seta variáveis de ambiente apenas se ainda não estiverem definidas
        setIfAbsent("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        setIfAbsent("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        setIfAbsent("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        setIfAbsent("SPRING_DATASOURCE_DRIVER", dotenv.get("SPRING_DATASOURCE_DRIVER"));
        setIfAbsent("OPEN_AI_KEY", dotenv.get("OPEN_AI_KEY"));

        SpringApplication.run(MentoraXApplication.class, args);
    }

    private static void setIfAbsent(String key, String value) {
        if (System.getenv(key) == null && value != null) {
            System.setProperty(key, value);
        }
    }

}
