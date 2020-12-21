package my.money.app.service;

import my.money.app.controller.transaction.request.TransactionRequest;
import my.money.app.controller.transaction.response.TransactionResponse;
import my.money.app.entity.Transaction;
import my.money.app.entity.User;
import my.money.app.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionResponse> List(User logged) {
        List<Transaction> transactions = transactionRepository.findAllByUser_CpfOrderByDataDesc(logged.getCpf());
        return transactions.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public TransactionResponse Create(TransactionRequest request, User logged) {
        Transaction transaction = toEntity(request, logged);
        transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    @Override
    public TransactionResponse Update(TransactionRequest request, String id, User logged) {

        Optional<Transaction> savedTransaction = transactionRepository.findByIdAndUser_Cpf(id, logged.getCpf());
        savedTransaction.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        Transaction newTransaction = toEntity(request, logged);
        newTransaction.setId(id);
        transactionRepository.save(newTransaction);
        return toResponse(newTransaction);
    }

    @Override
    public void Delete(String id, User logged) {

        Optional<Transaction> savedTransaction = transactionRepository.findByIdAndUser_Cpf(id, logged.getCpf());
        savedTransaction.ifPresent(goal -> transactionRepository.delete(goal));

    }

    private TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .value(transaction.getValue())
                .type(transaction.getType())
                .data(transaction.getData())
                .build();
    }

    private Transaction toEntity(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setDescription(request.getDescription());
        transaction.setUser(user);
        transaction.setType(request.getType());
        transaction.setData(request.getData());
        transaction.setValue(request.getValue());
        return transaction;
    }
}
