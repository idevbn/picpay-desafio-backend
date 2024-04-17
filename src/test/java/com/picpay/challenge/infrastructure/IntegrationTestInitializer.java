package com.picpay.challenge.infrastructure;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestInitializer {

    @ClassRule
    public static PostgresTestContainer postgresContainerConfig
            = PostgresTestContainer.getInstance();

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Test
    public void simpleAssertIntegrationTestInitializer() {
        Assert.assertTrue(true);
    }

}
