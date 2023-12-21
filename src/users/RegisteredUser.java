package src.users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import src.account.Account;
import src.account.CreditCard;
import src.booking.Booking;
import src.database.DbFlight;
import src.database.DbPayment;
import src.database.DbRegisteredUser;
import src.database.DbSeat;

public class RegisteredUser extends User {
    private Account account;
    private int registeredUserID;
    private Boolean hasMembership;
    private CreditCard otherCard;
    private CreditCard companyCard;

    public RegisteredUser(Account account, boolean wantsMembership, boolean wantsCompanyCard) throws SQLException, ClassNotFoundException, IOException {
        super(account);
        this.companyCard = null;
        this.otherCard = null;
        Map<String, Object> registeredUserInfo = DbRegisteredUser.getRegisteredUserInfo(this.account.getAccountID());
        this.registeredUserID = (int) registeredUserInfo.get("registeredUserID");
        if (wantsMembership) {
            this.hasMembership = true;
            if (wantsCompanyCard) {
                createCompanyCard(this.registeredUserID);
            }
        }
    }

    public RegisteredUser(Account account) throws ClassNotFoundException, IOException {
        super(account);
        Map<String, Object> registeredUserInfo = DbRegisteredUser.getRegisteredUserInfo(account.getAccountID());
        this.account = account;
        this.registeredUserID = (int) registeredUserInfo.get("registeredUserID");
        String hasMem = (String) registeredUserInfo.get("hasMembership");
        if (hasMem.compareTo("yes") == 0) {
            this.hasMembership = true;
        }
        int companyCard = (int) registeredUserInfo.get("companyCard");
        if (companyCard != 0) {
            Map<String, Object> companyCardInfo = DbPayment.getCompanyCardInfo(registeredUserID);
            String cardDigits = (String) companyCardInfo.get("companyCardDigits");
            String cardExpirationDate = (String) companyCardInfo.get("companyExpirationDate");
            String cardSecurityCode = (String) companyCardInfo.get("companySecurityCode");
            this.companyCard = new CreditCard(cardDigits, cardExpirationDate, cardSecurityCode);
        }
        Map<String, Object> cardInfo = DbPayment.getPersonalCardInfo(this.registeredUserID);
        String cardDigits = (String) cardInfo.get("cardDigits");
        String cardExpirationDate = (String) cardInfo.get("expirationDate");
        String cardSecurityCode = (String) cardInfo.get("securityCode");
        this.otherCard = new CreditCard(cardDigits, cardExpirationDate, cardSecurityCode);
    }

    public void createOtherCard(int registeredUser, String digits, String expDate, String code) throws SQLException, ClassNotFoundException, IOException {
        DbPayment.newCreditCard(registeredUserID, digits, expDate, code);
        this.otherCard = new CreditCard(digits, expDate, code);
    }

    public void createCompanyCard(int registeredUserID) throws SQLException, ClassNotFoundException, IOException {
        DbPayment.newCompanyCard(registeredUserID);
        Map<String, Object> companyCardInfo = DbPayment.getCompanyCardInfo(registeredUserID);
        String cardDigits = (String) companyCardInfo.get("companyCardDigits");
        String cardExpirationDate = (String) companyCardInfo.get("companyExpirationDate");
        String cardSecurityCode = (String) companyCardInfo.get("companySecurityCode");
        this.companyCard = new CreditCard(cardDigits, cardExpirationDate, cardSecurityCode);
    }

    public void cancelTicket(int ticketID) throws ClassNotFoundException, IOException{
        DbRegisteredUser.cancelTicket(ticketID);
    }

    @Override
    public int browse() throws SQLException, ClassNotFoundException, IOException {
        int flightID = -1;
        List<Integer> destinations = DbFlight.getAllFlights();
        flightID = 1;
        return flightID;
    }

    public int seatSelection(int flightID) throws ClassNotFoundException, IOException {
        int seatMapID = -1;
        List<Map<String, Object>> seatChart = DbFlight.getSeatingChart(flightID);
        return seatMapID;
    }

