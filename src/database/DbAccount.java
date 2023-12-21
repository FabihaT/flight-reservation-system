package src.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbAccount {

    public static Map<String, Object> getAccountInfo(String email, String password) throws ClassNotFoundException, IOException {
        Map<String, Object> accountInfo = new HashMap<>();

        try {
            String query = "SELECT * FROM ACCOUNTS a " +
                           "JOIN NAMES n ON a.nameID = n.nameID " +
                           "JOIN ADDRESSES adr ON a.addressID = adr.addressID " +
                           "WHERE a.email = ? AND a.password = ?";

            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountInfo.put("accountID", resultSet.getString("accountID"));
                accountInfo.put("firstName", resultSet.getString("firstName"));
                accountInfo.put("lastName", resultSet.getString("lastName"));
                accountInfo.put("houseNum", resultSet.getString("houseNumber"));
                accountInfo.put("streetName", resultSet.getString("streetName"));
                accountInfo.put("city", resultSet.getString("city"));
                accountInfo.put("province", resultSet.getString("province"));
                accountInfo.put("postalCode", resultSet.getString("postalCode"));
                accountInfo.put("email", resultSet.getString("email"));
                accountInfo.put("role", resultSet.getString("role"));
                accountInfo.put("password", resultSet.getString("password"));
                // Add other information you want to include as key-value pairs
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accountInfo;
    }

    public static Map<String, Object> getAccountInfo(int accountID) throws ClassNotFoundException, IOException {
        Map<String, Object> accountInfo = new HashMap<>();

        try {
            String query = "SELECT * FROM ACCOUNTS a " +
                           "JOIN NAMES n ON a.nameID = n.nameID " +
                           "JOIN ADDRESSES adr ON a.addressID = adr.addressID " +
                           "WHERE a.accountID = ?";

            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, accountID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountInfo.put("accountID", resultSet.getString("accountID"));
                accountInfo.put("firstName", resultSet.getString("firstName"));
                accountInfo.put("lastName", resultSet.getString("lastName"));
                accountInfo.put("houseNum", resultSet.getString("houseNumber"));
                accountInfo.put("streetName", resultSet.getString("streetName"));
                accountInfo.put("city", resultSet.getString("city"));
                accountInfo.put("province", resultSet.getString("province"));
                accountInfo.put("postalCode", resultSet.getString("postalCode"));
                accountInfo.put("email", resultSet.getString("email"));
                accountInfo.put("role", resultSet.getString("role"));
                accountInfo.put("password", resultSet.getString("password"));
                // Add other information you want to include as key-value pairs
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accountInfo;
    }

    public static boolean verifyLogin(String email, String password, String role) throws ClassNotFoundException, IOException {
        try {
            // Prepare SQL statement
            String sql = "SELECT COUNT(*) FROM ACCOUNTS WHERE email = ? AND password = ? AND role = ?";
            try (PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(sql)) {
                // Set parameters
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);

                // Execute query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if there is a row with the given email and password
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return true; // Account exists with the given credentials
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
        }

        return false; // No account found with the given credentials
    }
}
