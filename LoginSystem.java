import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class LoginSystem implements ActionListener {
    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("User   ID");
    JLabel userPasswordLabel = new JLabel("Password");
    JLabel messageLabel = new JLabel("");

    HashMap<String, String> userLogins;
    HashMap<String, String> adminLogins;

    public LoginSystem(HashMap<String, String> users, HashMap<String, String> admins) {
        this.userLogins = users;
        this.adminLogins = admins;

        userIDLabel.setBounds(50, 100, 75, 25);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        messageLabel.setBounds(100, 250, 300, 25);
        messageLabel.setFont(new Font(null, Font.ITALIC, 15));

        userIDField.setBounds(125, 100, 200, 25);
        userPasswordField.setBounds(125, 150, 200, 25);

        loginButton.setBounds(250, 200, 75, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(150, 200, 75, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        userPasswordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(messageLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login System");
        frame.setSize(500, 750);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText("");
        }

        if (e.getSource() == loginButton) {
            login();
        }
    }

    private void login() {
        String userID = userIDField.getText();
        String password = String.valueOf(userPasswordField.getPassword());

        if (adminLogins.containsKey(userID) && adminLogins.get(userID).equals(password)) {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Admin login successful!");
            frame.dispose();
            new AdminPanel1();
        }
        else if (userLogins.containsKey(userID) && userLogins.get(userID).equals(password)) {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("User login successful!");
            frame.dispose();
            new ReservationSystem();
        }
        else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid username or password!");
        }
    }
}
