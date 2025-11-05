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

        // Pause game with P key
        if (keyCode == KeyEvent.VK_P) {
            gamePanel.togglePause();
        }

        // Exit with ESC
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        // Toggle music with M key
        if (keyCode == KeyEvent.VK_M) {
            SoundManager soundManager = gamePanel.getSoundManager();
            soundManager.toggleMusic();
        }

        // Toggle sound effects with S key
        if (keyCode == KeyEvent.VK_S) {
            SoundManager soundManager = gamePanel.getSoundManager();
            soundManager.toggleSFX();
        }
    }
}