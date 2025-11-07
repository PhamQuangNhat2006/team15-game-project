import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Brick {
    private int x, y;
    private int width, height;
    private int state = 0;
    private String prefix;
    private boolean destroyed = false;
    private BufferedImage[] images = new BufferedImage[5];

    // --- Constructor chính (5 tham số) ---
    public Brick(int x, int y, String prefix, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.prefix = prefix;

        // Tải ảnh cho các trạng thái của gạch
        try {
            for (int i = 0; i < images.length; i++) {
                File file = new File("resources/" + prefix + "_" + i + ".png");
                if (file.exists()) {
                    images[i] = ImageIO.read(file);
                } else {
                    System.out.println("Không tìm thấy file: " + file.getPath());
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh gạch: " + prefix + " - " + e.getMessage());
        }
    }

    // --- Constructor rút gọn (3 tham số, dùng mặc định 50x30) ---
    public Brick(int x, int y, String prefix) {
        this(x, y, prefix, 50, 30);
    }

    // --- Getter ---
    public int getX() { return x; }
    public int getY() { return y; }

    // --- Lấy vùng va chạm ---
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // --- Trạng thái gạch ---
    public boolean isDestroyed() { return destroyed; }

    // --- Khi bóng chạm vào gạch ---
    public void hit() {
        if (!destroyed) {
            state++;
            if (state >= images.length) {
                destroyed = true;
            }
        }
    }

    // --- Vẽ gạch ---
    public void draw(Graphics g) {
        if (!destroyed && images[state] != null) {
            g.drawImage(images[state], x, y, width, height, null);
        }
    }

    // --- Di chuyển gạch (nếu cần, như power-up đặc biệt) ---
    public void move(int dx, int panelWidth) {
        x += dx;
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;
    }
}
