package my.money.app.repository;

import my.money.app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findAllByUser_CpfOrderByDataDesc(String cpf);
    Optional<Transaction> findByIdAndUser_Cpf(String id , String cpf);
}
