package src.users;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import src.account.Account;
import src.database.DbFlightAttendant;

public class FlightAttendant extends User {
    private Account account;
    private int flightAttendantID;

    public FlightAttendant(Account account) throws ClassNotFoundException, IOException {
        super(account);
        this.account = account;
        Map<String, Object> flightAttendantInfo = DbFlightAttendant.getFlightAttendantInfo(this.account.getAccountID());
        this.flightAttendantID = (int) flightAttendantInfo.get("flightAttendantID");
    }

    @Override
    public int browse() throws ClassNotFoundException, IOException {
        int flightID = -1;
        List<Map<String, Object>> flightsForAttendant = DbFlightAttendant.getFlightsForFlightAttendant(flightAttendantID);
        return flightID;
    }

    public void browsePassengerList(int flightID) throws ClassNotFoundException, IOException {
        List<String> passList = DbFlightAttendant.getPassengerList(flightID);
    }

    // getters
    public Account getAccount() {
        return account;
    }

    public int getFlightAttendantID() {
        return flightAttendantID;
    }

    // setters
    public void setAccount(Account account) {
        this.account = account;
    }

    public void setFlightAttendantID(int flightAttendantID) {
        this.flightAttendantID = flightAttendantID;
    }
}
