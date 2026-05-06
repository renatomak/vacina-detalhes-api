package br.gov.saude.goiania.saude.api.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Configuração do banco de dados embutido (PostgreSQL real via EmbeddedPostgres).
 * Substitui o H2 nos perfis local e test, garantindo compatibilidade total
 * com a sintaxe PostgreSQL usada nas queries da aplicação.
 */
@Configuration
@Profile({"local", "test"})
public class EmbeddedPostgresConfig {

    @Bean(destroyMethod = "close")
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        return EmbeddedPostgres.start();
    }

    @Primary
    @Bean
    public DataSource dataSource(EmbeddedPostgres embeddedPostgres) {
        return embeddedPostgres.getPostgresDatabase();
    }
}

