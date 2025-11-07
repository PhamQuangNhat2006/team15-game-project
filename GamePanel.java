import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener {
    private final int BASE_WIDTH = 600, BASE_HEIGHT = 800;
    private Timer timer;
    private ArrayList<Ball> balls = new ArrayList<>();
    private Paddle paddle;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private BufferedImage backgroundImage;
    private final int MAX_PADDLE_WIDTH = 200;
    private int score = 0;
    private int lives = 3;
    private boolean isPaused = false;
    private boolean ballAttached = true;
    private long slowStartTime = 0;
    private boolean isSlowed = false;

    public GamePanel() {
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setBackground(Color.BLACK);
        setCursor(getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                "blank cursor"
        ));
        setFocusable(true);
        requestFocusInWindow();
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/resources/background.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
        }

        paddle = new Paddle(250, 750, 100, 15);
        Ball initialBall = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10, paddle.getY() - 20, 20, 7, -8);
        initialBall.attachToPaddle(paddle);
        balls.add(initialBall);

        generateBricks();

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (!isPaused) {
                    double scaleX = getWidth() / (double) BASE_WIDTH;
                    int scaledWidth = (int) (paddle.getWidth() * scaleX);
                    int newX = (int) ((e.getX() / scaleX) - scaledWidth / 2);
                    newX = Math.max(0, Math.min(newX, BASE_WIDTH - paddle.getWidth()));
                    paddle.setX(newX);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (ballAttached) ballAttached = false;
            }
        });

        addKeyListener(new InputHandler(this));
        timer = new Timer(10, this);
        timer.start();
    }

    private void generateBricks() {
        List<Boolean> destroyedStates = new ArrayList<>();
        for (Brick b : bricks) destroyedStates.add(b.isDestroyed());

        bricks.clear();
        int brickCols = 10, brickRows = 5;
        int brickWidth = BASE_WIDTH / brickCols;
        int brickHeight = 30;
        String[] prefixes = {"brick", "yellowbrick", "greenbrick", "bluebrick"};
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < brickCols; col++) {
                int x = col * brickWidth;
                int y = row * brickHeight + 50;
                String prefix = prefixes[row % prefixes.length];
                bricks.add(new Brick(x, y, prefix, brickWidth, brickHeight));
            }
        }

        for (int i = 0; i < bricks.size(); i++) {
            if (i < destroyedStates.size() && destroyedStates.get(i)) {
                bricks.get(i).hit();
            }
        }
    }

    private void activatePowerUp(String type) {
        switch (type) {
            case "expand":
                if (paddle.getWidth() < MAX_PADDLE_WIDTH) {
                    paddle.setWidth(paddle.getWidth() + 50);
                }
                break;
            case "slow":
                for (Ball b : balls) b.setSpeed(b.getDx() / 2, b.getDy() / 2);
                slowStartTime = System.currentTimeMillis();
                isSlowed = true;
                break;
            case "shield":
                if (lives < 6) lives++;
                break;
            case "fire":
                for (Ball b : balls) b.setFireMode(true);
                break;
            case "multi":
                if (!balls.isEmpty()) {
                    Ball source = balls.get(0);
                    Ball clone = new Ball(source.getRect().x, source.getRect().y, 20, -source.getDx(), source.getDy());
                    balls.add(clone);
                }
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            for (PowerUp p : powerUps) {
                p.move();
                if (p.getRect().intersects(paddle.getRect()) && p.isActive()) {
                    activatePowerUp(p.getType());
                    p.deactivate();
                }
            }

            for (Ball b : balls) {
                if (ballAttached) {
                    b.attachToPaddle(paddle);
                } else {
                    b.move();
                    b.checkWallCollision(BASE_WIDTH, BASE_HEIGHT);
                }

                if (b.getRect().intersects(paddle.getRect()) && !ballAttached) {
                    b.bounceFromPaddle(paddle);
                }

                for (Brick brick : bricks) {
                    if (!brick.isDestroyed() && b.getRect().intersects(brick.getBounds())) {
                        brick.hit();
                        score += 10;
                        if (b.isFireMode()) b.consumeFireHit();
                        else b.reverseY();

                        if (Math.random() < 0.1) {
                            ArrayList<String> availableTypes = new ArrayList<>();
                            if (paddle.getWidth() < MAX_PADDLE_WIDTH) availableTypes.add("expand");
                            if (lives < 6) availableTypes.add("shield");
                            availableTypes.add("slow");
                            availableTypes.add("fire");
                            availableTypes.add("multi");

                            String type = availableTypes.get((int) (Math.random() * availableTypes.size()));
                            powerUps.add(new PowerUp(brick.getX(), brick.getY(), type));
                        }
                        break;
                    }
                }
            }

            if (isSlowed) {
                long elapsed = System.currentTimeMillis() - slowStartTime;
                if (elapsed >= 7000) {
                    for (Ball b : balls) {
                        int dirX = b.getDx() >= 0 ? 1 : -1;
                        int dirY = b.getDy() >= 0 ? 1 : -1;
                        b.setSpeed(dirX * b.getBaseSpeedX(), dirY * b.getBaseSpeedY());
                    }
                    isSlowed = false;
                }
            }

            ArrayList<Ball> toRemove = new ArrayList<>();
            for (Ball b : balls)
                if (!ballAttached && b.getY() > BASE_HEIGHT) toRemove.add(b);
            balls.removeAll(toRemove);

            if (balls.isEmpty()) {
                lives--;
                if (lives <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over!\nĐiểm của bạn: " + score);
                    SwingUtilities.invokeLater(() -> {
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        if (topFrame != null) {
                            topFrame.setContentPane(new MenuPanel());
                            topFrame.revalidate();
                        }
                    });
                } else {
                    Ball newBall = new Ball(300, 400, 20, 7, -8);
                    balls.add(newBall);
                    ballAttached = true;
                    newBall.attachToPaddle(paddle);
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double scaleX = getWidth() / (double) BASE_WIDTH;
        double scaleY = getHeight() / (double) BASE_HEIGHT;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.scale(scaleX, scaleY);

        // Nền
        if (backgroundImage != null)
            g2d.drawImage(backgroundImage, 0, 0, BASE_WIDTH, BASE_HEIGHT, null);

        // Vẽ game objects
        for (Brick brick : bricks) brick.draw(g2d);
        for (PowerUp p : powerUps) p.draw(g2d);
        for (Ball b : balls) b.draw(g2d);
        paddle.draw(g2d);

        // HUD
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 20, 30);
        g2d.drawString("Lives: " + lives, BASE_WIDTH - 100, 30);

        // === SLOW MODE progress bar ===
        if (isSlowed) {
            long elapsed = System.currentTimeMillis() - slowStartTime;
            int maxTime = 7000;
            if (elapsed < maxTime) {
                int barWidth = 200;
                int barHeight = 10;
                int filled = (int) ((elapsed / (float) maxTime) * barWidth);
                filled = Math.min(filled, barWidth);

                int barX = (BASE_WIDTH - barWidth) / 2;
                int barY = 60;

                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(barX, barY, barWidth, barHeight);

                g2d.setColor(Color.BLUE);
                g2d.fillRect(barX, barY, filled, barHeight);

                g2d.setColor(Color.WHITE);
                g2d.drawRect(barX, barY, barWidth, barHeight);
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.drawString("SLOW MODE", barX + barWidth / 2 - 30, barY - 5);
            }
        }

        // PAUSE overlay
        if (isPaused) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            String pauseText = "PAUSED";
            int stringWidth = g2d.getFontMetrics().stringWidth(pauseText);
            g2d.drawString(pauseText, (BASE_WIDTH - stringWidth) / 2, BASE_HEIGHT / 2);
        }

        g2d.dispose();
    }


    public void togglePause() {
        this.isPaused = !this.isPaused;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
        if (bricks.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                generateBricks();
                if (!balls.isEmpty()) balls.get(0).attachToPaddle(paddle);
            });
        }
    }
}
