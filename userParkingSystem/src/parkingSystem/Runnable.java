package parkingSystem;

import parkingSystem.gui2.MainPanel;
import parkingSystem.model.AbstractUser;
import parkingSystem.model.ManagerUser;
import parkingSystem.model.StudentUser;
import parkingSystem.model.SuperManager;
import parkingSystem.model.parking.ParkingLot;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.*;
import java.util.List;

public class Runnable {
    public static void main(String[] args) {

        MainPanel run = new MainPanel();

        ParkingLot lot = new ParkingLot("Scarborough");
        AbstractUser student = new StudentUser("oy&oy", "oy@oy.com", "123");
        AbstractUser manager = new ManagerUser("m&m", "m@m.com","123");
        SuperManager superManager = SuperManager.generateSuperManagerAccount("s&s", "s@s.com", "123");

        ParkingSystem.getInstance().addLot(lot);
        List<AbstractUser> users = new ArrayList<>();
        users.add(student);
        users.add(manager);
        users.add(superManager);
        ParkingSystem.getInstance().setUsers(users);
        

    }
}
