package banking.io;

import banking.Account;
import banking.bankcard.Card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class UserHistoryFile {
    private File uFile;
    private static final String localPath = "./user_history_files/";
    private static final String prefix = "bank_history_user_id# ";
    private static final String ext = ".txt";

    public UserHistoryFile(Account account) {
        Card userCard = account.getCard();
        uFile = new File(localPath + prefix + userCard.getNumber() + ext);
        if (!uFile.exists()) {
            try {
                uFile.createNewFile();
            } catch (IOException e) {
                System.out.println("file doesn't exist, error");
            }
        }
    }

    public void deleteFile() {
        uFile.delete();
    }

    public void printInfo(String info) {
        try (PrintStream ps = new PrintStream(new FileOutputStream(uFile, true))) {
            ps.println("\n" + info);
        } catch (IOException e) {
            System.out.println("file exception!");
        }

    }

    public void printMessage(String message) {
        printInfo(message);
        System.out.println(message);
    }

}
