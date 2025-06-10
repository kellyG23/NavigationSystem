import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;

public class Map extends JFrame {
    private Set<String> validLocations;
    private JLabel resultLabel;
    private ZoomableMapPanel mapPanel;
    private JPanel bottomNav;
    private JScrollPane scrollPane;

    public Map() {
        initializeLocations();

        setTitle("Campus Map");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

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

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        inputPanel.add(searchField);
        inputPanel.add(searchButton);

        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultLabel.setForeground(Color.DARK_GRAY);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchPanel.add(inputPanel);
        searchPanel.add(resultLabel);

        // Map Panel
        mapPanel = new ZoomableMapPanel("floorplan/annex2/1.png");
        scrollPane = new JScrollPane(mapPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mapPanel.enableDragToPan(scrollPane);
        scrollPane.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                mapPanel.zoom(e.getWheelRotation() < 0 ? 1.1 : 0.9);
            }
        });

        // Search logic
        searchButton.addActionListener(e -> {
            String input = searchField.getText().trim().toLowerCase();
            if (input.isEmpty()) {
                resultLabel.setText("Please enter a location.");
            } else if (validLocations.contains(input)) {
                resultLabel.setText("Location found: " + capitalizeWords(input));
            } else {
                resultLabel.setText("Location not found: " + capitalizeWords(input));
            }
        });

        // Rotate controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton rotateLeft = new JButton("⟲ Rotate Left");
        JButton rotateRight = new JButton("⟳ Rotate Right");
        controls.add(rotateLeft);
        controls.add(rotateRight);

        rotateLeft.addActionListener(e -> mapPanel.rotate(-Math.PI / 12));
        rotateRight.addActionListener(e -> mapPanel.rotate(Math.PI / 12));

        // Bottom Navigation Bar (buildings)
        bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] buildings = {"annex2", "main", "annex1", "jmb"};
        for (String bld : buildings) {
            JButton btn = new JButton("Building " + bld);
            btn.addActionListener(e -> showFloorsForBuilding(bld));
            bottomNav.add(btn);
        }

        // Assemble UI
        mainPanel.add(navbar, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controls, BorderLayout.SOUTH);
        mainPanel.add(bottomNav, BorderLayout.PAGE_END);

        this.add(mainPanel);
        this.setVisible(true);
    }

    private void showFloorsForBuilding(String building) {
        bottomNav.removeAll();

        for (int i = 1; i <= 3; i++) {
            String label = "Floor " + i;
            int floorNum = i;
            JButton floorBtn = new JButton(label);
            floorBtn.addActionListener(e -> {
                String path = "floorplan/" + building + "/" + floorNum + ".png";
                mapPanel.loadNewImage(path);
            });
            bottomNav.add(floorBtn);
        }

        JButton backBtn = new JButton("Back to Buildings");
        backBtn.addActionListener(e -> resetBuildingNav());
        bottomNav.add(backBtn);

        bottomNav.revalidate();
        bottomNav.repaint();
    }

    private void resetBuildingNav() {
        bottomNav.removeAll();
        String[] buildings = {"annex2", "main", "annex1", "jmb"};
        for (String bld : buildings) {
            JButton btn = new JButton("Building " + bld);
            btn.addActionListener(e -> showFloorsForBuilding(bld));
            bottomNav.add(btn);
        }
        bottomNav.revalidate();
        bottomNav.repaint();
    }

    private void initializeLocations() {
        validLocations = new HashSet<>();
        for (int i = 1; i <= 4; i++) validLocations.add(("building " + i).toLowerCase());
        for (int i = 1; i <= 500; i++) validLocations.add(("room " + i).toLowerCase());
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Map::new);
    }
}

class ZoomableMapPanel extends JPanel {
    private BufferedImage image;
    private double scale = 1.0;
    private double rotation = 0;

    public ZoomableMapPanel(String imagePath) {
        loadNewImage(imagePath);
    }

    public void loadNewImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
            scale = 1.0;
            rotation = 0;
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image not found: " + imagePath, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void zoom(double factor) {
        scale *= factor;
        revalidate();
        repaint();
    }

    public void rotate(double angle) {
        rotation += angle;
        repaint();
    }

    public void enableDragToPan(JScrollPane scrollPane) {
        final Point[] start = {null};

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                start[0] = e.getPoint();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (start[0] != null) {
                    JViewport viewPort = scrollPane.getViewport();
                    Point vp = viewPort.getViewPosition();
                    int dx = start[0].x - e.getX();
                    int dy = start[0].y - e.getY();
                    vp.translate(dx, dy);
                    scrollRectToVisible(new Rectangle(vp, viewPort.getSize()));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        int iw = (int) (image.getWidth() * scale);
        int ih = (int) (image.getHeight() * scale);

        setPreferredSize(new Dimension(iw, ih));
        revalidate();

        AffineTransform at = new AffineTransform();
        at.translate(getWidth() / 2.0, getHeight() / 2.0);
        at.rotate(rotation);
        at.scale(scale, scale);
        at.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);
        g2.drawImage(image, at, this);
        g2.dispose();
    }
}
