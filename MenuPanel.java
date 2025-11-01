import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {
    private JButton startButton;

    public MenuPanel() {
        setLayout(new GridBagLayout());
        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            SoundManager.play("button_click");
            System.out.println("Game started!");
        });
        add(startButton);
    }
}
