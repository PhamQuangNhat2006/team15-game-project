import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private BufferedImage menuImage;
    private final int WIDTH = 610, HEIGHT = 800;

    private Rectangle startRect       = new Rectangle(200, 300, 200, 40);
    private Rectangle exitRect        = new Rectangle(200, 580, 200, 40);

    public MenuPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            menuImage = ImageIO.read(new File("resources/menu_bg.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh menu: " + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point click = e.getPoint();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MenuPanel.this);

                if (startRect.contains(click)) {
                    topFrame.setContentPane(new GamePanel());
                    topFrame.revalidate();
                } else if (exitRect.contains(click)) {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuImage != null) {
            g.drawImage(menuImage, 0, 0, WIDTH, HEIGHT, null);
        }
    }
}