package src.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {

    private static Connection dbConnect;
    private static DbConnect onlyDBInstance;

    private DbConnect() throws ClassNotFoundException, IOException {
        createConnection();
    }

    public static DbConnect getOnlyDBInstance() throws ClassNotFoundException, IOException {
        if (onlyDBInstance == null) {
            onlyDBInstance = new DbConnect();
        }
        return onlyDBInstance;
    }

    public static Connection getConnection() throws ClassNotFoundException, IOException {
        if (dbConnect == null) {
            createConnection();
        }
        return dbConnect;
    }

    private static void createConnection() throws ClassNotFoundException, IOException {
        try {
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/airline", "user", "password");
            System.out.println("connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetDatabase() {
        try {
            // Read the SQL script
            BufferedReader reader = new BufferedReader(new FileReader("airline.sql"));
            StringBuilder script = new StringBuilder();
            String line;

            // Read the script line by line
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");

                // If a semicolon is found, execute the current statement
                if (line.trim().endsWith(";")) {
                    executeScript(script.toString());
                    script.setLength(0); // Clear the StringBuilder for the next statement
                }
            }

            // Close resources
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeScript(String script) {
        try {
            // Execute the script
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate(script);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
