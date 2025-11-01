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
        // Load sound effects
        loadSound("hit", "resources/sounds/hit.wav");
        loadSound("bounce", "resources/sounds/bounce.wav");
        loadSound("powerup", "resources/sounds/powerup.wav");
        loadSound("lose", "resources/sounds/lose.wav");
        loadSound("brick", "resources/sounds/brick.wav");
        loadSound("gameover", "resources/sounds/gameover.wav");

        // Load background music
        loadBackgroundMusic("resources/sounds/background.wav");
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

    private void loadBackgroundMusic(String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                System.out.println("Không tìm thấy file nhạc nền: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Lỗi khi tải nhạc nền: " + e.getMessage());
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

    public void playBackgroundMusic() {
        if (backgroundMusic != null && soundEnabled) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void toggleSound() {
        soundEnabled = !soundEnabled;
        if (!soundEnabled) {
            stopBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }
}