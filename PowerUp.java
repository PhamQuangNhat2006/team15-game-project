import java.awt.*;

public class PowerUp {
    private int x, y, width = 30, height = 30;
    private String type;
    private boolean active = true;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void move() {
        y += 2; // rơi xuống
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (!active) return;

        switch (type) {
            case "expand":
                g.setColor(Color.GREEN);
                break;
            case "slow":
                g.setColor(Color.BLUE);
                break;
            case "shield":
                g.setColor(Color.ORANGE);
                break;
            default:
                g.setColor(Color.GRAY);
        }

        g.fillRect(x, y, width, height);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(type.toUpperCase(), x + 2, y + height / 2 + 4);
    }

    public String getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}