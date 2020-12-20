package my.money.app.auth;

import my.money.app.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public User GetLoggedUser(){
        User user = new User();
        user.setCpf("96328612052");
        user.setEmail("test@test.com");
        user.setUsername("test");
        user.setBirthDate(LocalDate.of(1999, 1 ,1 ));
        user.setPassword("some-hash");
        user.setPhoneNumber("(34)9-1234-5678)");
        return user;
    }
}
