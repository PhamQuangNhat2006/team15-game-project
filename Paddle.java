import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Paddle {
    private int x, y, width, height;
    private BufferedImage paddleImage;

    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            paddleImage = ImageIO.read(new File("resources/paddle.png")); // đổi tên nếu cần
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh paddle: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;}

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        if (paddleImage != null) {
            g.drawImage(paddleImage, x, y, width, height, null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(x, y, width, height);
        }
    }
    public void move(int dx, int panelWidth) {
        x += dx;
        //giới hạn paddle
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;
    }

}