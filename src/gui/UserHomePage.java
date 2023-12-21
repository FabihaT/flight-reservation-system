package src.gui;
import javax.swing.*;

import src.Application;

import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class UserHomePage extends HomePage {

    public UserHomePage(JPanel cardPanel, CardLayout cardLayout, String role, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        super(role, cardLayout, cardPanel, cardStack);
        setupHomePage();
    }

    @Override
    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton viewFlightsButton = new JButton("View Flights");
        viewFlightsButton.addActionListener(e -> {
            try {
                
                Application.getFlightsPage().initializeFlightsPage();
                PageNavigation.navigateTo("flightsPage");
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
            
        });
        buttonPanel.add(viewFlightsButton);
        return buttonPanel;
    }
}
