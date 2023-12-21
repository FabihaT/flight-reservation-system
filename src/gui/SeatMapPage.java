package src.gui;
import javax.swing.*;

import src.Application;
import src.database.DbFlight;
import src.database.DbSeat;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SeatMapPage extends JPanel {
    private JPanel selectionInfoPanel;
    private JLabel selectionLabel;
    private JPanel activeSeatPanel;
    private int flightID;
    private static int chosenSeatMapID;
    // private static int generatedTicketID;
    private static String chosenSeatValue;
    private String chosenSeatInfo;
    private static boolean addInsurance;

    private static final List<String> seatTypes = Arrays.asList("Business-class", "Comfort", "Ordinary");

    public SeatMapPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
    }

    public static int getChosenSeatMapID(){
        return chosenSeatMapID;
    }

    public static String getChosenSeatValue(){
        return chosenSeatValue;
    }

    public static boolean getAddInsurance() {
        return addInsurance;
    }

    public void initializeSeatMapPage() throws ClassNotFoundException, IOException {
        flightID = FlightsPage.getFlightID();
        
        List<Map<String, Object>> seatingChart = DbFlight.getSeatingChart(flightID);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a panel for the "Please select a seat" label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel selectSeatLabel = new JLabel("Please select a seat:");
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

        // Create a panel for the "Proceed to Pay" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox wantInsurance = new JCheckBox("Add Cancellation Insurance");
        JButton proceedToPayButton = new JButton("Confirm Seat and Proceed to Payment");

        proceedToPayButton.addActionListener(e -> {
            addInsurance = wantInsurance.isSelected();



            if (activeSeatPanel == null) {
                // No seat selected, show an error message
                JOptionPane.showMessageDialog(this, "Please select a seat first.", "Seat Not Selected", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    remove(topPanel);
                    remove(gridPanel);
                    remove(selectionInfoPanel);
                    remove(buttonPanel);
                    try {
                        Application.getPaymentPage().initializePaymentPage();
                    } catch (SQLException e1) {

                        e1.printStackTrace();
                    }
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                };

                PageNavigation.navigateTo("paymentPage");
            }

        });

        buttonPanel.add(proceedToPayButton);
        buttonPanel.add(wantInsurance);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(buttonPanel, gbc);
    }

    private JPanel createGridPanel() throws ClassNotFoundException, IOException {

        List<Map<String, Object>> seatingChart = DbFlight.getSeatingChart(flightID);
        flightID = FlightsPage.getFlightID();

        JPanel gridPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Map<String, Object> seat : seatingChart) {
            // int seatMapID = (int) seat.get("seatMapID");
            int seatMapID = Integer.parseInt((String) seat.get("seatMapID"));
            String seatName = (String) seat.get("seatName");
            String seatStatus = (String) seat.get("seatStatus");
            String seatType = (String) seat.get("seatType");
            
            JPanel seatPanel = createBoxPanel(seatMapID, seatName, seatStatus, seatType);
            gridPanel.add(seatPanel);
        }

        return gridPanel;
    }

    private JPanel createBoxPanel(int seatMapID, String seatName, String seatStatus, String seatType) {
        JPanel boxPanel = new JPanel(new BorderLayout());
        boxPanel.setPreferredSize(new Dimension(150, 100));
        boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel seatLabel = new JLabel(seatName);
        
        seatLabel.setHorizontalAlignment(JLabel.CENTER);

        Color seatColor = getSeatColor(seatType, seatStatus);
        boxPanel.setBackground(seatColor);

        boxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (seatStatus.compareTo("available") == 0) {
                    boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (activeSeatPanel != boxPanel) {
                    boxPanel.setBorder(BorderFactory.createEmptyBorder());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                chosenSeatValue = seatName;
                chosenSeatMapID = seatMapID;
                if (seatStatus.compareTo("available") == 0) {
                    
                    if (activeSeatPanel != null) {
                    activeSeatPanel.setBorder(BorderFactory.createEmptyBorder());                    
                }
                boxPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                activeSeatPanel = boxPanel;

                try {
                    updateSelectionLabel(seatMapID);
                } catch (ClassNotFoundException | IOException e1) {

                    e1.printStackTrace();
                }
                }

                
            }
        });
        boxPanel.add(seatLabel, BorderLayout.CENTER);
        return boxPanel;
    }

    private Color getSeatColor(String seatType, String seatStatus) {
        if (seatStatus.compareTo("available") == 0) {
            if (seatType.compareTo("ordinary") == 0) {
                return Color.LIGHT_GRAY;
            }
            else if (seatType.compareTo("comfort") == 0) {
                return new Color(144, 238, 144);
            }
            else {
                return new Color(173, 216, 230);
            }
        }
        else {
            return Color.DARK_GRAY;
        }
        
    }

    private void updateSelectionLabel(int seatMapID) throws ClassNotFoundException, IOException {
        Map<String, Object> seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);
        selectionLabel.setText("You have selected: " + seatInfo.get("seatClass") + " seat: " + seatInfo.get("seatName"));
    }
}
