package parkingSystem.gui;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.parking.Booking;
import parkingSystem.model.parking.ParkingLot;
import parkingSystem.model.parking.ParkingSpace;
import parkingSystem.model.util.EmailVerifier;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class PaymentTab extends JPanel {

    private LoginTab loginTab;
    private JComboBox<String> payOptions;
    private JButton payButton;
    private JTextField name;
    private JTextField email;
    private JTextField cardNumber;
    private JTextField expNumber;
    private JTextField cvv;
    private JPasswordField password;
    private JTextArea status;
    private JTextArea totalPay;
    private final String expiryPattern = "^[0-9]{1,2}/[0-9]{1,2}$";

    PaymentTab(LoginTab loginTab) {

        this.loginTab = loginTab;

        setLayout(new GridLayout(9, 2, 10, 10));

        payOptions = new JComboBox<>();
        payOptions.addItem("Credit Card");
        payOptions.addItem("Debit Card");
        payOptions.addItem("PayPal");
        payOptions.addItem("Mobile Pay");

        name = new JTextField();
        email = new JTextField();
        cardNumber = new JTextField();
        expNumber = new JTextField();
        cvv = new JTextField();
        password = new JPasswordField();

        payButton = new JButton("Pay");

        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        totalPay = new JTextArea("");
        totalPay.setEditable(false);
        totalPay.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        add(new JLabel("Payment Method: "));
        add(payOptions);
        add(new JLabel("Enter Name (Card Pay): "));
        add(name);
        add(new JLabel("Enter Email (PayPal): "));
        add(email);
        add(new JLabel("Enter Card Number: "));
        add(cardNumber);
        add(new JLabel("Expiry Date (MM/YY): "));
        add(expNumber);
        add(new JLabel("CVV (3-digit code): "));
        add(cvv);
        add(new JLabel("PayPal Password: "));
        add(password);
        add(new JLabel(""));
        add(payButton);
        add(totalPay);
        add(status);

        payOptions.addActionListener(e -> refreshOptions());
        payButton.addActionListener(e -> processPay(e));
        refreshOptions();

    }

    private void processPay(ActionEvent e) {

        String selected = (String) payOptions.getSelectedItem();
        boolean credit = selected.equalsIgnoreCase("credit card");
        boolean debit = selected.equalsIgnoreCase("debit card");
        boolean payPal = selected.equalsIgnoreCase("paypal");
        boolean mobile = selected.equalsIgnoreCase("mobile pay");

        AbstractUser user = loginTab.getLoggedInUser();

        if(user.getAmountOwed() == 0) {
            status.setText("No pending balance.");
            return;
        }

        if(credit || debit) {
            processCardPayment(e, user);
        }
        else if(payPal) {
            processPayPal(e, user);
        }
        else if(mobile) {
            processMobile(e, user);
        }
        else {
            throw new IllegalArgumentException("Not suppose to be here");
        }

    }
    private void processMobile(ActionEvent e, AbstractUser user) {
        //Mobile doesn't really need any info
        double amountOwed = user.getAmountOwed();
        status.setText("Payment of $" + String.format("%.2f", amountOwed) + " successful via Mobile.");
        user.setAmountOwed(0);

        for(ParkingLot lot : ParkingSystem.getInstance().getLots()) {
            for(ParkingSpace space : lot.getSpaces()) {
                List<Booking> temp = new ArrayList<>(space.getTempBookings());

                for(Booking booking : temp) {
                    if(space.getTempBookings().contains(booking)) {
                        if (booking.getUser().equals(user) && !booking.getHasDeposit()) {
                            space.addBooking(booking);
                            space.removeFromTemp(booking);
                        }
                    }
                }
            }
        }

        refreshOptions();
    }

    private void processPayPal(ActionEvent e, AbstractUser user) {

        String ppEmail = email.getText();
        String ppPass = new String(password.getPassword());

        if(ppEmail.isEmpty() || ppPass.isEmpty()) {
            status.setText("Please fill all areas.");
            return;
        }
        if(!EmailVerifier.isValidEmail(ppEmail)) {
            status.setText("Invalid email format.");
            return;
        }

        double amountOwed = user.getAmountOwed();
        status.setText("Payment of $" + String.format("%.2f", amountOwed) + " successful via PayPal.");
        user.setAmountOwed(0);
        for(ParkingLot lot : ParkingSystem.getInstance().getLots()) {
            for(ParkingSpace space : lot.getSpaces()) {
                for(Booking booking : space.getTempBookings()) {
                    if(booking.getUser().equals(user) && !booking.getHasDeposit()) {
                        space.addBooking(booking);
                        space.removeFromTemp(booking);
                    }
                }
            }
        }
        refreshOptions();


        //Do the PayPal payment here, maybe csv or sum

    }

    private void processCardPayment(ActionEvent e, AbstractUser user) {

        String creditOrDebit = (String) payOptions.getSelectedItem();
        String cardName = name.getText();
        String cardNum = cardNumber.getText();
        String exp = expNumber.getText();
        String cVV = cvv.getText();

        if(cardName.isEmpty() || cardNum.isEmpty() || exp.isEmpty() || cVV.isEmpty()) {
            status.setText("Please fill all areas.");
            return;
        }
        if(!(cardNum.length() == 16)) {
            status.setText("Invalid card number length!");
            return;
        }
        if(!exp.matches(expiryPattern)) {
            status.setText("Invalid expiry format!");
            return;
        }
        if(!(cVV.length() == 3)) {
            status.setText("Invalid CVV length.");
            return;
        }

        //Process card payment somehow
        double amountOwed = user.getAmountOwed();
        status.setText("Payment of $" + String.format("%.2f", amountOwed) + " successful via Card.");
        user.setAmountOwed(0);
        for(ParkingLot lot : ParkingSystem.getInstance().getLots()) {
            for(ParkingSpace space : lot.getSpaces()) {
                for(Booking booking : space.getTempBookings()) {
                    if(booking.getUser().equals(user) && !booking.getHasDeposit()) {
                        space.addBooking(booking);
                        space.removeFromTemp(booking);
                    }
                }
            }
        }
        refreshOptions();

    }

    public void refreshOptions() {

        AbstractUser user = loginTab.getLoggedInUser();

        if(user == null) {
            status.setText("Please login first.");
            return;
        }

        double amountOwed = user.getAmountOwed();
        totalPay.setText("Your total: $" + String.format("%.2f", amountOwed));

        String selected = (String) payOptions.getSelectedItem();
        boolean credit = selected.equalsIgnoreCase("credit card");
        boolean debit = selected.equalsIgnoreCase("debit card");
        boolean payPal = selected.equalsIgnoreCase("paypal");
        boolean mobile = selected.equalsIgnoreCase("mobile pay");

        name.setEnabled((credit || debit));
        cardNumber.setEnabled(credit || debit);
        expNumber.setEnabled(credit || debit);
        cvv.setEnabled(credit || debit);
        email.setEnabled(payPal);
        password.setEnabled(payPal);

    }


}
