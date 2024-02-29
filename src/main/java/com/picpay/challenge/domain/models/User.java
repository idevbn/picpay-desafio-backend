package com.picpay.challenge.domain.models;

import com.picpay.challenge.domain.enums.UserType;
import com.picpay.challenge.records.UserRecord;
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

    public User(final UserRecord userRecord) {
        this.firstName = userRecord.firstName();
        this.lastName = userRecord.lastName();
        this.cpf = userRecord.cpf();
        this.balance = userRecord.balance();
        this.email = userRecord.email();
        this.userType = UserType.valueOf(userRecord.type());
        this.password = userRecord.password();
    }

}
