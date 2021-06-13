package my.money.app.service;

import my.money.app.controller.goals.request.GoalsRequest;
import my.money.app.controller.goals.response.GoalsResponse;
import my.money.app.entity.Goal;
import my.money.app.entity.User;
import my.money.app.repository.GoalsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GoalsServiceImplTest {

    @Mock
    GoalsRepository goalsRepository;

    @InjectMocks
    GoalsServiceImpl goalsService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void list() {
        User logged = new User();
        logged.setUsername("test");

        List<Goal> goals = new ArrayList<>();

        Goal goal1 = new Goal();
        goal1.setId("1");
        goal1.setValue(BigDecimal.valueOf(45.90));
        goal1.setData(LocalDate.of(2021, 6, 1));
        goal1.setData(LocalDate.of(2021, 6, 1));
        goal1.setDescription("Mensalidade Netflix");
        goal1.setUser(logged);
        goals.add(goal1);


        Goal goal2 = new Goal();
        goal2.setId("2");
        goal2.setValue(BigDecimal.valueOf(27.90));
        goal2.setData(LocalDate.of(2021, 6, 1));
        goal2.setData(LocalDate.of(2021, 6, 1));
        goal2.setDescription("Mensalidade Disney Plus ");
        goal2.setUser(logged);
        goals.add(goal2);


        when(goalsRepository.findAllByUser_CpfOrderByDataDesc(any())).thenReturn(goals);

        List<GoalsResponse> result = goalsService.List(logged);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getId(), goal1.getId());
        assertEquals(result.get(0).getData(), goal1.getData());
        assertEquals(result.get(0).getDescription(), goal1.getDescription());
        assertEquals(result.get(0).getValue(), goal1.getValue());

        assertNotNull(result);
        assertEquals(result.get(1).getId(), goal2.getId());
        assertEquals(result.get(1).getData(), goal2.getData());
        assertEquals(result.get(1).getDescription(), goal2.getDescription());
        assertEquals(result.get(1).getValue(), goal2.getValue());
    }

    @Test
    void createSuccess() {

        User logged = new User();
        logged.setUsername("test");

        GoalsRequest request = new GoalsRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setData(LocalDate.of(2021, 6, 1));
        request.setDescription("Mensalidade Netflix");

        Goal goal = new Goal();
        goal.setId("1");
        goal.setValue(BigDecimal.valueOf(45.90));
        goal.setData(LocalDate.of(2021, 6, 1));
        goal.setData(LocalDate.of(2021, 6, 1));
        goal.setDescription("Mensalidade Netflix");
        goal.setUser(logged);

        when(goalsRepository.save(any())).thenReturn(goal);

        GoalsResponse result = goalsService.Create(request, logged);

        assertNotNull(result);
        assertEquals(result.getId(), goal.getId());
        assertEquals(result.getData(), goal.getData());
        assertEquals(result.getDescription(), goal.getDescription());
        assertEquals(result.getValue(), goal.getValue());

        verify(goalsRepository, atLeast(1)).save(any());

    }

    @Test
    void createThrowWhenValueIsZero() {

        User logged = new User();
        logged.setUsername("test");

        GoalsRequest request = new GoalsRequest();
        request.setValue(BigDecimal.ZERO);
        request.setData(LocalDate.of(2021, 6, 1));
        request.setDescription("Mensalidade Netflix");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> goalsService.Create(request, logged),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

    }

    @Test
    void createThrowWhenValueIsNegative() {

        User logged = new User();
        logged.setUsername("test");

        GoalsRequest request = new GoalsRequest();
        request.setValue(BigDecimal.valueOf(-1.5));
        request.setData(LocalDate.of(2021, 6, 1));
        request.setDescription("Mensalidade Netflix");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> goalsService.Create(request, logged),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

    }

    @Test
    void update() {

        String id = "1";

        User logged = new User();
        logged.setUsername("test");
        logged.setCpf("69038401094");

        GoalsRequest request = new GoalsRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setData(LocalDate.of(2021, 6, 1));
        request.setDescription("Mensalidade Netflix");

        Goal goal = new Goal();
        goal.setId(id);
        goal.setValue(BigDecimal.valueOf(39.90));
        goal.setData(LocalDate.of(2021, 6, 1));
        goal.setData(LocalDate.of(2021, 6, 1));
        goal.setDescription("Mensalidade Netflix");
        goal.setUser(logged);

        Goal newGoal = new Goal();
        newGoal.setId(id);
        newGoal.setValue(BigDecimal.valueOf(45.90));
        newGoal.setData(LocalDate.of(2021, 6, 1));
        newGoal.setData(LocalDate.of(2021, 6, 1));
        newGoal.setDescription("Mensalidade Netflix");
        newGoal.setUser(logged);

        when(goalsRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.of(goal));
        when(goalsRepository.save(newGoal)).thenReturn(newGoal);

        GoalsResponse result = goalsService.Update(request, id, logged);

        assertNotNull(result);
        assertEquals(result.getId(), newGoal.getId());
        assertEquals(result.getData(), newGoal.getData());
        assertEquals(result.getDescription(), newGoal.getDescription());
        assertEquals(result.getValue(), newGoal.getValue());

        verify(goalsRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
        verify(goalsRepository, atLeast(1)).save(newGoal);
    }

    @Test
    void updateThrowWhenIdNotFound() {

        String id = "1";

        User logged = new User();
        logged.setUsername("test");
        logged.setCpf("69038401094");

        GoalsRequest request = new GoalsRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setData(LocalDate.of(2021, 6, 1));
        request.setDescription("Mensalidade Netflix");


        when(goalsRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> goalsService.Update(request, id, logged),
                "Expected ResponseStatusException"
        );

        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);

        verify(goalsRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
    }

    @Test
    void deleteWithSuccess() {
        String id = "1";

        User logged = new User();
        logged.setUsername("test");
        logged.setCpf("69038401094");

        Goal goal = new Goal();
        goal.setId(id);
        goal.setValue(BigDecimal.valueOf(39.90));
        goal.setData(LocalDate.of(2021, 6, 1));
        goal.setData(LocalDate.of(2021, 6, 1));
        goal.setDescription("Mensalidade Netflix");
        goal.setUser(logged);


        when(goalsRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.of(goal));

        goalsService.Delete(id, logged);


        verify(goalsRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
        verify(goalsRepository, atLeast(1)).delete(goal);
    }

    @Test
    void deleteNotThrowWhenIdNotExist() {
        String id = "1";

        User logged = new User();
        logged.setUsername("test");

        when(goalsRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.empty());

        goalsService.Delete(id, logged);


        verify(goalsRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
    }
}