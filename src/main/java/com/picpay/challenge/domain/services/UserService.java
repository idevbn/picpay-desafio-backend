package com.picpay.challenge.domain.services;

import com.picpay.challenge.domain.enums.UserType;
import com.picpay.challenge.domain.exceptions.TransactionNotAllowedException;
import com.picpay.challenge.domain.exceptions.InsufficientBalanceException;
import com.picpay.challenge.domain.exceptions.UserNotFoundException;
import com.picpay.challenge.domain.models.User;
import com.picpay.challenge.domain.repositories.UserRepository;
import com.picpay.challenge.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateTransaction(final User sender, final BigDecimal amount) {
        if (sender.getUserType().equals(UserType.SHOPKEEPER)) {
            throw new TransactionNotAllowedException();
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
    }

    public User findUserById(final Long id) {
        return this.userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void saveUser(final User user) {
        this.userRepository.save(user);
    }

    public User createUser(final UserDTO userDTO) {
        final User user = new User(userDTO);

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        this.saveUser(user);

        return user;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

}
