package my.money.app.controller.transaction;

import my.money.app.auth.AuthService;
import my.money.app.controller.transaction.request.TransactionRequest;
import my.money.app.controller.transaction.response.TransactionResponse;
import my.money.app.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    AuthService authService;
    TransactionService transactionService;

    public TransactionController(AuthService authService, TransactionService transactionService) {
        this.authService = authService;
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> list(
            @RequestHeader(value = "x-authorization"
            ) String token) {
        return transactionService.List(authService.GetLoggedUser(token));
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(
            @RequestHeader(value = "x-authorization") String token,
            @RequestBody TransactionRequest request
    ) {
        return transactionService.Create(request, authService.GetLoggedUser(token));
    }

    @PutMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse edit(
            @RequestHeader(value = "x-authorization") String token,
            @RequestBody TransactionRequest request,
            @PathVariable("id") String id
    ) {
        return transactionService.Update(request, id, authService.GetLoggedUser(token));
    }

    @DeleteMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(
            @RequestHeader(value = "x-authorization") String token,
            @PathVariable("id") String id
    ) {
        transactionService.Delete(id, authService.GetLoggedUser(token));
    }
}
