package parkingSystem.gui2;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.ManagerUser;
import parkingSystem.model.SuperManager;
import parkingSystem.model.util.Generator;
import parkingSystem.model.util.ParkingSystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GenerateTab extends JPanel {

    private LoginTab loginTab;
    private JButton generateButton;
    private JButton removeButton;
    private JTextArea status;

    private DefaultListModel<AbstractUser> model;
    private JList<AbstractUser> managers;

    public GenerateTab(LoginTab loginTab) {

        this.loginTab = loginTab;

        setLayout(new BorderLayout());

        model = new DefaultListModel<>();
        managers = new JList<>(model);
        managers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        generateButton = new JButton("Generate");
        removeButton = new JButton("Remove");

        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        buttons.add(generateButton);
        buttons.add(removeButton);

        add(new JScrollPane(managers), BorderLayout.CENTER);
        add(status, BorderLayout.NORTH);
        add(buttons, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> generateManagerAccount());
        removeButton.addActionListener(e -> removeManagerAccount());

        viewManagers();

    }

    private void removeManagerAccount() {
        AbstractUser manager = managers.getSelectedValue();
        if(manager == null) {
            status.setText("Please select a manager.");
            return;
        }

        ParkingSystem.getInstance().removeUser(manager);
        viewManagers();
        status.setText(manager.getName() + " successfully removed from management.");

    }

    private void generateManagerAccount() {
        String email = "null";
        String name = Generator.generateUniqueUsername(ParkingSystem.getInstance().getUsers());
        String password = Generator.generateStrongPassword();

        AbstractUser manager = new ManagerUser(name, email, password);
        ParkingSystem.getInstance().registerUser(manager);
        viewManagers();
        status.setText(name + " successfully generated! pass: " + password);
    }

    private void viewManagers() {
        model.clear();

        List<AbstractUser> managers = new ArrayList<>();

        for(AbstractUser user : ParkingSystem.getInstance().getUsers()) {
            if(user.getRole().equals("Manager")) {
                if(user.equals(SuperManager.getInstance())) {
                    continue;
                }
                model.addElement(user);
                managers.add(user);
            }
        }

        if(managers.isEmpty()) {
            status.setText("No managers found!");
            return;
        }
        status.setText(managers.size() + " managers found!");
    }



}
