package src.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.IOException;
import java.sql.*;

public class DbPayment {

    // START DBPAYMENT

    public static void newCompanyCard(int registeredUserID) throws SQLException, ClassNotFoundException, IOException {
        // Insert into CREDITCARDS
        Random random = new Random();

        String digits;
        StringBuilder buildDigits = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            buildDigits.append(digit);
        }
        digits = buildDigits.toString();

        String expirationDate = "05/30";

        String code;
        StringBuilder buildCode = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int digit = random.nextInt(10);
            buildCode.append(digit);
        }
        code = buildCode.toString();

        String insertCreditCardsQuery = "INSERT INTO COMPANYCREDITCARDS (digits, expirationDate, code) VALUES (?, ?, ?)";
        try (PreparedStatement insertCompanyCreditCardsStatement = DbConnect.getConnection().prepareStatement(insertCreditCardsQuery, Statement.RETURN_GENERATED_KEYS)) {

            insertCompanyCreditCardsStatement.setString(1, digits);
            insertCompanyCreditCardsStatement.setString(2, expirationDate);
            insertCompanyCreditCardsStatement.setString(3, code);
            insertCompanyCreditCardsStatement.executeUpdate();

            // Retrieve the generated accountID
            int companyCreditCardID;
            try (ResultSet generatedKeys = insertCompanyCreditCardsStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    companyCreditCardID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating credit card failed, no ID obtained.");
                }
            }

            // Insert into REGISTEREDUSERS
            String insertRegisteredUserQuery = "INSERT INTO REGISTEREDUSERS (companyCard) VALUES (?)";
            try (PreparedStatement insertRegisteredUserStatement = DbConnect.getConnection().prepareStatement(insertRegisteredUserQuery)) {
                insertRegisteredUserStatement.setInt(1, companyCreditCardID);
                insertRegisteredUserStatement.executeUpdate();
            }
        }
    }

    public static void newCreditCard(int registeredUserID, String digits, String expDate, String code) throws SQLException, ClassNotFoundException, IOException {
        // Insert into CREDITCARDS
        String insertCreditCardsQuery = "INSERT INTO CREDITCARDS (cardDigits, expirationDate, securityCode) VALUES (?, ?, ?)";
        try (PreparedStatement insertCompanyCardsStatement = DbConnect.getConnection().prepareStatement(insertCreditCardsQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertCompanyCardsStatement.setString(1, digits);
            insertCompanyCardsStatement.setString(2, expDate);
            insertCompanyCardsStatement.setString(3, code);
            insertCompanyCardsStatement.executeUpdate();

            // Retrieve the generated accountID
            int creditCardID;
            try (ResultSet generatedKeys = insertCompanyCardsStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    creditCardID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating credit card failed, no ID obtained.");
                }
            }

            // Insert into REGISTEREDUSERS
            String insertRegisteredUserQuery = "UPDATE REGISTEREDUSERS SET creditCard = ? WHERE registeredUserID = ?";
            try (PreparedStatement insertRegisteredUserStatement = DbConnect.getConnection().prepareStatement(insertRegisteredUserQuery)) {
                insertRegisteredUserStatement.setInt(1, creditCardID);
                insertRegisteredUserStatement.setInt(2, registeredUserID);
                insertRegisteredUserStatement.executeUpdate();
            }
        }
    }

    public static Map<String, Object> getCompanyCardInfo(int companyCardID) throws ClassNotFoundException, IOException {
        Map<String, Object> companyCardInfo = new HashMap<>();

        try {
            String query1 = "SELECT ru.registeredUserID " +
                    "FROM REGISTEREDUSERS ru " +
                    "JOIN COMPANYCREDITCARDS cc ON ru.companyCard = cc.companyCardID " +
                    "WHERE ru.registeredUserID = ?";

            String query = "SELECT * FROM COMPANYCREDITCARDS ccc " +
                    "WHERE ccc.companyCardID = ?";

            try (PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, companyCardID);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    companyCardInfo.put("companyCardID", resultSet.getInt("companyCardID"));
                    companyCardInfo.put("companyCardDigits", resultSet.getString("companyCardDigits"));
                    companyCardInfo.put("companyExpirationDate", resultSet.getString("companyExpirationDate"));
                    companyCardInfo.put("companySecurityCode", resultSet.getString("companySecurityCode"));

                    return companyCardInfo;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Return an empty map or handle the case where the ticket information is not found
        return companyCardInfo;
    }

    public static Map<String, Object> getPersonalCardInfo(int cardID) throws ClassNotFoundException, IOException {
        Map<String, Object> personalCardInfo = new HashMap<>();

        try {
            String query = "SELECT * FROM CREDITCARDS c " +
                    "WHERE c.cardID = ?";

            try (PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, cardID);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    personalCardInfo.put("cardID", resultSet.getInt("cardID"));
                    personalCardInfo.put("cardDigits", resultSet.getString("CardDigits"));
                    personalCardInfo.put("expirationDate", resultSet.getString("expirationDate"));
                    personalCardInfo.put("securityCode", resultSet.getString("securityCode"));

                    return personalCardInfo;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Return an empty map or handle the case where the ticket information is not found
        return personalCardInfo;
    }

    public static List<Integer> getDiscounts() throws ClassNotFoundException, IOException {
        List<Integer> discounts = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT discountVal FROM PERKS WHERE perkType = 'priceDiscount'";
            try (Statement statement = DbConnect.getConnection().createStatement()) {

                // Execute query
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    // Retrieve and store discount values
                    while (resultSet.next()) {
                        discounts.add(resultSet.getInt("discountVal"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
        }

        return discounts;
    }

    public static List<String> getPerks() throws ClassNotFoundException, IOException {
        List<String> perks = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM PERKS";
            try (Statement statement = DbConnect.getConnection().createStatement()) {

                // Execute query
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    // Retrieve and store discount values
                    while (resultSet.next()) {
                        perks.add(resultSet.getString("perkDescription"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
        }

        return perks;
    }

    // END DBPAYMENT
}
