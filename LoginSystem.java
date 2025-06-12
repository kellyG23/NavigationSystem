import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginSystem implements ActionListener {
    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("User  ID");
    JLabel userPasswordLabel = new JLabel("Password");
    JLabel messageLabel = new JLabel("");

    HashMap<String, String> loginfo = new HashMap<String, String>();

    LoginSystem(HashMap<String, String> loginforOriginal) {
        loginfo = loginforOriginal; // Assign the provided login information

        userIDLabel.setBounds(50, 100, 75, 25);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        messageLabel.setBounds(100, 250, 250, 25); // Adjusted width for message
        messageLabel.setFont(new Font(null, Font.ITALIC, 15));

        userIDField.setBounds(125, 100, 200, 25);
        userPasswordField.setBounds(125, 150, 200, 25);

        loginButton.setBounds(250, 200, 75, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(150, 200, 75, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(messageLabel); // Added messageLabel to the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login System");
        frame.setSize(500, 750);
        frame.setLayout(null);
        frame.setVisible(true); // Set visibility after adding components
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText(""); // Clear message on reset
        }
        if (e.getSource() == loginButton) {
            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

            if (loginfo.containsKey(userID)) {
                if (loginfo.get(userID).equals(password)) {
                    messageLabel.setForeground(Color.GREEN);
                    messageLabel.setText("You are successfully logged in!");
                    ReservationSystem rs = new ReservationSystem();
                }
                else {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Your password is incorrect!");
                }
            }
            else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Your user id is incorrect!");
            }
        }
    }
}