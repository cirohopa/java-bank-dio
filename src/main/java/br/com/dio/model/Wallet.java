// Em Wallet.java - CÓDIGO CORRIGIDO E FINALIZADO

package br.com.dio.model;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class Wallet {

    @Getter
    private final BankService service;
    protected final List<Money> money;
    private final List<MoneyAudit> financialTransactions = new ArrayList<>();

    public Wallet(BankService serviceType) {
        this.service = serviceType;
        this.money = new ArrayList<>();
    }

    public long getFunds() {
        return money.size();
    }

    public List<MoneyAudit> getFinancialTransactions() {
        return this.financialTransactions;
    }

    /**
     * NOVO MÉTODO: Para criar dinheiro do zero (depósitos, criação de conta).
     * Ele gera o dinheiro e já registra a transação.
     */
    public void deposit(long amount, String description) {
        var moneyList = Stream.generate(Money::new).limit(amount).toList();
        var history = new MoneyAudit(UUID.randomUUID(), this.service, description, OffsetDateTime.now());
        this.financialTransactions.add(history);
        this.money.addAll(moneyList);
    }

    /**
     * NOVO MÉTODO: Para receber dinheiro que já existe (transferências).
     */
    public void receive(List<Money> moneyToReceive, String description) {
        var history = new MoneyAudit(UUID.randomUUID(), this.service, description, OffsetDateTime.now());
        this.financialTransactions.add(history);
        this.money.addAll(moneyToReceive);
    }

    /**
     * NOVO MÉTODO: Para remover dinheiro (saques, transferências).
     * Ele remove o dinheiro, REGISTRA a transação e retorna o dinheiro removido.
     */
    public List<Money> withdraw(long amount, String description) {
        List<Money> toRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            toRemove.add(this.money.removeFirst());
        }

        var history = new MoneyAudit(UUID.randomUUID(), this.service, description, OffsetDateTime.now());
        this.financialTransactions.add(history);

        return toRemove;
    }
}