package my.money.app.controller.users.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    String username;
    String password;
}
