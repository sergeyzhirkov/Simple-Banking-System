package banking.database;

import banking.Account;
import banking.bankcard.Card;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class SQLiteDatabase implements InterfaceDB {
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)";
    private static final String SQL_ADD_ACCOUNT = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private static final String SQL_FIND_ACCOUNT = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";
    private SQLiteDataSource dataSource;
    private Connection con;
    private final String DATABASE_URL;

    public SQLiteDatabase(String databaseName) {
        dataSource = new SQLiteDataSource();
        DATABASE_URL = "jdbc:sqlite:" + databaseName;
        dataSource.setUrl(DATABASE_URL);
        try {
            con = dataSource.getConnection();
        } catch (SQLException throwables) {
            System.out.println("Connection error!");
        }
        createTable();
    }

    private void createTable() {
        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            System.out.println("Creation table error!");
        }
    }

    @Override
    public void createAccount(Account userAccount) {
        try (PreparedStatement ps = con.prepareStatement(SQL_ADD_ACCOUNT)) {
            ps.setString(1, userAccount.getCard().getNumber());
            ps.setString(2, userAccount.getCard().getPin());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Creating account error!");
        }
    }

    @Override
    public Account findAccount(Card userCard) {
        Account resultAccount = null;
        try (PreparedStatement ps = con.prepareStatement(SQL_FIND_ACCOUNT)) {
            ps.setString(1, userCard.getNumber());
            ps.setString(2,userCard.getPin());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return resultAccount;
            }
            int balance = rs.getInt("balance");
            resultAccount =  new Account(balance, userCard);
        } catch (SQLException e) {
            System.out.println("Finding account error!");
        }
        return resultAccount;
    }

    @Override
    public void close() {
        try {
            con.close();
        } catch (SQLException throwables) {
            System.out.println("Closing error!");
        }
    }
}
