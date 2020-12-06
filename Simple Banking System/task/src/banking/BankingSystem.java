package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankingSystem {
    static List<Account> accounts = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void run() {
        boolean isExit = false;

        while (!isExit) {
            welcomeInterface();
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    registerCard();
                    break;
                case 2:
                    loggingInBankSystem();
                    break;
                case 0:
                    System.out.println("Bye!");
                    isExit = true;
                    break;
            }
        }
    }

    private static void loggingInBankSystem() {
        System.out.println("Enter your card number:");
        String cardNumberTest = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String  pinTest = scanner.nextLine();
        boolean isExist = false;
        Account current = null;
        for (Account account : accounts) {
            if (account.getCard().equals(new Card(cardNumberTest, pinTest))) {
                isExist = true;
                current = account;
                break;
            }
        }
        if (isExist) {
            System.out.println("You have successfully logged in!");
            boolean isLogout = false;

            while (!isLogout) {

                System.out.println("\n1. Balance\n2. Log out\n0. Exit\n");
                int loginAction = Integer.parseInt(scanner.nextLine());

                switch (loginAction) {
                    case 1:
                        System.out.println(current.getBalance());
                        break;
                    case 2:
                        System.out.println("\nYou have successfully logged out!");
                        isLogout = true;
                        break;
                    case 0:
                        System.out.println("\nBye!");
                        return;
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!");
        }
        
    }

    private static void registerCard() {
        Account newUser = new Account(CardGenerator.create());
        accounts.add(newUser);
        System.out.println(String.format(
                "Your card has been created\n" +
                        "Your card number:\n" +
                        "%s\n" +
                        "Your card PIN:\n" +
                        "%s",
                newUser.getCard().getNumber(),
                newUser.getCard().getPin())
        );
    }

    private static void welcomeInterface() {
        System.out.println(
                "1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit"
        );
    }


}
