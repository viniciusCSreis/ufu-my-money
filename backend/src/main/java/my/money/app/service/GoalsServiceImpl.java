package my.money.app.service;

import my.money.app.controller.goals.request.GoalsRequest;
import my.money.app.controller.goals.response.GoalsResponse;
import my.money.app.entity.Goal;
import my.money.app.entity.User;
import my.money.app.repository.GoalsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalsServiceImpl implements GoalsService {

    GoalsRepository goalsRepository;

    public GoalsServiceImpl(GoalsRepository goalsRepository) {
        this.goalsRepository = goalsRepository;
    }

    @Override
    public List<GoalsResponse> List(User logged) {
        List<Goal> goals = goalsRepository.findAllByUser_CpfOrderByDataDesc(logged.getCpf());
        return goals.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public GoalsResponse Create(GoalsRequest request, User logged) {
        Goal goal = toEntity(request, logged);
        if (goal.getValue().compareTo(BigDecimal.ZERO) != 1 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "value must be greater then zero");
        }
        Goal created = goalsRepository.save(goal);
        return toResponse(created);
    }

    @Override
    public GoalsResponse Update(GoalsRequest request, String id, User logged) {

        Optional<Goal> savedGoal = goalsRepository.findByIdAndUser_Cpf(id, logged.getCpf());
        savedGoal.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        Goal newGoal = toEntity(request, logged);
        newGoal.setId(id);
        goalsRepository.save(newGoal);
        return toResponse(newGoal);
    }

    @Override
    public void Delete(String id, User logged) {

        Optional<Goal> savedGoal = goalsRepository.findByIdAndUser_Cpf(id, logged.getCpf());
        savedGoal.ifPresent(goal -> goalsRepository.delete(goal));

    }

    private GoalsResponse toResponse(Goal goal) {
        return GoalsResponse.builder()
                .id(goal.getId())
                .description(goal.getDescription())
                .value(goal.getValue())
                .data(goal.getData())
                .build();
    }

    private Goal toEntity(GoalsRequest request, User user) {
        Goal goal = new Goal();
        goal.setId(UUID.randomUUID().toString());
        goal.setDescription(request.getDescription());
        goal.setUser(user);
        goal.setData(request.getData());
        goal.setValue(request.getValue());
        return goal;
    }
}
