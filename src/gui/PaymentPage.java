package src.gui;
import javax.swing.*;

import src.Application;
import src.account.CreditCard;
import src.database.DbAccount;
import src.database.DbBooking;
import src.database.DbFlight;
import src.database.DbPayment;
import src.database.DbRegisteredUser;
import src.database.DbSeat;
import src.database.DbTicket;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import java.util.Map;
import java.util.Stack;

public class PaymentPage extends JPanel {

    private JCheckBox saveCardInfo;
    private JTextField ccNumberField;
    private JTextField expDateField;
    private JTextField cvvField;

    private int registeredUserID;
    private boolean hasMembership;
    private boolean hasCompanyCard;
    private boolean hasPersonalCard;
    private boolean addInsurance;
    private CreditCard companyCard;
    private CreditCard personalCard;
    private static String chosenSeatValue;
    private static int flightID;
    private static int generatedTicketID;

    public PaymentPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) {
        PageNavigation.initialize(cardStack, cardLayout, cardPanel);
    }

    public static int getGeneratedTicketID() {
        return generatedTicketID;
    }

    public void initializePaymentPage() throws ClassNotFoundException, IOException, SQLException {
        
        chosenSeatValue = SeatMapPage.getChosenSeatValue();
        flightID = FlightsPage.getFlightID();
        generatedTicketID = DbBooking.bookFlight(chosenSeatValue, flightID, Application.getIDtoBook());
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a panel for the "Payment Information" label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel paymentLabel = new JLabel("Payment Information");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(paymentLabel);
        add(topPanel, gbc);

        // Create a panel for credit card information
        gbc.gridy = 1;
        JPanel priceInfoPanel = createPriceInfoPanel();
        add(priceInfoPanel, gbc);

        // Create a panel for credit card information
        gbc.gridy = 2;
        JPanel creditCardPanel = createCreditCardPanel();
        add(creditCardPanel, gbc);

        // Create a panel for the "Proceed to Confirm" button
        gbc.gridy = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(e -> {
           
            String receipt = "Receipt for Payement: \n";
            String ticket = "Ticket: \n";
            Map<String, String> ticketInfo;
            String cost = "";
            int accountID = 0;
            int flightID = 0;
            int seatMapID = 0;
            String seatClass = "";
            String seatName = "";
            String origin = "";
            String dest = "";
            String departDate = "";
            String arriveTime = "";
            String departTime = "";
            String gate = "";
            String fName = "";
            String lName = "";

            Map<String, Object> flightInfo;
            Map<String, Object> seatInfo;
            Map<String, Object> accountInfo;
            try {
                ticketInfo = DbTicket.getTicketInfo(generatedTicketID);
                cost = ticketInfo.get("cost");
                flightID = Integer.parseInt((String)ticketInfo.get("flightID"));
                seatMapID = Integer.parseInt((String)ticketInfo.get("seatMapID"));
                accountID = Integer.parseInt((String)ticketInfo.get("accountID"));

            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }

            try {
                seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);
                seatClass = (String) seatInfo.get("seatClass");
                seatName = (String) seatInfo.get("seatName");
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }


            try {
                flightInfo = DbFlight.getFlightInfo(flightID);
                origin = (String) flightInfo.get("Origin");
                dest = (String) flightInfo.get("Destination");
                departDate = (String) flightInfo.get("Departure Date");
                arriveTime = (String) flightInfo.get("Arrival Time");
                departTime = (String) flightInfo.get("Departure Time");
                gate = (String) flightInfo.get("Gate");
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }


            try {
                accountInfo = DbAccount.getAccountInfo(accountID);
                fName = (String) accountInfo.get("firstName");
                lName = (String) accountInfo.get("lastName");

            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
            
            receipt = receipt + "\nSeat: " + seatClass + " " + seatName + "\n Cost: " + cost;

            ticket = ticket + "\n Ticket ID: " + generatedTicketID 
                    + "\n " + origin + "---> " + dest
                    + "\n Departs at: " + departTime + " on " + departDate
                    + "\n Arrives at: " + arriveTime
                    + "\n Seat Type: " + seatClass 
                    + "\n Seat Name: " + seatName
                    + "\n Passenger Name: " + fName + " " + lName; 
        
            JOptionPane.showMessageDialog(this, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this, ticket, "Ticket", JOptionPane.INFORMATION_MESSAGE);

            String enteredDigits = ccNumberField.getText(); 
            String enteredExpDate = expDateField.getText();
            String enteredCVV = cvvField.getText();
            if (enteredDigits.equals("") || enteredExpDate.equals("") || enteredCVV.equals("")) {
                JOptionPane.showMessageDialog(this, "Missing Information", "Payment Confirmation Failed", JOptionPane.ERROR_MESSAGE);
            }
            else {
                CreditCard cardUsed = new CreditCard(enteredDigits, enteredExpDate, enteredCVV);
                if ((hasCompanyCard == true && cardUsed.equals(companyCard) == false) || (hasPersonalCard == true && cardUsed.equals(personalCard) == false) || (hasCompanyCard == false) || (hasPersonalCard == false)) {
                    if (saveCardInfo.isSelected() == true) {
                        // add card used to be personal card
                        try {
                            DbPayment.newCreditCard(registeredUserID, enteredDigits, enteredExpDate, enteredCVV);
                           
                        } catch (ClassNotFoundException | SQLException | IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                PageNavigation.navigateTo("userHomePage");
            } 
        });
        
        buttonPanel.add(confirmButton);
        add(buttonPanel, gbc);
    }

    private JPanel createPriceInfoPanel() throws ClassNotFoundException, IOException, SQLException {
        JPanel priceInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        Map<String, String> ticketInfo = DbTicket.getTicketInfo(generatedTicketID);

        int accountID = Integer.parseInt(ticketInfo.get("accountID"));
        int seatMapID = Integer.parseInt(ticketInfo.get("seatMapID"));
        int flightID = Integer.parseInt(ticketInfo.get("flightID"));

        Map<String, Object> seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);
        String seatType = (String) seatInfo.get("seatClass");
        double seatCostFactor = 1;
        if (seatType.compareTo("businessClass") == 0) {
            seatCostFactor = 2;
        }
        else if (seatType.compareTo("comfort") == 0){
            seatCostFactor = 1.4;
        }

        Map<String, Object> accountInfo = DbRegisteredUser.getRegisteredUserInfo(accountID);
        registeredUserID = (int) accountInfo.get("registeredUserID");
        String hasMembershipStr = (String) accountInfo.get("hasMembership");
        int companyCardID = (int) accountInfo.get("companyCard");
        int personalCardID = (int) accountInfo.get("creditCard");
        if (hasMembershipStr.compareTo("yes") == 0) {
            hasMembership = true;
        }

        if (companyCardID != 0 && companyCardID > 1) {
            hasCompanyCard = true;
            Map<String, Object> cardInfo = DbPayment.getCompanyCardInfo(companyCardID);
            String digits = (String) cardInfo.get("companyCardDigits");
            String expDate = (String) cardInfo.get("companyExpirationDate");
            String code = (String) cardInfo.get("companySecurityCode");
            companyCard = new CreditCard(digits, expDate, code);

        }

        if (personalCardID != 0) {
            hasPersonalCard = true;
            Map<String, Object> cardInfo = DbPayment.getPersonalCardInfo(personalCardID);
            String digits = (String) cardInfo.get("cardDigits");
            String expDate = (String) cardInfo.get("expirationDate");
            String code = (String) cardInfo.get("securityCode");
            personalCard = new CreditCard(digits, expDate, code);
        }


        Map<String, Object> flightInfo = DbFlight.getFlightInfo(flightID);
        
        double basePrice = (double) flightInfo.get("Price");

        DecimalFormat format = new DecimalFormat("#.##");
        double price = 0;

        gbc.anchor = GridBagConstraints.WEST;

        price = price + basePrice;
        JLabel basePriceLabel = new JLabel("Ticket Base Price:                                        $" + basePrice);
        priceInfoPanel.add(basePriceLabel, gbc);
        gbc.gridy++;

        double preSeat = price;
        price = price * seatCostFactor;
        double seatCost = price - preSeat;
        JLabel addSeatPrice = new JLabel("Add Seat cost:                                          + $" + format.format(seatCost));
        priceInfoPanel.add(addSeatPrice, gbc);
        gbc.gridy++;

        if (SeatMapPage.getAddInsurance() == true) {
            double preInsurance = price;
            price = price + 50;
            double insuranceCost = price - preInsurance;
            JLabel addInsuranceCost = new JLabel("Add Cancellation Insurance cost:             + $" + format.format(insuranceCost));
            priceInfoPanel.add(addInsuranceCost, gbc);
            gbc.gridy++;
        }
        

        if (hasMembership == true) {
            double preDiscount = price;
            List<Integer> discounts = DbPayment.getDiscounts();
            for (double discount : discounts) {
                double value = 1 - (discount / 100);
                price = price * value;
            }
            
            double discountValue = preDiscount - price;

            JLabel applyDiscount = new JLabel("Apply Member Discount:                          -" + format.format(discountValue));
            priceInfoPanel.add(applyDiscount, gbc);
            gbc.gridy++;
        }
        
        double preGST = price;
        price = price * 1.05;
        double GSTcost = price - preGST;
        JLabel addTax = new JLabel("Add GST:                                                 + $" + format.format(GSTcost));
        priceInfoPanel.add(addTax, gbc);
        gbc.gridy++;

        JLabel total = new JLabel("Total:                                                     = $" + format.format(price));
        priceInfoPanel.add(total, gbc);
        gbc.gridy++;
        
        DbTicket.updateTicketCost(generatedTicketID, price);
        return priceInfoPanel;
    }


    private JPanel createCreditCardPanel() {
        JPanel creditCardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        // Credit Card Number label and input field
        JLabel ccNumberLabel = new JLabel("Credit Card Number:");
        ccNumberField = new JTextField(20);
        
        // Expiry Date label and input field
        JLabel expDateLabel = new JLabel("Exp. Date:");
        expDateField = new JTextField(10);
    
        // CVV label and input field
        JLabel cvvLabel = new JLabel("CVV:");
        cvvField = new JTextField(5);

        saveCardInfo = new JCheckBox("Save my Card Information");
    
        JButton usePersonalCardButton = new JButton("Pay with Saved Personal Card");
        
        usePersonalCardButton.addActionListener(e -> {
            ccNumberField.setText(personalCard.getCardDigits());
            expDateField.setText(personalCard.getCardExpirationDate());
            cvvField.setText(personalCard.getCardSecurityCode());

        });

        JButton useCompanyCardButton = new JButton("Pay with Company Card");
        useCompanyCardButton.addActionListener(e -> {
            ccNumberField.setText(companyCard.getCardDigits());
            expDateField.setText(companyCard.getCardExpirationDate());
            cvvField.setText(companyCard.getCardSecurityCode());
        });


        // Add components to the credit card panel
        gbc.anchor = GridBagConstraints.WEST;

        if (hasCompanyCard == true) {
            creditCardPanel.add(useCompanyCardButton, gbc);
            gbc.gridy++;
        }

        if (hasPersonalCard == true) {
            creditCardPanel.add(usePersonalCardButton, gbc);
            gbc.gridy++;
        }

        creditCardPanel.add(ccNumberLabel, gbc);
        gbc.gridy++;
        creditCardPanel.add(ccNumberField, gbc);
        gbc.gridy++;
        creditCardPanel.add(expDateLabel, gbc);
        gbc.gridy++;
        creditCardPanel.add(expDateField, gbc);
        gbc.gridy++;
        creditCardPanel.add(cvvLabel, gbc);
        gbc.gridy++;
        creditCardPanel.add(cvvField, gbc);
        gbc.gridy++;
        
        if (hasPersonalCard == false) {
            creditCardPanel.add(saveCardInfo, gbc);
            gbc.gridy++;
        }

        return creditCardPanel;
    }
}
