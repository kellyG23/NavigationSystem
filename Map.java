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
    private final java.util.Map<String, String> roomDirectory = new HashMap<>();

    private final DefaultListModel<String> roomListModel;

    private String currentBuilding = "Annex_2";
    private int currentFloor = 1;

    public Map() {
        setTitle("Campus Map");
        setSize(640, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        populateRoomDirectory();

        JPanel mainPanel = new JPanel(new BorderLayout());

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
        mapPanel.setBackground(new Color(155, 172, 189));
        bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addBuildingButtons();

        bottomNav.setVisible(true);
        bottomNav.setPreferredSize(new Dimension(500, 100));
        bottomNav.setBackground(new Color(230, 240, 250));
        bottomNav.setLayout(new GridLayout(0, 2, 10, 10));
        bottomNav.setBorder(new EmptyBorder(10, 20, 10, 20));

        appearButton = new JToggleButton("☰");
        appearButton.setBackground(Color.WHITE);
        appearButton.setFocusable(false);
        appearButton.setBounds(163, 605, 40, 10);
        appearButton.addActionListener(_ -> {
            boolean selected = appearButton.isSelected();
            bottomNav.setVisible(selected);
            appearButton.setBounds(selected ? 163 : 230, selected ? 605 : 701, 40, 10);
        });

        titleLabel = new JLabel("Annex_2 - Floor 1", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(230, 240, 250));
        titleLabel.setBounds(0, 0, 500, 30);

        searchField = new JTextField();
        searchField.setForeground(new Color(60, 90, 120));
        searchField.setBounds(10, 35, 350, 25);

        JButton searchButton = getJButton();
        searchButton.setBackground(new Color(60, 90, 120));
        searchButton.setForeground(Color.white);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBorder(BorderFactory.createLineBorder(new Color(60, 90, 120), 2, true));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 700));
        scrollPane.setBounds(0, 0, 500, 700);

        layeredPane.setLayout(null);
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(appearButton, JLayeredPane.DRAG_LAYER);
        layeredPane.add(titleLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.add(searchField, JLayeredPane.DRAG_LAYER);
        layeredPane.add(searchButton, JLayeredPane.DRAG_LAYER);

        // Room list on the left side
        roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        roomList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane roomScrollPane = new JScrollPane(roomList);
        roomScrollPane.setPreferredSize(new Dimension(130, 700));
        mainPanel.add(roomScrollPane, BorderLayout.WEST);
        updateRoomList(currentBuilding, currentFloor);
        roomList.setBackground(new Color(230, 240, 250));

        mainPanel.add(layeredPane, BorderLayout.CENTER);
        mainPanel.add(bottomNav, BorderLayout.PAGE_END);

        this.add(mainPanel);
        this.setVisible(true);
    }

    private JButton getJButton() {
        JButton searchButton = new JButton("Search Room");
        searchButton.setBounds(360, 35, 120, 25);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(26, 26, 26));
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

                currentBuilding = building;
                currentFloor = Integer.parseInt(floor);
                updateRoomList(currentBuilding, currentFloor);
            } else {
                JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return searchButton;
    }

    private void populateRoomDirectory() {
        String annex = "Annex_2";
        roomDirectory.put("meeting room", annex + " - Floor 1");
        roomDirectory.put("clinic", annex + " - Floor 1");
        roomDirectory.put("faculty room", annex + " - Floor 1");
        roomDirectory.put("student services", annex + " - Floor 1");
        roomDirectory.put("canteen", annex + " - Floor 1");
        for (int i = 101; i <= 107; i++) {
            roomDirectory.put("room " + i, annex + " - Floor 1");
        }

        roomDirectory.put("lrc", annex + " - Floor 2");
        roomDirectory.put("computer lab 1", annex + " - Floor 2");
        roomDirectory.put("computer lab 2", annex + " - Floor 2");
        roomDirectory.put("consultation room", annex + " - Floor 2");
        for (int i = 201; i <= 208; i++) {
            roomDirectory.put("room " + i, annex + " - Floor 2");
        }

        for (int i = 301; i <= 311; i++) {
            roomDirectory.put("room " + i, annex + " - Floor 3");
        }
        for (int i = 401; i <= 411; i++) {
            roomDirectory.put("room " + i, annex + " - Floor 4");
        }
    }

    private void addBuildingButtons() {
        bottomNav.removeAll();
        String[] buildings = {"Main", "Annex_1", "Annex_2", "JMB"};
        for (String bld : buildings) {
            JButton btn = new JButton(bld);
            btn.addActionListener(_ -> showFloorsForBuilding(bld));
            bottomNav.add(btn);

            btn.setForeground(new Color(60, 90, 120));
            btn.setBackground(Color.white);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createLineBorder(new Color(60, 90, 120), 2, true));
        }
        bottomNav.revalidate();
        bottomNav.repaint();
    }

    private void showFloorsForBuilding(String building) {
        bottomNav.removeAll();
        bottomNav.setLayout(new GridLayout(0, 5, 5, 5));
        int floors = switch (building) {
            case "Main", "JMB" -> 8;
            case "Annex_1" -> 12;
            case "Annex_2" -> 4;
            default -> 0;
        };
        for (int i = 1; i <= floors; i++) {
            int floorNum = i;
            JButton floorBtn = new JButton("Floor " + floorNum);
            floorBtn.setForeground(new Color(60, 90, 120));
            floorBtn.setBackground(Color.white);
            floorBtn.setFocusPainted(false);
            floorBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            floorBtn.setBorder(BorderFactory.createLineBorder(new Color(60, 90, 120), 2, true));

            floorBtn.addActionListener(_ -> {
                String path = "floorplan/" + building + "/" + floorNum + ".png";
                mapPanel.loadNewImage(path);
                titleLabel.setText(building + " - Floor " + floorNum);
                currentBuilding = building;
                currentFloor = floorNum;
                updateRoomList(currentBuilding, currentFloor);

            });
            bottomNav.add(floorBtn);
        }

        JButton backBtn = new JButton("Buildings");
        backBtn.setForeground(new Color(46, 139, 87));
        backBtn.setBackground(Color.white);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(46, 139, 87), 2, true));
        backBtn.addActionListener(_ -> {
            addBuildingButtons();
            bottomNav.setLayout(new GridLayout(0, 2, 5, 5));
            titleLabel.setText("Select a Building");
        });

        JButton resBtn = new JButton("Reserve");
        resBtn.setForeground(new Color(46, 139, 87));
        resBtn.setBackground(Color.white);
        resBtn.setFocusPainted(false);
        resBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resBtn.setBorder(BorderFactory.createLineBorder(new Color(46, 139, 87), 2, true));

        resBtn.addActionListener(_ -> {
            this.dispose();
            Accounts accounts = new Accounts();
            new LoginSystem(accounts.getUserLogins(), accounts.getAdminLogins());
        });

        bottomNav.add(backBtn);
        bottomNav.add(resBtn);
        bottomNav.revalidate();
        bottomNav.repaint();
    }

    private void updateRoomList(String building, int floor) {
        roomListModel.clear();
        String floorKey = building + " - Floor " + floor;
        for (String room : roomDirectory.keySet()) {
            if (roomDirectory.get(room).equalsIgnoreCase(floorKey)) {
                roomListModel.addElement(capitalize(room));
            }
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
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
            scale = 0.3;
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
