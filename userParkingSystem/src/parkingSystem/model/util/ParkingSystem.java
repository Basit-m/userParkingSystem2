package parkingSystem.model.util;

import parkingSystem.model.*;
import parkingSystem.model.parking.ParkingLot;

import java.util.ArrayList;
import java.util.List;

public class ParkingSystem {

    private static ParkingSystem instance;
    private List<AbstractUser> users;
    private List<ParkingLot> lots;
    private SuperManager superManager;

    private ParkingSystem() {
        users = new ArrayList<>();
        lots = new ArrayList<>();

    }

    public static ParkingSystem getInstance() {
        if(instance == null) {
            instance = new ParkingSystem();
        }
        return instance;
    }

    public void generateSuperManager(String name, String email, String password) {
        if(superManager == null) {
            superManager = SuperManager.generateSuperManagerAccount(name, email, password);
            users.add(superManager);
        }
        System.out.println("Super manager instance already exists.");
    }

    public void registerUser(AbstractUser user) {
        this.users.add(user);

        if(!user.isValid()) {
            ValidationRequestBuffer.getInstance().addRequest(user);
        }
    }
    public void removeUser(AbstractUser user) {
        this.users.remove(user);
    }

    public AbstractUser login(String email, String password) {
        if(email.equals("null")) {
            return null;
        }

        for(AbstractUser user : users) {
            if(user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    public AbstractUser loginViaName(String username, String password) {
        for(AbstractUser user : users) {
            if(user.getName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void addLot(ParkingLot lot) {
        this.lots.add(lot);
    }
    public List<AbstractUser> getUsers() {
        return this.users;
    }
    public List<ParkingLot> getLots() {
        return this.lots;
    }
    public void setUsers(List<AbstractUser> users) {
        this.users = users;
    }
    public void setLots(List<ParkingLot> lots) {
        this.lots = lots;
    }

}
