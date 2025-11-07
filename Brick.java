import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
public class Brick {
    private int x, y;
    private int width;
    private int height;
    private int state = 0;
    private String prefix;
    private boolean destroyed = false;
    private BufferedImage[] images = new BufferedImage[5];

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Brick(int x, int y, String prefix, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.prefix = prefix;
        try {
            for (int i = 0; i < 5; i++) {
                images[i] = ImageIO.read(new File("resources/" + prefix + "_" + i + ".png"));
            }
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh gạch: " + prefix + " - " + e.getMessage());
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void hit() {
        if (!destroyed) {
            state++;
            if (state >= images.length) {
                destroyed = true;
            }
        }
    }

    public void draw(Graphics g) {
        if (!destroyed && images[state] != null) {
            g.drawImage(images[state], x, y, width, height, null);
        }
    }
}