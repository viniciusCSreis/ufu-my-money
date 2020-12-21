package my.money.app.controller.transaction.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {
    String id;
    BigDecimal value;
    String description;
    String type;
    LocalDate data;
}