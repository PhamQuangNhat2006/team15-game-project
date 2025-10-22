import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
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


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setCursor(getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                "blank cursor"
        ));
        setBackground(Color.BLACK);
        try {
            backgroundImage = ImageIO.read(new File("resources/background.png")); // đổi tên nếu cần
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
        }

        ball = new Ball(300, 400, 20, 5, -6);
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

        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.move();
        ball.checkWallCollision(WIDTH, HEIGHT);

        if (ball.getRect().intersects(paddle.getRect())) {
            ball.bounceFromPaddle(paddle);
        }

        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getRect().intersects(brick.getBounds())) {
                brick.hit();
                ball.reverseY();
                score += 10;
                break;
            }
        }

        if (ball.getY() > HEIGHT) {
            lives--;
            if (lives <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!\nĐiểm của bạn: " + score);
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.setContentPane(new MenuPanel());
                topFrame.revalidate();
            } else {
                ball.resetPosition();
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
    }
}