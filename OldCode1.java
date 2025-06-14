import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//a
public class OldCode1 extends JFrame {

    private DefaultTableModel roomReservationTableModel;
    private JTable roomReservationTable;

    public OldCode1() {
        setTitle("Room Reservation System - Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] roomColumnNames = { "Date", "Time Out", "Time In", "Building", "Room Number", "Purpose" };
        roomReservationTableModel = new DefaultTableModel(roomColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomReservationTable = new JTable(roomReservationTableModel);
        roomReservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomReservationTable.setRowHeight(25);
        roomReservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomReservationTable.setFillsViewportHeight(true);

        JScrollPane roomScrollPane = new JScrollPane(roomReservationTable);
        roomScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(roomScrollPane, BorderLayout.CENTER);

        JButton refreshRoomReservationsButton = new JButton("Refresh Room Reservations");
        refreshRoomReservationsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshRoomReservationsButton.addActionListener(e -> loadRoomReservationsIntoTable());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshRoomReservationsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadRoomReservationsIntoTable();
    }

    private void loadRoomReservationsIntoTable() {
        roomReservationTableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("room_reservations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|\\|");
                if (parts.length == 6) {
                    roomReservationTableModel.addRow(parts);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading room_reservations.txt: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OldCode1().setVisible(true));
    }
}