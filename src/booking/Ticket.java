package src.booking;

import java.io.IOException;
import java.util.Map;

import src.database.DbTicket;

public class Ticket {
    // private variables
    private int accountID; // account ID of the ticket
    private int flightID; // flight ID of the ticket
    private Seat seat; // seat object associated with the ticket
    
    private int ticketID; // ID of the ticket

    // constructor
    public Ticket(int ticketID) throws ClassNotFoundException, IOException {
        Map<String, String> ticketInfo = DbTicket.getTicketInfo(ticketID);
        this.ticketID = Integer.parseInt(ticketInfo.get("ticketID"));
        this.accountID = Integer.parseInt(ticketInfo.get("accountID"));
        this.flightID = Integer.parseInt(ticketInfo.get("flightID"));
        int seatMapID = Integer.parseInt(ticketInfo.get("seatMapID"));

        this.seat = new Seat(flightID, seatMapID);

        // can get flight info from the flight ID
        
    }

    // getters
    public int getAccountID() {
        return accountID;
    }

    public int getFlightID() {
        return flightID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public Seat getSeat() {
        return seat;
    }

    // setters
    public void setAccount(int accountID) {
        this.accountID = accountID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    
}
