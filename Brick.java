import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Brick {
    private int x, y;
    private final int width = 60, height = 20;
    private int state = 0;
    private boolean destroyed = false;
    private BufferedImage[] images = new BufferedImage[3];

    public Brick(int x, int y, String prefix) {
        this.x = x;
        this.y = y;

        try {
            for (int i = 0; i < images.length; i++) {
                images[i] = ImageIO.read(new File("resources/" + prefix + "_" + i + ".png"));
            }
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh gạch: " + e.getMessage());
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
            SoundManager.play("hit");
            if (state >= images.length) {
                destroyed = true;
                SoundManager.play("brick_break");
            }
        }
    }

    public void draw(Graphics g) {
        if (!destroyed && images[state] != null) {
            g.drawImage(images[state], x, y, width, height, null);
        }
    }
}
