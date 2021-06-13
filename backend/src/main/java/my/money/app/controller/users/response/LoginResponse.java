package my.money.app.controller.users.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    String Token;
}
