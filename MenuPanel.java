import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private BufferedImage menuImage;
    private final int WIDTH = 610, HEIGHT = 800;
    private Rectangle startRect = new Rectangle(200, 300, 200, 40);
    private Rectangle exitRect = new Rectangle(200, 580, 200, 40);
    private SoundManager soundManager;

    public MenuPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        soundManager = SoundManager.getInstance();

        try {
            menuImage = ImageIO.read(getClass().getResource("/Resources/menu_bg.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh menu: " + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point click = e.getPoint();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MenuPanel.this);

                if (startRect.contains(click)) {
                    soundManager.playSound("button_click");

                    // Small delay to let sound play
                    Timer delayTimer = new Timer(100, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            GamePanel gamePanel = new GamePanel();
                            topFrame.setContentPane(gamePanel);
                            topFrame.revalidate();
                            gamePanel.requestFocusInWindow();
                            ((Timer)evt.getSource()).stop();
                        }
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();

                } else if (exitRect.contains(click)) {
                    soundManager.playSound("button_click");

                    // Small delay to let sound play before exiting
                    Timer delayTimer = new Timer(200, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            System.exit(0);
                        }
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuImage != null) {
            g.drawImage(menuImage, 0, 0, WIDTH, HEIGHT, null);
        }
    }
}