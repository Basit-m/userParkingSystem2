package parkingSystem.gui2;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.ManagerUser;
import parkingSystem.model.util.DuplicateChecker;
import parkingSystem.model.util.ParkingSystem;
import parkingSystem.model.util.factories.*;
import parkingSystem.model.util.factories.*;
import parkingSystem.model.util.EmailVerifier;
import parkingSystem.model.util.PasswordVerifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class RegisterTab extends JPanel {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JTextArea status;

    public RegisterTab() {

        setLayout(new GridLayout(7, 2, 10, 10));

        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        roleBox = new JComboBox<>();

        roleBox.addItem("Student");
        roleBox.addItem("Faculty");
        roleBox.addItem("NonFaculty");
        roleBox.addItem("Visitor");

        JButton registerButton = new JButton("Register");
        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        add(new JLabel("Name: "));
        add(nameField);
        add(new JLabel("Email: "));
        add(emailField);
        add(new JLabel("Password: "));
        add(passwordField);
        add(new JLabel("Account type: "));
        add(roleBox);
        add(new JLabel(""));
        add(registerButton);
        add(new JLabel(""));
        add(status);

        registerButton.addActionListener(this::register);

    }

    private void register(ActionEvent e) {

        String username = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            status.setText("Please fill all areas");
            return;
        }
        if(!EmailVerifier.isValidEmail(email)) {
            status.setText("Please enter a valid email address");
            return;
        }
        if(!PasswordVerifier.isStrongPassword(password)) {
            status.setText("Please enter a strong password\nMust have upper, lower, number, symbol");
            return;
        }
        if(!DuplicateChecker.usernameNotTaken(ParkingSystem.getInstance().getUsers(), username)) {
            status.setText("Username already taken");
            return;
        }
        if(!DuplicateChecker.emailNotTaken(ParkingSystem.getInstance().getUsers(), email)) {
            status.setText("This email is already registered");
            return;
        }

        AbstractUserFactory factory;
        AbstractUser user;

        switch(role) {
            case "Student":
                factory = new StudentUserFactory();
                user = factory.createUser(username, email, password);
                break;
            case "Faculty":
                factory = new FacultyUserFactory();
                user = factory.createUser(username, email, password);
                break;
            case "NonFaculty":
                factory = new NonFacultyUserFactory();
                user = factory.createUser(username, email, password);
                break;
            case "Visitor":
                factory = new VisitorUserFactory();
                user = factory.createUser(username, email, password);
                break;
            default:
                user = null;
        }

        ParkingSystem.getInstance().registerUser(user);
        String hourlyRate = String.format("%.2f", user.getRate());

        if(user.getRole().equalsIgnoreCase("visitor")) {
            status.setText("Registration Successful, Your rate now: $" + hourlyRate);
        } else {
            status.setText("Registration Successful, Your rate now: $" + hourlyRate);
            user.validateUser();
            String trueRate = String.format("%.2f", user.getRate());
            user.removeValidation();
            status.append("\nYour rate after validation: $" + trueRate);
        }


    }


}
