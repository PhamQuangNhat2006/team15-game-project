import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MenuPanel extends JPanel {
    private BufferedImage background;

    public MenuPanel(JFrame frame) {
        setPreferredSize(new Dimension(600, 800));
        setLayout(null); // dùng vị trí tuyệt đối

        try {

            URL imageUrl = getClass().getResource("/background.png");

            if (imageUrl == null) {
                System.out.println("Lỗi: Không tìm thấy tệp /background.png");
            } else {
                background = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
            e.printStackTrace();
        }

        JButton startButton = new JButton("Bắt đầu");
        startButton.setBounds(200, 300, 200, 50);
        add(startButton);

        JButton exitButton = new JButton("Thoát");
        exitButton.setBounds(200, 370, 200, 50);
        add(exitButton);

        startButton.addActionListener(e -> {
            frame.setContentPane(new GamePanel());
            frame.revalidate();
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
    }
}