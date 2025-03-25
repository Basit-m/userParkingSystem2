package parkingSystem.gui2;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.parking.ParkingLot;
import parkingSystem.model.parking.ParkingSpace;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;

public class ControlParkingTab extends JPanel {

    private LoginTab loginTab;
    private JComboBox<ParkingLot> lots;
    private JComboBox<ParkingSpace> spaces;
    private JTextArea status;
    private JButton enableButton;
    private JButton disableButton;
    private JButton logOutButton;

    public ControlParkingTab(LoginTab loginTab) {

        this.loginTab = loginTab;

        setLayout(new GridLayout(7, 2, 10, 10));

        lots = new JComboBox<>();
        for(ParkingLot lot : ParkingSystem.getInstance().getLots()) {
            lots.addItem(lot);
        }

        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        spaces = new JComboBox<>();
        lots.addActionListener(e -> updateParkingSpace());
        lots.setSelectedIndex(0);
        spaces.setSelectedIndex(0);
        spaces.addActionListener(e -> updateStatus());
        updateStatus();



        enableButton = new JButton("Enable");
        disableButton = new JButton("Disable");
        logOutButton = new JButton("Log out");

        add(new JLabel("Select Lot:"));
        add(lots);
        add(new JLabel("Select Space:"));
        add(spaces);
        add(enableButton);
        add(disableButton);
        add(logOutButton);
        add(status);

        enableButton.addActionListener(e -> enableSpace());
        disableButton.addActionListener(e -> disableSpace());
        logOutButton.addActionListener(e -> logOut());


    }
    private void updateStatus() {
        ParkingSpace selectedSpace = (ParkingSpace) spaces.getSelectedItem();
        String currStatus = (selectedSpace.isEnabled()) ? "enabled" : "disabled";
        status.setText("This space is " + currStatus);
    }

    private void enableSpace() {
        ParkingSpace selectedSpace = (ParkingSpace) spaces.getSelectedItem();

        if(selectedSpace.isEnabled()) {
            status.setText("This space is already enabled!");
            return;
        }
        status.setText("This space is now enabled!");
        selectedSpace.enable();
    }
    private void disableSpace() {
        ParkingSpace selectedSpace = (ParkingSpace) spaces.getSelectedItem();

        if(!selectedSpace.isEnabled()) {
            status.setText("This space is already disabled!");
            return;
        }
        status.setText("This space is now disabled!");
        selectedSpace.disable();
    }
    private void logOut() {
        AbstractUser user = loginTab.getLoggedInUser();

        if(user != null) {
            status.setText("You have been logged-out");
            loginTab.logOutUser();
            return;
        }
        status.setText("You are not logged-in");
    }

    private void updateParkingSpace() {
        spaces.removeAllItems();
        ParkingLot currentLot = (ParkingLot) lots.getSelectedItem();
        if(currentLot != null) {
            for(ParkingSpace space : currentLot.getSpaces()) {
                spaces.addItem(space);
            }
        }
    }

}
