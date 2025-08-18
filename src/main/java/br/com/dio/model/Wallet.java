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
    // 1. ADICIONE a lista de histórico aqui
    private final List<MoneyAudit> financialTransactions = new ArrayList<>();

    public Wallet(BankService serviceType) {
        this.service = serviceType;
        this.money = new ArrayList<>();
    }

    // 2. SIMPLIFIQUE a geração de dinheiro
    protected List<Money> generateMoney(final long amount){
        return Stream.generate(Money::new).limit(amount).toList();
    }

    public long getFunds(){
        return money.size();
    }

    // 3. CORRIJA o método addMoney para registrar a transação na carteira
    public void addMoney(final List<Money> money, final BankService service, final String description){
        var history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        this.financialTransactions.add(history); // Adiciona ao histórico da CARTEIRA
        this.money.addAll(money);
    }

    public List<Money> reduceMoney(final long amount) {
        List<Money> toRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++){
            toRemove.add(this.money.removeFirst());
        }
        return toRemove;
    }

    // 4. ATUALIZE este método para retornar o histórico da carteira
    public List<MoneyAudit> getFinancialTransactions (){
        return this.financialTransactions;
    }
}