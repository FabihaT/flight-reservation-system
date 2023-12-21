package src.gui;
import javax.swing.*;

import src.Application;

import java.awt.*;
import java.io.IOException;
import java.util.Stack;

// Abstract class for the homepage
public abstract class HomePage extends JPanel {

    private JPanel homePage = new JPanel(new BorderLayout());
    private static String role;

    HomePage(String role, CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
        this.role = role;
    }

    // Abstract method for creating the button panel
    protected abstract JPanel createButtonPanel() throws ClassNotFoundException, IOException;

    // Common method for creating the top panel
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + role + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Create a panel for My Account and Logout buttons
        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton myAccountButton = new JButton("My Account");
        JButton logoutButton = new JButton("Logout");

        // Add ActionListener for the Logout button
        myAccountButton.addActionListener(e -> {
            String accountPageIdentifier = getAccountPageIdentifier(role);
            try {
                Application.getUserAccountPage().setupAccountPage();
                Application.getAttendantAccountPage().setupAccountPage();
                Application.getAgentAccountPage().setupAccountPage();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
            PageNavigation.navigateTo(accountPageIdentifier);
        });
        logoutButton.addActionListener(e -> PageNavigation.navigateTo("loginPage"));

        accountPanel.add(myAccountButton);
        accountPanel.add(logoutButton);

        topPanel.add(accountPanel, BorderLayout.SOUTH);

        return topPanel;
    }

    // Override this method in each subclass for specific setup
    protected void setupHomePage() throws ClassNotFoundException, IOException {
        setLayout(new BorderLayout());
        add(homePage, BorderLayout.CENTER);
        add(createTopPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
    }

    private String getAccountPageIdentifier(String role) {
        if ("User".equals(role)) {
            return "userAccountPage";
        } else if ("Flight Attendant".equals(role)) {
            return "attendantAccountPage";
        } else if ("Flight Agent".equals(role)) {
            return "agentAccountPage";
        }
        return ""; 
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String r) {
        role = r;
    }
    
}
