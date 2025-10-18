import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

public class Ball {
    private int x, y;
    private int dx = 4; // tốc độ ngang (tăng từ 2 lên 4)
    private int dy = -4; // tốc độ dọc (tăng từ -2 lên -4)
    private final int size = 20;
    private BufferedImage image;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(new File("resources/ball.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng: " + e.getMessage());
        }
    }

    public void move() {
        x += dx;
        y += dy;

        // Va chạm với mép trái/phải
        if (x <= 0 || x >= 600 - size) {
            dx *= -1;
        }

        // Va chạm với mép trên
        if (y <= 0) {
            dy *= -1;
        }
    }

    public void checkCollision(Paddle paddle, List<Brick> bricks, GamePanel panel) {
        Rectangle ballRect = new Rectangle(x, y, size, size);

        if (ballRect.intersects(paddle.getBounds())) dy *= -1;

        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            if (ballRect.intersects(brick.getBounds())) {
                brick.hit();
                if (brick.isDestroyed()) {
                    bricks.remove(brick);
                }
                dy *= -1;
                GamePanel.scoreUp();
                break;
            }
        }

        if (y >= 800) {
            panel.loseLife();
        }
    }
    public void reset() {
        x = 290;
        y = 730;
        dx = 4;
        dy = -4;
    }
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, size, size, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(x, y, size, size);
        }
    }
}