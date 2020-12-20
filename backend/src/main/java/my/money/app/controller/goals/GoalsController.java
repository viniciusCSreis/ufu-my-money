package my.money.app.controller.goals;

import my.money.app.auth.AuthService;
import my.money.app.controller.goals.request.GoalsRequest;
import my.money.app.controller.goals.response.GoalsResponse;
import my.money.app.service.GoalsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoalsController {

    AuthService authService;
    GoalsService goalsService;

    public GoalsController(AuthService authService, GoalsService goalsService) {
        this.authService = authService;
        this.goalsService = goalsService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/goals")
    public List<GoalsResponse> List() {
        return goalsService.List(authService.GetLoggedUser());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/goals")
    public GoalsResponse Create(@RequestBody GoalsRequest request) {
        return goalsService.Create(request, authService.GetLoggedUser());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/goals/{id}")
    public GoalsResponse Update(@RequestBody GoalsRequest request, @PathVariable("id") String id) {
        return goalsService.Update(request, id, authService.GetLoggedUser());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/goals/{id}")
    public void Delete(@PathVariable("id") String id) {
        goalsService.Delete(id, authService.GetLoggedUser());
    }

}
