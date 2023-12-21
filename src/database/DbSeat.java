package src.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbSeat {
    // START DBSEAT
    public static Map<String, Object> getSeatInfo(int flightID, int seatMapID) throws ClassNotFoundException, IOException {
        Map<String, Object> seatInfo = new HashMap<>();
        try {
            String query = "SELECT * FROM SEATMAPPING sm " +
                            "JOIN SEATS s ON sm.seatID = s.seatID " +
                            "WHERE sm.flightID = ? AND sm.seatMapID = ?";
            try (PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, flightID);
                preparedStatement.setInt(2, seatMapID);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    seatInfo.put("seatMapID", resultSet.getInt("seatMapID"));
                    seatInfo.put("seatID", resultSet.getInt("seatID"));
                    seatInfo.put("flightID", resultSet.getInt("flightID"));
                    seatInfo.put("seatStatus", resultSet.getString("seatStatus"));
                    seatInfo.put("seatName", resultSet.getString("seatName"));
                    seatInfo.put("seatClass", resultSet.getString("seatClass"));
                    return seatInfo;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Return an empty map or handle the case where the ticket information is not found
        return seatInfo;
    }

    public static String getSeatType(int ticketID) throws ClassNotFoundException, IOException {
        try {
            String query = "SELECT s.seatClass " +
                           "FROM AIRLINE.SEATS s " +
                           "JOIN AIRLINE.SEATMAPPING sm ON s.seatID = sm.seatID " +
                           "JOIN AIRLINE.TICKETS t ON sm.seatMapID = t.seatMapID " +
                           "WHERE t.ticketID = ?";
    
            try (PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, ticketID);
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    return resultSet.getString("seatClass");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        // Return a default value or handle the case where the seat information is not found
        return "Seat information not available";
    }
    // END DBSEAT
}
