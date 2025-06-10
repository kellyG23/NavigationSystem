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
        JButton startButton = new JButton("Start") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners

                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30); // Border radius
                g2.dispose();
            }
        };

        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setBackground(new Color(67, 63, 63));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(250, 40)); // Increased size
        startButton.setMaximumSize(new Dimension(250, 40));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        panel.add(Box.createRigidArea(new Dimension(0, -20))); // spacing before button
        panel.add(startButton);
        panel.add(Box.createVerticalGlue());

        this.add(panel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new);
    }
}
