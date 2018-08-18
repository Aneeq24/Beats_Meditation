package binauralbeats;

import android.media.AudioTrack;
import android.os.Build.VERSION;
import android.support.v4.view.InputDeviceCompat;

import com.andrognito.kerningview.KerningTextView.Kerning;

public class Isochronic implements BeatsEngine {
    private final int AMPLITUDE_INCREMENT = 256;
    private final int FADER = 128;
    private final int SAMPLE_RATE = 44100;
    private boolean doRelease;
    private int frame;
    private boolean isOn;
    private boolean isPlaying;
    private AudioTrack mAudio;
    private int multiplier;
    private int sampleCount;

    public Isochronic(float frequency, float isoBeat) {
        int amplitudeMax = Helpers.getAdjustedAmplitudeMax(frequency);
        int sCount = (int) (44100.0f / frequency);
        this.multiplier = (int) (22050.0f / isoBeat);
        this.sampleCount = this.multiplier * 2;
        this.mAudio = new AudioTrack(3, 44100, 4, 2, this.sampleCount * 2, 0);
        short[] samples = new short[this.sampleCount];
        int amplitude = 0;
        double twopi = 8.0d * Math.atan(1.0d);
        double phase = 0.0d;
        this.isOn = true;
        this.frame = 0;
        for (int i = 0; i < this.sampleCount; i++) {
            this.frame++;
            if (this.frame == this.multiplier) {
                boolean z;
                if (this.isOn) {
                    amplitude = amplitudeMax;
                } else {
                    amplitude = 0;
                }
                this.frame = 0;
                if (this.isOn) {
                    z = false;
                } else {
                    z = true;
                }
                this.isOn = z;
            } else if (this.frame <= 128) {
                if (this.isOn) {
                    amplitude = amplitude + 256 > amplitudeMax ? amplitudeMax : amplitude + 256;
                } else {
                    amplitude = amplitude + InputDeviceCompat.SOURCE_ANY < 0 ? 0 : amplitude + InputDeviceCompat.SOURCE_ANY;
                }
            }
            samples[i] = (short) ((int) (((double) amplitude) * Math.sin(phase)));
            if (i % sCount == 0) {
                phase = 0.0d;
            }
            phase += (((double) frequency) * twopi) / 44100.0d;
        }
        this.mAudio.write(samples, 0, this.sampleCount);
        if (VERSION.SDK_INT >= 21) {
            this.mAudio.setVolume(0.0f);
        } else {
            this.mAudio.setStereoVolume(0.0f, 0.0f);
        }
        Helpers.napThread();
    }

    public void release() {
        this.doRelease = true;
        stop();
    }

    public void start() {
        this.mAudio.reloadStaticData();
        this.mAudio.setLoopPoints(0, this.sampleCount, -1);
        this.isPlaying = true;
        this.mAudio.play();
        Helpers.napThread();
        if (VERSION.SDK_INT >= 21) {
            this.mAudio.setVolume(Kerning.SMALL);
        } else {
            this.mAudio.setStereoVolume(Kerning.SMALL, Kerning.SMALL);
        }
    }

    public void stop() {
        if (VERSION.SDK_INT >= 21) {
            this.mAudio.setVolume(0.0f);
        } else {
            this.mAudio.setStereoVolume(0.0f, 0.0f);
        }
        Helpers.napThread();
        this.mAudio.stop();
        this.isPlaying = false;
        if (this.doRelease) {
            this.mAudio.flush();
            this.mAudio.release();
        }
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }
}
