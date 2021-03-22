package banking;

import banking.bankcard.Card;
import banking.bankcard.CardGenerator;
import banking.database.InterfaceDB;
import banking.database.SQLiteDatabase;
import banking.io.UserHistoryFile;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;

public class BankingSystem {
    private static final Logger logger = Logger.getLogger(BankingSystem.class.getPackageName());

    static {
        logger.setLevel(Level.ALL);
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler("log.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileHandler.setLevel(Level.INFO);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
    }

    private Scanner scanner = new Scanner(System.in);
    private InterfaceDB dataBase;
    private boolean isExit = false;
    private UserHistoryFile userHistoryFile;

    public void run(String databaseName) {
        dataBase = new SQLiteDatabase(databaseName);
        logger.info("new start system.");

        while (!isExit) {
            welcomeUI();
            int action;
            try {
                action = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                action = -1;
                logger.warning("can't parse number");
            }
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
                default:
            }
            if (isExit) {
                logger.info("exit program");
                dataBase.close();
                if (userHistoryFile != null) {
                    userHistoryFile.closeExecuteService();
                }
            }
        }
    }

    private void loggingInBankSystem() {
        System.out.println("Enter your card number:");
        String cardNumberTest = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pinTest = scanner.nextLine();
        Account current = dataBase.loginAccount(new Card(cardNumberTest, pinTest));

        userHistoryFile = new UserHistoryFile(current);

        if (current != null) {
            userHistoryFile.printMessage("You have successfully logged in!");
            boolean isLogout = false;

            while (!isLogout) {
                loginUI();
                int loginAction;
                try {
                    loginAction = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    loginAction = -1;
                    logger.warning("can't parse number");
                }

                switch (loginAction) {
                    case 1:
                        userHistoryFile.printInfo("Card balance: ");
                        userHistoryFile.printMessage(current.getBalance() + "$");
                        logger.info("user get card balance");
                        break;
                    case 2:
                        addIncome(current);
                        break;
                    case 3:
                        doTransfer(current);
                        break;
                    case 4:
                        System.out.println("The account has been closed!");
                        dataBase.deleteAccount(current);
                        userHistoryFile.deleteFile();
                        isLogout = true;
                        logger.info("user close account");
                        break;
                    case 5:
                        userHistoryFile.printMessage("\nYou have successfully logged out!");
                        if (userHistoryFile != null) {
                            userHistoryFile.closeExecuteService();
                        }
                        userHistoryFile = null;
                        isLogout = true;
                        logger.info("user logged out");
                        break;
                    case 0:
                        System.out.println("\nBye!");
                        isExit = true;
                        logger.info("user is exit");
                        return;
                    default:
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!");
        }

    }

    private void doTransfer(Account current) {
        System.out.println("Transfer\n" + "Enter card number:");
        String recipientCardNumber = scanner.nextLine();
        if (recipientCardNumber.equals(current.getCard().getNumber())) {
            System.out.println("You can't transfer money to the same account!");
        } else if (!CardGenerator.isCorrectNumber(recipientCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        } else if (!dataBase.findAccount(recipientCardNumber)) {
            System.out.println("Such a card does not exist.");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            int moneyTransfer = 0;
            try {
                moneyTransfer = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("You need to write a number!");
                logger.warning("number format exception");
            }
            if (moneyTransfer > current.getBalance()) {
                System.out.println("Not enough money!" + current.getBalance());
            } else {
                dataBase.updateAccount(current.getCard().getNumber(), -moneyTransfer);
                current.addBalance(-moneyTransfer);
                userHistoryFile.printInfo(String.format("Transfer money: %d \n to card: %s", moneyTransfer, recipientCardNumber));
                dataBase.updateAccount(recipientCardNumber, moneyTransfer);
                userHistoryFile.printMessage("Success!");
                logger.info("transfer money");
            }
        }
    }

    private void addIncome(Account current) {
        System.out.println("Enter income:");
        int increment;
        try {
            increment = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            increment = 0;
            logger.warning("can't parse number");
        }
        dataBase.updateAccount(current.getCard().getNumber(), increment);
        current.addBalance(increment);
        userHistoryFile.printInfo("add balance for: " + increment);
        userHistoryFile.printMessage("Income was added!");
        logger.info("add money to card");
    }

    private void registerCard() {
        Account newUser = new Account(CardGenerator.create());
        dataBase.createAccount(newUser);
        userHistoryFile = new UserHistoryFile(newUser);
        userHistoryFile.printMessage(String.format(
                "Your card has been created\n" +
                        "Your card number:\n" +
                        "%s\n" +
                        "Your card PIN:\n" +
                        "%s",
                newUser.getCard().getNumber(),
                newUser.getCard().getPin())
        );
        userHistoryFile.printInfo("Balance: " + newUser.getBalance() + "$");
        userHistoryFile.closeExecuteService();
        userHistoryFile = null;
        logger.info("register new card " + newUser.getCard().getNumber());
    }

    private void loginUI() {
        System.out.println("" +
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    private void welcomeUI() {
        System.out.println("" +
                "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit"
        );
    }


}
