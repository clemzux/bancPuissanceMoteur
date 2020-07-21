package org.clemzux.utils;

import java.util.List;

public class AudioTir {

    private List<Float> audioFic;
    private String ficPath;
    private String ficName;
    private int totalFrames;
    double duration;

    public AudioTir(List<Float> audioFic, String ficPath, String ficName, int totalFrames, double duration) {

        this.audioFic = audioFic;
        this.ficPath = ficPath;
        this.ficName = ficName;
        this.totalFrames = totalFrames;
        this.duration = duration;
    }

    public int calculateRoundPerMinutes() {

        int roundPerMinutes = 0;

        return roundPerMinutes;
    }

    public List<Float> getAudioFic() { return audioFic; }

    public String getFicPath() { return ficPath; }

    public int getTotalFrames() { return totalFrames; }

    public double getDuration() { return duration; }

    public String getFicName() { return ficName; }
}
