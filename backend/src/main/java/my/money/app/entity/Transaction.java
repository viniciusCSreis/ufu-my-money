package my.money.app.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Transaction {
    @Id
    String id;
    BigDecimal value;
    String description;
    String Type;
    LocalDate data;
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_cpf")
    User user;
}
