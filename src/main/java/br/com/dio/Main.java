package br.com.dio;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.model.AccountWallet;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    static Scanner sc = new Scanner(System.in);

    public static void main(String [] args){

        System.out.println("Bem vindo(a) ao Banco DIO!");

        while(true){

            System.out.println("Selecione a operação desejada:");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferência entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12- Atualizar investimentos");
            System.out.println("13 - Histórico da conta");
            System.out.println("14 - Sair");

            var opc = sc.nextInt();
            sc.nextLine();

            switch (opc){
                case 1-> createAccount();
                case 2-> createInvestment();
                case 3-> createWalletInvestment();
                case 4-> deposit();
                case 5-> withdraw();
                case 6-> transferToAccount();
                case 7-> investMore();
                case 8-> rescueInvestment();
                case 9-> accountRepository.list().forEach(System.out::println);
                case 10-> investmentRepository.list().forEach(System.out::println);
                case 11-> investmentRepository.listWallets().forEach(System.out::println);
                case 12-> {
                    investmentRepository.updateAmount();
                    System.out.println("Atualização dos investimentos concluída");
                }
                case 13-> checkHistory();
                case 14-> System.exit(0);
                default-> System.out.println("Opção inválida!");

            }
        }
    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix, separadas por ; (ponto e vírgula)");
        var pix = Arrays.stream(sc.nextLine().split(";")).toList();
        System.out.println("Digite o valor de depósito inicial:");
        var amount = Long.parseLong(sc.nextLine());
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada: " + wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento");
        var tax = sc.nextInt();
        System.out.println("Digite o valor de depósito inicial:");
        var initialFunds = sc.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento criado: " + investment);
    }

    private static void deposit(){
        System.out.println("Digite a chave pix para depósito: ");
        var pix = sc.next();
        System.out.println("Digite o valor do depósito: ");
        var amount = sc.nextLong();

        try {
            accountRepository.deposit(pix,amount);
            System.out.println("Depósito realizado com sucesso!");
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void withdraw(){
        System.out.println("Digite a chave pix para saque: ");
        var pix = sc.next();
        System.out.println("Digite o valor do saque: ");
        var amount = sc.nextLong();

        try {
            accountRepository.withDraw(pix,amount);
            System.out.println("Saque realizado com sucesso!");
        } catch (NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }

    private static void transferToAccount(){
        System.out.println("Digite a chave pix da conta de origem: ");
        var source = sc.next();
        System.out.println("Digite a chave pix da conta de destino: ");
        var target = sc.next();
        System.out.println("Digite o valor do depósito: ");
        var amount = sc.nextLong();

        try {
            accountRepository.transferMoney(source, target, amount);
            System.out.println("Transferência realizada com sucesso!");
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void createWalletInvestment(){
        System.out.println("Digite a chave pix da conta: ");
        var pix = sc.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Digite o identificador do investimento: ");
        var investmentId = sc.nextInt();
        var investmentWallet = investmentRepository.initInvestment(account, investmentId);
        System.out.println("Conta de investimento criada com sucesso!");
    }

    private static void investMore() { // Renomeado para mais clareza
        System.out.println("Digite a chave pix da conta para investimento: ");
        var pix = sc.next();
        System.out.println("Digite o valor do investimento: ");
        var amount = sc.nextLong();

        try {
            investmentRepository.investMore(pix, amount);
            System.out.println("Investimento realizado com sucesso!");
        } catch (RuntimeException ex) { // Captura exceções como AccountNotFound, etc.
            System.out.println("Erro ao realizar investimento: " + ex.getMessage());
        }
    }

    private static void rescueInvestment() {
        System.out.println("Digite a chave pix para resgate do investimento: ");
        var pix = sc.next();
        System.out.println("Digite o valor do resgate: ");
        var amount = sc.nextLong();

        try {
            investmentRepository.rescueInvestment(pix, amount);
            System.out.println("Resgate realizado com sucesso!");
        } catch (RuntimeException ex) {
            System.out.println("Erro ao realizar resgate: " + ex.getMessage());
        }
    }

    private static void checkHistory() {
        System.out.println("Digite a chave pix da conta para ver o extrato: ");
        var pix = sc.next();
        AccountWallet wallet;

        try {
            wallet = accountRepository.findByPix(pix);
            var audit = wallet.getFinancialTransactions();

            if (audit.isEmpty()) {
                System.out.println("Não há transações no histórico desta conta.");
            } else {
                System.out.println("---- EXTRATO DA CONTA: " + pix + " ----");
                audit.forEach(System.out::println); // LINHA ADICIONADA PARA IMPRIMIR
                System.out.println("---------------------------------");
            }

        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
