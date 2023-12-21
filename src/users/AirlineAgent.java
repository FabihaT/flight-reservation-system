package src.users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import src.account.Account;
import src.booking.Booking;
import src.database.DbAccount;
import src.database.DbAirlineAgent;
import src.database.DbFlight;
import src.database.DbPayment;
import src.database.DbRegisteredUser;
import src.database.DbSeat;

public class AirlineAgent extends User {
    private Account account;
    private int airlineAgentID;
    private int clientAccountID;
    private RegisteredUser client;

    public AirlineAgent(Account account) {
        super(account);
    }

    // Browse flights and return the selected flight ID
    @Override
    public int browse() throws SQLException, ClassNotFoundException, IOException {
        int flightID = -1;
        this.clientAccountID = 1;
        List<Integer> destinations = DbFlight.getAllFlights();
        flightID = 1;
        return flightID;
    }

    // Get the list of client IDs associated with the airline agent
    public List<Integer> getClientList() throws ClassNotFoundException, IOException {
        List<Integer> clientIDs = DbAirlineAgent.getAgentsRegUsers(this.airlineAgentID);
        return clientIDs;
    }

    // Select a client from the client list
    public void clientSelection() throws SQLException, ClassNotFoundException, IOException {
        List<Integer> clientIDs = getClientList();
        this.clientAccountID = 1;
        Map<String, Object> clientInfo = DbAccount.getAccountInfo(this.clientAccountID);
        String clientEmail = (String) clientInfo.get("email");
        String clientPassword = (String) clientInfo.get("password");
        Account clientAccount = new Account(clientEmail, clientPassword);
        this.client = new RegisteredUser(clientAccount);
    }

    // Select a seat for a given flight and return the seat map ID
    public int seatSelection(int flightID) throws ClassNotFoundException, IOException {
        int seatMapID = -1;
        List<Map<String, Object>> seatChart = DbFlight.getSeatingChart(flightID);
        System.out.println(seatChart);
        return seatMapID;
    }

    // Book a flight for the selected client and return the ticket ID
    public int airlineAgentBook(int flightID, int seatMapID) throws ClassNotFoundException, IOException {
        Map<String, Object> seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);
        String seatValue = (String) seatInfo.get("seatName");
        int accountIDtoBook = this.clientAccountID;
        Booking userBooking = new Booking(flightID, accountIDtoBook, seatValue);
        int ticketID = userBooking.getTicket().getTicketID();
        return ticketID;
    }

    // Select a payment card for the transaction
    public int cardSelection() {
        return 1;
    }

    // Perform payment for the booking using the selected card
    public void agentPay(double price) throws SQLException, ClassNotFoundException, IOException {
        Map<String, Object> clientInfo = DbRegisteredUser.getRegisteredUserInfo(this.clientAccountID);
        int clientCompanyCard = (int) clientInfo.get("companyCard");
        int clientOtherCard = (int) clientInfo.get("creditCard");
        String digits = null;
        String expDate = null;
        String code = null;
        if (clientCompanyCard == 0 && clientOtherCard == 0) {
            this.client.createOtherCard(this.clientAccountID, digits, expDate, code);
            Map<String, Object> personalCardInfo = DbPayment.getPersonalCardInfo(this.clientAccountID);
            digits = (String) personalCardInfo.get("cardDigits");
            expDate = (String) personalCardInfo.get("expirationDate");
            code = (String) personalCardInfo.get("securityCode");
        } else {
            if (clientCompanyCard == 0 && clientOtherCard != 0) {
                Map<String, Object> personalCardInfo = DbPayment.getPersonalCardInfo(this.clientAccountID);
                digits = (String) personalCardInfo.get("cardDigits");
                expDate = (String) personalCardInfo.get("expirationDate");
                code = (String) personalCardInfo.get("securityCode");
            } else if (clientCompanyCard != 0 && clientOtherCard != 0) {
                Map<String, Object> companyCardInfo = DbPayment.getCompanyCardInfo(this.clientAccountID);
                digits = (String) companyCardInfo.get("companyCardDigits");
                expDate = (String) companyCardInfo.get("companyExpirationDate");
                code = (String) companyCardInfo.get("companySecurityCode");
            } else {
                int selectedID = cardSelection();
                Map<String, Object> personalCardInfo = DbPayment.getPersonalCardInfo(this.client.getRegisteredUserID());
                int cardIDP = (int) personalCardInfo.get("cardID");
                String digitsP = (String) personalCardInfo.get("cardDigits");
                String expDateP = (String) personalCardInfo.get("expirationDate");
                String codeP = (String) personalCardInfo.get("securityCode");
                Map<String, Object> companyCardInfo = DbPayment.getCompanyCardInfo(this.client.getRegisteredUserID());
                int cardIDC = (int) companyCardInfo.get("companyCardID");
                String digitsC = (String) companyCardInfo.get("companyCardDigits");
                String expDateC = (String) companyCardInfo.get("companyExpirationDate");
                String codeC = (String) companyCardInfo.get("companySecurityCode");
                if (selectedID == cardIDP) {
                    digits = digitsP;
                    expDate = expDateP;
                    code = codeP;
                } else if (selectedID == cardIDC) {
                    digits = digitsC;
                    expDate = expDateC;
                    code = codeC;
                }
            }
        }
    }

    // Calculate the ticket price for a given flight and seat, considering additional options
    public double calculateTicketPrice(int flightID, int seatMapID, boolean chooseInsurance) throws ClassNotFoundException, IOException {
        Map<String, Object> flightInfo = DbFlight.getFlightInfo(flightID);
        double calculatedPrice = (double) flightInfo.get("Price");
        String seatType = "ordinary";
        if (seatType.compareTo("businessClass") == 0) {
            calculatedPrice = calculatedPrice * 2;
        } else if (seatType.compareTo("comfort") == 0) {
            calculatedPrice = calculatedPrice * 1.4;
        }
        if (chooseInsurance == true) {
            calculatedPrice = calculatedPrice * 1.05;
        }
        if (this.client.getIsMembership() == true) {
            List<Integer> discounts = DbPayment.getDiscounts();
            for (int discount : discounts) {
                int value = 1 - (discount / 100);
                calculatedPrice = calculatedPrice * value;
            }
        }
        calculatedPrice = calculatedPrice * 1.05;
        return calculatedPrice;
    }

    // getters
    public Account getAccount() {
        return account;
    }

    public int getAirlineAgentID() {
        return airlineAgentID;
    }

    // setters
    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAirlineAgentID(int airlineAgentID) {
        this.airlineAgentID = airlineAgentID;
    }
}
