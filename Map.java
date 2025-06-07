import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class Map extends JFrame {
    private Set<String> validLocations;
    private JLabel resultLabel; // New label to display search result

    public Map() {
        initializeLocations();

        // Frame setup
        setTitle("Campus Map");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setBackground(new Color(30, 144, 255));
        navbar.setPreferredSize(new Dimension(getWidth(), 50));
        navbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel navTitle = new JLabel("National University Navigation");
        navTitle.setForeground(Color.WHITE);
        navTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        navbar.add(navTitle);

        // Search Panel (North)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");

        inputPanel.add(searchField);
        inputPanel.add(searchButton);

        resultLabel = new JLabel(" "); // Will show search results here
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultLabel.setForeground(Color.DARK_GRAY);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchPanel.add(inputPanel);
        searchPanel.add(resultLabel);

        // Map Image
        ImageIcon mapImage = new ImageIcon("elements/map.png");
        JLabel mapLabel = new JLabel(mapImage);
        mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane scrollPane = new JScrollPane(mapLabel);
        scrollPane.setBorder(null);

        // Search logic
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = searchField.getText().trim().toLowerCase();

                if (input.isEmpty()) {
                    resultLabel.setText("Please enter a location.");
                } else if (validLocations.contains(input)) {
                    resultLabel.setText("Location found: " + capitalizeWords(input));
                } else {
                    resultLabel.setText("Location not found: " + capitalizeWords(input));
                }
            }
        });

        // Assemble layout
        mainPanel.add(navbar, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(mainPanel);
        this.setVisible(true);
    }

    private void initializeLocations() {
        validLocations = new HashSet<>();

        for (int i = 1; i <= 4; i++) {
            validLocations.add(("building " + i).toLowerCase());
        }

        for (int i = 1; i <= 500; i++) {
            validLocations.add(("room " + i).toLowerCase());
        }
    }

    private String capitalizeWords(String text) {
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() == 0) continue;
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return result.toString().trim();
    }
}
