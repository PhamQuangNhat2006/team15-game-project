import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class PowerUp {
    private int x, y, width = 30, height = 30;
    private String type;
    private boolean active = true;
    private BufferedImage image;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Load ảnh từ thư mục resources
        try {
            String path = "/Resources/power_" + type + ".png";
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Không thể tải ảnh PowerUp " + type + ": " + e.getMessage());
            image = null;
        }
    }

    public void move() {
        y += 2; // rơi xuống
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (!active) return;

        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            // fallback nếu ảnh lỗi
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(type.toUpperCase(), x + 2, y + height / 2 + 4);
        }
    }

    public String getType() { return type; }
    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
}
