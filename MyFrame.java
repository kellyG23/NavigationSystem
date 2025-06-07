import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    MyFrame() {
        // Frame settings
        this.setTitle("National University Navigation System");
        this.setSize(600, 750);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("elements/Logo.png");
        this.setIconImage(icon.getImage());

        // Panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo image
        ImageIcon logoIcon = new ImageIcon("elements/Logo.png");
        Image scaledLogoImage = logoIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text image
        ImageIcon textImageIcon = new ImageIcon("elements/TextImage.png");
        Image scaledTextImage = textImageIcon.getImage().getScaledInstance(600, 100, Image.SCALE_SMOOTH);
        JLabel textLabel = new JLabel(new ImageIcon(scaledTextImage));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Path image
        ImageIcon pathIcon = new ImageIcon("elements/path.png");
        Image scaledPathIcon = pathIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel pathLabel = new JLabel(new ImageIcon(scaledPathIcon));
        pathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(100, 40));
        startButton.setMaximumSize(new Dimension(120, 40));

        // Optional: Customize button appearance
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.addActionListener(e -> {
            this.dispose(); // Close current window
            new Map(); // Open Map window
        });


        // Add components to panel
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(logoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(textLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(pathLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30))); // spacing before button
        panel.add(startButton);
        panel.add(Box.createVerticalGlue());

        this.add(panel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new);
    }
}
