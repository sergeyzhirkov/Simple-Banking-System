package banking.database;

import banking.Account;
import banking.bankcard.Card;

public interface InterfaceDB {

    void createAccount(Account userAccount);

    Account findAccount(Card userCard);

    void close();
}
