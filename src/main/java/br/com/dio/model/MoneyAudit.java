package br.com.dio.model;

import java.time.format.DateTimeFormatter;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MoneyAudit(
        UUID transactionId, /*ID da transação*/
        BankService targetService, /*serviço utilizado na transação*/
        String description, /*descrição*/
        OffsetDateTime createdAt /*quando ocorreu a transação*/) {

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = createdAt.format(formatter);
        return String.format("[%s] - %s", formattedDate, description);
    }
}
