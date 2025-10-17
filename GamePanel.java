import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private BufferedImage background;

    public GamePanel() {
        setPreferredSize(new Dimension(600, 800));
        setFocusable(true);
        addKeyListener(this);

        try {
            background = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
        }

        ball = new Ball(290, 730);
        paddle = new Paddle(250, 750);
        bricks = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                bricks.add(new Brick(70 * j + 10, 30 * i + 10));
            }
        }

        timer = new Timer(10, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        ball.draw(g);
        paddle.draw(g);
        for (Brick brick : bricks) {
            brick.draw(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
        ball.move();
        ball.checkCollision(paddle, bricks);
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) paddle.moveLeft();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) paddle.moveRight();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}