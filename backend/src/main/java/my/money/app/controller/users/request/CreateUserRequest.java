package my.money.app.controller.users.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateUserRequest {
    String cpf;
    String username;
    String email;
    String password;
    String phoneNumber;
    LocalDate birthDate;
}
