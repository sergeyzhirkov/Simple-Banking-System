package banking.io;

import banking.Account;
import banking.bankcard.Card;
import banking.database.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHistoryFile {
    private static final Logger logger = Logger.getLogger(UserHistoryFile.class.getPackageName());

    static {
        logger.setLevel(Level.ALL);
    }

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
                logger.info("create new file user_banking_history " + userCard.getNumber());
            } catch (IOException e) {
                logger.severe("file doesn't exist, error");
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
                logger.severe("file exception!");
            }
        });
    }

    public void printMessage(String message) {
        logger.fine("print message" + message);
        printInfo(message);
        System.out.println(message);
    }

    public void closeExecuteService() {
        logger.info("stop file thread service");
        fileThreadService.shutdown();
    }

}
