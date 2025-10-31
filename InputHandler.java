import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {

    private GamePanel gamePanel; 

    public InputHandler(GamePanel panel) {
        this.gamePanel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Đã nhấn phím, mã phím là: " + e.getKeyCode());
        int keyCode = e.getKeyCode();

        // Nếu nhấn phím "P"
        if (keyCode == KeyEvent.VK_P) {
            gamePanel.togglePause(); 
        }

        
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}