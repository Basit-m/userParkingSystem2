package parkingSystem.model.parking;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {

    private String location;
    private List<ParkingSpace> spaces;

    public ParkingLot(String location) {
        this.location = location;
        this.spaces = new ArrayList<>();

        for(int i = 1; i <= 100; i++) {
            this.spaces.add(new ParkingSpace(i, this));
        }
    }

    public List<ParkingSpace> getOpenSpaces() {
        List<ParkingSpace> available = new ArrayList<>();
        for(ParkingSpace space : spaces) {
            if(!space.isOccupied() && space.isEnabled()) {
                available.add(space);
            }
        }
        return available;
    }

    public String getLocation() {
        return this.location;
    }
    public void setLocation(String newLocation) {
        this.location = newLocation;
    }
    public void setSpaces(List<ParkingSpace> newSpaces) {
        this.spaces = newSpaces;
    }
    public List<ParkingSpace> getSpaces() {
        return this.spaces;
    }
    public String toString() {
        return location;
    }

}
