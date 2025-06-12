import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ReservationSystem extends JFrame {

    private JLabel dateLabel;
    private JComboBox<String> dateComboBox;

    private JLabel timeOutLabel;
    private JFormattedTextField timeOutField;

    private JLabel timeInLabel;
    private JFormattedTextField timeInField;

    private JLabel buildingLabel;
    private JTextField buildingField;

    private JLabel roomNumberLabel;
    private JTextField roomNumberField;

    private JLabel purposeLabel;
    private JTextField purposeField;

    private JButton reserveButton;

    public ReservationSystem() {
        setTitle("Reservation System");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        dateLabel = new JLabel("Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(dateLabel, gbc);
        dateComboBox = new JComboBox<>(generateDateOptions());
        dateComboBox.setBounds(170, 250, 150, 25);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(dateComboBox, gbc);

        timeOutLabel = new JLabel("Time-out:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(timeOutLabel, gbc);
        try {
            timeOutField = new JFormattedTextField(new SimpleDateFormat("hh:mm a"));
            timeOutField.setColumns(8);
            timeOutField.setFocusLostBehavior(JFormattedTextField.COMMIT);
        } catch (IllegalArgumentException e) {
            timeOutField = new JFormattedTextField();
            timeOutField.setText("HH:MM AM/PM");
            System.err.println("Error creating time SimpleDateFormat: " + e.getMessage());
        }
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(timeOutField, gbc);

        timeInLabel = new JLabel("Time-in:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(timeInLabel, gbc);
        try {
            timeInField = new JFormattedTextField(new SimpleDateFormat("hh:mm a"));
            timeInField.setColumns(8);
            timeInField.setFocusLostBehavior(JFormattedTextField.COMMIT);
        } catch (IllegalArgumentException e) {
            timeInField = new JFormattedTextField();
            timeInField.setText("HH:MM AM/PM");
            System.err.println("Error creating time SimpleDateFormat: " + e.getMessage());
        }
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(timeInField, gbc);

        buildingLabel = new JLabel("Building:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(buildingLabel, gbc);
        buildingField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(buildingField, gbc);

        roomNumberLabel = new JLabel("Room No.:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(roomNumberLabel, gbc);
        roomNumberField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(roomNumberField, gbc);

        purposeLabel = new JLabel("Purpose:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(purposeLabel, gbc);
        purposeField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(purposeField, gbc);

        reserveButton = new JButton("Submit Reservation");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(reserveButton, gbc);

        reserveButton.addActionListener(e -> {
            String date = (String) dateComboBox.getSelectedItem();
            String timeOut = timeOutField.getText();
            String timeIn = timeInField.getText();
            String building = buildingField.getText();
            String roomNumber = roomNumberField.getText();
            String purpose = purposeField.getText();

            if (date.isEmpty() || timeOut.isEmpty() || timeIn.isEmpty() ||
                    building.isEmpty() || roomNumber.isEmpty() || purpose.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
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

            saveRoomReservation(date, timeOut, timeIn, building, roomNumber, purpose);

            ReservationDetailsFrame detailsFrame = new ReservationDetailsFrame(
                    date, timeOut, timeIn, building, roomNumber, purpose
            );
            detailsFrame.setVisible(true);

            clearFormFields();
        });

        add(formPanel);
        setVisible(true);
    }

    private String[] generateDateOptions() {
        String[] dateOptions = new String [30];
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 30; i++) {
            dateOptions [i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dateOptions;
    }

    private void saveRoomReservation(String date, String timeOut, String timeIn,
                                     String building, String roomNumber, String purpose) {
        String dataLine = String.join("||", date, timeOut, timeIn, building, roomNumber, purpose);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("room_reservations.txt", true))) {
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
        timeOutField.setText("");
        timeInField.setText("");
        buildingField.setText("");
        roomNumberField.setText("");
        purposeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReservationSystem::new);
    }
}