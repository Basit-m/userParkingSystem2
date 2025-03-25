package parkingSystem.gui2;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.SuperManager;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginTab extends JPanel {

    private JTextField userField;
    private JPasswordField passwordField;
    private JTextArea status;
    private AbstractUser loggedUser;
    private JTabbedPane tabs;
    private BookingTab bookingTab;
    private ViewBookTab viewBookTab;
    private ControlParkingTab controlParkingTab;
    private ValidatorTab validatorTab;
    private GenerateTab generateTab;

    public LoginTab(JTabbedPane tabs) {

        this.tabs = tabs;
        setLayout(new GridLayout(7, 2, 10, 10));

        userField = new JTextField();
        passwordField = new JPasswordField();
        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        JButton loginButton = new JButton("Login");

        add(new JLabel("Email/Username: "));
        add(userField);
        add(new JLabel("Password: "));
        add(passwordField);
        add(new JLabel(""));
        add(loginButton);
        add(new JLabel(""));
        add(status);

        loginButton.addActionListener(this::login);

    }

    public void clearFields() {
        this.userField.setText("");
        this.passwordField.setText("");
        this.status.setText("");
    }

    private void login(ActionEvent e) {

        if(loggedUser != null) {
            status.setText("Logged in as: " + loggedUser.getName() + "\nType: " + loggedUser.getRole());
            return;
        }

        String inputUser = userField.getText();
        String password = new String(passwordField.getPassword());
        AbstractUser user = ParkingSystem.getInstance().login(inputUser, password);

        //Login with user if email is not in database/ParkingSystem
        if(user == null) {
            user = ParkingSystem.getInstance().loginViaName(inputUser, password);
        }

        if(user == null) {
            status.setText("Invalid credentials!");
            return;
        }
        this.loggedUser = user;

        status.setText("Logged in as a " + user.getRole().toLowerCase() + ": " + user.getName());
        if(!user.getRole().equalsIgnoreCase("Manager")) {

            status.append("\nRate: " + user.getRate());
            bookingTab = new BookingTab(this);
            viewBookTab = new ViewBookTab(this);

            this.tabs.add("Book", bookingTab);
            this.tabs.add("View Bookings", viewBookTab);
            this.tabs.addChangeListener(a -> refreshTabs());
        }
        else {
            controlParkingTab = new ControlParkingTab(this);
            validatorTab = new ValidatorTab(this);

            this.tabs.add("Maintenance", controlParkingTab);
            this.tabs.add("Validator", validatorTab);

            if(user.equals(SuperManager.getInstance())) {
                status.setText("Logged in as the super manager: " + user.getName());

                generateTab = new GenerateTab(this);
                this.tabs.add("Generate", generateTab);
            }
        }


    }
    public void refreshTabs() {
        int index = tabs.getSelectedIndex();

        if (tabs.getTitleAt(index).equalsIgnoreCase("View Bookings")) {
            viewBookTab.viewBookings();
        }
    }

    public AbstractUser getLoggedInUser() {
        return this.loggedUser;
    }
    public void logOutUser() {
        this.loggedUser = null;
        clearFields();
        while(tabs.getTabCount() > 2) {
            this.tabs.remove(2);
        }
    }
}
