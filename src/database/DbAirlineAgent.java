package src.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.sql.*;

public class DbAirlineAgent {
    
    public static Map<String, Object> getAirlineAgentInfo(int accountID) throws ClassNotFoundException, IOException {
        Map<String, Object> agentInfo = new HashMap<>();

        try {
            String query = "SELECT * FROM AIRLINEAGENTS a " +
                           "WHERE a.accountID = ?";
            
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, accountID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                agentInfo.put("airlineAgentID", resultSet.getInt("airlineAgentID"));
                agentInfo.put("accountID", resultSet.getInt("accountID"));
            }
    
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return agentInfo;
    }

    public static List<Integer> getAgentsRegUsers(int accountID) throws ClassNotFoundException, IOException {
        List<Integer> registeredUsers = new ArrayList<>();

        try {
            String query = "SELECT * FROM REGISTEREDUSERS ru " +
                           "JOIN AIRLINEAGENTS a ON ru.airlineAgentID = a.airlineAgentID " +
                           "WHERE a.accountID = ?";
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, accountID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                registeredUsers.add(resultSet.getInt("registeredUserID"));
            }
               
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registeredUsers;
    }
}
