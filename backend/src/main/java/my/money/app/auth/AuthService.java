package my.money.app.auth;

import my.money.app.entity.User;

public interface AuthService {
    User GetLoggedUser(String token);
}
