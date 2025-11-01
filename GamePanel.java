import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private ArrayList<PowerUp> powerUps;

    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private boolean gameWin = false;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        ball = new Ball(300, 400, 20, 6, -6);
        paddle = new Paddle(350, 550, 100, 15);
        powerUps = new ArrayList<>();

        createBricks();

        timer = new Timer(16, this);
        timer.start();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) paddle.moveLeft();
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) paddle.moveRight();
            }
        });
    }

    private void createBricks() {
        bricks = new ArrayList<>();
        int startX = 60, startY = 60;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                bricks.add(new Brick(startX + col * 65, startY + row * 25, "brick"));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !gameWin) {
            ball.move();
            ball.checkWallCollision(getWidth(), getHeight());
            checkCollisions();
            updatePowerUps();
        }
        repaint();
    }

    private void checkCollisions() {
        // 🔹 Bóng chạm paddle
        if (ball.getRect().intersects(paddle.getBounds())) {
            ball.bounceFromPaddle(paddle);
        }

        // 🔹 Bóng chạm gạch
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getRect().intersects(brick.getBounds())) {
                brick.hit();
                ball.reverseY();
                score += 10;
                if (Math.random() < 0.15) { // 15% spawn power-up
                    powerUps.add(new PowerUp(brick.getX(), brick.getY(), "powerup"));
                }
                break;
            }
        }

        // 🔹 Bóng rơi xuống đáy
        if (ball.getY() > getHeight()) {
            lives--;
            SoundManager.play("lose_life");
            if (lives <= 0) {
                gameOver = true;
            } else {
                ball.resetPosition();
            }
        }

        // 🔹 Kiểm tra thắng
        if (bricks.stream().allMatch(Brick::isDestroyed)) {
            gameWin = true;
            SoundManager.play("win");
        }

        // 🔹 Paddle bắt được PowerUp
        for (PowerUp p : powerUps) {
            if (p.isActive() && p.getRect().intersects(paddle.getBounds())) {
                p.collect();
                SoundManager.play("powerup");
                score += 50;
            }
        }
    }

    private void updatePowerUps() {
        for (PowerUp p : powerUps) {
            if (p.isActive()) {
                p.move();
                if (p.getRect().y > getHeight()) {
                    p.deactivate();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // vẽ bóng, paddle, gạch, powerup
        ball.draw(g);
        paddle.draw(g);
        for (Brick brick : bricks) brick.draw(g);
        for (PowerUp p : powerUps) p.draw(g);

        // UI
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 20, 20);
        g.drawString("Lives: " + lives, getWidth() - 100, 20);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);
        } else if (gameWin) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.GREEN);
            g.drawString("YOU WIN!", getWidth() / 2 - 100, getHeight() / 2);
        }
    }
}
