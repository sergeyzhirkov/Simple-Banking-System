package banking.bankcard;

import banking.bankcard.Card;

import java.util.Random;

public class CardGenerator {
    static Random random = new Random();

    public static Card create() {
        String cardNumber = getCardNumber();
        String pin = "" + (1000 + random.nextInt(9000));
        return new Card(cardNumber, pin);
    }

    private static String getCardNumber() {
        final int bin = 400000;
        final int accountIdentifier = random.nextInt(900_000_000) + 100_000_000;
        final int checkSum = algorithmLuhn(bin, accountIdentifier);
        String cardNumber = "" + bin + accountIdentifier + checkSum;
        return cardNumber;
    }

    private static int algorithmLuhn(int bin, int accountIdentifier) {
        String  rawNumbers = ("" + bin + accountIdentifier);
        int sum = 0;
        for (int i = 0; i < rawNumbers.length(); i++) {
            int digit = Integer.parseInt(rawNumbers.substring(i, i+1));
            if (i % 2 == 0) {
                digit *= 2;
            }
            digit = (digit % 10) + (digit / 10);
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }


}
