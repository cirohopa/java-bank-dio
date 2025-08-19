// Em InvestmentWallet.java - CÓDIGO CORRIGIDO

package br.com.dio.model;

import lombok.Getter;
// A anotação ToString não é mais necessária aqui, vamos criar uma personalizada
// import lombok.ToString;

import static br.com.dio.model.BankService.INVESTMENT;

@Getter
public class InvestmentWallet extends Wallet {

    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet(Investment investment, AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        // LÓGICA CORRIGIDA:
        // 1. Saca o dinheiro da conta principal (e registra a transação lá)
        var moneyToInvest = account.withdraw(amount, "Aplicação no Investimento ID: " + investment.id());
        // 2. Recebe o dinheiro na carteira de investimento (e registra a transação aqui)
        this.receive(moneyToInvest, "Aplicação inicial do Investimento ID: " + investment.id());
    }

    public void updateAmount(final long percent) {
        var amount = getFunds() * percent / 100;
        if (amount <= 0) {
            return;
        }
        // LÓGICA CORRIGIDA: Usa o método "deposit" para criar dinheiro novo (rendimentos)
        this.deposit(amount, "Rendimentos do Investimento ID: " + investment.id());
    }

    // Adicionando um toString personalizado para a opção 11 (Listar carteiras de investimento) funcionar
    @Override
    public String toString() {
        return "InvestmentWallet{" +
                "investmentId=" + investment.id() +
                ", accountPix=" + account.getPix() +
                ", saldo=" + getFunds() +
                '}';
    }
}
