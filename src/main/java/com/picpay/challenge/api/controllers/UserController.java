package com.picpay.challenge.api.controllers;

import com.picpay.challenge.domain.models.User;
import com.picpay.challenge.domain.services.UserService;
import com.picpay.challenge.records.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUser(@PathVariable("userId") final Long userId) {
        final User user = this.userService.findUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        final List<User> users = this.userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody final UserRecord userRecord) {
        final User user = this.userService.createUser(userRecord);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
