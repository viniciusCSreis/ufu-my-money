package my.money.app.auth;

import my.money.app.entity.User;
import my.money.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User GetLoggedUser() {

        User user = new User();
        user.setCpf("96328612052");
        user.setEmail("test@test.com");
        user.setUsername("test");
        user.setBirthDate(LocalDate.of(1999, 1, 1));
        user.setPassword("some-hash");
        user.setPhoneNumber("(34)9-1234-5678)");

        Optional<User> savedUser = userRepository.findById(user.getCpf());
        if (!savedUser.isPresent()) {
            userRepository.save(user);
        }

        return user;
    }
}
