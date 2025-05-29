import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;


public class NavigationSystem {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("TITE");
        frame.setResizable(false);

        ImageIcon image = new ImageIcon("img.png");
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(0, 0, 100));

    }
}
