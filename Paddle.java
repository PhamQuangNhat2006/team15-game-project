import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Paddle {
    private int x, y, width = 100, height = 15;
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

    public void moveLeft() {
        if (x > 0) x -= 20;
    }

    public void moveRight() {
        if (x < 600 - width) x += 20;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }
    }
}