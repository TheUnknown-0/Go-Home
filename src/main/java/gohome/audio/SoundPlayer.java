package gohome.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
    private static Clip coinClip, moveClip, winClip;

    private static Clip loadClip(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) return null;
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to load sound: " + path + " -> " + e.getMessage());
            return null;
        }
    }

    public static void playCoinFlip() {
        if (!gohome.settings.Settings.get().isSoundEnabled()) return;
        if (coinClip == null) coinClip = loadClip("assets/coin_flip.wav");
        if (coinClip == null) return;
        playOnce(coinClip);
    }

    public static void playMove() {
        if (!gohome.settings.Settings.get().isSoundEnabled()) return;
        if (moveClip == null) moveClip = loadClip("assets/move.wav");
        if (moveClip == null) return;
        playOnce(moveClip);
    }

    public static void playWin() {
        if (!gohome.settings.Settings.get().isSoundEnabled()) return;
        if (winClip == null) winClip = loadClip("assets/win.wav");
        if (winClip == null) return;
        playOnce(winClip);
    }

    private static void playOnce(Clip c) {
        if (c == null) return;
        try {
            if (c.isRunning()) c.stop();
            FloatControl gain = null;
            try {
                gain = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (IllegalArgumentException ignore) {
                // some platforms may not support gain control
            }
            if (gain != null) {
                float vol = gohome.settings.Settings.get().getVolume();
                float dB;
                if (vol <= 0f) dB = gain.getMinimum();
                else dB = (float) (20.0 * Math.log10(vol));
                // clamp
                dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
                gain.setValue(dB);
            }
            c.setFramePosition(0);
            c.start();
        } catch (Exception e) {
            System.err.println("Error playing clip: " + e.getMessage());
        }
    }
}
