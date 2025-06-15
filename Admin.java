import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Admin extends JFrame {

    public Admin() {
        setTitle("Room Reservation System - Admin Panel");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("🏫 Room Reservation System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleLabel.setForeground(new Color(50, 70, 100));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make it transparent
        buttonPanel.setLayout(new GridLayout(1, 1, 0, 20)); // 1 row, 1 column
        gbc.gridy = 1;
        add(buttonPanel, gbc);

        // Button to launch admin panel
        buttonPanel.add(createStyledButton("Reservation Forms", new Color(46, 139, 87), "Opening Admin Panel..."));

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color, String message) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setForeground(color);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 60));
        button.setBorder(BorderFactory.createLineBorder(color, 2, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(color);
            }
        });

        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, message);
            new RoomAdminPanel().setVisible(true);
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Admin::new);
    }

    // Class to display reservation details
    static class ReservationDetailsFrame extends JFrame {
        public ReservationDetailsFrame(String date, String email, String timeOut, String timeIn,
                                       String building, String roomNumber, String purpose) {
            setTitle("Reservation Details");
            setSize(450, 420); // Slightly increased size
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 2, 15, 15)); // Increased gaps
            panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            Font labelFont = new Font("SansSerif", Font.BOLD, 15);
            Font dataFont = new Font("SansSerif", Font.PLAIN, 15);

            addItem(panel, "Date:", date, labelFont, dataFont);
            addItem(panel, "Email:", email, labelFont, dataFont);
            addItem(panel, "Time Out:", timeOut, labelFont, dataFont);
            addItem(panel, "Time In:", timeIn, labelFont, dataFont);
            addItem(panel, "Building:", building, labelFont, dataFont);
            addItem(panel, "Room No.:", roomNumber, labelFont, dataFont);
            addItem(panel, "Purpose:", purpose, labelFont, dataFont);

            JButton closeButton = new JButton("OK");
            closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            closeButton.setBackground(new Color(100, 149, 237));
            closeButton.setForeground(Color.WHITE);
            closeButton.addActionListener(e -> dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(panel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        }

        private void addItem(JPanel panel, String labelText, String dataText, Font labelFont, Font dataFont) {
            JLabel label = new JLabel(labelText);
            label.setFont(labelFont);
            panel.add(label);
            JLabel data = new JLabel(dataText);
            data.setFont(dataFont);
            panel.add(data);
        }
    }

    // Renamed from AdminPanel1, now handles admin approval/disapproval
    static class RoomAdminPanel extends JFrame {
        private DefaultTableModel roomReservationTableModel;
        private JTable roomReservationTable;

        public RoomAdminPanel() {
            setTitle("Room Reservation System - Unavailable Rooms");
            setSize(1200, 650); // Increased width and height for better visibility
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose only this window
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(15, 15)); // Added gaps
            getContentPane().setBackground(new Color(245, 250, 255)); // Lighter background

            JLabel headerLabel = new JLabel("Manage Room Reservations", JLabel.CENTER);
            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            headerLabel.setForeground(new Color(40, 60, 90));
            add(headerLabel, BorderLayout.NORTH);

            // Updated column names to include Email and Status
            String[] roomColumnNames = {"Date", "Email", "Time Out", "Time In", "Building", "Room Number", "Purpose", "Status"};
            roomReservationTableModel = new DefaultTableModel(roomColumnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Cells are not directly editable in the table
                }
            };
            roomReservationTable = new JTable(roomReservationTableModel);
            roomReservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            roomReservationTable.setRowHeight(28);
            roomReservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
            roomReservationTable.setFillsViewportHeight(true);
            roomReservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row can be selected

            JScrollPane roomScrollPane = new JScrollPane(roomReservationTable);
            roomScrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 20, 10, 20), // Outer padding
                    BorderFactory.createLineBorder(new Color(180, 200, 220), 1) // Inner border
            ));
            add(roomScrollPane, BorderLayout.CENTER);

            JButton refreshRoomReservationsButton = createActionButton("🔄 Refresh Reservations", new Color(100, 149, 237));
            refreshRoomReservationsButton.addActionListener(e -> loadRoomReservationsIntoTable());

            JButton approveButton = createActionButton("✔ Approve Selected", new Color(46, 139, 87)); // Green
            approveButton.addActionListener(e -> processReservation(true)); // true for approve

            JButton disapproveButton = createActionButton("✖ Disapprove Selected", new Color(200, 50, 50)); // Red
            disapproveButton.addActionListener(e -> processReservation(false)); // false for disapprove

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15)); // Horizontal gap
            buttonPanel.setOpaque(false);
            buttonPanel.add(refreshRoomReservationsButton);
            buttonPanel.add(approveButton);
            buttonPanel.add(disapproveButton);
            add(buttonPanel, BorderLayout.SOUTH);

            loadRoomReservationsIntoTable();
        }

        private JButton createActionButton(String text, Color color) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return button;
        }

        private void loadRoomReservationsIntoTable() {
            roomReservationTableModel.setRowCount(0); // Clear existing rows
            try (BufferedReader reader = new BufferedReader(new FileReader("res/room_reservations.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|\\|");
                    // Expect 8 parts now: Date, Email, Time Out, Time In, Building, Room Number, Purpose, Status
                    if (parts.length == 8) {
                        roomReservationTableModel.addRow(parts);
                    } else {
                        // Handle older entries or malformed lines if necessary
                        System.err.println("Skipping malformed line (unexpected number of parts): " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading room_reservations.txt: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error loading reservations. File might not exist or is corrupted.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void processReservation(boolean approve) {
            int selectedRow = roomReservationTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation to " + (approve ? "approve" : "disapprove") + ".", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String currentStatus = (String) roomReservationTableModel.getValueAt(selectedRow, 7); // Status is the 8th column (index 7)
            if (currentStatus.equalsIgnoreCase("approved") || currentStatus.equalsIgnoreCase("disapproved")) {
                JOptionPane.showMessageDialog(this, "This reservation has already been " + currentStatus + ".", "Status Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to " + (approve ? "approve" : "disapprove") + " this reservation?",
                    (approve ? "Approve Reservation" : "Disapprove Reservation"),
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Get all reservation details from the selected row
                Vector<String> rowData = (Vector<String>) roomReservationTableModel.getDataVector().elementAt(selectedRow);
                String date = rowData.elementAt(0);
                String email = rowData.elementAt(1);
                String timeOut = rowData.elementAt(2);
                String timeIn = rowData.elementAt(3);
                String building = rowData.elementAt(4);
                String roomNumber = rowData.elementAt(5);
                String purpose = rowData.elementAt(6);

                String newStatus = approve ? "approved" : "disapproved";

                // Update the status in the table model first
                roomReservationTableModel.setValueAt(newStatus, selectedRow, 7); // Update the table UI

                // Now, update the actual file
                updateReservationStatusInFile(date, email, timeOut, timeIn, building, roomNumber, purpose, newStatus);

                // Simulate email notification
                JOptionPane.showMessageDialog(this, "An email has been sent to " + email + " informing them their reservation has been " + newStatus + ".", "Email Sent", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void updateReservationStatusInFile(String targetDate, String targetEmail, String targetTimeOut, String targetTimeIn,
                                                   String targetBuilding, String targetRoomNumber, String targetPurpose, String newStatus) {

            File inputFile = new File("res/room_reservations.txt");
            File tempFile = new File("temp_room_reservations.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] parts = currentLine.split("\\|\\|");
                    if (parts.length == 8) { // Ensure line has all expected parts
                        String fileDate = parts[0];
                        String fileEmail = parts[1];
                        String fileTimeOut = parts[2];
                        String fileTimeIn = parts[3];
                        String fileBuilding = parts[4];
                        String fileRoomNumber = parts[5];
                        String filePurpose = parts[6];

                        // Check if this is the reservation we need to update
                        // Compare all relevant fields to ensure uniqueness
                        if (fileDate.equals(targetDate) &&
                                fileEmail.equals(targetEmail) &&
                                fileTimeOut.equals(targetTimeOut) &&
                                fileTimeIn.equals(targetTimeIn) &&
                                fileBuilding.equals(targetBuilding) &&
                                fileRoomNumber.equals(targetRoomNumber) &&
                                filePurpose.equals(targetPurpose)) {

                            // Write the updated line
                            writer.write(String.join("||", targetDate, targetEmail, targetTimeOut, targetTimeIn,
                                    targetBuilding, targetRoomNumber, targetPurpose, newStatus));
                        } else {
                            // Write the original line
                            writer.write(currentLine);
                        }
                    } else {
                        // If line format is unexpected, write it back as is (or handle error)
                        writer.write(currentLine);
                        System.err.println("Warning: Encountered malformed line in room_reservations.txt: " + currentLine);
                    }
                    writer.newLine();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error updating reservation file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;
            }

            // Replace the original file with the temporary file
            if (!inputFile.delete()) {
                JOptionPane.showMessageDialog(this, "Could not delete original reservation file.", "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!tempFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(this, "Could not rename temporary reservation file.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
