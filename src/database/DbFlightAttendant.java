package src.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbFlightAttendant {
    
    // START DBFLIGHTATTENDANT

    public static List<String> getPassengerList(int flightID) throws ClassNotFoundException, IOException {
        List<String> passengerList = new ArrayList<>();
    
        try {
            String query = "SELECT n.firstName, n.lastName FROM TICKETS t " +
                           "JOIN ACCOUNTS a ON t.accountID = a.accountID " +
                           "JOIN NAMES n ON a.nameID = n.nameID " +
                           "WHERE t.flightID = ?";
            
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, flightID);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                String passengerName = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
                passengerList.add(passengerName);
            }
    
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return passengerList;
    }

    public static List<Map<String, Object>> getFlightsForFlightAttendant(int flightAttendantID) throws ClassNotFoundException, IOException {
        List<Map<String, Object>> flightList = new ArrayList<>();
    
        try {
            // Query to get flights for a given flight attendant
            String query = "SELECT f.*, n.firstName, n.lastName FROM FLIGHTATTENDENTASSIGNMENTS fa " +
                           "JOIN FLIGHTS f ON fa.flightID = f.flightID " +
                           "JOIN FLIGHTATTENDENTS ft ON fa.flightAttendentID = ft.flightAttendentID " +
                           "JOIN ACCOUNTS a ON ft.accountID = a.accountID " +
                           "JOIN NAMES n ON a.nameID = n.nameID " +
                           "WHERE fa.flightAttendentID = ?";
            
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, flightAttendantID);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                Map<String, Object> flightInfo = new HashMap<>();
                flightInfo.put("flightID", resultSet.getInt("flightID"));
                flightInfo.put("aircraftID", resultSet.getInt("aircraftID"));
                flightInfo.put("gate", resultSet.getString("gate"));
                flightInfo.put("departureDate", resultSet.getDate("departureDate"));
                flightInfo.put("departureTime", resultSet.getTime("departureTime"));
                flightInfo.put("arrivalTime", resultSet.getTime("arrivalTime"));
                flightInfo.put("destination", resultSet.getString("destination"));
                flightInfo.put("origin", resultSet.getString("origin"));
                flightInfo.put("firstName", resultSet.getString("firstName"));
                flightInfo.put("lastName", resultSet.getString("lastName"));
    
                flightList.add(flightInfo);
            }
    
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return flightList;
    }


    public static Map<String, Object> getFlightAttendantInfo(int accountID) throws ClassNotFoundException, IOException {
        Map<String, Object> attendantInfo = new HashMap<>();

        try {
            String query = "SELECT * FROM FLIGHTATTENDENTS f " +
                           "WHERE f.accountID = ?";
            
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, accountID);
            
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                attendantInfo.put("flightAttendentID", resultSet.getInt("flightAttendentID"));
                attendantInfo.put("accountID", resultSet.getInt("accountID"));
                }
    
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return attendantInfo;
    }
    

    // END DBFLIGHTATTENDANT
}

