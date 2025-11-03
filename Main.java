import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Arkanoid Neon");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true); //  kéo giãn cửa sổ
            frame.setSize(600, 800); // Kích thước khởi đầu
            frame.setMinimumSize(new java.awt.Dimension(400, 600)); //Giới hạn nhỏ nhất
            frame.setIconImage(new ImageIcon(Main.class.getResource("/Resources/icon.png")).getImage());
            MenuPanel menu = new MenuPanel();
            frame.setContentPane(menu);

            frame.setLocationRelativeTo(null); // Căn giữa màn hình
            frame.setVisible(true);
        });
    }
}