    public int userBook(int flightID, int seatMapID) throws ClassNotFoundException, IOException {
        Map<String, Object> seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);
        String seatValue = (String) seatInfo.get("seatName");
        int accountIDtoBook = this.account.getAccountID();
        Booking userBooking = new Booking(flightID, accountIDtoBook, seatValue);
        int ticketID = userBooking.getTicket().getTicketID();
        return ticketID;
    }

    public void userPay(double price) throws SQLException, ClassNotFoundException, IOException {
        String digits = null;
        String expDate = null;
        String code = null;
        if (this.companyCard == null && this.otherCard == null) {
            createOtherCard(this.registeredUserID, digits, expDate, code);
            Map<String, Object> personalCardInfo = DbPayment.getPersonalCardInfo(this.registeredUserID);
            digits = (String) personalCardInfo.get("cardDigits");
            expDate = (String) personalCardInfo.get("expirationDate");
            code = (String) personalCardInfo.get("securityCode");
        }
        else{
            if (this.companyCard == null && this.otherCard != null) {
                Map<String, Object> personalCardInfo = DbPayment.getPersonalCardInfo(this.registeredUserID);
                digits = (String) personalCardInfo.get("cardDigits");
                expDate = (String) personalCardInfo.get("expirationDate");
                code = (String) personalCardInfo.get("securityCode");
            }
            else if (this.companyCard != null && this.otherCard != null) {
                Map<String, Object> companyCardInfo = DbPayment.getCompanyCardInfo(this.registeredUserID);
                digits = (String) companyCardInfo.get("companyCardDigits");
                expDate = (String) companyCardInfo.get("companyExpirationDate");
                code = (String) companyCardInfo.get("companySecurityCode");
            }
            else {
                int cardIDP = -1;
                String digitsP = null;
                String expDateP = null;
                String codeP = null;
                int cardIDC = -1;
                String digitsC = null;
                String expDateC = null;
                String codeC = null;
                int selectedID = -1;
                Map<String, Object> personalCardInfo = DbPayment.getPersonalCardInfo(this.registeredUserID);
                cardIDP = (int) personalCardInfo.get("cardID");
                digitsP = (String) personalCardInfo.get("cardDigits");
                expDateP = (String) personalCardInfo.get("expirationDate");
                codeP = (String) personalCardInfo.get("securityCode");
                Map<String, Object> companyCardInfo = DbPayment.getCompanyCardInfo(this.registeredUserID);
                cardIDC = (int) companyCardInfo.get("companyCardID");
                digitsC = (String) companyCardInfo.get("companyCardDigits");
                expDateC = (String) companyCardInfo.get("companyExpirationDate");
                codeC = (String) companyCardInfo.get("companySecurityCode");
                if (selectedID == cardIDP) {
                    digits = digitsP;
                    expDate = expDateP;
                    code = codeP;
                }
                else if (selectedID == cardIDC){
                    digits = digitsC;
                    expDate = expDateC;
                    code = codeC;
                }
            }
        }
    }

    public double calculateTicketPrice(int flightID, int seatMapID, boolean chooseInsurance) throws ClassNotFoundException, IOException {
        Map<String, Object> flightInfo = DbFlight.getFlightInfo(flightID);
        double calculatedPrice = (double) flightInfo.get("Price");
        String seatType = "ordinary";
        if (seatType.compareTo("businessClass") == 0) {
            calculatedPrice = calculatedPrice * 2;
        }
        else if (seatType.compareTo("comfort") == 0) {
            calculatedPrice = calculatedPrice * 1.4;
        }
        if (chooseInsurance) {
            calculatedPrice = calculatedPrice * 1.05;
        }
        if (getIsMembership()) {
            List<Integer> discounts = DbPayment.getDiscounts();
            for (int discount : discounts) {
                int value = 1 - (discount / 100);
                calculatedPrice = calculatedPrice * value;
            }
        }
        calculatedPrice = calculatedPrice * 1.05;
        return calculatedPrice;
    }

    public Account getAccount() {
        return account;
    }

    public Boolean getIsMembership() {
        return hasMembership;
    }

    public CreditCard getCompanyCard() {
        return companyCard;
    }

    public int getRegisteredUserID() {
        return registeredUserID;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setIsMembership(Boolean hasMembership) {
        this.hasMembership = hasMembership;
    }

    public void setCompanyCard(CreditCard companyCard) {
        this.companyCard = companyCard;
    }

    public void setRegisteredUserID(int registeredUserID) {
        this.registeredUserID = registeredUserID;
    }
}
