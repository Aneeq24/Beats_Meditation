package binauralbeats;

import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import com.andrognito.kerningview.KerningTextView;


public class Binaural extends AsyncTask implements BeatsEngine {
    private final int SAMPLE_RATE = 44100;
    private boolean doRelease;
    private float frequency;
    private boolean isPlaying;
    private float isoBeat;
    private AudioTrack mAudio;
    private int sampleCount;
    private float volume;

    public Binaural(float frequency, float isoBeat, float volume) {
        this.frequency = frequency;
        this.isoBeat = isoBeat;
        this.volume = volume;
    }

    public void release() {
        this.doRelease = true;
        stop();
    }

    public void start() {
        this.execute();
    }

    public void stop() {
        if (this.mAudio != null) {
            this.mAudio.setStereoVolume(0.0f, 0.0f);
            Helpers.napThread();
            try {
                this.mAudio.stop();
                this.onCancelled();
            } catch (IllegalStateException e) {
                Log.d("Binaural", "error stopping");
            }
            this.isPlaying = false;
            if (this.doRelease) {
                this.mAudio.flush();
                this.mAudio.release();
            }
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (this.mAudio != null) {
            this.mAudio.setStereoVolume(volume, volume);
        }
    }

    public float getVolume() {
        return this.volume;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    protected Object doInBackground(Object[] objects) {
        int amplitudeMax = Helpers.getAdjustedAmplitudeMax(this.frequency);
        float freqLeft = this.frequency - (this.isoBeat / 2.0f);
        float freqRight = this.frequency + (this.isoBeat / 2.0f);
        this.volume = this.volume;
        if (freqLeft < KerningTextView.Kerning.SMALL) {
            freqLeft = KerningTextView.Kerning.SMALL;
        }
        int sCountLeft = (int) (44100.0f / freqLeft);
        int sCountRight = (int) (44100.0f / freqRight);
        this.sampleCount = Helpers.LCM(sCountLeft, sCountRight) * 2;
        this.mAudio = new AudioTrack(3, 44100, 12, 2, this.sampleCount * 4, 0);
        short[] samples = new short[this.sampleCount];
        int amplitude = amplitudeMax;
        double twopi = 8.0d * Math.atan(1.0d);
        double leftPhase = 0.0d;
        double rightPhase = 0.0d;
        for (int i = 0; i < this.sampleCount; i += 2) {
            samples[i] = (short) ((int) (((double) amplitude) * Math.sin(leftPhase)));
            samples[i + 1] = (short) ((int) (((double) amplitude) * Math.sin(rightPhase)));
            if ((i / 2) % sCountLeft == 0) {
                leftPhase = 0.0d;
            }
            leftPhase += (((double) freqLeft) * twopi) / 44100.0d;
            if ((i / 2) % sCountRight == 0) {
                rightPhase = 0.0d;
            }
            rightPhase += (((double) freqRight) * twopi) / 44100.0d;
        }
        this.mAudio.write(samples, 0, this.sampleCount);
        this.mAudio.setStereoVolume(0.0f, 0.0f);
        this.mAudio.reloadStaticData();
        this.mAudio.setLoopPoints(0, this.sampleCount / 2, -1);
        this.isPlaying = true;
        if (this.mAudio.getState() == AudioTrack.STATE_INITIALIZED)
            this.mAudio.play();
        Helpers.napThread();
        this.mAudio.setStereoVolume(this.volume, this.volume);
        return null;
    }
}
