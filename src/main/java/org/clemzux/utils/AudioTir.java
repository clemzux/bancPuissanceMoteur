package org.clemzux.utils;

public class AudioTir {

    private short[] audioFic;
    private String ficPath;
    private String ficName;
    private int totalFrames;
    double duration;

    public AudioTir(short[] audioFic, String ficPath, String ficName, int totalFrames, double duration) {

        this.audioFic = audioFic;
        this.ficPath = ficPath;
        this.ficName = ficName;
        this.totalFrames = totalFrames;
        this.duration = duration;
    }

    public short[] getAudioFic() { return audioFic; }

    public String getFicPath() { return ficPath; }

    public int getTotalFrames() { return totalFrames; }

    public double getDuration() { return duration; }

    public String getFicName() { return ficName; }
}
