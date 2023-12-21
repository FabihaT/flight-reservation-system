package src.booking;

import java.io.IOException;

import src.database.DbBooking;

public class Booking {
    // private variables
    private int flightID; // ID of the flight associated with the booking
    private int accountID; // ID of the account associated with the booking
    private int seatMapID; // ID of the seat map associated with the booking
    private Ticket ticket; // Ticket object associated with the booking

    // constructor
    public Booking(int flightID, int accountID, String seatValue) throws ClassNotFoundException, IOException {
        this.flightID = flightID;
        this.accountID = accountID;
        int generatedTicketID = DbBooking.bookFlight(seatValue, flightID, accountID);
        this.ticket = new Ticket(generatedTicketID);
    }

    // getters
    public int getFlightID() {
        return flightID;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getAccountID() {
        return accountID;
    }

    public int getSeatMapID() {
        return seatMapID;
    }
}
