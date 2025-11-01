import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Clip> sounds;
    private Clip backgroundMusic;
    private boolean soundEnabled = true;

    private SoundManager() {
        sounds = new HashMap<>();
        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {
        // Load sound effects matching your files
        loadSound("brick_break", "resources/brick_break.wav");
        loadSound("win", "resources/win.wav");
        loadSound("lose_life", "resources/lose_life.wav");
        loadSound("hit", "resources/hit.wav");
        loadSound("powerup", "resources/powerup.wav");
        loadSound("button_click", "resources/button_click.wav");
    }

    private void loadSound(String name, String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                System.out.println("Không tìm thấy file âm thanh: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            sounds.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Lỗi khi tải âm thanh " + name + ": " + e.getMessage());
        }
    }

    public void playSound(String name) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void toggleSound() {
        soundEnabled = !soundEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void stopAllSounds() {
        for (Clip clip : sounds.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }
}