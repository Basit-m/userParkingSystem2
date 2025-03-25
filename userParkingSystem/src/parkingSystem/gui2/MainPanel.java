package parkingSystem.gui2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MainPanel extends JFrame {

    private LoginTab loginTab;
    private BookingTab bookingTab;
    private ViewBookTab viewBookTab;
    public JTabbedPane tabs;

    public MainPanel() {

        setTitle("YorkU Booking Parking App");
        setSize(600, 400);

        //Add tabs
        tabs = new JTabbedPane();

        loginTab = new LoginTab(tabs);
        //bookingTab = new BookingTab(loginTab);
        viewBookTab = new ViewBookTab(loginTab);

        tabs.add("Register", new RegisterTab());
        tabs.add("Login", loginTab);
        //tabs.add("Book", bookingTab);
        //tabs.add("View Bookings", viewBookTab);

        //tabs.addChangeListener(e -> refreshTabs(e,tabs));

        add(tabs);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void refreshTabs(ChangeEvent e, JTabbedPane tabs) {
        int index = tabs.getSelectedIndex();

        if(index == 1) {
            loginTab.clearFields();
        }
        if(index == 3) {
            viewBookTab.viewBookings();
        }
    }

    public JTabbedPane getTabs() {
        return this.tabs;
    }

}
