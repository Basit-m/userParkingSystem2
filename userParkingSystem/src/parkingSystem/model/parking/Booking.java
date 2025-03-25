package parkingSystem.model.parking;

import parkingSystem.model.AbstractUser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {

    private AbstractUser user;
    private ParkingSpace space;
    private boolean noShow;
    private boolean checkedIn;
    private boolean checkedOut;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime actualEndTime;
    private String licensePlate;

    public Booking(AbstractUser user, ParkingSpace space,
                   LocalDateTime startTime, LocalDateTime endTime, String licensePlate) {
        this.user = user;
        this.space = space;
        this.startTime = startTime;
        this.endTime = endTime;
        this.checkedIn = false;
        this.checkedOut = false;
        this.noShow = false;
        this.licensePlate = licensePlate;
    }

    public double calculateFees() {
        Duration duration = Duration.between(startTime, endTime);
        double totalMin = (double) duration.toMinutes();
        return totalMin * (user.getRate()/60);
    }
    public void processDeposit() {
        double deposit = user.getRate();
        //TODO: Process deposit for no-shows
    }
    public void refundFees() {
        double totalHours = Duration.between(startTime, endTime).toHours();
        double fees = totalHours * user.getRate();
        //TODO: Process refund for cancellation
    }

    public void noShow() {
        if(!checkedIn && LocalDateTime.now().isAfter(this.startTime)) {
            this.noShow = true;
            this.space.enable();
        }
    }
    public void updateTime(LocalDateTime newStart, LocalDateTime newEnd) {
        this.startTime = newStart;
        this.endTime = newEnd;
    }
    public void extendTime(LocalDateTime newEnd) {
        this.endTime = newEnd;
    }
    public void checkedIn() {
        this.checkedIn = true;
    }
    public void checkedOut() {
        this.checkedOut = true;
        this.actualEndTime = LocalDateTime.now();
    }
    public LocalDateTime getStartTime() {
        return this.startTime;
    }
    public LocalDateTime getEndTime() {
        return this.endTime;
    }
    public boolean isCheckedOut() {
        return this.checkedOut;
    }
    public void setCheckedInStatus(boolean b) {
        this.checkedIn = b;
    }
    public AbstractUser getUser() {
        return this.user;
    }
    public ParkingSpace getSpace() {
        return this.space;
    }
    public void setLicensePlate(String plate) {
        this.licensePlate = plate;
    }
    public String getLicensePlate() {
        return this.licensePlate;
    }

    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");

        String start = startTime.format(timeFormatter) + " " + startTime.format(dateFormatter);
        String end = endTime.format(timeFormatter) + " " + endTime.format(dateFormatter);

        return "Booking: " + user.getEmail() + " - Location: " + space.getLot() + " - Lot: " + space.getId() + " - Time: " + start + " -> " + end;
    }

}
