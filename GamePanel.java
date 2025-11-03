import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener {
    private final int WIDTH = 600, HEIGHT = 800;
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
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
        Ball initialBall = new Ball(300, 400, 20, 7, -8);
        balls.add(initialBall);
        initialBall.attachToPaddle(paddle);

        String[] prefixes = {"brick", "yellowbrick", "greenbrick", "bluebrick"};
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                int x = 60 * col + 10;
                int y = 40 * row + 50;
                String prefix = prefixes[row % prefixes.length];
                bricks.add(new Brick(x, y, prefix));
            }
        }

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (!isPaused) {
                    int panelWidth = getWidth();
                    int newX = Math.max(0, Math.min(e.getX() - paddle.getWidth() / 2, panelWidth - paddle.getWidth()));
                    paddle.setX(newX);

                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (ballAttached) {
                    ballAttached = false;
                }
            }
        });

        addKeyListener(new InputHandler(this));
        timer = new Timer(10, this);
        timer.start();
    }

    private void activatePowerUp(String type) {
        switch (type) {
            case "expand":
                if (paddle.getWidth() < MAX_PADDLE_WIDTH) {
                    paddle.setWidth(paddle.getWidth() + 50);
                }
                break;
            case "slow":
                for (Ball b : balls) {
                    b.setSpeed(b.getDx() / 2, b.getDy() / 2);
                }
                slowStartTime = System.currentTimeMillis();
                isSlowed = true;
                break;
            case "shield":
                if (lives < 6) {
                    lives++;
                }
                break;

            case "fire":
                for (Ball b : balls) {
                    b.setFireMode(true);
                }
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

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            for (Ball b : balls) {

                if (ballAttached) {
                    b.attachToPaddle(paddle);
                } else {
                    b.move();
                    b.checkWallCollision(panelWidth, panelHeight);
                }

                if (b.getRect().intersects(paddle.getRect()) && !ballAttached) {
                    b.bounceFromPaddle(paddle);
                }

                for (Brick brick : bricks) {
                    if (!brick.isDestroyed() && b.getRect().intersects(brick.getBounds())) {
                        brick.hit();
                        score += 10;

                        if (b.isFireMode()) {
                            b.consumeFireHit();
                        } else {
                            b.reverseY();
                        }

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
                        b.setSpeed(7, -8);
                    }
                    isSlowed = false;
                }
            }

            ArrayList<Ball> toRemove = new ArrayList<>();
            for (Ball b : balls) {
                if (b.getY() > panelHeight) {
                    toRemove.add(b);
                }
            }
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
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
        }

        for (Ball b : balls) {
            b.draw(g);
        }

        paddle.draw(g);

        for (Brick brick : bricks) {
            brick.draw(g);
        }

        for (PowerUp p : powerUps) {
            p.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, panelWidth - 100, 30); // căn phải

        for (Ball b : balls) {
            if (b.isFireMode()) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("FIREBALL: " + b.getFireHitsRemaining(), panelWidth / 2 - 50, 30);
                break;
            }
        }

        if (isSlowed) {
            long elapsed = System.currentTimeMillis() - slowStartTime;
            int maxTime = 7000;
            if (elapsed < maxTime) {
                int barWidth = 200;
                int barHeight = 10;
                int filled = (int) ((elapsed / (float) maxTime) * barWidth);
                filled = Math.min(filled, barWidth);

                int barX = (panelWidth - barWidth) / 2;
                int barY = 60;

                g.setColor(Color.DARK_GRAY);
                g.fillRect(barX, barY, barWidth, barHeight);

                g.setColor(Color.BLUE);
                g.fillRect(barX, barY, filled, barHeight);

                g.setColor(Color.WHITE);
                g.drawRect(barX, barY, barWidth, barHeight);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString("SLOW MODE", barX + barWidth / 2 - 30, barY - 5);
            }
        }

        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, panelWidth, panelHeight);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            String pauseText = "PAUSED";
            int stringWidth = g.getFontMetrics().stringWidth(pauseText);
            g.drawString(pauseText, (panelWidth - stringWidth) / 2, panelHeight / 2);
        }
    }

    public void togglePause() {
        this.isPaused = !this.isPaused;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
}