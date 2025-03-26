package parkingSystem;

import parkingSystem.gui.MainPanel;
import parkingSystem.model.AbstractUser;
import parkingSystem.model.ManagerUser;
import parkingSystem.model.StudentUser;
import parkingSystem.model.SuperManager;
import parkingSystem.model.parking.ParkingLot;
import parkingSystem.model.util.ParkingSystem;

import java.util.*;
import java.util.List;

public class Runnable {
    public static void main(String[] args) {

        MainPanel run = new MainPanel();

        ParkingLot lot = new ParkingLot("Scarborough");
        AbstractUser student = new StudentUser("kratos", "kratos@gmail.com", "123");
        AbstractUser manager = new ManagerUser("atlas", "atlas@gmail.com","123");
        SuperManager superManager = SuperManager.generateSuperManagerAccount("zeus", "zeus@gmail.com", "123");

        ParkingSystem.getInstance().addLot(lot);
        List<AbstractUser> users = new ArrayList<>();
        users.add(student);
        users.add(manager);
        users.add(superManager);
        ParkingSystem.getInstance().setUsers(users);

    }

}
