package my.edu.utar;

public class ScheduleItem {
    private String scheduleId;
    private String departureTime;
    private String arrivalTime;
    private String scheduleDate;
    private String seatAvailable;
    private String busPlate;

    public ScheduleItem(String scheduleId, String departureTime, String arrivalTime, String scheduleDate, String seatAvailable, String busPlate) {
        this.scheduleId = scheduleId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.scheduleDate = scheduleDate;
        this.seatAvailable = seatAvailable;
        this.busPlate = busPlate;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getSeatAvailable() {
        return seatAvailable;
    }

    public String getBusPlate() {
        return busPlate;
    }
}

