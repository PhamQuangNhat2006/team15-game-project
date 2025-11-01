import java.awt.*;

public class PowerUp {
    private int x, y, width = 30, height = 30;
    private String type;
    private boolean active = true;
    private boolean collected = false;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        SoundManager.play("powerup");
    }

    public void move() {
        y += 2;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (!active) return;

        switch (type) {
            case "expand" -> g.setColor(Color.GREEN);
            case "slow" -> g.setColor(Color.BLUE);
            case "shield" -> g.setColor(Color.ORANGE);
            default -> g.setColor(Color.GRAY);
        }

        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(type.toUpperCase(), x + 2, y + height / 2 + 4);
    }

    public void collect() {
        if (!collected) {
            SoundManager.play("powerup");
            collected = true;
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }
}
