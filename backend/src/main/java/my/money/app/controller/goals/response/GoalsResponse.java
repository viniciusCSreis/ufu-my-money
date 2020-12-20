package my.money.app.controller.goals.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class GoalsResponse {
    String id;
    BigDecimal value;
    String description;
    LocalDate data;
}
