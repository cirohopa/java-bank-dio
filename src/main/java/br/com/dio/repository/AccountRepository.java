// Em AccountRepository.java - CÓDIGO CORRIGIDO

package br.com.dio.repository;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.PixInUseException;
import br.com.dio.model.AccountWallet;

import java.util.ArrayList;
import java.util.List;

import static br.com.dio.repository.CommonsRepository.checkFundsForTransaction;

public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds) {
        // ... (seu método create continua igual, sem alterações) ...
        if(!accounts.isEmpty()){
            var pixInUse = accounts.stream().flatMap(a -> a.getPix().stream()).toList();

            for (var p : pix) {
                if (pixInUse.contains(p)) {
                    throw new PixInUseException("Este pix (" + p + ") já está em uso!");
                }
            }
        }

        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount) {
        var target = findByPix(pix);
        // Usa o novo método seguro da Wallet
        target.deposit(fundsAmount, "Depósito");
    }

    public void withDraw(final String pix, final long amount) {
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        // Usa o novo método que registra a transação
        source.withdraw(amount, "Saque");
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount) {
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);

        // Passo 1: Saca da origem (isso já registra a transação de débito)
        var moneyToMove = source.withdraw(amount, "Transferência enviada para PIX: " + targetPix);

        // Passo 2: Deposita no destino (isso já registra a transação de crédito)
        target.receive(moneyToMove, "Transferência recebida do PIX: " + sourcePix);
    }

    public AccountWallet findByPix(final String pix) {
        // ... (seu método findByPix continua igual, sem alterações) ...
        return accounts.stream().filter(a -> a.getPix().contains(pix))
                .findFirst().orElseThrow(()-> new AccountNotFoundException("A conta com esta chave pix: " + pix + " não existe ou foi excluída."));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }
}
