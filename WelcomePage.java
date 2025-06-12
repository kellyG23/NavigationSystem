import javax.swing.*;
import java.awt.*;

public class WelcomePage {

    JFrame frame = new JFrame("Welcome");
    JLabel welcomeLabel = new JLabel("Welcome to Java");

    WelcomePage() {

        welcomeLabel.setBounds(0,0,800,600);
        welcomeLabel.setFont(new Font(null, Font.PLAIN,25));

        frame.add(welcomeLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 750);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}