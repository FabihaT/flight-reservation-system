package src.gui;

import javax.swing.*;
import src.database.DbFlightAttendant;
import java.awt.*;
import java.io.IOException;

import java.util.List;
import java.util.Stack;


public class newPage extends JPanel{
    
    private JPanel selectionInfoPanel;
    private JLabel selectionLabel;
    private int flightIDTwo;

    public newPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
    }

    public void initializeNewPage(int flightIDtoView) throws ClassNotFoundException, IOException {
        flightIDTwo = flightIDtoView;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a panel for the "Please select a seat" label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel selectSeatLabel = new JLabel("Passenger List");
        selectSeatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(selectSeatLabel);
        add(topPanel, gbc);

        // Create a panel for the grid of small grey boxes
        JPanel gridPanel = createGridPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(gridPanel, gbc);

        // Create a panel for seat selection information
        selectionInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectionLabel = new JLabel("");
        selectionInfoPanel.add(selectionLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(selectionInfoPanel, gbc);
    }

    private JPanel createGridPanel() throws ClassNotFoundException, IOException {

        List<String> passengerDisplay = DbFlightAttendant.getPassengerList(flightIDTwo);
        flightIDTwo = FlightsPage.getFlightID();

        JPanel gridPanel = new JPanel(new GridLayout(12, 1, 5, 1));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        int i = 0;
        for (String passenger : passengerDisplay) {
            JPanel seatPanel = createBoxPanel(passengerDisplay.get(i));
            i++;
            gridPanel.add(seatPanel);
        }
        JButton returnbutton = new JButton("Return to HomePage");
        gridPanel.add(returnbutton);
        returnbutton.addActionListener(e -> {
            if (HomePage.getRole().compareTo("Flight Attendant") == 0) {
                PageNavigation.navigateTo("attendantHomePage");
            } else if (HomePage.getRole().compareTo("Flight Agent") == 0) {
                PageNavigation.navigateTo("agentHomePage");
            }
            
        });
        return gridPanel;
    }

    private JPanel createBoxPanel(String seatName) {
        JPanel boxPanel = new JPanel(new BorderLayout());
        boxPanel.setPreferredSize(new Dimension(150, 100));
        boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel seatLabel = new JLabel(seatName);
        seatLabel.setHorizontalAlignment(JLabel.CENTER);
        boxPanel.add(seatLabel, BorderLayout.CENTER);
        return boxPanel;
    }
}
