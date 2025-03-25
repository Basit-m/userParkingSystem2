package parkingSystem.gui2;

import parkingSystem.model.AbstractUser;
import parkingSystem.model.util.ValidationRequestBuffer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ValidatorTab extends JPanel {

    private LoginTab loginTab;
    private JButton validateButton;
    private JButton validateAllButton;
    private JTextArea status;

    private DefaultListModel<AbstractUser> model;
    private JList<AbstractUser> requests;

    public ValidatorTab(LoginTab loginTab) {

        this.loginTab = loginTab;

        setLayout(new BorderLayout());

        model = new DefaultListModel<>();
        requests = new JList<>(model);
        requests.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        validateButton = new JButton("Validate");
        validateAllButton = new JButton("Validate All");

        status = new JTextArea("");
        status.setEditable(false);
        status.setOpaque(false);
        status.setBackground(new Color(0,0,0,0));

        buttons.add(validateButton);
        buttons.add(validateAllButton);

        add(new JScrollPane(requests), BorderLayout.CENTER);
        add(status, BorderLayout.NORTH);
        add(buttons, BorderLayout.SOUTH);

        validateButton.addActionListener(e -> validateOne());
        validateAllButton.addActionListener(e -> validateAll());

        viewRequests();

    }

    private void viewRequests() {
        model.clear();

        List<AbstractUser> requestList = ValidationRequestBuffer.getInstance().getRequests();

        for(AbstractUser user : requestList) {
            model.addElement(user);
        }

        if(requestList.isEmpty()) {
            status.setText("No pending validation request.");
            return;
        }
        status.setText(requestList.size() + " users needs validation!");
    }

    private void validateOne() {
        AbstractUser user = requests.getSelectedValue();

        if(user == null) {
            status.setText("Please select a user to validate.");
            return;
        }

        user.validateUser();
        ValidationRequestBuffer.getInstance().removeRequest(user);
        viewRequests();
        status.setText(user.getName() + " successfully validated as a " + user.getRole().toLowerCase());
    }
    private void validateAll() {
        List<AbstractUser> requestList = new ArrayList<>(ValidationRequestBuffer.getInstance().getRequests());

        if(requestList.isEmpty()) {
            status.setText("Please select a user to validate.");
        }

        for(AbstractUser user : requestList) {
            ValidationRequestBuffer.getInstance().removeRequest(user);
            user.validateUser();
        }
        viewRequests();
        status.setText(requestList.size() + " users successfully validated.");
    }


}
