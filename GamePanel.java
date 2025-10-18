import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private BufferedImage background;
    public static int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(600, 700));
        setFocusable(true);
        requestFocusInWindow();

        try {
            background = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image invisibleImage = toolkit.createImage(new byte[0]);
        Cursor invisibleCursor = toolkit.createCustomCursor(invisibleImage, new Point(0, 0), "InvisibleCursor");
        setCursor(invisibleCursor);

        ball = new Ball(290, 730);
        paddle = new Paddle(250, 750);
        bricks = new ArrayList<>();

        String[] colors = {"brick", "yellowbrick", "bluebrick", "greenbrick"};
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                String color = colors[(i + j) % colors.length];
                bricks.add(new Brick(70 * j + 10, 30 * i + 10, color));
            }
        }

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                paddle.setX(e.getX() - paddle.getWidth() / 2);
                repaint();
            }
        });

        timer = new Timer(10, this);
        timer.start();
    }

    public static void scoreUp() {
        score += 10;
    }

    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
            timer.stop();
        } else {
            ball.reset();
        }
    }

    @Override
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

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Điểm: " + score, 20, 40);
        g.drawString("Mạng: " + lives, 480, 40);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 180, 400);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            ball.move();
            ball.checkCollision(paddle, bricks, this);
        }
        repaint();
    }
}