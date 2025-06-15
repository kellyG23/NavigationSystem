import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    MyFrame() {
        // Frame settings
        this.setTitle("National University Navigation System");
        this.setSize(500, 750);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("elements/Logo.png");
        this.setIconImage(icon.getImage());

        // Panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(new Color(230, 240, 250));

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton startButton = new JButton("Start") ;
        gbc.gridy = 0;
        startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        startButton.setForeground(new Color(50, 70, 100));
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(true);
        startButton.setBackground(Color.white);
        startButton.setPreferredSize(new Dimension(250, 40)); // Increased size
        startButton.setMaximumSize(new Dimension(250, 40));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(_ -> {
            this.dispose();
            new Map();
        });


        // Add components to the panel
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(logoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(textLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(pathLabel);
        panel.add(Box.createRigidArea(new Dimension(0, -20))); // spacing before button
        panel.add(startButton, gbc);
        panel.add(Box.createVerticalGlue());

        this.add(panel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new);
    }
}
