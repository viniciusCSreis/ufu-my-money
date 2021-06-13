package my.money.app.controller.users;

import my.money.app.controller.users.request.CreateUserRequest;
import my.money.app.controller.users.request.LoginRequest;
import my.money.app.controller.users.response.LoginResponse;
import my.money.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody CreateUserRequest request){
        userService.Create(request);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@RequestBody LoginRequest request){
       return userService.Login(request);
    }

}
