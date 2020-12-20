package my.money.app.service;

import my.money.app.controller.goals.request.GoalsRequest;
import my.money.app.controller.goals.response.GoalsResponse;
import my.money.app.entity.User;

import java.util.List;

public interface GoalsService {
    List<GoalsResponse> List(User logged);

    GoalsResponse Create(GoalsRequest request, User logged);

    GoalsResponse Update(GoalsRequest request, String id, User logged);

    void Delete(String id, User logged);
}