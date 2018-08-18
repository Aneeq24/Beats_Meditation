package binauralbeats;

import android.media.AudioTrack;

public class PlayWave {
    private final int SAMPLE_RATE = 44100;
    int buffsize = AudioTrack.getMinBufferSize(44100, 4, 2);
    private AudioTrack mAudio = new AudioTrack(3, 44100, 4, 2, this.buffsize, 0);
    private int sampleCount;

    public void setWave(int frequency) {
        this.sampleCount = (int) (44100.0f / ((float) frequency));
        short[] samples = new short[this.sampleCount];
        double twopi = 8.0d * Math.atan(1.0d);
        double phase = 0.0d;
        for (int i = 0; i < this.sampleCount; i++) {
            samples[i] = (short) ((int) (32767.0d * Math.sin(phase)));
            phase += (((double) frequency) * twopi) / 44100.0d;
        }
        this.mAudio.write(samples, 0, this.sampleCount);
    }

    public void start() {
        this.mAudio.reloadStaticData();
        this.mAudio.setLoopPoints(0, this.sampleCount, -1);
        this.mAudio.play();
    }

    public void stop() {
        this.mAudio.stop();
    }
}
