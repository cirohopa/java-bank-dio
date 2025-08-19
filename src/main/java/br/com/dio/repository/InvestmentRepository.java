// Em InvestmentRepository.java - CÓDIGO CORRIGIDO

package br.com.dio.repository;

import br.com.dio.exception.AccountWithInvestmentException;
import br.com.dio.exception.InvestmentNotFoundException;
import br.com.dio.exception.WalletNotFoundException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.Investment;
import br.com.dio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static br.com.dio.repository.CommonsRepository.checkFundsForTransaction;

public class InvestmentRepository {

    private long nextId = 0;
    private final List<Investment> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final long initialFunds) {
        this.nextId++;
        var invesment = new Investment(this.nextId, tax, initialFunds);
        investments.add(invesment);
        return invesment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        if (!wallets.isEmpty()) {
            var accountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();
            if (accountsInUse.contains(account)) {
                throw new AccountWithInvestmentException("A conta (" + account + ") já possui um investimento!");
            }
        }

        var investment = findById(id);
        checkFundsForTransaction(account, investment.initialFunds());
        // A criação da carteira já realiza a primeira transação, conforme corrigimos em InvestmentWallet.java
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet); // Adiciona a nova carteira à lista
        return wallet;
    }

    // O nome "deposit" é confuso, significa "investir mais dinheiro"
    public InvestmentWallet investMore(final String pix, final long funds) {
        var investmentWallet = findWalletByAccountPix(pix);
        var sourceAccount = investmentWallet.getAccount();
        checkFundsForTransaction(sourceAccount, funds);

        // LÓGICA CORRIGIDA:
        // 1. Saca da conta principal
        var moneyToInvest = sourceAccount.withdraw(funds, "Aplicação adicional no Investimento ID: " + investmentWallet.getInvestment().id());
        // 2. Recebe na carteira de investimento
        investmentWallet.receive(moneyToInvest, "Aplicação adicional");

        return investmentWallet;
    }

    // O nome "withDraw" significa "resgatar o investimento"
    public InvestmentWallet rescueInvestment(final String pix, final long funds) {
        var investmentWallet = findWalletByAccountPix(pix);
        checkFundsForTransaction(investmentWallet, funds);
        var destinationAccount = investmentWallet.getAccount();

        // LÓGICA CORRIGIDA:
        // 1. Saca da carteira de investimento
        var rescuedMoney = investmentWallet.withdraw(funds, "Resgate de investimento");
        // 2. Recebe na conta principal
        destinationAccount.receive(rescuedMoney, "Valor resgatado do Investimento ID: " + investmentWallet.getInvestment().id());

        if (investmentWallet.getFunds() == 0) {
            wallets.remove(investmentWallet);
        }

        return investmentWallet;
    }

    public void updateAmount() {
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investment findById(final long id) {
        return investments.stream().filter(a -> a.id() == id)
                .findFirst()
                .orElseThrow(
                        () -> new InvestmentNotFoundException("Não foi encontrado investimento com o id: " + id)
                );
    }

    public InvestmentWallet findWalletByAccountPix(final String pix) {
        return wallets.stream()
                .filter(w -> w.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(
                        () -> new WalletNotFoundException("Esta carteira não foi encontrada.")
                );
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investment> list() {
        return this.investments;
    }

}