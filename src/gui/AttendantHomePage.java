package src.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.Map;
import src.Application;
import src.database.*;

public class AttendantHomePage extends HomePage {

    private int flightIDtoView;
    private JPanel activepanel;

    public AttendantHomePage(JPanel cardPanel, CardLayout cardLayout, String role, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        super(role, cardLayout, cardPanel, cardStack);
        setupHomePage();
    }

    @Override
    protected JPanel createButtonPanel() throws ClassNotFoundException, IOException {
        return new JPanel();
    }

    public void displayflights (int accID) throws ClassNotFoundException, IOException {
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        Map<String,Object> attendantinfo = DbFlightAttendant.getFlightAttendantInfo(accID);
        int attendantID = (int)attendantinfo.get("flightAttendentID");
        List<Map<String, Object>> flightsForAttendant = DbFlightAttendant.getFlightsForFlightAttendant(attendantID);
        for (Map<String, Object> flightInfo : flightsForAttendant) {
            String IDs = ("Flight ID: " + flightInfo.get("flightID") +", Gate: " + flightInfo.get("gate"));
            String locations = (flightInfo.get("origin") + "->" + flightInfo.get("destination"));
            String times = ("Departs at: " + flightInfo.get("departureTime") + flightInfo.get("departureDate") + "and arrives at: " + flightInfo.get("arrivalTime"));
            JPanel boxpanel = createBoxPanel(IDs, locations, times);
            gridPanel.add(boxpanel);
            showFullGrid();
        }
        add(gridPanel, BorderLayout.CENTER);
        
    }

    public void goToNewPage () {
        if (flightIDtoView > 0) {
             try {
                    Application.getNewPage().initializeNewPage(flightIDtoView);
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                };
                PageNavigation.navigateTo("newPage");
        }
    }

    private JPanel createBoxPanel(String line1, String line2, String line3) {
        JPanel boxPanel = new JPanel(new GridLayout(0,1));
        boxPanel.setPreferredSize(new Dimension(150, 100));
        boxPanel.setBackground(Color.LIGHT_GRAY);
        boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lineOne = new JLabel(line1); 
        JLabel lineTwo = new JLabel(line2);
        JLabel lineThree = new JLabel(line3);
        lineOne.setHorizontalAlignment(JLabel.CENTER);
        lineTwo.setHorizontalAlignment(JLabel.CENTER);
        lineThree.setHorizontalAlignment(JLabel.CENTER);
        boxPanel.add(lineOne, BorderLayout.CENTER);
        boxPanel.add(lineTwo, BorderLayout.CENTER);
        boxPanel.add(lineThree, BorderLayout.CENTER);
        
        boxPanel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boxPanel != activepanel) {
                    boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boxPanel != activepanel) {
                    boxPanel.setBorder(BorderFactory.createEmptyBorder());
                    boxPanel.setBackground(Color.LIGHT_GRAY);
                }
                
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (activepanel != null) {
                    activepanel.setBorder(BorderFactory.createEmptyBorder());
                    activepanel.setBackground(Color.LIGHT_GRAY);
                    // get flight ID from box panel
                    String chosenFlightInfo = lineOne.getText();
                  
                    flightIDtoView = extractFlightID(chosenFlightInfo);
                  

                }
                boxPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                activepanel = boxPanel;
                goToNewPage();
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
            String flightIDString = matcher.group(1);
            return Integer.parseInt(flightIDString);
        }
        // Return -1 if Flight ID is not found or not a valid integer
        return -1;
    }

    public int getFlightID() {
        return flightIDtoView;
    }

    private void showFullGrid() {
        for (Component component : getComponents()) {
            if (component instanceof JPanel && component != getComponent(getComponentCount() - 1)) {
                JPanel gridPanel = (JPanel) component;
            }
        }
    }
}
