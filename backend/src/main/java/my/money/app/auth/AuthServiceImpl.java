package my.money.app.auth;

import my.money.app.entity.Session;
import my.money.app.entity.User;
import my.money.app.repository.SessionRepository;
import my.money.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    SessionRepository sessionRepository;

    public AuthServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public User GetLoggedUser(String token) {

        Optional<Session> optionalSession = sessionRepository.findById(token);
        optionalSession.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "invalid token header"
        ));

        return optionalSession.get().getUser();
    }
}
