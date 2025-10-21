import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    public static int score = 0;

    private final int WIDTH = 600, HEIGHT = 700;
    private Timer timer;
    private int ballX = 300, ballY = 400;
    private int ballDX = 3, ballDY = -3;
    private final int ballSize = 20;

    private int paddleX = 250;
    private final int paddleY = 650;
    private final int paddleWidth = 100, paddleHeight = 15;

    private ArrayList<Rectangle> bricks;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        timer = new Timer(10, this);
        timer.start();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                paddleX = e.getX() - paddleWidth / 2;
            }
        });

        bricks = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                bricks.add(new Rectangle(60 * col + 10, 40 * row + 50, 50, 20));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ballX += ballDX;
        ballY += ballDY;

        // Va chạm với tường
        if (ballX <= 0 || ballX + ballSize >= WIDTH) ballDX *= -1;
        if (ballY <= 0) ballDY *= -1;

        // Va chạm với paddle (có quán tính)
        if (ballY + ballSize >= paddleY && ballY + ballSize <= paddleY + paddleHeight) {
            if (ballX + ballSize >= paddleX && ballX <= paddleX + paddleWidth) {
                ballDY = -Math.abs(ballDY);

                int hitPos = ballX + ballSize / 2 - paddleX;
                double relative = (double) hitPos / paddleWidth;
                ballDX = (int) ((relative - 0.5) * 10);
                if (ballDX == 0) ballDX = (Math.random() < 0.5) ? -1 : 1;
            }
        }

        // Va chạm với gạch
        for (int i = 0; i < bricks.size(); i++) {
            Rectangle brick = bricks.get(i);
            Rectangle ballRect = new Rectangle(ballX, ballY, ballSize, ballSize);
            if (ballRect.intersects(brick)) {
                bricks.remove(i);
                ballDY *= -1;
                score += 10;
                break;
            }
        }

        // Game over
        if (ballY > HEIGHT) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!\nĐiểm của bạn: " + score);
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(new MenuPanel());
            topFrame.revalidate();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ bóng
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, ballSize, ballSize);

        // Vẽ paddle
        g.setColor(Color.CYAN);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

        // Vẽ gạch
        g.setColor(Color.ORANGE);
        for (Rectangle brick : bricks) {
            g.fillRect(brick.x, brick.y, brick.width, brick.height);
        }

        // Vẽ điểm
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
    }
}