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
import org.springframework.http.*;
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

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/user/create_user.sql"})
    public void shouldBeAbleToGetAnUserById() {
        final String resourceLocation = "/users/1";

        final ResponseEntity<User> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.GET,
                new HttpEntity<>(User.class),
                User.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final User responseBody = response.getBody();
        final HttpStatusCode statusCode = response.getStatusCode();

        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(1L, responseBody.getId());
        assertNotNull(responseBody.getCpf());
        assertNotNull(responseBody.getEmail());
        assertNotNull(responseBody.getPassword());
        assertNotNull(responseBody.getUserType());
        assertNotNull(responseBody.getFirstName());
        assertNotNull(responseBody.getLastName());
        assertNotNull(responseBody.getBalance());
    }

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/user/create_user.sql"})
    public void shouldNotBeAbleToGetAnUserById() {

        final long id = 2L;
        final String resourceLocation = "/users/";

        final ResponseEntity<ApiError> response = this.testRestTemplate.exchange(
                resourceLocation + id,
                HttpMethod.GET,
                new HttpEntity<>(User.class),
                ApiError.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final ApiError responseBody = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());
        assertEquals("Não existe um usuário cadastrado com o id=2", responseBody.getResume());
    }

    @Test
    @Sql(scripts = {"/database/clear_database.sql"})
    public void shouldReturnAnEmptyListIfDoNotExistsUsers() {

        final String resourceLocation = "/users";

        final ResponseEntity<User[]> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.GET,
                new HttpEntity<>(User.class),
                User[].class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final int responseBodySize = response.getBody().length;

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, responseBodySize);
    }

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/user/create_user.sql"})
    public void shouldReturnAListWithSizeOfUsers() {
        final String resourceLocation = "/users";

        final ResponseEntity<User[]> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.GET,
                new HttpEntity<>(User.class),
                User[].class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final int responseBodySize = response.getBody().length;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, responseBodySize);
    }

}
