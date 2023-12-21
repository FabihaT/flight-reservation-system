package src.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.*;

public class DbBooking {

    // START DBBOOKING

    public static int bookFlight(String seatValue, int flightID, int accountID) throws ClassNotFoundException, IOException {
        int generatedTicketID = -1;
        try {
            DbConnect.getConnection().setAutoCommit(false);

            // Check if the seat is available
            int seatMapID = getSeatMapID(flightID, seatValue);

            if (seatMapID == -1) {
                return -1;
            }

            // Insert into TICKETS
            String insertTicketQuery = "INSERT INTO TICKETS (flightID, accountID, seatMapID) VALUES (?, ?, ?)";
            try (PreparedStatement insertTicketStatement = DbConnect.getConnection().prepareStatement(insertTicketQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertTicketStatement.setInt(1, flightID);
                insertTicketStatement.setInt(2, accountID);
                insertTicketStatement.setInt(3, seatMapID);
                insertTicketStatement.executeUpdate();

                // Retrieve the generated ticketID
                int ticketID;
                try (var generatedKeys = insertTicketStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticketID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating ticket failed, no ID obtained.");
                    }
                }
                generatedTicketID = ticketID;

                // Update SEATMAPPING
                String updateSeatMappingQuery = "UPDATE SEATMAPPING SET seatStatus = 'occupied' WHERE seatMapID = ?";
                try (PreparedStatement updateSeatMappingStatement = DbConnect.getConnection().prepareStatement(updateSeatMappingQuery)) {
                    updateSeatMappingStatement.setInt(1, seatMapID);
                    updateSeatMappingStatement.executeUpdate();
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
        return generatedTicketID;
    }

    private static int getSeatMapID(int flightID, String seatValue) throws SQLException, ClassNotFoundException, IOException {
        String selectSeatMapIDQuery = "SELECT sm.seatMapID " +
                                      "FROM SEATMAPPING sm " +
                                      "JOIN SEATS s ON sm.seatID = s.seatID " +
                                      "WHERE sm.flightID = ? AND s.seatName = ? AND sm.seatStatus = 'available'";
        try (PreparedStatement selectSeatMapIDStatement = DbConnect.getConnection().prepareStatement(selectSeatMapIDQuery)) {
            selectSeatMapIDStatement.setInt(1, flightID);
            selectSeatMapIDStatement.setString(2, seatValue);

            try (ResultSet resultSet = selectSeatMapIDStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("seatMapID");
                } else {
                    return -1; // Seat not available
                }
            }
        }
    }

    // END DBBOOKING
}
