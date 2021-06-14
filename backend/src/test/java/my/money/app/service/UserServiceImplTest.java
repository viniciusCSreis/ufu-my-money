package my.money.app.service;

import my.money.app.controller.users.request.CreateUserRequest;
import my.money.app.controller.users.request.LoginRequest;
import my.money.app.controller.users.response.LoginResponse;
import my.money.app.entity.Session;
import my.money.app.entity.User;
import my.money.app.repository.SessionRepository;
import my.money.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWithSuccess() {
        String password = UUID.randomUUID().toString();

        CreateUserRequest request = new CreateUserRequest();
        request.setCpf( "425.751.736-04" );
        request.setUsername( "Fulano da Silva" );
        request.setEmail( "fulanodasilva@gmail.com" );
        request.setPassword( password );
        request.setPhoneNumber( "5534911112222" );
        request.setBirthDate((LocalDate.of(2000, 6, 1)) );

        User user = new User();
        user.setCpf( "425.751.736-04" );
        user.setUsername( "Fulano da Silva" );
        user.setEmail( "fulanodasilva@gmail.com" );
        user.setPassword( password );
        user.setPhoneNumber( "5534911112222" );
        user.setBirthDate((LocalDate.of(2000, 6, 1)) );

        when(userRepository.save(any())).thenReturn(user);

        userService.Create(request);

        verify(userRepository, atLeast(1)).save(any());
    }

    @Test
    void createThrowWhenInvalidCPF() {
        String password = UUID.randomUUID().toString();

        CreateUserRequest request = new CreateUserRequest();
        request.setCpf( "111.111.111-11" );
        request.setUsername( "Fulano da Silva" );
        request.setEmail( "fulanodasilva@gmail.com" );
        request.setPassword( password );
        request.setPhoneNumber( "5534911112222" );
        request.setBirthDate((LocalDate.of(2000, 6, 1)) );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> userService.Create(request),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createThrowWhenInvalidEmail() {
        String password = UUID.randomUUID().toString();

        CreateUserRequest request = new CreateUserRequest();
        request.setCpf( "425.751.736-04" );
        request.setUsername( "Fulano da Silva" );
        request.setEmail( "fulanodasilva" );
        request.setPassword( password );
        request.setPhoneNumber( "5534911112222" );
        request.setBirthDate((LocalDate.of(2000, 6, 1)) );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> userService.Create(request),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void loginWithSuccess() {
        String password = UUID.randomUUID().toString();

        LoginRequest request = new LoginRequest();
        request.setUsername( "Fulano da Silva" );
        request.setPassword( password );

        User user = new User();
        user.setCpf( "425.751.736-04" );
        user.setUsername( "Fulano da Silva" );
        user.setEmail( "fulanodasilva@gmail.com" );
        user.setPassword( userService.hashPassword(password) );
        user.setPhoneNumber( "5534911112222" );
        user.setBirthDate((LocalDate.of(2000, 6, 1)) );

        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setUser(user);
        session.setCreatedAt(LocalDateTime.now());

        when(sessionRepository.save(any())).thenReturn(session);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        LoginResponse result = userService.Login(request);

        assertFalse(result.equals(""));
        verify(sessionRepository, atLeast(1)).save(any());
        verify(userRepository, atLeast(1)).findByUsername(request.getUsername());
    }

    @Test
    void loginThrowWhenInvalidUsername() {
        String password = UUID.randomUUID().toString();
        String otherPassword = UUID.randomUUID().toString();

        LoginRequest request = new LoginRequest();
        request.setUsername( "Ciclano" );
        request.setPassword( password );

        User user = new User();
        user.setCpf( "073.625.930-90" );
        user.setUsername( "Ciclano" );
        user.setEmail( "ciclano@gmail.com" );
        user.setPassword( userService.hashPassword(otherPassword) );
        user.setPhoneNumber( "5534911112222" );
        user.setBirthDate((LocalDate.of(2000, 6, 1)) );

        User user2 = new User();
        user2.setCpf( "425.751.736-04" );
        user2.setUsername( "Ciclano da Cunha" );
        user2.setEmail( "ciclanodacunha@gmail.com" );
        user2.setPassword( userService.hashPassword(password) );
        user2.setPhoneNumber( "5534911113333" );
        user2.setBirthDate((LocalDate.of(2000, 6, 1)) );

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> userService.Login(request),
                "Expected ResponseStatusException"
        );

        assertEquals(exception.getStatus(), HttpStatus.FORBIDDEN);

        verify(userRepository, atLeast(1)).findByUsername(request.getUsername());
    }

    @Test
    void loginThrowWhenInvalidPassword() {
        String password = UUID.randomUUID().toString();
        String otherPassword = UUID.randomUUID().toString();

        LoginRequest request = new LoginRequest();
        request.setUsername( "Ciclano da Cunha" );
        request.setPassword( otherPassword );

        User user = new User();
        user.setCpf( "073.625.930-90" );
        user.setUsername( "Ciclano da Cunha" );
        user.setEmail( "ciclanodacunha@gmail.com" );
        user.setPassword( userService.hashPassword(password) );
        user.setPhoneNumber( "5534911112222" );
        user.setBirthDate((LocalDate.of(2000, 6, 1)) );

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> userService.Login(request),
                "Expected ResponseStatusException"
        );

        assertEquals(exception.getStatus(), HttpStatus.FORBIDDEN);

        verify(userRepository, atLeast(1)).findByUsername(request.getUsername());
    }
}