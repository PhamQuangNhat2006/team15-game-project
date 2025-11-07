import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SoundManager {
    private HashMap<String, Clip> sounds = new HashMap<>();
    private static SoundManager instance;
    private boolean soundEnabled = true;
    private Clip backgroundMusic;

    private SoundManager() {
        loadAllSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadAllSounds() {
        // Tải tất cả các âm thanh từ thư mục Resources/sound
        loadSound("background", "Resources/sound/background.wav");
        loadSound("fire", "Resources/sound/fire.wav");
        loadSound("wall", "Resources/sound/wall.wav");
        loadSound("paddle", "Resources/sound/paddle.wav");
        loadSound("brick_break", "Resources/sound/brick_break.wav");
        loadSound("win", "Resources/sound/win.wav");
        loadSound("lose_life", "Resources/sound/lose_life.wav");
        loadSound("hit", "Resources/sound/hit.wav");
        loadSound("powerup", "Resources/sound/powerup.wav");

        // Lưu reference đến nhạc nền
        backgroundMusic = sounds.get("background");
    }

    private void loadSound(String name, String path) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                System.out.println("Không tìm thấy file âm thanh: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            sounds.put(name, clip);

            System.out.println("Đã tải âm thanh: " + name);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Định dạng file không được hỗ trợ: " + path);
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + path + " - " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("Không thể phát âm thanh: " + path);
        }
    }

    /**
     * Phát một âm thanh một lần
     */
    public void play(String soundName) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(soundName);
        if (clip != null) {
            // Dừng và reset về đầu nếu đang phát
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * Phát âm thanh lặp lại liên tục (dùng cho nhạc nền)
     */
    public void loop(String soundName) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Dừng một âm thanh cụ thể
     */
    public void stop(String soundName) {
        Clip clip = sounds.get(soundName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    /**
     * Dừng tất cả âm thanh
     */
    public void stopAll() {
        for (Clip clip : sounds.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }

    /**
     * Bật/tắt âm thanh
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;

        if (!enabled) {
            stopAll();
        } else {
            // Khi bật lại âm thanh, phát lại nhạc nền
            if (backgroundMusic != null) {
                loop("background");
            }
        }
    }

    /**
     * Kiểm tra âm thanh có đang bật không
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Tạm dừng nhạc nền
     */
    public void pauseBackground() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    /**
     * Tiếp tục phát nhạc nền
     */
    public void resumeBackground() {
        if (soundEnabled && backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Điều chỉnh âm lượng (nếu hỗ trợ)
     */
    public void setVolume(String soundName, float volume) {
        Clip clip = sounds.get(soundName);
        if (clip != null) {
            try {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Volume từ 0.0 đến 1.0
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                volumeControl.setValue(dB);
            } catch (IllegalArgumentException e) {
                System.out.println("Không thể điều chỉnh âm lượng cho: " + soundName);
            }
        }
    }

    /**
     * Giải phóng tài nguyên
     */
    public void cleanup() {
        for (Clip clip : sounds.values()) {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
        sounds.clear();
    }
}