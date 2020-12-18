package banking;

import banking.bankcard.Card;
import banking.bankcard.CardGenerator;
import banking.database.InterfaceDB;
import banking.database.SQLiteDatabase;
import java.util.Scanner;

public class BankingSystem {
    private Scanner scanner = new Scanner(System.in);
    private InterfaceDB dataBase;
    private boolean isExit = false;

    public void run(String databaseName) {
        dataBase = new SQLiteDatabase(databaseName);

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
            if (isExit) {
                dataBase.close();
            }
        }
    }

    private void loggingInBankSystem() {
        System.out.println("Enter your card number:");
        String cardNumberTest = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String  pinTest = scanner.nextLine();
        Account current = dataBase.findAccount(new Card(cardNumberTest, pinTest));

        if (current != null) {
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
                        isExit = true;
                        return;
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!");
        }
        
    }

    private void registerCard() {
        Account newUser = new Account(CardGenerator.create());
        dataBase.createAccount(newUser);
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

    private void welcomeInterface() {
        System.out.println(
                "1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit"
        );
    }


}
