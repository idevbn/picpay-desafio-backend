package com.picpay.challenge.api.controllers;

import com.picpay.challenge.api.exceptionhandler.ApiError;
import com.picpay.challenge.domain.enums.UserType;
import com.picpay.challenge.domain.models.User;
import com.picpay.challenge.infrastructure.IntegrationTestInitializer;
import com.picpay.challenge.records.UserRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTest extends IntegrationTestInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Sql(scripts = "/database/clear_database.sql")
    public void shouldBeAbleToCreateAUser() {
        final String resourceLocation = "/users";

        final UserRecord userRecord = new UserRecord(
                "JOHN",
                "DOE",
                "80497677440",
                BigDecimal.valueOf(100).setScale(2),
                "john_doe@email.com",
                "REGULAR",
                "123456"
        );

        final HttpEntity<UserRecord> httpEntity = new HttpEntity<>(userRecord);

        final ResponseEntity<User> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.POST,
                httpEntity,
                User.class
        );

        final TypedQuery<User> userQuery = this.entityManager
                .createQuery("SELECT user FROM User AS user", User.class);

        assertNotNull(response);
        assertNotNull(response.getBody());

        final User user = userQuery.getResultList().get(0);
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        final BigDecimal balance = user.getBalance();
        final String cpf = user.getCpf();
        final String email = user.getEmail();
        final UserType userType = user.getUserType();

        assertEquals(firstName, userRecord.firstName());
        assertEquals(lastName, userRecord.lastName());
        assertEquals(balance, userRecord.balance());
        assertEquals(cpf, userRecord.cpf());
        assertEquals(email, userRecord.email());
        assertEquals(String.valueOf(userType), userRecord.type());
    }

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/user/create_user.sql"})
    public void shouldNotBeAbleToCreateAUserThatAlreadyExists() {
        final String resourceLocation = "/users";

        final UserRecord userRecord = new UserRecord(
                "JOHN",
                "DOE",
                "80497677440",
                BigDecimal.valueOf(100).setScale(2),
                "john_doe@email.com",
                "REGULAR",
                "123456"
        );

        final HttpEntity<UserRecord> httpEntity = new HttpEntity<>(userRecord);

        final ResponseEntity<ApiError> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.POST,
                httpEntity,
                ApiError.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final ApiError responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertEquals("Usuário já cadastrado.", responseBody.getResume());
    }

}
