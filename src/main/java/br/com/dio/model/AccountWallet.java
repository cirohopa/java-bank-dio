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
        // Usa o novo método "deposit" da classe Wallet
        deposit(amount, "Valor de criação da conta");
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