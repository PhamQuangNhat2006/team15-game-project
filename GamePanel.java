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
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private BufferedImage backgroundImage;
    private int score = 0;
    private int lives = 3;
    private boolean isPaused = false;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private long expandPaddleEndTime = 0;
    private long slowBallEndTime = 0;
    private long fireBallEndTime = 0;
    private void activatePowerUp(String type) {
        switch (type) {
            case "expand":
                // Sửa: Luôn tính từ kích thước gốc
                paddle.setWidth((int)(paddle.getOriginalWidth() * 1.5)); 
                expandPaddleEndTime = System.currentTimeMillis() + 10000; // Hẹn giờ 10 giây
                break;
            case "slow":
                // Sửa: Dùng hàm slowDown()
                ball.slowDown(); 
                slowBallEndTime = System.currentTimeMillis() + 10000; // Hẹn giờ 10 giây
                break;
            case "shield":
                lives++;
                break;
            case "fire":
                ball.setFireMode(true);
                fireBallEndTime = System.currentTimeMillis() + 10000; // Hẹn giờ 10 giây
                break;
        }
    }
    // THÊM HÀM MỚI NÀY: Để kiểm tra và tắt Power-up
    private void checkPowerUpTimers() {
        long currentTime = System.currentTimeMillis();

        // 1. Kiểm tra "Expand" timer
        if (expandPaddleEndTime > 0 && currentTime > expandPaddleEndTime) {
            paddle.setWidth(paddle.getOriginalWidth()); // Reset paddle
            expandPaddleEndTime = 0; // Tắt đồng hồ
        }

        // 2. Kiểm tra "Slow" timer
        if (slowBallEndTime > 0 && currentTime > slowBallEndTime) {
            ball.resetSpeed(); // Reset tốc độ bóng
            slowBallEndTime = 0; // Tắt đồng hồ
        }
        
        // 3. Kiểm tra "Fire" timer
        if (fireBallEndTime > 0 && currentTime > fireBallEndTime) {
            ball.setFireMode(false); // Tắt bóng lửa
            fireBallEndTime = 0; // Tắt đồng hồ
        }
    }

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setCursor(getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                "blank cursor"
        ));
        setBackground(Color.BLACK);
        try {

    backgroundImage = ImageIO.read(getClass().getResource("/resources/background.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
            e.printStackTrace();
        }

        ball = new Ball(300, 400, 20, 8, -9);
        paddle = new Paddle(250, 750, 100, 15);
        bricks = new ArrayList<>();

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

            @Override

            public void mouseMoved(MouseEvent e) {
                paddle.setX(e.getX() - paddle.getWidth() / 2);
            }

        });

        setFocusable(true);
        addKeyListener(new InputHandler(this));
        requestFocusInWindow();

        timer = new Timer(10, this);
        timer.start();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(!isPaused) {
            checkPowerUpTimers();
            for (PowerUp p : powerUps) {
            p.move();
            if (p.getRect().intersects(paddle.getRect()) && p.isActive()) {
                activatePowerUp(p.getType());
                p.deactivate();
            }
        }
        ball.move();
        ball.checkWallCollision(WIDTH, HEIGHT);
        if (ball.getRect().intersects(paddle.getRect())) {

            ball.bounceFromPaddle(paddle);

        }

        for (Brick brick : bricks) {

            if (!brick.isDestroyed() && ball.getRect().intersects(brick.getBounds())) {
                brick.hit();
               
                score += 10;

                if (!ball.isFireMode()) {
                     ball.reverseY();
                }
                else {
                    ball.reverseY(); // chỉ bật lại nếu không phải fireball
                }

                // 30% tạo power-up
                if (Math.random() < 0.3) {
                    String[] types = {"expand", "slow", "shield", "fire"};
                    String type = types[(int)(Math.random() * types.length)];
                    powerUps.add(new PowerUp(brick.getX(), brick.getY(), type));
                }

                break;
            }

        }

        if (ball.getY() > HEIGHT) {
            lives--;
            if (lives <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!\nĐiểm của bạn: " + score);

                SwingUtilities.invokeLater(() -> {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    if (topFrame != null) {
                        topFrame.setContentPane(new MenuPanel());
                        topFrame.revalidate();
                    } else {
                        System.err.println("Không tìm thấy JFrame cha.");
                    }
                });
            }
            else {
                ball.resetPosition();
            }

        }
    }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // gọi trước để xóa nền cũ
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        }
        ball.draw(g);
        paddle.draw(g);
        for (Brick brick : bricks) {
            brick.draw(g);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Lives: " + lives, 500, 30);
        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150)); 
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            String pauseText = "PAUSED";
            int stringWidth = g.getFontMetrics().stringWidth(pauseText);
            g.drawString(pauseText, (WIDTH - stringWidth) / 2, HEIGHT / 2);
        }
        for (PowerUp p : powerUps) {
            p.draw(g);
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