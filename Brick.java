import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Brick {
    private int x, y, width = 60, height = 20;
    private BufferedImage image;

    public Brick(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(new File("resources/brick_new.png")); // ảnh mới
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh brick: " + e.getMessage());
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }
}