package src.gui;

import src.database.*;
import src.users.RegisteredUser;
import src.account.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class RegistrationPage extends JPanel {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField houseNumField;
    private JTextField streetNameField;
    private JCheckBox sameAsBillingCheckBox;
    private JTextField cityField;
    private JTextField provinceField;
    private JTextField postalCodeField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private String hasMembership;
    private boolean wantsCompanyCard;
    private Account newAccount;
    private RegisteredUser newRegisteredUser;
    private String enteredfName;
    private String enteredlName;
    private int enteredhouseNum;
    private String enteredStreetNum;
    private String enteredCity;
    private String enteredProvince;
    private String enteredPostalCode;
    private JComboBox<String> membershipDropdown;

    public RegistrationPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
        initRegistrationPage();
    }

    private void initRegistrationPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
    
        // Left column with Personal Information
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(personalInfoLabel, gbc);
    
        // Add input fields and labels to the left column
        gbc.gridy++;
        add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        firstNameField = new JTextField(20);
        add(firstNameField, gbc);
    
        gbc.gridy++;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        lastNameField = new JTextField(20);
        add(lastNameField, gbc);
    
        gbc.gridy++;
        add(new JLabel("House Number:"), gbc);
        gbc.gridy++;
        houseNumField = new JTextField(20);
        add(houseNumField, gbc);
    
        gbc.gridy++;
        add(new JLabel("Street Name:"), gbc);
        gbc.gridy++;
        streetNameField = new JTextField(20);
        add(streetNameField, gbc);
    
        gbc.gridy++;
        JLabel billingLabel = new JLabel("Same as billing address");
        add(createCheckboxPanel(billingLabel), gbc);
    
        gbc.gridy++;
        add(new JLabel("City:"), gbc);
        gbc.gridy++;
        cityField = new JTextField(20);
        add(cityField, gbc);
    
        gbc.gridy++;
        add(new JLabel("Province:"), gbc);
        provinceField = new JTextField(20);
        gbc.gridy++;
    
        add(provinceField, gbc);
    
        gbc.gridy++;
        add(new JLabel("Postal Code:"), gbc);
        gbc.gridy++;
        postalCodeField = new JTextField(10);
        add(postalCodeField, gbc);
    
        // Right column with Account Information
        gbc.gridx = 1;
        gbc.gridy = 0;
    
        JLabel accountInfoLabel = new JLabel("Account Information");
        accountInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(accountInfoLabel, gbc);
    
        // Add input fields and labels to the right column
        gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        emailField = new JTextField(20);
        add(emailField, gbc);
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);
    
        // Add the dropdown below the Password field
        gbc.gridy++;
        add(new JLabel("Choose Membership:"), gbc);
        gbc.gridy++;
        String[] membershipOptions = {"Select One", "I want to be a member", "I want to be a member and get a company card", "I don't want to be a member"};
        membershipDropdown = new JComboBox<>(membershipOptions);
        membershipDropdown.addActionListener(e -> {
            String membershipOption = (String) membershipDropdown.getSelectedItem();
            if (membershipOption.compareTo("I want to be a member") == 0) {
                hasMembership = "yes";
                wantsCompanyCard = false;
            }
            else if (membershipOption.compareTo("I want to be a member and get a company card") == 0) {
                hasMembership = "yes";
                wantsCompanyCard = true;
            }
            else {
                hasMembership = "no";
                wantsCompanyCard = false;
            }
    
        });
        add(membershipDropdown, gbc);

        // Add Register button below the Password field
        gbc.gridy++;
        JButton registerButton = new JButton("Sign Up");
        registerButton.setPreferredSize(new Dimension(130, 30));
        registerButton.addActionListener(e -> {
            String enteredEmail = emailField.getText();
            String enteredPassword = new String(passwordField.getPassword());
            if (validateFields()) {
                try {
                        DbRegisteredUser.addNewRegisteredUser(enteredfName, enteredlName, enteredEmail, "registeredUser", enteredPassword, hasMembership, enteredhouseNum, enteredStreetNum, enteredCity, enteredProvince, enteredPostalCode);
                        newAccount = new Account(enteredEmail, enteredPassword);
                        newRegisteredUser = new RegisteredUser(newAccount);
                        JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                        PageNavigation.navigateTo("loginPage");
                } catch (HeadlessException | ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        gbc.gridwidth = 2; 
        gbc.gridy++;
        add(buttonPanel, gbc);
    }

    private boolean validateFields() {
        enteredfName = firstNameField.getText();
        enteredlName = lastNameField.getText();
        enteredStreetNum = streetNameField.getText();
        enteredCity = cityField.getText();
        enteredProvince = provinceField.getText();
        enteredPostalCode = postalCodeField.getText();
    
        // Check for empty fields
        if (enteredfName.isEmpty() || enteredlName.isEmpty() || enteredCity.isEmpty() || enteredProvince.isEmpty() || enteredPostalCode.isEmpty() || houseNumField.getText().isEmpty() || streetNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Field Missing", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //Check membership option selected
        if (membershipDropdown.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a valid membership option.", "Membership Missing", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        // Check postal code length
        if (enteredPostalCode.length() != 6 || !enteredPostalCode.equals(enteredPostalCode.toUpperCase())) {
            JOptionPane.showMessageDialog(this, "Postal code must be 6 characters and uppercase.", "Invalid Postal Code", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        try {
            // Check if houseNum is a valid integer
            enteredhouseNum = Integer.parseInt(houseNumField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "House number must be a valid integer.", "Invalid House Number", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        // Additional checks can be added as needed
    
        return true;
    }

    private JPanel createCheckboxPanel(JLabel label) {
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JCheckBox checkBox = new JCheckBox();
        checkboxPanel.add(checkBox);
        checkboxPanel.add(label);
        return checkboxPanel;

    }

    public RegisteredUser getNewRegisteredUser(){
        return newRegisteredUser;
    }
}
