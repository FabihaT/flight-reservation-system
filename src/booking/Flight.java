package src.booking;

import java.io.IOException;
import java.util.Map;

import src.database.DbFlight;

public class Flight {
    // flight attributes
    private int flightID;
    private int aircraftID;
    private String gate;
    private String departDate;
    private String departTime;
    private String arriveTime;
    private String dest;
    private String origin;
    private int baseCost;

    // constructor
    public Flight(int fID) throws ClassNotFoundException, IOException {
        // retrieve flight information from the database
        Map<String, Object> flightInfo = DbFlight.getFlightInfo(fID);

        // set the flight attributes based on the retrieved information
        this.flightID = (int) flightInfo.get("ticketID");
        this.aircraftID = (int) flightInfo.get("accountID");
        this.gate = (String) flightInfo.get("flightID");
        this.departDate = (String) flightInfo.get("seatMapID");
        this.departTime = (String) flightInfo.get("ticketID");
        this.arriveTime = (String) flightInfo.get("accountID");
        this.dest = (String) flightInfo.get("flightID");
        this.origin = (String) flightInfo.get("seatMapID");
        this.baseCost = (int) flightInfo.get("price");
    }

    // getters
    public int getFlightID() {
        return this.flightID;
    }

    public int getAircraftID() {
        return this.aircraftID;
    }

    public String getGate() {
        return this.gate;
    }

    public String getDepartDate() {
        return this.departDate;
    }

    public String getDepartTime() {
        return this.departTime;
    }

    public String getArriveTime() {
        return this.arriveTime;
    }

    public String getDest() {
        return this.dest;
    }

    public String getOrigin() {
        return this.origin;
    }

    public int getBaseCost() {
        return this.baseCost;
    }
}
