package com.picpay.challenge.infrastructure;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;

@Log4j2
@Profile("test")
@TestConfiguration
public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String POSTGRES_IMAGE = "postgres:13.3";
    private static final String DATABASE_NAME = "desafio_picpay";

    private PostgresTestContainer() {
        super(POSTGRES_IMAGE);
    }

    public static PostgresTestContainer getInstance() {
        return new PostgresTestContainer()
                .withDatabaseName(DATABASE_NAME)
                .withInitScript("database/initial_commands.sql");
    }

    @Override
    public void start() {
        super.start();

        System.setProperty("DB_URL", this.getJdbcUrl());
        System.setProperty("DB_USERNAME", this.getUsername());
        System.setProperty("DB_PASSWORD", this.getPassword());

        log.info("DB_URL {}", this.getJdbcUrl());
        log.info("DB_USERNAME {}", this.getUsername());
        log.info("DB_PASSWORD {}", this.getPassword());
        log.info("DB_NAME {}", this.getDatabaseName());
        log.info("CONTAINER_ID {}", this.getContainerId());
        log.info("CONTAINER_NAME {}", this.getContainerName());
    }

    @Override
    public void stop() {}

}
