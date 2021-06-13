package my.money.app.service;

import my.money.app.controller.users.request.CreateUserRequest;
import my.money.app.controller.users.request.LoginRequest;
import my.money.app.controller.users.response.LoginResponse;

public interface UserService {
    void Create(CreateUserRequest request);

    LoginResponse Login(LoginRequest request);
}
