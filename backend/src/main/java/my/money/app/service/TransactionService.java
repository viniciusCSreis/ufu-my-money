package my.money.app.service;

import my.money.app.controller.transaction.request.TransactionRequest;
import my.money.app.controller.transaction.response.TransactionResponse;
import my.money.app.entity.User;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> List(User logged);

    TransactionResponse Create(TransactionRequest request, User logged);

    TransactionResponse Update(TransactionRequest request, String id, User logged);

    void Delete(String id, User logged);
}
