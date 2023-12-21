package src.account;

import java.io.IOException;
import java.util.Map;

import src.database.DbAccount;
import src.database.DbRegisteredUser;

public class Account {
    // private member variables
    private Name fullName; // Full name of the account holder
    private Address address; // Address of the account holder
    private String email; // Email of the account holder
    private String password; // Password of the account holder
    private int accountID; // Account ID of the account holder
    private String role; // Role of the account holder

    // constructor for registering a new account
    public Account(String firstName, String lastName, String email, String password,
        String hasMembership, int houseNumber, String streetName, String city, String province, String postalCode) throws ClassNotFoundException, IOException {

        // Add new registered user to the database
        DbRegisteredUser.addNewRegisteredUser(firstName, lastName, email, "registeredUser", password,
        hasMembership, houseNumber, streetName, city, province, postalCode);

        // Retrieve account information from the database
        Map<String, Object> accountInfo = DbAccount.getAccountInfo(email, password);

        // Extract account information from the retrieved data
        String fName = (String) accountInfo.get("firstName");
        String lName = (String) accountInfo.get("lastName");
        int houseNum = (int) accountInfo.get("houseNum");
        String sName = (String) accountInfo.get("streetName");
        String c = (String) accountInfo.get("city");
        String p = (String) accountInfo.get("province");
        String pCode = (String) accountInfo.get("postalCode");

        // Assign the extracted information to member variables
        this.accountID = (int) accountInfo.get("accountID");
        this.fullName = new Name(fName, lName);
        this.address = new Address(houseNum, sName, c, p, pCode);
        this.email = (String) accountInfo.get("email");
        this.password = (String) accountInfo.get("password");
        this.role = (String) accountInfo.get("role");
    }
    
    // constructor for retrieving an existing account from the database
    public Account(String email, String password) throws ClassNotFoundException, IOException {
        // Retrieve account information from the database
        Map<String, Object> accountInfo = DbAccount.getAccountInfo(email, password);

        // Extract account information from the retrieved data
        String firstName = (String) accountInfo.get("firstName");
        String lastName = (String) accountInfo.get("lastName");
        int houseNum = Integer.parseInt((String) accountInfo.get("houseNum"));
        String streetName = (String) accountInfo.get("streetName");
        String city = (String) accountInfo.get("city");
        String province = (String) accountInfo.get("province");
        String postalCode = (String) accountInfo.get("postalCode");

        // Assign the extracted information to member variables
        this.accountID = Integer.parseInt((String) accountInfo.get("accountID"));
        this.fullName = new Name(firstName, lastName);
        this.address = new Address(houseNum, streetName, city, postalCode, province);
        this.email = (String) accountInfo.get("email");
        this.password = (String) accountInfo.get("password");
        this.role = (String) accountInfo.get("role");
    }

    // method to display account information
    public void displayAccountInfo() {
        // Display account information
    }

    // getters
    public Name getFullName() {
        return fullName;
    }    

    public Address getAddress() {
        return address;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getRole() {
        return role;
    }
}
