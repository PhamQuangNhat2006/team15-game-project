import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
public class Brick {
    private int x, y;
    private final int width = 60;
    private final int height = 20;
    private int state = 0;
    private boolean destroyed = false;
    private BufferedImage[] images = new BufferedImage[5];

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Brick(int x, int y, String prefix) {
        this.x = x;
        this.y = y;

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