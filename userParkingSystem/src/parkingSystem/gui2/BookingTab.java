package parkingSystem.gui2;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.parking.*;
import parkingSystem.model.util.LicensePlateVerifier;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.Calendar;

public class BookingTab extends JPanel{

    private JComboBox<ParkingLot> lots;
    private JComboBox<ParkingSpace> spaces;
    private JButton startButton;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private JComboBox<String> duration;
    private JTextField licenseBox;
    private String license = "";
    private JButton bookButton;
    private JTextArea status;
    private LoginTab loginTab;
    private JButton logoutButton;
    private int[] selectedIndexes;

    public BookingTab(LoginTab loginTab) {

        this.loginTab = loginTab;

        selectedIndexes = new int[]{0, 0, 0, 0, 0};

        setLayout(new GridLayout(7, 2, 10, 10));

        lots = new JComboBox<>();
        for(ParkingLot lot : ParkingSystem.getInstance().getLots()) {
            lots.addItem(lot);
        }
        lots.setSelectedIndex(0);

        spaces = new JComboBox<>();
        lots.addActionListener(e -> updateParkingSpace());
        updateParkingSpace();

        startButton = new JButton("2025/01/01 12:00 AM");
        duration = new JComboBox<>();
        duration.addItem("1:00 hr");
        for(int i = 1; i <= 9; i++) {
            duration.addItem(i + ":30 hr");
            duration.addItem(i+1 + ":00 hr");
        }

        licenseBox = new JTextField();
        bookButton = new JButton("Book");
        logoutButton = new JButton("Log out");

        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        add(new JLabel("Select Lot: "));
        add(lots);
        add(new JLabel("Select Space: "));
        add(spaces);
        add(new JLabel("Start Time: "));
        add(startButton);
        add(new JLabel("Parking Duration (hrs): "));
        add(duration);
        add(new JLabel("License Plate: "));
        add(licenseBox);
        add(logoutButton);
        add(bookButton);
        add(new JLabel(""));
        add(status);

        startButton.addActionListener(e -> showMenuForTime());

        bookButton.addActionListener(this::book);

        logoutButton.addActionListener(e -> logOut());

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

    private void book(ActionEvent e) {

        AbstractUser user = loginTab.getLoggedInUser();

        if(user == null) {
            status.setText("Please login before booking");
            return;
        }

        license = licenseBox.getText();
        if(license.isEmpty()) {
            status.setText("Please enter your license plate");
            return;
        }

        ParkingSpace space = (ParkingSpace) spaces.getSelectedItem();
        assignDate();
        if(space == null) {
            status.setText("No space selected");
            return;
        }
        if(!space.isEnabled()) {
            status.setText("Current parking space is under\nmaintenance");
            return;
        }
        if(!LicensePlateVerifier.isValidLicense(license)) {
            status.setText("Please enter a valid license plate");
            return;
        }

        try {
            if(!space.isAvailable(startTime, endTime)) {
                LocalDateTime next = space.getNextAvailableTime(startTime);
                status.setText("Booking conflict!\nNext open slot is: " + next);
                return;
            }

            Booking booking = new Booking(user, space, startTime, endTime, license);
            space.addBooking(booking);
            status.setText("Booked for " + user.getName() + "!\nEst. Cost: " + String.format("%.2f",booking.calculateFees()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void assignDate() {
        String dayString;
        String monthString;
        String yearString;
        String hourString;
        String minuteString;

        //DAY
        if((selectedIndexes[0]+1) < 10) {
            dayString = "0" + (selectedIndexes[0]+1);
        } else {
            dayString = "" + (selectedIndexes[0]+1);
        }

        //MONTH
        if((selectedIndexes[1]+1) < 10) {
            monthString = "0" + (selectedIndexes[1]+1);
        } else {
            monthString = "" + (selectedIndexes[1]+1);
        }

        //YEAR
        yearString = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - selectedIndexes[2]);

        //HOUR
        if((selectedIndexes[3]+1) < 10) {
            hourString = "0" + (selectedIndexes[3]);
        } else {
            hourString = "" + (selectedIndexes[3]);
        }

        //MINUTE
        if((selectedIndexes[4])*5 < 10) {
            minuteString = "0" + (selectedIndexes[4]*5);
        } else {
            minuteString = "" + (selectedIndexes[4]*5);
        }

        String translated = yearString + "-" + monthString + "-" + dayString + "T" + hourString + ":" + minuteString;
        startTime = LocalDateTime.parse(translated);

        //0: 1
        //1: 1.5
        //2: 2
        //3: 2.5
        long durHour = duration.getSelectedIndex()/2 + 1;
        long durMin = 0;
        if(duration.getSelectedIndex()%2 != 0) {
            durMin = 30;
        }
        endTime = startTime.plusHours(durHour).plusMinutes(durMin);

    }

    private void showMenuForTime() {
        JDialog box = new JDialog((Frame) null, "Select Date & Time", true);
        box.setSize(600, 400);
        box.setLayout(new GridLayout(7, 2, 10, 10));

        JComboBox<Integer> day = new JComboBox<>();
        JComboBox<String> month = new JComboBox<>();
        JComboBox<Integer> year = new JComboBox<>();
        JComboBox<String> hour = new JComboBox<>();
        JComboBox<String> minute = new JComboBox<>();

        fillComboBox(day, month, year, hour, minute, selectedIndexes);

        month.addActionListener(e -> updateDay(day, month));
        hour.addActionListener(e -> updateMinute(hour, minute));

        box.add(new JLabel("Day: ")); box.add(day);
        box.add(new JLabel("Month: ")); box.add(month);
        box.add(new JLabel("Year: ")); box.add(year);
        box.add(new JLabel("Hour: ")); box.add(hour);
        box.add(new JLabel("Minute: ")); box.add(minute);
        box.add(new JLabel(""));

        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            //Display selected in dialogue box and update button text
            int selectedDay = (int) day.getSelectedItem();
            int selectedMonth = month.getSelectedIndex() + 1;
            int selectedYear = (int) year.getSelectedItem();
            String selectedMin = (String) minute.getSelectedItem();

            String selectedTime = selectedDay + "/" + selectedMonth + "/" + selectedYear + " " + selectedMin;

            updateStartButton(selectedTime);
            JOptionPane.showMessageDialog(box, "Selected Start:\n" + selectedTime);

            //Save selected indexes and assign startTime
            int iDay = (int) day.getSelectedIndex();
            int iMonth = (int) month.getSelectedIndex();
            int iYear = (int) year.getSelectedIndex();
            int iHour = (int) hour.getSelectedIndex();
            int iMin = (int) minute.getSelectedIndex();

            selectedIndexes = new int[]{iDay, iMonth, iYear, iHour, iMin};
            assignDate();

            box.dispose();

        });

        box.add(confirm);
        box.setLocationRelativeTo(null);
        box.setVisible(true);

    }

    private void updateStartButton(String line) {
        startButton.setText(line);
    }

    private void updateMinute(JComboBox<String> hour, JComboBox<String> minute) {

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
    }

    private void updateDay(JComboBox<Integer> day, JComboBox<String> month) {
        //Fill Day
        int index = month.getSelectedIndex();
        int days;

        switch(index) {
            case 1:
                days = 28;
                break;
            case 3: case 5: case 8: case 10:
                days = 30;
                break;
            default:
                days = 31;
                break;
        }
        day.removeAllItems();
        for(int i = 1; i <= days; i++) {
            day.addItem(i);
        }
    }
    private void fillComboBox(JComboBox<Integer> day, JComboBox<String> month,
                              JComboBox<Integer> year, JComboBox<String> hour,
                              JComboBox<String> minute, int[] selectedIndexes) {

        //Fill Day
        int index = month.getSelectedIndex();
        int days;

        switch(index) {
            case 1:
                days = 28;
                break;
            case 3: case 5: case 8: case 10:
                days = 30;
                break;
            default:
                days = 31;
                break;
        }
        day.removeAllItems();
        for(int i = 1; i <= days; i++) {
            day.addItem(i);
        }
        day.setSelectedIndex(selectedIndexes[0]);

        //Fill Month
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        month.removeAllItems();
        for(String s : months) {
            month.addItem(s);
        }
        month.setSelectedIndex(selectedIndexes[1]);

        //Fill Year
        int curr = Calendar.getInstance().get(Calendar.YEAR);
        year.removeAllItems();
        for(int i = curr; i >= (curr-10); i--) {
            year.addItem(i);
        }
        year.setSelectedIndex(selectedIndexes[2]);

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
        hour.setSelectedIndex(selectedIndexes[3]);

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
        minute.setSelectedIndex(selectedIndexes[4]);

    }

}
