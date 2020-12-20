package my.money.app.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "usuario")
public class User {
    @Id
    String cpf;
    String username;
    String email;
    String password;
    String phoneNumber;
    LocalDate birthDate;
}
