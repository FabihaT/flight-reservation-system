package src.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbFlight {

    // START DBFLIGHTINFO

    public static List<Map<String, Object>> getSeatingChart(int flightID) throws ClassNotFoundException, IOException {
        List<Map<String, Object>> seatingChart = new ArrayList<>();

        try {
            String query1 = "SELECT * FROM SEATS s " +
                    "JOIN SEATMAPPING sm ON s.seatID = sm.seatID " +
                    "WHERE sm.flightID = ?";

            String query = "SELECT * FROM SEATMAPPING sm " +
                    "JOIN SEATS s ON sm.seatID = s.seatID " +
                    "WHERE sm.flightID = ?";


            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, flightID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> seatInfo = new HashMap<>();

                seatInfo.put("seatMapID", resultSet.getString("seatMapID"));
                seatInfo.put("seatName", resultSet.getString("seatName"));
                seatInfo.put("seatStatus", resultSet.getString("seatStatus"));
                seatInfo.put("seatType", resultSet.getString("seatClass"));
                seatingChart.add(seatInfo);
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return seatingChart;
    }

    public static Map<String, Object> getFlightInfo(int flightID) throws ClassNotFoundException, IOException {
        Map<String, Object> flightInfo = new HashMap<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM FLIGHTS WHERE flightID = ?";
            try (PreparedStatement statement = DbConnect.getConnection().prepareStatement(sql)) {
                statement.setInt(1, flightID);

                // Execute query
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Check if a row was returned
                    if (resultSet.next()) {
                        // Retrieve and store flight information
                        flightInfo.put("Flight ID", resultSet.getInt("flightID"));
                        flightInfo.put("Aircraft ID", resultSet.getInt("aircraftID"));
                        flightInfo.put("Gate", resultSet.getString("gate"));
                        flightInfo.put("Departure Date", resultSet.getString("departureDate"));
                        flightInfo.put("Departure Time", resultSet.getString("departureTime"));
                        flightInfo.put("Arrival Time", resultSet.getString("arrivalTime"));
                        flightInfo.put("Destination", resultSet.getString("destination"));
                        flightInfo.put("Origin", resultSet.getString("origin"));
                        flightInfo.put("Price", resultSet.getDouble("price"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
        }

        return flightInfo;
    }

    public static List<Integer> getAllFlights() throws ClassNotFoundException, IOException {
        List<Integer> flightList = new ArrayList<>();

        try {
            // Query to get flights for a given flight attendant
            String query = "SELECT * FROM FLIGHTS";

            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                flightList.add(resultSet.getInt("flightID"));
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return flightList;
    }

    // END DBFLIGHTINFO
}
