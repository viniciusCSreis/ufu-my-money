package my.money.app.controller.goals.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalsRequest {
    BigDecimal value;
    String description;
    LocalDate data;
}
