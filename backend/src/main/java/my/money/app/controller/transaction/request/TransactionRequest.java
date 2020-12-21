package my.money.app.controller.transaction.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionRequest {
    BigDecimal value;
    String description;
    String type;
    LocalDate data;
}
