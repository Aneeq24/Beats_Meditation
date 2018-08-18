package binauralbeats;

import android.util.Log;

public class Helpers {
    private static final int AMPLITUDE_MAX = 32767;

    public static int getAdjustedAmplitudeMax(float frequency) {
        float amplitudeScale = 100.0f / frequency;
        if (frequency <= 100.0f) {
            return AMPLITUDE_MAX;
        }
        return (int) (32767.0f * amplitudeScale);
    }

    public static int getLCM(int a, int b) {
        int x;
        int y;
        if (a < b) {
            x = a;
            y = b;
        } else {
            x = b;
            y = a;
        }
        int i = 1;
        while (true) {
            int x1 = x * i;
            int y1 = y * i;
            for (int j = 1; j <= i; j++) {
                if (x1 == y * j) {
                    Log.d("Binaural", "iteracciones:" + i);
                    return x1;
                }
            }
            i++;
        }
    }

    public static int LCM(int a, int b) {
        return (a * b) / GCF(a, b);
    }

    public static int GCF(int a, int b) {
        return b == 0 ? a : GCF(b, a % b);
    }

    public static void napThread() {
        try {
            Thread.currentThread();
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
