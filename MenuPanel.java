import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private BufferedImage menuImage;
    private final int WIDTH = 600, HEIGHT = 700;

    private Rectangle startRect       = new Rectangle(200, 280, 200, 40);
    private Rectangle continueRect    = new Rectangle(200, 340, 200, 40);
    private Rectangle newGameRect     = new Rectangle(200, 400, 200, 40);
    private Rectangle highScoreRect   = new Rectangle(200, 460, 200, 40);
    private Rectangle howToPlayRect   = new Rectangle(200, 520, 200, 40);
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

                if (startRect.contains(click) || newGameRect.contains(click)) {
                    GamePanel.score = 0;
                    topFrame.setContentPane(new GamePanel());
                    topFrame.revalidate();
                } else if (continueRect.contains(click)) {
                    JOptionPane.showMessageDialog(MenuPanel.this, "Chức năng CONTINUE chưa được triển khai.");
                } else if (highScoreRect.contains(click)) {
                    JOptionPane.showMessageDialog(MenuPanel.this, "Điểm cao: " + GamePanel.score);
                } else if (howToPlayRect.contains(click)) {
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "- Di chuyển paddle bằng chuột\n" +
                                    "- Đỡ bóng để phá hết các viên gạch\n" +
                                    "- Mỗi viên gạch bị phá: +10 điểm\n" +
                                    "- Mất bóng: -1 mạng (tối đa 3 mạng)\n" +
                                    "- Game Over khi hết mạng"
                    );
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