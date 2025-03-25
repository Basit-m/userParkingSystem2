package parkingSystem.gui2;

import parkingSystem.gui2.LoginTab;
import parkingSystem.model.AbstractUser;
import parkingSystem.model.parking.Booking;
import parkingSystem.model.parking.ParkingLot;
import parkingSystem.model.parking.ParkingSpace;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.time.LocalDateTime;
import java.util.List;

public class ViewBookTab extends JPanel {

    private LoginTab loginTab;
    private JButton cancelButton;
    private JButton editButton;
    private JButton extendButton;
    private JTextArea status;
    private LocalDateTime updatedStart;
    private LocalDateTime updatedEnd;

    private DefaultListModel<Booking> model;
    private JList<Booking> bookings;

    public ViewBookTab(LoginTab loginTab) {

        this.loginTab = loginTab;

        setLayout(new BorderLayout());

        model = new DefaultListModel<>();
        bookings = new JList<>(model);
        bookings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 5, 5));
        cancelButton = new JButton("Cancel");
        editButton = new JButton("Edit");
        extendButton = new JButton("Extend");

        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        buttons.add(cancelButton);
        buttons.add(editButton);
        buttons.add(extendButton);

        add(new JScrollPane(bookings), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        add(status, BorderLayout.NORTH);

        cancelButton.addActionListener(e -> cancel());
        editButton.addActionListener(e -> edit());
        extendButton.addActionListener(e -> extend());

        viewBookings();
    }

    private void cancel() {
        Booking curr = bookings.getSelectedValue();
        if(curr != null) {
            if (LocalDateTime.now().isAfter(curr.getStartTime())) {
                status.setText("Can not cancel active booking!");
            }
            curr.getSpace().getBookings().remove(curr);
            viewBookings();
            status.setText("Booking successfully cancelled!");
            return;
        }
        status.setText("Please select a booking");
    }

    private void edit() {
        Booking curr = bookings.getSelectedValue();
        if(curr != null) {
            if(LocalDateTime.now().isAfter(curr.getStartTime())) {
                status.setText("Can not edit active booking");
            }

            selectNewStart(curr);

            try {
                ParkingSpace space = curr.getSpace();
                space.getBookings().remove(curr);

                if(space.isAvailable(updatedStart, updatedEnd)) {
                    curr.updateTime(updatedStart, updatedEnd);
                    space.getBookings().add(curr);
                    viewBookings();
                    status.setText("Booking successfully edited!");
                }
                else {
                    space.getBookings().add(curr);
                    JOptionPane.showMessageDialog(null, "Conflict with another booking!",
                                             "Error", JOptionPane.INFORMATION_MESSAGE);
                    status.setText("Booking Conflict!");
                }
            } catch (Exception e) {
                status.setText("Invalid time");
            }

            return;
        }
        status.setText("Please select a booking");
    }

    private void extend() {
        Booking curr = bookings.getSelectedValue();
        if(curr != null) {
            long duration = Integer.parseInt(JOptionPane.showInputDialog("Enter duration (hr): "));

            try {
                LocalDateTime reqEnd = curr.getEndTime().plusHours(duration);
                ParkingSpace space = curr.getSpace();

                if(space.isAvailable(curr.getEndTime().plusMinutes(5), reqEnd)) {
                    curr.extendTime(reqEnd);
                    viewBookings();
                    status.setText("Booking successfully extended!");
                }
                else {
                    status.setText("Booking Conflict!");
                }
            } catch (Exception e) {
                status.setText("Invalid time!");
            }

            return;
        }
        status.setText("Please select a booking");
    }


    public void viewBookings() {

        model.clear();

        AbstractUser user = loginTab.getLoggedInUser();

        if(user == null) {
            status.setText("Please login to view your bookings!");
            return;
        }

        List<Booking> myBookings = new ArrayList<>();
        for(ParkingLot lot : ParkingSystem.getInstance().getLots()) {
            for(ParkingSpace space : lot.getSpaces()) {
                for(Booking booking : space.getBookings()) {
                    if(booking.getUser().equals(user)) {
                        myBookings.add(booking);
                    }
                }
            }
        }

        for(Booking booking : myBookings) {
            model.addElement(booking);
        }

        status.setText(myBookings.size() + " bookings found for " + user.getName());

    }

    private void selectNewStart(Booking booking) {
        JDialog box = new JDialog((Frame) null, "New Time Slot", true);
        box.setSize(600, 400);
        box.setLayout(new GridLayout(7, 2, 10, 10));

        JComboBox<String> hour = new JComboBox<>();
        JComboBox<String> minute = new JComboBox<>();
        JComboBox<String> duration = new JComboBox<>();

        fillBox(hour, minute, duration);

        box.add(new JLabel("Start Hour: ")); box.add(hour);
        box.add(new JLabel("Start Minute: ")); box.add(minute);
        box.add(new JLabel("Duration: ")); box.add(duration);
        box.add(new JLabel(""));

        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            //Display selected in dialogue box and update button text
            String selectedMin = (String) minute.getSelectedItem();
            String selectedDur = (String) duration.getSelectedItem();

            LocalDate temp = booking.getStartTime().toLocalDate();

            String[] parts = selectedMin.split(" ");
            String timePart = parts[0];
            String aMpM = parts[1];

            String[] timeParts = timePart.split(":");
            int timeHour = Integer.parseInt(timeParts[0]);
            int timeMin = Integer.parseInt(timeParts[1]);

            if(timeHour == 12 && aMpM.equals("AM")) {
                timeHour = 0;
            } else if(timeHour != 12 && aMpM.equals("PM")) {
                timeHour += 12;
            }

            LocalTime time = LocalTime.of(timeHour, timeMin);
            LocalDateTime newStart = temp.atTime(time);

            String[] durationParts = selectedDur.replace(" hr","").split(":");
            int dureHour = Integer.parseInt(durationParts[0]);
            int dureMin = Integer.parseInt(durationParts[1]);

            LocalDateTime newEnd = newStart.plusHours(dureHour).plusMinutes(dureMin);

            JOptionPane.showMessageDialog(box, "Selected New Time: " + selectedMin + "\nSelected Duration: " + selectedDur);

            updatedStart = newStart;
            updatedEnd = newEnd;
            box.dispose();

        });

        box.add(confirm);
        box.setLocationRelativeTo(null);
        box.setVisible(true);

    }

    private void fillBox(JComboBox<String> hour, JComboBox<String> minute, JComboBox<String> duration) {

        //Fill Hour
        hour.removeAllItems();
        for(int i = 0; i < 12; i++) {
            if(i == 0) {
                hour.addItem("12:00 AM");
            }
            else if(i < 10){
                hour.addItem("0" + i + ":00 AM");
            }
            else {
                hour.addItem(i + ":00 AM");
            }
        }
        for(int i = 0; i < 12; i++) {
            if(i == 0) {
                hour.addItem("12:00 PM");
            }
            else if(i < 10){
                hour.addItem("0" + i + ":00 PM");
            }
            else {
                hour.addItem(i + ":00 PM");
            }
        }

        //Fill Minute - 5 min increments
        String selectedHour = (String) hour.getSelectedItem();
        String aMpM = selectedHour.substring(5);
        selectedHour = selectedHour.substring(0,3);

        //12:00 PM
        minute.removeAllItems();
        for(int i = 0; i <= 55; i+=5) {
            if(i < 10) {
                minute.addItem(selectedHour + "0" + i + aMpM);
            }
            else {
                minute.addItem(selectedHour + i + aMpM);
            }
        }

        duration.addItem("1:00 hr");
        for(int i = 1; i <= 9; i++) {
            duration.addItem(i + ":30 hr");
            duration.addItem(i + 1 + ":00 hr");
        }
    }


}
