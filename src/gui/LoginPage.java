package src.gui;
import javax.swing.*;

import src.Application;
import src.database.DbAccount;
import src.database.DbPayment;
import src.database.DbRegisteredUser;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.List;

public class LoginPage extends JPanel {

    private static Application app = new Application();
    private static boolean loggedin;   

    private static Map<String, String> userCredentials = new HashMap<>();

    public LoginPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);

        // Set up the layout
        setupLayout();
    }

    public static boolean getLoggedIn() {
        return loggedin;
    }

    public static Application getLoginApp() {
        return app;
    }

    private void setupLayout() {
        // Use GridBagLayout for the main panel
        setLayout(new GridBagLayout());

        // Create the three card panels
        JPanel topPanel = createTopPanel();
        JPanel middlePanel = createMiddlePanel();
        JPanel bottomPanel = createBottomPanel();

        // Add the card panels to the main panel using GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(topPanel, gbc);

        gbc.gridy++;
        add(middlePanel, gbc);

        gbc.gridy++;
        add(bottomPanel, gbc);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, User!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMiddlePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));

        // Create outer panels with padding and border
        JPanel leftColumn = createPaddedPanelWithBorder();
        JPanel rightColumn = createPaddedPanelWithBorder();

        // Left column
        JPanel leftContent = new JPanel(new GridBagLayout());
        leftContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        leftContent.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        leftContent.add(new JLabel("Password:"), gbc);

        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ensure horizontal filling

        // Use JTextField instead of emailField to demonstrate width preservation
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        leftContent.add(emailField, gbc);

        gbc.gridy++;
        leftContent.add(passwordField, gbc);

        gbc.gridy++;
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            HomePage.setRole("User");
            boolean correctLogin = checkLogin(emailField, passwordField);
            if (correctLogin == true) {
                try {
                    userCredentials = getUserCredentials(emailField.getText(), new String(passwordField.getPassword()));
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }
                if (userCredentials.get("hasMembership").compareTo("yes") == 0) {
                    String perks = "As a member you recieve these perks: \n";
                    List<String> perkList =new ArrayList<>();
                    try {
                        perkList = DbPayment.getPerks();
                    } catch (ClassNotFoundException | IOException e1) {
                        e1.printStackTrace();
                    }

                    for (String perk : perkList) {
                        perks = perks + "\n*    " + perk;
                    }
                    JOptionPane.showMessageDialog(this, perks, "Membership Perks", JOptionPane.INFORMATION_MESSAGE);
                }

                Application.setIDtoBook(getLoginApp().getMainAccount().getAccountID());
                loggedin = true;
                PageNavigation.navigateTo("userHomePage");
            }
        });

        registerButton.addActionListener(e -> PageNavigation.navigateTo("registerPage"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        leftContent.add(buttonPanel, gbc);

        leftColumn.add(leftContent);

        // Right column
        JPanel rightContent = new JPanel(new GridBagLayout());
        rightContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        rightContent.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        rightContent.add(new JLabel("Password:"), gbc);

        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ensure horizontal filling

        JTextField emailField2 = new JTextField();
        JPasswordField passwordField2 = new JPasswordField();

        rightContent.add(emailField2, gbc);

        gbc.gridy++;
        rightContent.add(passwordField2, gbc);

        gbc.gridy++;
        JButton attendantButton = new JButton("Login as Attendant");
        JButton agentButton = new JButton("Login as Agent");

        attendantButton.addActionListener(e -> {
            HomePage.setRole("Flight Attendant");
            boolean correctLogin = checkLogin(emailField2, passwordField2);
    
            if (correctLogin == true) {
                loggedin = true;
                try {
                    int accID = Application.getMainUser().getAccount().getAccountID();
                    Application.getAttendantHomePage().displayflights(accID);
                    PageNavigation.navigateTo("attendantHomePage");
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                    
                }
            }

        });

        agentButton.addActionListener(e -> {
            HomePage.setRole("Flight Agent");
            boolean correctLogin = checkLogin(emailField2, passwordField2);
            if (correctLogin == true) {
                loggedin = true;
                try {
                int accID = Application.getMainUser().getAccount().getAccountID();
                Application.getAgentHomePage().displaycustomers(accID);
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
                PageNavigation.navigateTo("agentHomePage");
            }
        });


        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightButtonPanel.add(attendantButton);
        rightButtonPanel.add(agentButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightContent.add(rightButtonPanel, gbc);

        rightColumn.add(rightContent);

        // Add the left and right columns to the middle panel
        panel.add(leftColumn);
        panel.add(rightColumn);

        return panel;
    }

    private JPanel createPaddedPanelWithBorder() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        JButton browseButton = new JButton("Browse as Guest");
        panel.add(browseButton, BorderLayout.CENTER);

        browseButton.addActionListener(e -> {
            loggedin = false;
            try {
                Application.getFlightsPage().initializeFlightsPage();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
            PageNavigation.navigateTo("flightsPage");
        });

        return panel;
    }

    // Helper method to check if the account exists in the predefined list
    private boolean accountExists(String email, String password, String role) throws ClassNotFoundException, IOException {
      
        return DbAccount.verifyLogin(email, password, role);
    }
    public static Map<String, String> getUserCredentials(String email, String password) throws ClassNotFoundException, IOException {
        Map<String, Object> accountInfo = DbAccount.getAccountInfo(email, password);
        
        userCredentials.put("email", (String) accountInfo.get("email"));
        userCredentials.put("password", (String) accountInfo.get("password"));
        int accountID = Integer.parseInt((String) accountInfo.get("accountID"));
        Map<String, Object> regUserInfo = DbRegisteredUser.getRegisteredUserInfo(accountID);
        userCredentials.put("hasMembership", (String) regUserInfo.get("hasMembership"));
        return userCredentials;
    }

    public boolean checkLogin(JTextField emailField, JPasswordField passwordField) {
        String enteredEmail = emailField.getText();
        String enteredPassword = new String(passwordField.getPassword());
        String enteredRole = HomePage.getRole();
       

        if (enteredRole.compareTo("User") == 0){
            enteredRole = "registeredUser";
        }
        else if (enteredRole.compareTo("Flight Attendant") == 0) {
            enteredRole = "flightAttendent";
        }
        else if (enteredRole.compareTo("Flight Agent") == 0) {
            enteredRole = "airlineAgent";
        }


        try {
            if (accountExists(enteredEmail, enteredPassword, enteredRole)) {
                app.setMainAccount(enteredEmail, enteredPassword);

                app.setMainUser(app.getMainAccount());
                return true;

                
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (HeadlessException | ClassNotFoundException | IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }
}
