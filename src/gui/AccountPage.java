package src.gui;

import javax.swing.*;

import src.database.DbAccount;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;

// Abstract class for the account page
public abstract class AccountPage extends JPanel {
    Map<String, Object> accountInfo;
    // Variables for user information labels
    private JLabel firstNameLabel = new JLabel();
    private JLabel lastNameLabel = new JLabel();
    private JLabel emailLabel = new JLabel();
    private JLabel streetAddressLabel = new JLabel();
    private JLabel cityProvinceLabel = new JLabel();
    private JLabel postalCodeLabel = new JLabel();

    AccountPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
    }

    // Abstract method for setting up the bottom panel
    protected abstract void setupBottomPanel(JPanel bottomPanel) throws ClassNotFoundException, IOException;

    // Common method for creating the top panel
    protected JPanel createTopPanel() throws ClassNotFoundException, IOException {
        JPanel topPanel = new JPanel(new BorderLayout());
        // Create a back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> PageNavigation.navigateBack());
        topPanel.add(backButton, BorderLayout.WEST);
        
        JLabel accountLabel = new JLabel("Account Information", SwingConstants.CENTER);
        accountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(accountLabel, BorderLayout.NORTH);

        JPanel infoGridPanel = createInfoGridPanel();
        topPanel.add(infoGridPanel, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createInfoGridPanel() throws ClassNotFoundException, IOException{
        JPanel infoGridPanel = new JPanel(new GridLayout(0, 2, 0, 0));

        accountInfo = DbAccount.getAccountInfo(LoginPage.getLoginApp().getMainAccount().getAccountID());

        String firstName = (String) accountInfo.get("firstName");
        String lastName = (String) accountInfo.get("lastName");
        String email = (String) accountInfo.get("email");
        String houseNum = (String) accountInfo.get("houseNum");
        String streetName = (String) accountInfo.get("streetName");
        String city = (String) accountInfo.get("city");
        String province = (String) accountInfo.get("province");
        String postalCode = (String) accountInfo.get("postalCode");

        // Append values to labels
        firstNameLabel.setText("First Name: " + firstName);
        lastNameLabel.setText("Last Name: " + lastName);
        emailLabel.setText("Email: " + email);

        // Join houseNum and streetName for streetAddressLabel
        String streetAddress = houseNum + " " + streetName;
        streetAddressLabel.setText("Street Address: " + streetAddress);

        // Join city and province for cityProvinceLabel
        String cityProvince = city + ", " + province;
        cityProvinceLabel.setText("City, Province: " + cityProvince);

        postalCodeLabel.setText("Postal Code: " + postalCode);
    
        addLabelAndBorder(firstNameLabel, infoGridPanel);
        addLabelAndBorder(lastNameLabel, infoGridPanel);
        addLabelAndBorder(emailLabel, infoGridPanel);
        addLabelAndBorder(streetAddressLabel, infoGridPanel);
        addLabelAndBorder(cityProvinceLabel, infoGridPanel);
        addLabelAndBorder(postalCodeLabel, infoGridPanel);
    
        return infoGridPanel;
    }

    private void addLabelAndBorder(JLabel label, JPanel panel) {
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.BLACK));
        labelPanel.add(label, BorderLayout.CENTER);
        panel.add(labelPanel);
    }

    protected void setupAccountPage() throws ClassNotFoundException, IOException {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(createTopPanel());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        setupBottomPanel(bottomPanel); // Method to be implemented in subclasses

        JScrollPane scrollPane = new JScrollPane(bottomPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the topPanel and the JScrollPane to the AccountPage
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

}
