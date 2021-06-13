package my.money.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tokens")
public class Session {
    @Id
    String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_cpf")
    User user;

    LocalDateTime createdAt;
}
