package my.money.app.controller.users.request;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
