import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private BufferedImage background, logo;

    public MenuPanel() {
        setLayout(null); // dùng setBounds cho nút
        setPreferredSize(new Dimension(600, 800));

        // Tải ảnh nền và logo PNG
        try {
            background = ImageIO.read(new File("resources/menu_bg.png"));
            logo = ImageIO.read(new File("resources/arkanoid_logo.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh: " + e.getMessage());
        }

        // Tạo các nút menu
        add(createButton("START", 200, 280, Color.PINK, e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(new GamePanel());
            topFrame.revalidate();
        }));

        add(createButton("{CONTINUE}", 200, 340, Color.YELLOW, e -> {
            JOptionPane.showMessageDialog(this, "Chức năng CONTINUE chưa được triển khai.");
        }));

        add(createButton("NEW GAME", 200, 400, Color.GREEN, e -> {
            GamePanel.score = 0;
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(new GamePanel());
            topFrame.revalidate();
        }));

        add(createButton("HIGH SCORE", 200, 460, Color.CYAN, e -> {
            JOptionPane.showMessageDialog(this, "Điểm cao: " + GamePanel.score);
        }));

        add(createButton("HOW TO PLAY", 200, 520, Color.RED, e -> {
            JOptionPane.showMessageDialog(this,
                    "- Di chuyển paddle bằng chuột\n" +
                            "- Đỡ bóng để phá hết các viên gạch\n" +
                            "- Mỗi viên gạch bị phá: +10 điểm\n" +
                            "- Mất bóng: -1 mạng (tối đa 3 mạng)\n" +
                            "- Game Over khi hết mạng"
            );
        }));

        add(createButton("EXIT", 200, 580, Color.ORANGE, e -> System.exit(0)));
    }

    private JButton createButton(String text, int x, int y, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 40);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(color);
        button.setBackground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(action);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ ảnh nền
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        // Vẽ logo ở giữa trên
        if (logo != null) {
            int logoWidth = 400;
            int logoHeight = 150;
            int x = (getWidth() - logoWidth) / 2;
            int y = 100;
            g.drawImage(logo, x, y, logoWidth, logoHeight, null);
        }
    }
}