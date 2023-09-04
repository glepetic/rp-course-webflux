package com.rp.userservice.infrastructure.boot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class DBSetup {

    private final Resource initSQL;

    private final R2dbcEntityTemplate entityTemplate;

    public DBSetup(Resource initSQL,
                   R2dbcEntityTemplate entityTemplate) {
        this.initSQL = initSQL;
        this.entityTemplate = entityTemplate;
    }

    @PostConstruct
    public void setUp() {
        this.getScriptContent()
                .flatMap(query -> this.entityTemplate
                        .getDatabaseClient()
                        .sql(query)
                        .then())
                .doOnError(err -> log.warn("Problem encountered running initial sql script: {}", err.getMessage()))
                .doOnSuccess(v -> log.info("Ran initial sql script succesfully"))
                .subscribe();
    }

    private Mono<String> getScriptContent() {
        try {
            String content = StreamUtils.copyToString(this.initSQL.getInputStream(), StandardCharsets.UTF_8);
            return Mono.just(content);
        } catch (IOException e) {
            log.error("Could not get content of SQL script: {}", e.getMessage(), e);
            return Mono.error(e);
        }
    }

}
