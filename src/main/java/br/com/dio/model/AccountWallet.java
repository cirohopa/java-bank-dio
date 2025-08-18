// Em AccountWallet.java - Código Refatorado
package br.com.dio.model;

import lombok.Getter;
import java.util.List;

@Getter
public class AccountWallet extends Wallet {

    private final List<String> pix;

    public AccountWallet(final List<String> pix) {
        super(BankService.ACCOUNT);
        this.pix = pix;
    }

    public AccountWallet(final long amount, List<String> pix) {
        super(BankService.ACCOUNT);
        this.pix = pix;
        addMoney(amount, "Valor de criação da conta");
    }

    public void addMoney(final long amount, final String description){
        // Agora, este método cria o dinheiro e já o registra na própria carteira
        var moneyList = generateMoney(amount);
        super.addMoney(moneyList, getService(), description);
    }

    @Override
    public String toString() {
        return "AccountWallet{" +
                "pix=" + pix +
                ", saldo=" + getFunds() + // Mostra o total de fundos em vez da lista
                ", transacoes=" + getFinancialTransactions().size() + // Mostra a quantidade de transações
                '}';
    }
}