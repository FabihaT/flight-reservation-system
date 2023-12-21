package src.gui;

import javax.swing.*;

import src.database.DbFlight;
import src.database.DbRegisteredUser;
import src.database.DbSeat;
import src.database.DbTicket;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;
import java.util.List;
import java.util.Map;

public class UserAccountPage extends AccountPage {

    private JPanel activeBoxPanel;
    private int ticketToCancel;
    private JPanel selectedBoxPanel;

    public UserAccountPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        super(cardLayout, cardPanel, cardStack);
        activeBoxPanel = null;
    }

    @Override
    protected void setupBottomPanel(JPanel bottomPanel) throws ClassNotFoundException, IOException {
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // Your Flights label centered
        JLabel flightsLabel = new JLabel("Your Flights");
        flightsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        flightsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(flightsLabel);

        JPanel gridPanel = createGridPanel();
        // Wrap the gridPanel in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        bottomPanel.add(scrollPane);

        // Add some vertical space
        bottomPanel.add(Box.createVerticalStrut(10));

        // Add "Cancel Flight" button centered
        JButton cancelButton = new JButton("Cancel Flight");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(cancelButton);

        // Add ActionListener to the "Cancel Flight" button
        cancelButton.addActionListener(e -> {
            double cost = 0;
            if (activeBoxPanel != null) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel?", "Cancel Flight", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        // Call the cancelTicket function with the ticketID
                        Map<String, String> ticketInfo = DbTicket.getTicketInfo(ticketToCancel);
                        cost = Double.parseDouble((String) ticketInfo.get("cost"));
                        DbRegisteredUser.cancelTicket(ticketToCancel);

                        // Remove the selected boxPanel
                        if (selectedBoxPanel != null) {
                            gridPanel.remove(selectedBoxPanel);
                            gridPanel.revalidate();
                            gridPanel.repaint();
                        }
                    } catch (ClassNotFoundException | IOException ex) {
                        ex.printStackTrace();
                    }
                }

                
                JOptionPane.showMessageDialog(this, "A refund of $" + cost +" has been processed.", "Ticket Refund", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private JPanel createGridPanel() throws ClassNotFoundException, IOException{
        JPanel gridPanel = new JPanel(new GridLayout(0, 1, 0, 0));

        List<Integer> ticketlist = DbRegisteredUser.getUserTickets(LoginPage.getLoginApp().getMainAccount().getAccountID());
     
        for (int ticketID : ticketlist) {
            Map<String, String> ticketInfo = DbTicket.getTicketInfo(ticketID);
      
            int flightID = Integer.parseInt( (String)ticketInfo.get("flightID"));
            int seatMapID = Integer.parseInt((String) ticketInfo.get("seatMapID"));

            // Create a box panel for each ticket and add it to the grid
            JPanel boxPanel = createBoxPanel(flightID, seatMapID, ticketID);
            gridPanel.add(boxPanel);
        }

        return gridPanel;
    }

    private JPanel createBoxPanel(int flightID, int seatMapID, int ticketID) throws ClassNotFoundException, IOException {
        JPanel boxPanel = new JPanel(new GridLayout(0, 2));

        //Retrieve Flight and Seat info of ticket
        Map<String, Object> flightInfo = DbFlight.getFlightInfo(flightID);
        String origin = (String) flightInfo.get("Origin");
        String destination = (String) flightInfo.get("Destination");
        String departureDate = (String) flightInfo.get("Departure Date");
        String arrivalTime = (String) flightInfo.get("Arrival Time");
        String departureTime = (String) flightInfo.get("Departure Time");
        String gate = (String) flightInfo.get("Gate");

        Map<String, String> ticketInfo = DbTicket.getTicketInfo(ticketID);
        double price = Double.parseDouble(ticketInfo.get("cost"));

        Map<String, Object> seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);
        String seatNum = (String) seatInfo.get("seatName");
        String seatClass = (String) seatInfo.get("seatClass");

        // Create labels for each piece of information
        JLabel routeLabel = new JLabel("<html><b>" + origin + " -> " + destination + "</b></html>");
        JLabel departureDateLabel = new JLabel("Departure Date: " + departureDate);
        JLabel arrivalTimeLabel = new JLabel("Arrives at: " + arrivalTime);
        JLabel departureTimeLabel = new JLabel("Departs at: " + departureTime);
        JLabel gateLabel = new JLabel("Gate Number: " + gate);
        JLabel seatClassLabel = new JLabel("Seat Class: " + seatClass);
        JLabel seatNumLabel = new JLabel("Seat Number: " + seatNum);
        JLabel priceLabel = new JLabel("Total Price: " + price);

        // Add labels to boxPanel
        boxPanel.add(routeLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(departureDateLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(arrivalTimeLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(departureTimeLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(gateLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(seatClassLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(seatNumLabel);
        boxPanel.add(new JLabel()); // Empty space
        boxPanel.add(priceLabel);

        // Set initial border
        boxPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        //Add hover and click listeners to the boxpanels
        boxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boxPanel != activeBoxPanel) {
                    boxPanel.setBackground(Color.LIGHT_GRAY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boxPanel != activeBoxPanel) {
                    boxPanel.setBackground(null);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Add an active state on click
                if (activeBoxPanel != null && activeBoxPanel != boxPanel) {
                    activeBoxPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                    activeBoxPanel.setBackground(null);
                }
                boxPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                activeBoxPanel = boxPanel;

                selectedBoxPanel = boxPanel;
                ticketToCancel = ticketID;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Reset the border after click
                boxPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GREEN));
            }
        });

        return boxPanel;
    }
}
