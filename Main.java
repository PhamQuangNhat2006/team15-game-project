import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid Vertical");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        MenuPanel menu = new MenuPanel(frame);
        frame.setContentPane(menu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        ImageIcon icon = new ImageIcon("resources/icon.png"); 
        frame.setIconImage(icon.getImage());
    }
}