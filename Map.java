import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

public class Map extends JFrame {
    private final ZoomableMapPanel mapPanel;
    private final JPanel bottomNav;
    private final JToggleButton appearButton;
    private final JLabel titleLabel;

    public Map() {
        setTitle("Campus Map");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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
        appearButton.setBackground(new Color(255, 255, 255));
        appearButton.setFocusable(false);
        appearButton.setBounds(230, 701, 40, 10);

        appearButton.addActionListener(e -> {
            boolean selected = appearButton.isSelected();
            bottomNav.setVisible(selected);
            appearButton.setBounds(230, selected ? 605 : 701, 40, 10);
        });

        // Label for building and floor
        titleLabel = new JLabel("Annex2 - Floor 1", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBounds(0, 0, 500, 30);

        // LayeredPane to overlap button + label on scrollPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 700));
        scrollPane.setBounds(0, 0, 500, 700);

        layeredPane.setLayout(null);
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(appearButton, JLayeredPane.DRAG_LAYER);
        layeredPane.add(titleLabel, JLayeredPane.DRAG_LAYER);

        mainPanel.add(layeredPane, BorderLayout.CENTER);
        mainPanel.add(bottomNav, BorderLayout.PAGE_END);

        this.add(mainPanel);
        this.setVisible(true);
    }

    private void addBuildingButtons() {
        bottomNav.removeAll();
        String[] buildings = {"Main", "Annex_1", "Annex_2", "JMB"};
        for (String bld : buildings) {
            JButton btn = new JButton(bld);
            btn.addActionListener(e -> showFloorsForBuilding(bld));
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
        int floors = 0;
        if (Objects.equals(building, "Main")) {
            floors = 8;
        }
        else if (Objects.equals(building, "Annex_1")) {
            floors = 12;
        }
        if (Objects.equals(building, "Annex_2")) {
            floors = 4;
        }
        if (Objects.equals(building, "JMB")) {
            floors = 8;
        }
        for (int i = 1; i <= floors; i++) {
            int floorNum = i;
            JButton floorBtn = new JButton("Floor " + floorNum);
            floorBtn.addActionListener(e -> {
                String path = "floorplan/" + building + "/" + floorNum + ".png";
                mapPanel.loadNewImage(path);
                titleLabel.setText(building + " - Floor " + floorNum);
            });
            bottomNav.add(floorBtn);
        }

        JButton backBtn = new JButton("Back to Buildings");
        backBtn.addActionListener(e -> {
            addBuildingButtons();
            bottomNav.setLayout(new GridLayout(0, 2, 10, 10));
            titleLabel.setText("Select a Building");
        });
        JButton Res = new JButton("Reserve a Room");
        Res.addActionListener(e -> {
            this.dispose(); // Close current window
            new LoginSystem(); // Open Map window
        });

        bottomNav.add(backBtn);
        bottomNav.add(Res);
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
