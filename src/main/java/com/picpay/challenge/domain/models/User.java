package com.picpay.challenge.domain.models;

import com.picpay.challenge.domain.enums.UserType;
import com.picpay.challenge.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "users")
@Table(name = "tb_users")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    public User(final UserDTO userDTO) {
        this.firstName = userDTO.firstName();
        this.lastName = userDTO.lastName();
        this.cpf = userDTO.cpf();
        this.balance = userDTO.balance();
        this.email = userDTO.email();
        this.userType = UserType.valueOf(userDTO.type());
        this.password = userDTO.password();
    }

}
