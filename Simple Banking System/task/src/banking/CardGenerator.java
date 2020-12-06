package banking;

import java.util.Random;

public class CardGenerator {
    static Random random = new Random();

    public static Card create() {
        final int BIN = 400000;
        final int ACCOUNT_IDENTIFIER = random.nextInt(900_000_000) + 100_000_000;
        final int CHECKSUM = random.nextInt(9);
        String cardNumber = "" + BIN + ACCOUNT_IDENTIFIER + CHECKSUM;
        String pin = "" + (1000 + random.nextInt(9000));
        return new Card(cardNumber, pin);
    }

}
