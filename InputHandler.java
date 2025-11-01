import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    private GamePanel gamePanel;
    private SoundManager soundManager;

    public InputHandler(GamePanel panel) {
        this.gamePanel = panel;
        this.soundManager = SoundManager.getInstance();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Đã nhấn phím, mã phím là: " + e.getKeyCode());
        int keyCode = e.getKeyCode();

        // Nếu nhấn phím "P" để pause
        if (keyCode == KeyEvent.VK_P) {
            gamePanel.togglePause();
        }

        // Nếu nhấn phím "M" để tắt/bật âm thanh
        if (keyCode == KeyEvent.VK_M) {
            soundManager.toggleSound();
            System.out.println("Âm thanh: " + (soundManager.isSoundEnabled() ? "Bật" : "Tắt"));
        }

        // Nếu nhấn phím ESC để thoát
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}