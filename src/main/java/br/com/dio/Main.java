package br.com.dio;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;

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

            switch (opc){
                case 1: createAccount();
                case 2: createInvestment();
                case 3:
                case 4: deposit();
                case 5: withdraw();
                case 6:
                case 7:
                case 8:
                case 9: accountRepository.list().forEach(System.out::println);
                case 10: investmentRepository.list().forEach(System.out::println);
                case 11: investmentRepository.listWallets().forEach(System.out::println);
                case 12: {
                    investmentRepository.updateAmount();
                    System.out.println("Atualização dos investimentos concluída");
                }
                case 13:
                case 14: System.exit(0);
                default: System.out.println("Opção inválida!");

            }
        }
    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix, separadas por ; (ponto e vírgula)");
        var pix = Arrays.stream(sc.next().split(";")).toList();
        System.out.println("Digite o valor de depósito inicial:");
        var amount = sc.nextLong();
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
        } catch (NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }
}
