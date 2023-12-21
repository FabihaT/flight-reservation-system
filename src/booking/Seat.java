package src.booking;

import java.io.IOException;
import java.util.Map;

import src.database.DbSeat;

public class Seat {
    // instance variables
    private int seatMapID;
    private int seatID;
    private String seatName;
    private String seatClass;

    // constructor
    public Seat(int flightID, int seatMapID) throws ClassNotFoundException, IOException {
        // retrieve seat information from the database
        Map<String, Object> seatInfo = DbSeat.getSeatInfo(flightID, seatMapID);

        // initialize instance variables with retrieved seat information
        this.seatMapID = (int) seatInfo.get("seatMapID");
        this.seatID = (int) seatInfo.get("seatID");
        this.seatName = (String) seatInfo.get("seatName");
        this.seatClass = (String) seatInfo.get("seatClass");
    }

    // getters
    public int getSeatMapID() {
        return this.seatMapID;
    }

    public int getSeatID() {
        return this.seatID;
    }

    public String getSeatName() {
        return this.seatName;
    }

    public String getSeatClass() {
        return this.seatClass;
    }
}
