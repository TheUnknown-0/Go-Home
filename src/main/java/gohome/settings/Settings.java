package gohome.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static final String DIR = System.getProperty("user.home") + File.separator + ".gohome";
    private static final String FILE = DIR + File.separator + "settings.properties";
    private static final Settings INSTANCE = new Settings();

    private boolean soundEnabled = true;
    private float volume = 1.0f; // 0..1

    private Settings() {
        load();
    }

    public static Settings get() { return INSTANCE; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean enabled) { this.soundEnabled = enabled; save(); }

    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = Math.max(0f, Math.min(1f, volume)); save(); }

    public void load() {
        Properties p = new Properties();
        File dir = new File(DIR);
        if (!dir.exists()) dir.mkdirs();
        File f = new File(FILE);
        if (!f.exists()) return;
        try (FileInputStream in = new FileInputStream(f)) {
            p.load(in);
            this.soundEnabled = Boolean.parseBoolean(p.getProperty("sound.enabled", "true"));
            this.volume = Float.parseFloat(p.getProperty("sound.volume", "1.0"));
        } catch (Exception e) {
            // ignore and keep defaults
        }
    }

    public void save() {
        Properties p = new Properties();
        p.setProperty("sound.enabled", Boolean.toString(soundEnabled));
        p.setProperty("sound.volume", Float.toString(volume));
        try (FileOutputStream out = new FileOutputStream(FILE)) {
            p.store(out, "GoHome settings");
        } catch (IOException e) {
            // ignore
        }
    }
}
