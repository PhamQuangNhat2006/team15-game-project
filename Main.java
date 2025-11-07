import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Arkanoid Neon");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setSize(600, 800);
            frame.setMinimumSize(new java.awt.Dimension(400, 600));

            // Load icon từ resources (đã mark là Resources Root)
            frame.setIconImage(new ImageIcon(Main.class.getResource("/Resources/icon.png")).getImage());

            MenuPanel menu = new MenuPanel();
            frame.setContentPane(menu);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
