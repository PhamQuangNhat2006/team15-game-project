import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Paddle {
    private int x, y;
    private final int width = 100;
    private final int height = 15;
    private BufferedImage image;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(new File("resources/paddle.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh paddle: " + e.getMessage());
        }
    }

    public void setX(int newX) {
        x = Math.max(0, Math.min(newX, 600 - width));
    }

    public int getWidth() {
        return width;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, width, height);
        }
    }
}