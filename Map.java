import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

public class Map extends JFrame {
    private final ZoomableMapPanel mapPanel;
    private final JPanel bottomNav;
    private final JToggleButton appearButton;
    private final JLabel titleLabel;
    private final JTextField searchField;

    // Room Directory
    private final java.util.Map<String, String> roomDirectory = new HashMap<>();

    public Map() {
        setTitle("Campus Map");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize room directory
        populateRoomDirectory();

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Map Panel inside scroll pane
        mapPanel = new ZoomableMapPanel("floorplan/Annex_2/1.png");
        JScrollPane scrollPane = new JScrollPane(
                mapPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        mapPanel.enableDragToPan(scrollPane);
        scrollPane.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                mapPanel.zoom(e.getWheelRotation() < 0 ? 1.1 : 0.9);
            }
        });

        // Bottom Navigation Bar (buildings)
        bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addBuildingButtons();

        bottomNav.setVisible(false);
        bottomNav.setPreferredSize(new Dimension(500, 100));
        bottomNav.setBackground(new Color(217, 217, 217));
        bottomNav.setLayout(new GridLayout(0, 2, 10, 10));
        bottomNav.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Appear Toggle Button
        appearButton = new JToggleButton("☰");
        appearButton.setBackground(Color.WHITE);
        appearButton.setFocusable(false);
        appearButton.setBounds(230, 701, 40, 10);

        appearButton.addActionListener(_ -> {
            boolean selected = appearButton.isSelected();
            bottomNav.setVisible(selected);
            appearButton.setBounds(230, selected ? 605 : 701, 40, 10);
        });

        // Label for building and floor
        titleLabel = new JLabel("Annex_2 - Floor 1", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBounds(0, 0, 500, 30);

        // Search Field
        searchField = new JTextField();
        searchField.setBounds(10, 35, 350, 25);

        // Search Button
        JButton searchButton = getJButton();

        // LayeredPane to overlap button + label on scrollPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 700));
        scrollPane.setBounds(0, 0, 500, 700);

        layeredPane.setLayout(null);
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(appearButton, JLayeredPane.DRAG_LAYER);
        layeredPane.add(titleLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.add(searchField, JLayeredPane.DRAG_LAYER);
        layeredPane.add(searchButton, JLayeredPane.DRAG_LAYER);

        mainPanel.add(layeredPane, BorderLayout.CENTER);
        mainPanel.add(bottomNav, BorderLayout.PAGE_END);

        this.add(mainPanel);
        this.setVisible(true);
    }

    private JButton getJButton() {
        JButton searchButton = new JButton("Search Room");
        searchButton.setBounds(370, 35, 120, 25);
        searchButton.addActionListener(_ -> {
            String query = searchField.getText().trim().toLowerCase();
            if (roomDirectory.containsKey(query)) {
                String location = roomDirectory.get(query);
                String[] parts = location.split(" - ");
                String building = parts[0];
                String floor = parts[1].replace("Floor ", "");
                String path = "floorplan/" + building + "/" + floor + ".png";
                mapPanel.loadNewImage(path);
                titleLabel.setText(location);
                appearButton.setSelected(false);
                bottomNav.setVisible(false);
                appearButton.setBounds(230, 701, 40, 10);
            } else {
                JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return searchButton;
    }

    private void populateRoomDirectory() {
        // Annex_2 - 1st Floor
        String b = "Annex_2";
        roomDirectory.put("meeting room", b + " - Floor 1");
        roomDirectory.put("clinic", b + " - Floor 1");
        roomDirectory.put("faculty room", b + " - Floor 1");
        roomDirectory.put("student services", b + " - Floor 1");
        roomDirectory.put("canteen", b + " - Floor 1");
        for (int i = 101; i <= 107; i++) {
            roomDirectory.put("room " + i, b + " - Floor 1");
        }

        // 2nd Floor
        roomDirectory.put("lrc", b + " - Floor 2");
        roomDirectory.put("computer lab 1", b + " - Floor 2");
        roomDirectory.put("computer lab 2", b + " - Floor 2");
        roomDirectory.put("consultation room", b + " - Floor 2");
        for (int i = 201; i <= 208; i++) {
            roomDirectory.put("room " + i, b + " - Floor 2");
        }

        // 3rd Floor
        for (int i = 301; i <= 311; i++) {
            roomDirectory.put("room " + i, b + " - Floor 3");
        }

        // 4th Floor
        for (int i = 401; i <= 411; i++) {
            roomDirectory.put("room " + i, b + " - Floor 4");
        }
    }

    private void addBuildingButtons() {
        bottomNav.removeAll();
        String[] buildings = {"Main", "Annex_1", "Annex_2", "JMB"};
        for (String bld : buildings) {
            JButton btn = new JButton(bld);
            btn.addActionListener(_ -> showFloorsForBuilding(bld));
            bottomNav.add(btn);
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(26, 26, 26));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
        }
        bottomNav.revalidate();
        bottomNav.repaint();
    }

    private void showFloorsForBuilding(String building) {
        bottomNav.removeAll();
        bottomNav.setLayout(new GridLayout(0, 4, 10, 10));
        int floors = switch (building) {
            case "Main", "JMB" -> 8;
            case "Annex_1" -> 12;
            case "Annex_2" -> 4;
            default -> 0;
        };
        for (int i = 1; i <= floors; i++) {
            int floorNum = i;
            JButton floorBtn = new JButton("Floor " + floorNum);
            floorBtn.addActionListener(_ -> {
                String path = "floorplan/" + building + "/" + floorNum + ".png";
                mapPanel.loadNewImage(path);
                titleLabel.setText(building + " - Floor " + floorNum);
            });
            bottomNav.add(floorBtn);
        }

        JButton backBtn = new JButton("Back to Buildings");
        backBtn.addActionListener(_ -> {
            addBuildingButtons();
            bottomNav.setLayout(new GridLayout(0, 2, 10, 10));
            titleLabel.setText("Select a Building");
        });

        JButton resBtn = new JButton("Reserve a Room");
        resBtn.addActionListener(_ -> {
            this.dispose(); // Close the current window
            UserAccount userAccount = new UserAccount();
            new LoginSystem(userAccount.getLoginInfo());
        });

        bottomNav.add(backBtn);
        bottomNav.add(resBtn);
        bottomNav.revalidate();
        bottomNav.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Map::new);
    }
}


class ZoomableMapPanel extends JPanel {
    private BufferedImage image;
    private double scale = 1.0;

    public ZoomableMapPanel(String imagePath) {
        loadNewImage(imagePath);
    }

    public void loadNewImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
            scale = 1.0;
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
        at.scale(scale, scale);
        at.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);

        g2.drawImage(image, at, this);
        g2.dispose();
    }
}
