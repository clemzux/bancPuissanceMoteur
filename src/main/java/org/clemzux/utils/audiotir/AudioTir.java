package org.clemzux.utils.audiotir;

import javafx.scene.paint.Color;

import java.util.List;

public class AudioTir {

    private List<Float> audioFic;
    private String ficPath;
    private String ficName;
    private int totalFrames;
    double duration;
    private Color curveColor;

    public AudioTir(List<Float> audioFic, String ficPath, String ficName, int totalFrames, double duration) {

        this.audioFic = audioFic;
        this.ficPath = ficPath;
        this.ficName = ficName;
        this.totalFrames = totalFrames;
        this.duration = duration;
        this.curveColor = Color.BLACK;
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

    public Color getCurveColor() { return curveColor; }

    public void setCurveColor(Color curveColor) { this.curveColor = curveColor; }
}
