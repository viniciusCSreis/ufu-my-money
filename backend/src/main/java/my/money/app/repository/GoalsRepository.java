package my.money.app.repository;

import my.money.app.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalsRepository extends JpaRepository<Goal, String> {
    List<Goal> findAllByUser_Cpf(String cpf);
    Optional<Goal> findByIdAndUser_Cpf(String id , String cpf);
}
