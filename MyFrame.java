import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    MyFrame() {
        // Frame settings
        this.setTitle("National University Navigation System");
        this.setSize(500, 750);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("elements/Logo.png");
        this.setIconImage(icon.getImage());

        // Panel with vertical layout to hold both images
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE); // optional
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo image
        ImageIcon logoIcon = new ImageIcon("elements/Logo.png");
        Image scaledLogoImage = logoIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text image
        ImageIcon textImageIcon = new ImageIcon("elements/TextImage.png");
        Image scaledTextImage = textImageIcon.getImage().getScaledInstance(636, 100, Image.SCALE_SMOOTH);
        JLabel textLabel = new JLabel(new ImageIcon(scaledTextImage));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to panel
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // spacing
        panel.add(logoLabel);
        panel.add(Box.createVerticalGlue()); // for vertical centering
        panel.add(Box.createRigidArea(new Dimension(0, -300))); // spacing
        panel.add(textLabel);
        panel.add(Box.createVerticalGlue());

        this.add(panel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new); // Ensures thread safety
    }
}