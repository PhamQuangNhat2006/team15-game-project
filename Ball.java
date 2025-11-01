import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.AlphaComposite;
import java.util.ArrayList;
import java.awt.Point;

public class Ball {
    private int x, y, size;
    private int dx, dy;
    private BufferedImage ballImage;
    private ArrayList<Point> trail = new ArrayList<>();
    private final int TRAIL_LENGTH = 10;
    private boolean fireMode = false;
    private int fireHits = 0;

    public Ball(int x, int y, int size, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.dx = dx;
        this.dy = dy;
        try {
            ballImage = ImageIO.read(new File("resources/ball.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh bóng: " + e.getMessage());
        }
    }

    public void move() {
        x += dx;
        y += dy;
        trail.add(new Point(x, y));
        if (trail.size() > TRAIL_LENGTH) trail.remove(0);
    }

    public void checkWallCollision(int width, int height) {
        if (x <= 0 || x + size >= width) {
            dx *= -1;
            SoundManager.play("hit");
        }
        if (y <= 0) {
            dy *= -1;
            SoundManager.play("hit");
        }
    }

    public void bounceFromPaddle(Paddle paddle) {
        dy = -Math.abs(dy);
        int hitPos = x + size / 2 - paddle.getX();
        double relative = (double) hitPos / paddle.getWidth();
        dx = (int) ((relative - 0.5) * 10);
        if (dx == 0) dx = (Math.random() < 0.5) ? -1 : 1;
        SoundManager.play("hit");
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color trailColor = fireMode ? Color.RED : Color.CYAN;

        for (int i = 0; i < trail.size(); i++) {
            Point p = trail.get(i);
            float alpha = (float) i / TRAIL_LENGTH;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(trailColor);
            g2.fillOval(p.x, p.y, size, size);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        if (ballImage != null) g2.drawImage(ballImage, x, y, size, size, null);
        else {
            g2.setColor(Color.WHITE);
            g2.fillOval(x, y, size, size);
        }
    }

    public void resetPosition() {
        x = 300;
        y = 400;
        dx = 7;
        dy = -8;
        fireMode = false;
        fireHits = 0;
    }

    public void setFireMode(boolean fireMode) {
        this.fireMode = fireMode;
        this.fireHits = fireMode ? 5 : 0;
    }
}
