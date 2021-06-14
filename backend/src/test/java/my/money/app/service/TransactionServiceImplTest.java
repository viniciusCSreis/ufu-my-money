package my.money.app.service;

import my.money.app.controller.transaction.request.TransactionRequest;
import my.money.app.controller.transaction.response.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import my.money.app.entity.Transaction;
import my.money.app.entity.User;
import my.money.app.repository.TransactionRepository;
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

class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listWithSuccess() {
        User logged = new User();
        logged.setUsername("test");

        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setValue(BigDecimal.valueOf(45.90));
        transaction.setDescription("Mensalidade Netflix");
        transaction.setType("Despesa");
        transaction.setData(LocalDate.of(2021, 6, 1));
        transaction.setUser(logged);
        transactions.add(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setId("2");
        transaction2.setValue(BigDecimal.valueOf(100.00));
        transaction2.setDescription("Reembolso Emprestimo ao Joao");
        transaction2.setType("RECEITA");
        transaction2.setData(LocalDate.of(2021, 6, 1));
        transaction2.setUser(logged);
        transactions.add(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setId("3");
        transaction3.setValue(BigDecimal.valueOf(27.90));
        transaction3.setDescription("Mensalidade Disney Plus");
        transaction3.setType("DESPESA");
        transaction3.setData(LocalDate.of(2021, 6, 1));
        transaction3.setUser(logged);
        transactions.add(transaction3);

        when(transactionRepository.findAllByUser_CpfOrderByDataDesc(any())).thenReturn(transactions);

        List<TransactionResponse> result = transactionService.List(logged);

        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertEquals(result.get(0).getId(), transaction.getId());
        assertEquals(result.get(0).getValue(), transaction.getValue());
        assertEquals(result.get(0).getDescription(), transaction.getDescription());
        assertEquals(result.get(0).getType(), transaction.getType());
        assertEquals(result.get(0).getData(), transaction.getData());

        assertNotNull(result);
        assertEquals(result.get(1).getId(), transaction2.getId());
        assertEquals(result.get(1).getValue(), transaction2.getValue());
        assertEquals(result.get(1).getDescription(), transaction2.getDescription());
        assertEquals(result.get(1).getType(), transaction2.getType());
        assertEquals(result.get(1).getData(), transaction2.getData());

        assertNotNull(result);
        assertEquals(result.get(2).getId(), transaction3.getId());
        assertEquals(result.get(2).getValue(), transaction3.getValue());
        assertEquals(result.get(2).getDescription(), transaction3.getDescription());
        assertEquals(result.get(2).getType(), transaction3.getType());
        assertEquals(result.get(2).getData(), transaction3.getData());
    }

    @Test
    void createWithSuccess() {
        User logged = new User();
        logged.setUsername("test");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setDescription("Mensalidade Netflix");
        request.setType("Despesa");
        request.setData(LocalDate.of(2021, 6, 1));

        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setValue(BigDecimal.valueOf(45.90));
        transaction.setDescription("Mensalidade Netflix");
        transaction.setType("Despesa");
        transaction.setData(LocalDate.of(2021, 6, 1));
        transaction.setUser(logged);

        when(transactionRepository.save(any())).thenReturn(transaction);

        TransactionResponse result = transactionService.Create(request, logged);

        assertNotNull(result);
        assertEquals(result.getId(), transaction.getId());
        assertEquals(result.getDescription(), transaction.getDescription());
        assertEquals(result.getType(), transaction.getType());
        assertEquals(result.getData(), transaction.getData());
        assertEquals(result.getValue(), transaction.getValue());

        verify(transactionRepository, atLeast(1)).save(any());
    }

    @Test
    void createThrowWhenValueIsZero() {
        User logged = new User();
        logged.setUsername("test");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.ZERO);
        request.setDescription("Mensalidade Netflix");
        request.setType("Despesa");
        request.setData(LocalDate.of(2021, 6, 1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> transactionService.Create(request, logged),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createThrowWhenValueIsNegative() {
        User logged = new User();
        logged.setUsername("test");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.valueOf(-1.5));
        request.setDescription("Mensalidade Netflix");
        request.setType("Despesa");
        request.setData(LocalDate.of(2021, 6, 1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> transactionService.Create(request, logged),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createThrowWhenTypeDoesNotExist() {
        User logged = new User();
        logged.setUsername("test");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setDescription("Mensalidade Netflix");
        request.setType("OtherType");
        request.setData(LocalDate.of(2021, 6, 1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> transactionService.Create(request, logged),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createThrowWhenTypeIsBlank() {
        User logged = new User();
        logged.setUsername("test");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setDescription("Mensalidade Netflix");
        request.setType("");
        request.setData(LocalDate.of(2021, 6, 1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> transactionService.Create(request, logged),
                "Expected BadRequest exception"
        );

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateWithSuccess() {
        String id = "1";

        User logged = new User();
        logged.setUsername("test");
        logged.setCpf("69038401094");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setDescription("Mensalidade Netflix");
        request.setType("Despesa");
        request.setData(LocalDate.of(2021, 6, 1));

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setValue(BigDecimal.valueOf(39.90));
        transaction.setDescription("Mensalidade Net");
        transaction.setType("Despesa");
        transaction.setData(LocalDate.of(2021, 6, 1));
        transaction.setUser(logged);

        Transaction newTransaction = new Transaction();
        newTransaction.setId(id);
        newTransaction.setValue(BigDecimal.valueOf(45.90));
        newTransaction.setDescription("Mensalidade Netflix");
        newTransaction.setType("Despesa");
        newTransaction.setData(LocalDate.of(2021, 6, 1));
        newTransaction.setUser(logged);

        when(transactionRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);

        TransactionResponse result = transactionService.Update(request, id, logged);

        assertNotNull(result);
        assertEquals(result.getId(), newTransaction.getId());
        assertEquals(result.getDescription(), newTransaction.getDescription());
        assertEquals(result.getType(), newTransaction.getType());
        assertEquals(result.getData(), newTransaction.getData());
        assertEquals(result.getValue(), newTransaction.getValue());

        verify(transactionRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
        verify(transactionRepository, atLeast(1)).save(newTransaction);
    }

    @Test
    void updateThrowWhenIdNotFound() {
        String id = "1";

        User logged = new User();
        logged.setUsername("test");
        logged.setCpf("69038401094");

        TransactionRequest request = new TransactionRequest();
        request.setValue(BigDecimal.valueOf(45.90));
        request.setDescription("Mensalidade Netflix");
        request.setType("Despesa");
        request.setData(LocalDate.of(2021, 6, 1));


        when(transactionRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> transactionService.Update(request, id, logged),
                "Expected ResponseStatusException"
        );

        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);

        verify(transactionRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
    }

    @Test
    void deleteWithSuccess() {
        String id = "1";

        User logged = new User();
        logged.setUsername("test");
        logged.setCpf("69038401094");

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setValue(BigDecimal.valueOf(45.90));
        transaction.setDescription("Mensalidade Netflix");
        transaction.setType("Despesa");
        transaction.setData(LocalDate.of(2021, 6, 1));
        transaction.setUser(logged);

        when(transactionRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.of(transaction));

        transactionService.Delete(id, logged);

        verify(transactionRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
        verify(transactionRepository, atLeast(1)).delete(transaction);
    }

    @Test
    void deleteNotThrowWhenIdNotExist() {
        String id = "1";

        User logged = new User();
        logged.setUsername("test");

        when(transactionRepository.findByIdAndUser_Cpf(id, logged.getCpf())).thenReturn(Optional.empty());

        transactionService.Delete(id, logged);

        verify(transactionRepository, atLeast(1)).findByIdAndUser_Cpf(id, logged.getCpf());
    }
}