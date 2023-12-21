package src.gui;
import javax.swing.*;

import src.Application;
import src.database.DbFlight;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightsPage extends JPanel {

    private static List<Integer> flightList;
    private static int flightIDToBook;
    private JPanel activeCityPanel;

    public FlightsPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
    }

    public void initializeFlightsPage() throws ClassNotFoundException, IOException {
        flightList = DbFlight.getAllFlights();
        setLayout(new BorderLayout());
        setupComponents();
    }

    private List<Map<String, String>> flightDisplayInfo() throws ClassNotFoundException, IOException{
        List<Map<String, String>> flightDisplayInfoList = new ArrayList<>();
        
        for (int flightID : flightList) {
            Map<String, Object> flightInfo = DbFlight.getFlightInfo(flightID);
            String origin = (String) flightInfo.get("Origin");
            String dest = (String) flightInfo.get("Destination");
            String departTime = (String) flightInfo.get("Departure Time");
            String arrivalTime = (String) flightInfo.get("Arrival Time");
            String departDate = (String) flightInfo.get("Departure Date");

            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("FlightID", String.valueOf(flightID));
            infoMap.put("Origin", origin);
            infoMap.put("Destination", dest);
            infoMap.put("DepartureTime", departTime);
            infoMap.put("ArrivalTime", arrivalTime);
            infoMap.put("DepartureDate", departDate);

            flightDisplayInfoList.add(infoMap);
        }

        return flightDisplayInfoList;
    }


    private void setupComponents() throws ClassNotFoundException, IOException {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel destinationLabel = new JLabel("Choose your Destination:");
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(destinationLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));

        List<Map<String, String>> flightDisplayList = flightDisplayInfo();

        for (Map<String, String> flightInfo : flightDisplayList) {
            JPanel boxPanel = createBoxPanel(flightInfo);
            gridPanel.add(boxPanel);
        }

        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton bookFlightButton = new JButton();
        bottomPanel.add(bookFlightButton);
        add(bottomPanel, BorderLayout.SOUTH);
     

        if (LoginPage.getLoggedIn() == true) {
            bookFlightButton.setText("Confirm Flight");
            bookFlightButton.addActionListener(e -> {
                if (activeCityPanel == null) {
                    // No flight selected, show an error message
                    JOptionPane.showMessageDialog(this, "Please select a flight first.", "Flight Not Selected", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Flight selected, proceed to the seat map page
                    try {
                        Application.getSeatMapPage().initializeSeatMapPage();
                    } catch (ClassNotFoundException | IOException e1) {
                        e1.printStackTrace();
                    }
                    PageNavigation.navigateTo("seatmapPage");
                }
            });
        } else if (LoginPage.getLoggedIn() == false){
            bookFlightButton.setText("Login to Book Flight");
            bookFlightButton.addActionListener(e -> PageNavigation.navigateTo("loginPage"));
        }
    }

    private JPanel createBoxPanel(Map<String, String> flightInfo) {
        JPanel boxPanel = new JPanel(new BorderLayout());
        boxPanel.setPreferredSize(new Dimension(150, 100));
        boxPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        JLabel flightIDLabel = new JLabel("Flight ID: " + flightInfo.get("FlightID"));
        // Create a custom panel for the details
        JPanel detailsPanel = new JPanel(new GridLayout(6, 1));

        // Customize the style of Origin -> Destination label
        JLabel routeLabel = new JLabel("<html><b><font size=+1>" +
                flightInfo.get("Origin") + " -> " + flightInfo.get("Destination") + "</font></b></html>");
        routeLabel.setHorizontalAlignment(JLabel.CENTER);
        detailsPanel.add(routeLabel);

        // Add other details
        detailsPanel.add(new JLabel("Date: " + flightInfo.get("DepartureDate")));
        detailsPanel.add(new JLabel("Departure Time: " + flightInfo.get("DepartureTime")));
        detailsPanel.add(new JLabel("Arrival Time: " + flightInfo.get("ArrivalTime")));

        // Add the details panel to the boxPanel
        boxPanel.add(detailsPanel, BorderLayout.CENTER);
        

        boxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boxPanel != activeCityPanel) {
                    boxPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boxPanel != activeCityPanel) {
                    boxPanel.setBorder(BorderFactory.createEmptyBorder());
                    boxPanel.setBackground(Color.LIGHT_GRAY);
                }
                
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (activeCityPanel != null) {
                    activeCityPanel.setBorder(BorderFactory.createEmptyBorder());
                    activeCityPanel.setBackground(Color.LIGHT_GRAY);
                    
                    

                }
                // get flight ID from box panel
                String chosenFlightInfo = flightIDLabel.getText();
              
                flightIDToBook = extractFlightID(chosenFlightInfo);
            
                boxPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                activeCityPanel = boxPanel;
            }
        });

        return boxPanel;
    }

    private static int extractFlightID(String input) {
        // Define the pattern for the Flight ID format
        Pattern pattern = Pattern.compile("Flight ID: (\\d+)");
        Matcher matcher = pattern.matcher(input);

        // Check if the pattern is found
        if (matcher.find()) {
            // Group 1 contains the Flight ID as a string
            String flightIDString = matcher.group(1);
            return Integer.parseInt(flightIDString);
        }

        // Return -1 if Flight ID is not found or not a valid integer
        return -1;
    }

    public static int getFlightID() {
        return flightIDToBook;
    }
}
