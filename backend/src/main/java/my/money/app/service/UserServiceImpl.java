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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    SessionRepository sessionRepository;

    public UserServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void Create(CreateUserRequest request) {
        if ( !this.isCPF(request.getCpf()) ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "invalid CPF"
            );
        } else if ( !request.getEmail().contains("@") ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "invalid Email"
            );
        }

        Optional<User> userOptional = userRepository.findById(request.getCpf());
        if (userOptional.isPresent()) {
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

        if (!user.getPassword().equals(hashPassword(request.getPassword()))) {
            throw invalidUserExpection;
        }

        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setUser(user);
        session.setCreatedAt(LocalDateTime.now());

        sessionRepository.save(session);

        return LoginResponse.builder().Token(session.getId()).build();
    }

    public String hashPassword(String pass) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(pass);
    }

    private boolean isCPF(String CPF) {
        CPF = CPF.replace(" ","")
                .replace(".","")
                .replace("-", "");

        if ((CPF.length() != 11) ||
                CPF.equals("00000000000") || CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999")
                )
            return(false);

        char dig10, dig11;
        int sm, i, r, num, weight;

        try {
            sm = 0;
            weight = 10;
            for (i=0; i<9; i++, weight--) {
                num = CPF.charAt(i) - 48;
                sm = sm + (num * weight);
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char)(r + 48);

            sm = 0;
            weight = 11;
            for(i=0; i<10; i++, weight--) {
                num = CPF.charAt(i) - 48;
                sm = sm + (num * weight);
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char)(r + 48);

            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

    private User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setCpf(request.getCpf());
        user.setUsername(request.getUsername());
        user.setPassword(hashPassword(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        return user;
    }
}
