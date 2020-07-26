package org.clemzux.utils.audiotir;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioTir {

    private List<Float> audioFicSpectrum;
    private List<Float> roundPerFrameVariation;
    // il y a 4 mesures par secondes
    private List<Integer> roundPerFrame;
    private List<Float> kiloWattsPerFrameCurve;
    private String ficPath;
    private String ficName;
    private int totalFrames;
    private float duration;
    private float inertiaMoment;
    private Color curveColor;

    public static Map<Color, String> audioTirColor;

    static {
        audioTirColor = new HashMap<>();
        audioTirColor.put(Color.BLUE, null);
        audioTirColor.put(Color.RED, null);
        audioTirColor.put(Color.GREEN, null);
        audioTirColor.put(Color.GOLD, null);
        audioTirColor.put(Color.VIOLET, null);
        audioTirColor.put(Color.ORANGE, null);
        audioTirColor.put(Color.MAROON, null);
    }

    public AudioTir(List<Float> audioFic, String ficPath, String ficName, int totalFrames, double duration, float inertiaMoment) {

        this.audioFicSpectrum = audioFic;
        this.ficPath = ficPath;
        this.ficName = ficName;
        this.totalFrames = totalFrames;
        this.duration = (float) duration;
        this.inertiaMoment = inertiaMoment;

        this.curveColor = determineColorAvailable(ficPath);

        calculateNewtonMetersCurve();
        calculateKilosWattsCurve();
    }

    private void calculateKilosWattsCurve() {

        kiloWattsPerFrameCurve = new ArrayList<>();

        // 2 * pi radians (1 tpm)
        float twoPiRad = (float) 2.68;
        float wattValue;

        for (int rpmInOneFrame : roundPerFrame) {

            wattValue = rpmInOneFrame * twoPiRad;
            // meme si on mesure a une precision de 4 par secondes, on multiplie par 4 pour
            // avoir la valeur par seconde a l'instant t
            kiloWattsPerFrameCurve.add(wattValue * 4);
//            System.out.println(wattValue*4);
        }

        kiloWattsPerFrameCurve = flatCruve(kiloWattsPerFrameCurve);
    }

    // cette fonction sert a calculer la courbe des newtons metres avec une precision
    // de 10 points par secondes
    private void calculateNewtonMetersCurve() {

        int spectrumLength = audioFicSpectrum.size();
        int nbCasesBy4mSec = (int) (spectrumLength / duration);
        nbCasesBy4mSec /= 4;

        roundPerFrameVariation = new ArrayList<>();
        roundPerFrame = new ArrayList<>();

        int index = 0;
        int index2 = 0;
        int nbRound = 0;
        int lastNbRound = 0;

        // cette boucle sert a calculer le nombre d'explosions tout les 4emes de secondes
        while (index < spectrumLength) {

            if (audioFicSpectrum.get(index) != 0) {

                nbRound++;
            }

            if (index2 == nbCasesBy4mSec) {

//                int variation = (int) Math.sqrt((nbRound - lastNbRound) * (nbRound - lastNbRound));
//                spectrumVariations.add((float) (nbRound - lastNbRound));
                roundPerFrameVariation.add((float) (nbRound));

                // on garde le nombre de tours par quart de sec
                roundPerFrame.add(nbRound);
//                System.out.println(nbRound);
//                System.out.println(lastNbRound);
//                System.out.println(nbRound - lastNbRound);
//                System.out.println("\n");
//                System.out.println(nbRound);
                lastNbRound = nbRound;
                nbRound = 0;
                index2 = 0;
            }

            index++;
            index2++;
        }

        // on lisse les tours car ils ne sont pas tres reguliers

        roundPerFrameVariation = flatCruve(roundPerFrameVariation);
        roundPerFrameVariation = flatCruve(roundPerFrameVariation);

        // on lisse les tours car ils ne sont pas tres reguliers

        index = 0;

        // on applique le coeficient aux variations trouvees
        while (index < roundPerFrameVariation.size()) {

            roundPerFrameVariation.set(index, roundPerFrameVariation.get(index) * inertiaMoment);
//            System.out.println(spectrumVariations.get(index) * inertiaMoment);
            index++;
        }

//        for (float round : spectrumVariations)
//            System.out.println(round);
    }

    private List<Float> flatCruve(List<Float> curve) {

        List<Float> curveFlatted = new ArrayList<>();

        int index;

        for (index = 1; index < curve.size() - 1; index++) {

            float lastVar = curve.get(index - 1);
            float currentVar = curve.get(index);
            float nextVar = curve.get(index + 1);

            curveFlatted.add((lastVar + currentVar + nextVar) / 3);
        }

        return curveFlatted;
    }

    // cette fonction sert a indiquer a la map que la couleur est maintenant disponible
    public void setColorAvailable(String ficPath) {

        for (Color color : audioTirColor.keySet()) {

            if (audioTirColor.get(color) == ficPath) {

                audioTirColor.put(color, null);
            }
        }
    }

    // cette fonction sert a determiner une couleur disopnible si une couleur est disponible,
    // on l'attribue a l'audiotir, si aucune n'est disponible, la couleur sera noire par defaut
    // on indique a la map des couleurs que l'audiotir lui est attribue en la reliant
    // par son chemin sur le disque
    private Color determineColorAvailable(String ficPath) {

        for (Color color : audioTirColor.keySet()) {

            if (audioTirColor.get(color) == null) {
                audioTirColor.put(color, ficPath);
                return color;
            }
        }

        return Color.BLACK;
    }

    public List<Float> getAudioFicSpectrum() { return audioFicSpectrum; }

    public String getFicPath() { return ficPath; }

    public int getTotalFrames() { return totalFrames; }

    public float getDuration() { return duration; }

    public String getFicName() { return ficName; }

    public Color getCurveColor() { return curveColor; }

    public void setCurveColor(Color curveColor) { this.curveColor = curveColor; }

    public List<Float> getRoundPerFrameVariation() { return roundPerFrameVariation; }

    public List<Float> getKiloWattsPerFrameCurve() { return kiloWattsPerFrameCurve; }
}
