package my.edu.utar.BookingHistory;

import java.io.Serializable;
import java.util.ArrayList;

public class AllTicketsWrapper implements Serializable {
    public ArrayList<String[]> ticketList;
    public ArrayList<String[]> username;
    public ArrayList<String[]> schedule;
    public ArrayList<String[]> busplate;

    public AllTicketsWrapper(ArrayList<String[]> ticketList, ArrayList<String[]> username, ArrayList<String[]> schedule, ArrayList<String[]> busplate) {
        this.ticketList = ticketList;
        this.username = username;
        this.schedule = schedule;
        this.busplate = busplate;
    }
}