package com.picpay.challenge.domain.repositories;

import com.picpay.challenge.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByCpf(final String cpf);
    Optional<User> findUserById(final Long id);

}
