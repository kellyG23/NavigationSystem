import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class SchoolRoomReservationSystem extends JFrame {

    public SchoolRoomReservationSystem() {
        setTitle("Room Reservation System - User Panel");
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
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20)); // 2 rows, 1 column, 20px vertical gap
        gbc.gridy = 1;
        add(buttonPanel, gbc);

        // Buttons to launch user form and admin panel
        buttonPanel.add(createStyledButton("Make a Reservation", new Color(70, 130, 180), "Opening Reservation Form..."));
        buttonPanel.add(createStyledButton("Occupied Rooms", new Color(46, 139, 87), "Opening List of Occupied Rooms..."));

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

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(color);
            }
        });

        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, message);
            if (text.equals("Make a Reservation")) {
                new UserReservationForm().setVisible(true);
            } else if (text.equals("Occupied Rooms")) {
                new RoomOccupiedPanel().setVisible(true);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SchoolRoomReservationSystem::new);
    }

    // Renamed from ReservationSystem for clarity, now handles user input
    static class UserReservationForm extends JFrame implements ActionListener {
        JTextField emailField, buildingField, roomNumberField, purposeField;
        JFormattedTextField timeOutField, timeInField;
        JComboBox<String> dateComboBox;
        JButton submitButton, resetButton;

        public UserReservationForm() {
            setTitle("Make a Room Reservation");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose only this window
            setSize(500, 700); // Adjusted size
            setLocationRelativeTo(null);
            setResizable(false);

            JPanel mainPanel = new JPanel(new BorderLayout(15, 15)); // Add gaps
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel title = new JLabel("Room Reservation Form", JLabel.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            title.setForeground(new Color(60, 90, 120));
            mainPanel.add(title, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8); // Padding
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            int row = 0;

            // Date
            JLabel dateLabel = new JLabel("Reservation Date:");
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(dateLabel, gbc);
            dateComboBox = new JComboBox<>(generateDateOptions());
            dateComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(dateComboBox, gbc);

            // Email Address
            JLabel emailLabel = new JLabel("Email Address:");
            emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(emailLabel, gbc);
            emailField = new JTextField(25);
            emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(emailField, gbc);

            // Time-out
            JLabel timeOutLabel = new JLabel("Time-out (hh:mm AM/PM):");
            timeOutLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(timeOutLabel, gbc);
            timeOutField = new JFormattedTextField(new SimpleDateFormat("hh:mm a"));
            timeOutField.setColumns(10);
            timeOutField.setFocusLostBehavior(JFormattedTextField.COMMIT);
            timeOutField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(timeOutField, gbc);

            // Time-in
            JLabel timeInLabel = new JLabel("Time-in (hh:mm AM/PM):");
            timeInLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(timeInLabel, gbc);
            timeInField = new JFormattedTextField(new SimpleDateFormat("hh:mm a"));
            timeInField.setColumns(10);
            timeInField.setFocusLostBehavior(JFormattedTextField.COMMIT);
            timeInField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(timeInField, gbc);

            // Building
            JLabel buildingLabel = new JLabel("Building:");
            buildingLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(buildingLabel, gbc);
            buildingField = new JTextField(25);
            buildingField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(buildingField, gbc);

            // Room No.
            JLabel roomNumberLabel = new JLabel("Room No.:");
            roomNumberLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(roomNumberLabel, gbc);
            roomNumberField = new JTextField(15);
            roomNumberField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(roomNumberField, gbc);

            // Purpose
            JLabel purposeLabel = new JLabel("Purpose:");
            purposeLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            gbc.gridx = 0;
            gbc.gridy = row;
            formPanel.add(purposeLabel, gbc);
            purposeField = new JTextField(35);
            purposeField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            gbc.gridx = 1;
            gbc.gridy = row++;
            formPanel.add(purposeField, gbc);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Horizontal gap
            submitButton = new JButton("Submit Reservation");
            resetButton = new JButton("Reset Form");

            submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            resetButton.setFont(new Font("SansSerif", Font.BOLD, 16));

            submitButton.setBackground(new Color(59, 89, 182));
            submitButton.setForeground(Color.WHITE);
            resetButton.setBackground(new Color(200, 50, 50));
            resetButton.setForeground(Color.WHITE);

            submitButton.addActionListener(this);
            resetButton.addActionListener(this);

            buttonPanel.add(submitButton);
            buttonPanel.add(resetButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel);
        }

        private String[] generateDateOptions() {
            String[] dateOptions = new String[30];
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 30; i++) {
                dateOptions[i] = sdf.format(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
            return dateOptions;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submitButton) {
                String date = (String) dateComboBox.getSelectedItem();
                String email = emailField.getText().trim();
                String timeOut = timeOutField.getText().trim();
                String timeIn = timeInField.getText().trim();
                String building = buildingField.getText().trim();
                String roomNumber = roomNumberField.getText().trim();
                String purpose = purposeField.getText().trim();

                if (date.isEmpty() || email.isEmpty() || timeOut.isEmpty() || timeIn.isEmpty() ||
                        building.isEmpty() || roomNumber.isEmpty() || purpose.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    new SimpleDateFormat("hh:mm a").parse(timeOut);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Time-out format. Please use hh:mm AM/PM.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    new SimpleDateFormat("hh:mm a").parse(timeIn);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Time-in format. Please use hh:mm AM/PM.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveRoomReservation(date, email, timeOut, timeIn, building, roomNumber, purpose, "pending");

                // Now that it's an inner class, call it directly
                new Admin.ReservationDetailsFrame(date, email, timeOut, timeIn, building, roomNumber, purpose).setVisible(true);

                clearFormFields();

            } else if (e.getSource() == resetButton) {
                clearFormFields();
            }
        }

        private void saveRoomReservation(String date, String email, String timeOut, String timeIn,
                                         String building, String roomNumber, String purpose, String status) {
            String dataLine = String.join("||", date, email, timeOut, timeIn, building, roomNumber, purpose, status);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("res/room_reservations.txt", true))) {
                writer.write(dataLine);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Reservation submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private void clearFormFields() {
            dateComboBox.setSelectedIndex(0);
            emailField.setText("");
            timeOutField.setText("");
            timeInField.setText("");
            buildingField.setText("");
            roomNumberField.setText("");
            purposeField.setText("");
        }
    }

    // Class to display occupied rooms with only approved reservations
    static class RoomOccupiedPanel extends JFrame {
        private DefaultTableModel roomReservationTableModel;
        private JTable roomReservationTable;

        public RoomOccupiedPanel() {
            setTitle("Occupied Rooms");
            setSize(1200, 650); // Increased width and height for better visibility
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose only this window
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(15, 15)); // Added gaps
            getContentPane().setBackground(new Color(245, 250, 255)); // Lighter background

            JLabel headerLabel = new JLabel("List of Occupied Rooms", JLabel.CENTER);
            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            headerLabel.setForeground(new Color(40, 60, 90));
            add(headerLabel, BorderLayout.NORTH);

            // Columns: Date, Email, Time Out, Time In, Building, Room Number, Purpose, Status
            String[] roomColumnNames = {"Date", "Email", "Time Out", "Time In", "Building", "Room Number", "Purpose", "Status"};
            roomReservationTableModel = new DefaultTableModel(roomColumnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Cells are not editable
                }
            };
            roomReservationTable = new JTable(roomReservationTableModel);
            roomReservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            roomReservationTable.setRowHeight(28);
            roomReservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
            roomReservationTable.setFillsViewportHeight(true);
            roomReservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane roomScrollPane = new JScrollPane(roomReservationTable);
            roomScrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 20, 10, 20),
                    BorderFactory.createLineBorder(new Color(180, 200, 220), 1)
            ));
            add(roomScrollPane, BorderLayout.CENTER);

            JButton refreshRoomReservationsButton = createActionButton("🔄 Refresh Reservations", new Color(100, 149, 237));
            refreshRoomReservationsButton.addActionListener(e -> loadRoomReservationsIntoTable());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
            buttonPanel.setOpaque(false);
            buttonPanel.add(refreshRoomReservationsButton);
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
                    if (parts.length == 8) {
                        String status = parts[7].trim();
                        if ("approved".equalsIgnoreCase(status)) {
                            roomReservationTableModel.addRow(parts);
                        }
                    } else {
                        System.err.println("Skipping malformed line (unexpected number of parts): " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading room_reservations.txt: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error loading reservations. File might not exist or is corrupted.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

