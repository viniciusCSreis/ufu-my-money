package my.money.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "usuario")
public class User {
    @Id
    String cpf;
    @Column(unique = true)
    String username;
    @Column(unique = true)
    String email;
    String password;
    String phoneNumber;
    LocalDate birthDate;
}
