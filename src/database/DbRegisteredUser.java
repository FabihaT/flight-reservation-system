package src.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.sql.*;

public class DbRegisteredUser {
    // START DBREGISTEREDUSER
    public static Map<String, Object> getRegisteredUserInfo(int accountID) throws ClassNotFoundException, IOException {
        Map<String, Object> registeredUserInfo = new HashMap<>();
        try {
            String query = "SELECT * FROM REGISTEREDUSERS r " +
                           "WHERE r.accountID = ?";
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, accountID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                registeredUserInfo.put("registeredUserID", resultSet.getInt("registeredUserID"));
                registeredUserInfo.put("accountID", resultSet.getInt("accountID"));
                registeredUserInfo.put("airlineAgentID", resultSet.getInt("airlineAgentID"));
                registeredUserInfo.put("hasMembership", resultSet.getString("hasMembership"));
                registeredUserInfo.put("companyCard", resultSet.getInt("companyCard"));
                registeredUserInfo.put("creditCard", resultSet.getInt("creditCard"));
               }
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return registeredUserInfo;
    }

    public static void cancelTicket(int ticketID) throws ClassNotFoundException, IOException {
        try {
            // Get the seatMapID associated with the ticket
            String seatMapQuery = "SELECT seatMapID FROM TICKETS WHERE ticketID = ?";
            PreparedStatement seatMapStatement = DbConnect.getConnection().prepareStatement(seatMapQuery);
            seatMapStatement.setInt(1, ticketID);
            ResultSet seatMapResult = seatMapStatement.executeQuery();
            int seatMapID = -1;
            if (seatMapResult.next()) {
                seatMapID = seatMapResult.getInt("seatMapID");
            }
            seatMapStatement.close();
    
            // Update the SEATMAPPING table to mark the seat as available
            String updateSeatStatusQuery = "UPDATE SEATMAPPING SET seatStatus = 'available' WHERE seatMapID = ?";
            PreparedStatement updateSeatStatusStatement = DbConnect.getConnection().prepareStatement(updateSeatStatusQuery);
            updateSeatStatusStatement.setInt(1, seatMapID);
            int seatUpdateRowCount = updateSeatStatusStatement.executeUpdate();
           
            updateSeatStatusStatement.close();
    
            // Delete the ticket from the TICKETS table
            String deleteTicketQuery = "DELETE FROM TICKETS WHERE ticketID = ?";
            PreparedStatement deleteTicketStatement = DbConnect.getConnection().prepareStatement(deleteTicketQuery);
            deleteTicketStatement.setInt(1, ticketID);
            int ticketDeleteRowCount = deleteTicketStatement.executeUpdate();
            
            deleteTicketStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void addNewRegisteredUser(String firstName, String lastName, String email, String role, String password,
        String hasMembership, int houseNumber, String streetName, String city, String province, String postalCode) throws ClassNotFoundException, IOException {
        try {
            DbConnect.getConnection().setAutoCommit(false);

            // Insert into NAMES
            String insertNameQuery = "INSERT INTO NAMES (firstName, lastName) VALUES (?, ?)";
            try (PreparedStatement insertNameStatement = DbConnect.getConnection().prepareStatement(insertNameQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertNameStatement.setString(1, firstName);
                insertNameStatement.setString(2, lastName);
                insertNameStatement.executeUpdate();

                // Retrieve the generated nameID
                int nameID;
                try (ResultSet generatedKeys = insertNameStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nameID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating name failed, no ID obtained.");
                    }
                }
                    // Insert into ACCOUNTS
                    String insertAccountQuery = "INSERT INTO ACCOUNTS (nameID, email, role, password) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertAccountStatement = DbConnect.getConnection().prepareStatement(insertAccountQuery, Statement.RETURN_GENERATED_KEYS)) {
                        insertAccountStatement.setInt(1, nameID);
                        insertAccountStatement.setString(2, email);
                        insertAccountStatement.setString(3, role);
                        insertAccountStatement.setString(4, password);
                        insertAccountStatement.executeUpdate();

                        // Retrieve the generated accountID
                        int accountID;
                        try (ResultSet generatedKeys = insertAccountStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                accountID = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("Creating account failed, no ID obtained.");
                            }
                        }

                        // Insert into ADDRESSES
                        String insertAddressQuery = "INSERT INTO ADDRESSES (houseNumber, streetName, city, province, postalCode) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertAddressStatement = DbConnect.getConnection().prepareStatement(insertAddressQuery, Statement.RETURN_GENERATED_KEYS)) {
                            insertAddressStatement.setInt(1, houseNumber);
                            insertAddressStatement.setString(2, streetName);
                            insertAddressStatement.setString(3, city);
                            insertAddressStatement.setString(4, province);
                            insertAddressStatement.setString(5, postalCode);

                            insertAddressStatement.executeUpdate();

                            // Retrieve the generated addressID
                            int addressID;
                            try (ResultSet generatedKeys = insertAddressStatement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    addressID = generatedKeys.getInt(1);
                                } else {
                                    throw new SQLException("Creating address failed, no ID obtained.");
                                }
                            }

                            // Update ACCOUNTS with the addressID
                            String updateAccountQuery = "UPDATE ACCOUNTS SET addressID = ? WHERE accountID = ?";
                            try (PreparedStatement updateAccountStatement = DbConnect.getConnection().prepareStatement(updateAccountQuery)) {
                                updateAccountStatement.setInt(1, addressID);
                                updateAccountStatement.setInt(2, accountID);
                                updateAccountStatement.executeUpdate();
                            }

                            // Assign a random airline agent
                            int airlineAgentID = getRandomAirlineAgent();

                            // Insert into REGISTEREDUSERS
                            String insertRegisteredUserQuery = "INSERT INTO REGISTEREDUSERS (accountID, airlineAgentID, hasMembership, companyCard, creditCard) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement insertRegisteredUserStatement = DbConnect.getConnection().prepareStatement(insertRegisteredUserQuery)) {
                                insertRegisteredUserStatement.setInt(1, accountID);
                                insertRegisteredUserStatement.setInt(2, airlineAgentID);
                                insertRegisteredUserStatement.setString(3, hasMembership);
                                insertRegisteredUserStatement.setObject(4, null);
                                insertRegisteredUserStatement.setObject(5, null);
                                insertRegisteredUserStatement.executeUpdate();
                            }
                        }
                    }
                
            }

            // Commit the transaction
            DbConnect.getConnection().commit();
        } catch (SQLException e) {
            try {
                // Rollback in case of exception
                DbConnect.getConnection().rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            e.printStackTrace();
        } finally {
            try {
                // Set autocommit back to true
                DbConnect.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getRandomAirlineAgent() throws SQLException, ClassNotFoundException, IOException {
        // Query to retrieve a random airline agent ID
        String query = "SELECT airlineAgentID FROM AIRLINEAGENTS ORDER BY RAND() LIMIT 1";
        
        try (PreparedStatement statement = DbConnect.getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("airlineAgentID");
            } else {
                throw new SQLException("No airline agents available.");
            }
        }
    }

    public static List<Integer> getUserTickets(int accountID) throws ClassNotFoundException, IOException {
        List<Integer> userTickets = new ArrayList<>();

        try {
            // Prepare SQL statement
            String query = "SELECT * FROM TICKETS t " +
                           "JOIN ACCOUNTS a ON t.accountID = a.accountID " +
                           "WHERE a.accountID = ?";
            try (PreparedStatement statement = DbConnect.getConnection().prepareStatement(query)) {
                    statement.setInt(1, accountID);
                // Execute query
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Retrieve and store discount values
                    while (resultSet.next()) {

                        userTickets.add(resultSet.getInt("ticketID"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
        }

        return userTickets;
    }
    // END DBREGISTEREDUSER
}
