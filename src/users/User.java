package src.users;

import java.io.IOException;
import java.sql.SQLException;

import src.account.Account;

public class User {
    private Account account;

    public User(Account account) {
        this.account = account;
    }

    public int browse() throws SQLException, ClassNotFoundException, IOException {
        return -1;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void displayAccountInfo() {
        // calls the GUI to display the account info
        // takes an Account object
    }
}
