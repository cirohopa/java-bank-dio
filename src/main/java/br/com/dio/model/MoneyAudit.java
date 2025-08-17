package br.com.dio.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MoneyAudit(
        UUID transactionId, /*ID da transação*/
        BankService targetService, /*serviço utilizado na transação*/
        String description, /*descrição*/
        OffsetDateTime createdAt /*quando ocorreu a transação*/) {

}
