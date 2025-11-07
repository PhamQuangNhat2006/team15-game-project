import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {
    private HashMap<String, Clip> sounds = new HashMap<>();
    private static SoundManager instance;
    private boolean soundEnabled = true;

    private SoundManager() {
        loadSound("background", "resources/background.wav");
        loadSound("fire", "resources/fire.wav");
        loadSound("wall", "resources/wall.wav");
        loadSound("paddle", "resources/paddle.wav");
        loadSound("brick_break", "resources/brick_break.wav");
        loadSound("win", "resources/win.wav");
        loadSound("lose_life", "resources/lose_life.wav");
        loadSound("hit", "resources/hit.wav");
        loadSound("powerup", "resources/powerup.wav");
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSound(String name, String path) {
        try {
            File soundFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            sounds.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Không thể tải âm thanh: " + path + " - " + e.getMessage());
        }
    }

    public void play(String soundName) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop(String soundName) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(String soundName) {
        Clip clip = sounds.get(soundName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void stopAll() {
        for (Clip clip : sounds.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopAll();
        }
    }


    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}