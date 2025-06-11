import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ReservationSystem extends JFrame {
    public ReservationSystem() {
        setTitle("Campus Map");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Make frame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReservationSystem::new);
    }
}