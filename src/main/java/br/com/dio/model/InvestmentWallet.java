package br.com.dio.model;

import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static br.com.dio.model.BankService.INVESTMENT;

@Getter
@ToString
public class InvestmentWallet extends Wallet {

    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet( Investment investment, AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getService(), "investimento");
    }

    public void updateAmount(final long percent) {
        // 1. Calcula o valor a ser adicionado (já estava certo)
        var amount = getFunds() * percent / 100;

        // Se não houver rendimento, não faz nada
        if (amount <= 0) {
            return;
        }

        // 2. Gera a lista de novos objetos Money (da forma correta, sem histórico)
        var newMoney = Stream.generate(Money::new).limit(amount).toList();

        // 3. Usa o método addMoney da classe Wallet para adicionar o dinheiro
        // E registrar a transação no histórico da carteira (essa é a parte chave)
        addMoney(newMoney, getService(), "rendimentos");
    }

}
