package src.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbTicket {
    // START DBTICKET
    public static Map<String, String> getTicketInfo(int ticketID) throws ClassNotFoundException, IOException {
        Map<String, String> ticketInfo = new HashMap<>();
        try {
            String query = "SELECT * FROM AIRLINE.TICKETS WHERE ticketID = ?";
            try (PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, ticketID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    ticketInfo.put("ticketID", Integer.toString(resultSet.getInt("ticketID")));
                    ticketInfo.put("accountID", Integer.toString(resultSet.getInt("accountID")));
                    ticketInfo.put("flightID", Integer.toString(resultSet.getInt("flightID")));
                    ticketInfo.put("seatMapID", Integer.toString(resultSet.getInt("seatMapID")));
                    ticketInfo.put("hasInsurance", resultSet.getString("hasInsurance"));
                    ticketInfo.put("cost", Double.toString(resultSet.getDouble("cost")));
                    return ticketInfo;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Return an empty map or handle the case where the ticket information is not found
        return ticketInfo;
    }

    public static void updateTicketCost(int ticketID, double cost) throws SQLException, ClassNotFoundException, IOException {
        String query = "UPDATE TICKETS SET cost = ? WHERE ticketID = ?";
        PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
        preparedStatement.setDouble(1, cost);
        preparedStatement.setInt(2, ticketID);
        preparedStatement.executeUpdate();
    }
    // END DBTICKET
}
