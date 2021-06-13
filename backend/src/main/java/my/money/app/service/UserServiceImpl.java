package my.money.app.service;

import my.money.app.controller.users.request.CreateUserRequest;
import my.money.app.controller.users.request.LoginRequest;
import my.money.app.controller.users.response.LoginResponse;
import my.money.app.entity.Session;
import my.money.app.entity.User;
import my.money.app.repository.SessionRepository;
import my.money.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    SessionRepository sessionRepository;

    public UserServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void Create(CreateUserRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user already exist"
            );
        }

        User user = toEntity(request);
        userRepository.save(user);
    }

    @Override
    public LoginResponse Login(LoginRequest request) {

        ResponseStatusException invalidUserExpection = new ResponseStatusException(
                HttpStatus.FORBIDDEN, "invalid Username or Password"
        );

        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        userOptional.orElseThrow(() -> invalidUserExpection);
        User user = userOptional.get();

        if (!user.getPassword().equals(request.getPassword())){
           throw invalidUserExpection;
        }


        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setUser(user);
        session.setCreatedAt(LocalDateTime.now());

        sessionRepository.save(session);


        return LoginResponse.builder().Token(session.getId()).build();

    }

    private User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setCpf(request.getCpf());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        return user;
    }
}
