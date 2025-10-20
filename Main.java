import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Tạo cửa sổ game
        JFrame frame = new JFrame("Arkanoid Neon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Gắn icon cho cửa sổ (ảnh PNG)
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());

        // Gắn menu chính
        frame.setContentPane(new MenuPanel());
        frame.pack();
        frame.setLocationRelativeTo(null); // căn giữa màn hình
        frame.setVisible(true);
    }
}