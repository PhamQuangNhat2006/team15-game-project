import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {
    private static final HashMap<String, String> soundMap = new HashMap<>();

    static {
        soundMap.put("hit", "/resources/hit.wav");
        soundMap.put("brick_break", "/resources/brick_break.wav");
        soundMap.put("lose_life", "/resources/lose_life.wav");
        soundMap.put("powerup", "/resources/powerup.wav");
        soundMap.put("button_click", "/resources/button_click.wav");
        soundMap.put("win", "/resources/win.wav");
    }

    public static void play(String name) {
        String path = soundMap.get(name);
        if (path == null) {
            System.out.println("Âm thanh không tồn tại: " + name);
            return;
        }

        try {
            URL soundURL = SoundManager.class.getResource(path);
            if (soundURL == null) {
                System.out.println("Không tìm thấy file âm thanh: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Lỗi phát âm thanh: " + e.getMessage());
        }
    }
}
