package banking.io;

import banking.Account;
import banking.bankcard.Card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserHistoryFile {
    private File uFile;
    private static final String LOCAL_PATH = "./user_history_files/";
    private static final String PREFIX = "bank_history_user_id# ";
    private static final String EXT = ".txt";
    private final ExecutorService fileThreadService = Executors.newSingleThreadExecutor();

    public UserHistoryFile(Account account) {
        Card userCard = account.getCard();
        uFile = new File(LOCAL_PATH + PREFIX + userCard.getNumber() + EXT);
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
        fileThreadService.execute(() -> {
            try (PrintStream ps = new PrintStream(new FileOutputStream(uFile, true))) {
                ps.println("\n" + info);
            } catch (IOException e) {
                System.out.println("file exception!");
            }
        });
    }

    public void printMessage(String message) {
        printInfo(message);
        System.out.println(message);
    }

    public void closeExecuteService() {
        fileThreadService.shutdown();
    }

}
