import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private BufferedImage background;

    public MenuPanel(JFrame frame) {
        setPreferredSize(new Dimension(600, 800));
        setLayout(null);

        try {
            background = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
        }

        JButton startButton = new JButton("Bắt đầu");
        startButton.setBounds(200, 500, 200, 50);
        add(startButton);

        JButton exitButton = new JButton("Thoát");
        exitButton.setBounds(200, 570, 200, 50);
        add(exitButton);

        startButton.addActionListener(e -> {
            GamePanel game = new GamePanel();
            frame.setContentPane(game);
            frame.revalidate();
            game.requestFocusInWindow();
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        // Vẽ hướng dẫn chơi
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("🎮 HƯỚNG DẪN CHƠI", 200, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("- Di chuyển paddle bằng chuột", 100, 140);
        g.drawString("- Đỡ bóng để phá hết các viên gạch", 100, 170);
        g.drawString("- Mỗi viên gạch bị phá: +10 điểm", 100, 200);
        g.drawString("- Mất bóng: -1 mạng (tối đa 3 mạng)", 100, 230);
        g.drawString("- Game Over khi hết mạng", 100, 260);
    }
}