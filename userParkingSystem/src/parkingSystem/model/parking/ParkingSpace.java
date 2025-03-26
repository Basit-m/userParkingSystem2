package parkingSystem.model.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParkingSpace {

    private int id;
    private ParkingLot lot;
    private boolean isOccupied;
    private boolean isEnabled;

    //Checks for conflicts with other bookings
    private List<Booking> bookings;

    //Temporarily adds it until user deposits required fees
    private Set<Booking> tempBookings;

    public ParkingSpace(int id, ParkingLot lot) {
        this.id = id;
        this.lot = lot;

        this.bookings = new ArrayList<>();
        this.tempBookings = new HashSet<>();

        this.isEnabled = true;
        this.isOccupied = false;
    }

    public String captureLicensePlate() {
        //Example
        return "ABC123";
    }

    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {

        clearPastBookings();

        for(Booking booking : bookings) {
            if(!(end.isBefore(booking.getStartTime()) || start.isAfter(booking.getEndTime()))) {
                return false;
            }
        }
        return true;
    }

    public LocalDateTime getNextAvailableTime(LocalDateTime start) {
        
        clearPastBookings();

        LocalDateTime reqStart = start;

        for(Booking booking : bookings) {
            if(reqStart.isBefore(booking.getEndTime())) {
                reqStart = booking.getEndTime().plusMinutes(5);
                break;
            }
            for(Booking b : bookings) {
                if(!isAvailable(reqStart, reqStart.plusMinutes(5))) {
                    reqStart = b.getEndTime().plusMinutes(5);
                }
            }
        }
        return reqStart;

    }
    public void clearPastBookings() {

        LocalDateTime now = LocalDateTime.now();

        for(Booking booking : bookings) {
            if(now.isAfter(booking.getEndTime()) && booking.isCheckedOut()) {
                bookings.remove(booking);
            }
        }

    }

    public void addToTemp(Booking booking) {
        if(tempBookings.contains(booking)) {
            return;
        }
        tempBookings.add(booking);
    }
    public void removeFromTemp(Booking booking) {
        this.tempBookings.remove(booking);
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
        //Add it to CSV
    }
    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
        //Remove it from csv
    }

    public Set<Booking> getTempBookings() {
        return this.tempBookings;
    }
    public boolean isOccupied() {
        return this.isOccupied;
    }
    public void setOccupied(boolean b) {
        this.isOccupied = b;
    }
    public boolean isEnabled() {
        return this.isEnabled;
    }
    public void disable() {
        this.isEnabled = false;
    }
    public void enable() {
        this.isEnabled = true;
    }
    public List<Booking> getBookings() {
        return this.bookings;
    }
    public ParkingLot getLot() {
        return this.lot;
    }
    public int getId() {
        return this.id;
    }
    public String toString() {
        return "Lot " + id;
    }

}
