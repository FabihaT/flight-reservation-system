package src.gui;
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class ConfirmationPage extends JPanel {

    public ConfirmationPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
        initializeConfirmationPage();
    }

    private void initializeConfirmationPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a centered panel for the confirmation message
        JPanel confirmationPanel = createConfirmationPanel();
        add(confirmationPanel, gbc);

        // Create a panel for the "Return to Home" button
        gbc.gridy = 1;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton returnToHomeButton = new JButton("Return to Home");
        returnToHomeButton.addActionListener(e -> PageNavigation.navigateTo("userHomePage"));
        buttonPanel.add(returnToHomeButton);
        add(buttonPanel, gbc);
    }

    private JPanel createConfirmationPanel() {
        JPanel confirmationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Create a large label saying "Thank you for your purchase!" centered
        JLabel thankYouLabel = new JLabel("Thank you for your purchase!");
        thankYouLabel.setFont(new Font("Arial", Font.BOLD, 24));
        confirmationPanel.add(thankYouLabel, gbc);

        // Create a label saying "Your flight reservation receipt has been emailed." centered
        gbc.gridy = 1;
        JLabel receiptLabel = new JLabel("Your flight receipt has been emailed.");
        receiptLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmationPanel.add(receiptLabel, gbc);

        return confirmationPanel;
    }
}
