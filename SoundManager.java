import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Clip> soundClips = new HashMap<>();
    private Clip backgroundMusic;
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;

    private SoundManager() {
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
        loadSound("paddle", "resources/sounds/paddle.wav");
        loadSound("wall", "resources/sounds/wall.wav");
        loadSound("powerup", "resources/sounds/powerup.wav");
        loadSound("lose", "resources/sounds/lose_life.wav");
        loadSound("break", "resources/sounds/brick_break.wav");
        loadSound("fire", "resources/sounds/fire.wav");
        loadSound("win", "resources/sounds/win.wav");

        // Load background music
        loadMusic("resources/sounds/background.wav");
    }

    private void loadSound(String name, String path) {
        try {
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundClips.put(name, clip);
            } else {
                System.out.println("Sound file not found: " + path);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading sound " + name + ": " + e.getMessage());
        }
    }

    private void loadMusic(String path) {
        try {
            File musicFile = new File(path);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
            } else {
                System.out.println("Music file not found: " + path);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading background music: " + e.getMessage());
        }
    }

    public void playSound(String name) {
        if (!sfxEnabled) return;

        Clip clip = soundClips.get(name);
        if (clip != null) {
            // Stop and reset to beginning
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playMusic() {
        if (backgroundMusic != null && musicEnabled) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void toggleMusic() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    public void toggleSFX() {
        sfxEnabled = !sfxEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isSFXEnabled() {
        return sfxEnabled;
    }

    public void setMusicVolume(float volume) {
        if (backgroundMusic != null) {
            FloatControl volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float value = min + (max - min) * volume;
            volumeControl.setValue(value);
        }
    }

    public void cleanup() {
        stopMusic();
        for (Clip clip : soundClips.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
    }
}