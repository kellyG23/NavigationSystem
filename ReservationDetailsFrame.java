import javax.swing.*;
import java.awt.*;

public class ReservationDetailsFrame extends JFrame {

    public ReservationDetailsFrame(String date, String timeOut, String timeIn,
                                   String building, String roomNumber, String purpose) {
        setTitle("Reservation Details");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Date:"));
        panel.add(new JLabel(date));

        panel.add(new JLabel("Time Out:"));
        panel.add(new JLabel(timeOut));

        panel.add(new JLabel("Time In:"));
        panel.add(new JLabel(timeIn));

        panel.add(new JLabel("Building:"));
        panel.add(new JLabel(building));

        panel.add(new JLabel("Room No.:"));
        panel.add(new JLabel(roomNumber));

        panel.add(new JLabel("Purpose:"));
        panel.add(new JLabel(purpose));

        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}