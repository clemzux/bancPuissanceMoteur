package org.clemzux.home.model;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.clemzux.constants.Constants;
import org.clemzux.home.view.HomeView;
import org.clemzux.sound.FFT;
import org.clemzux.sound.WaveDecoder;
import org.clemzux.utils.audiotir.AudioTir;
import org.clemzux.utils.Models;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeModel {

    private HomeView homeView;

    private short[] audioShort;

    private List<AudioTir> audioTirList;

    public HomeModel(HomeView view) {

        homeView = view;

        // on range le model actuel dans la liste des models
        Models.homeModel = this;
    }

    // cette fonction sert a ouvrir un fichier wav et ensuite le ranger dans la listView des tirs
    public void openTir() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Constants.strings.getProperty("fileChooserWindowName"));

        File tirBase = fileChooser.showOpenDialog(homeView.getPrimaryStage());

        // on choisit la meilleure facon d'ouvrir le fichier
        // puis on le range dans la liste des tir
        chooseTirOpenner(tirBase);

        // on rafraichit le canvas
        homeView.drawCanvasContent(audioTirList);
    }

    private void chooseTirOpenner(File tirBase) {

        String extention = tirBase.getName().split("\\.")[1];

        switch (extention) {

            case "wav":
            case "WAV":

                openWavTir(tirBase.getAbsolutePath());
                break;

            case "mp3":
            case "MP3":

                break;

        }
    }

    private void openWavTir(String wavTirPath) {

        try {

            File tirFile = new File(wavTirPath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tirFile);
            AudioFormat format = audioInputStream.getFormat();

            List<Float> spectralFlux = new ArrayList<Float>();
            float[] buffer = new float[1024];

            WaveDecoder waveDecoder = new WaveDecoder(new FileInputStream(wavTirPath));
            FFT fft = new FFT(1024, audioInputStream.getFormat().getSampleRate());

            while (waveDecoder.readSamples(buffer) > 0) {

                fft.forward(buffer, spectralFlux);
            }

            /////////////////////////////////////////////////////////////////
            // on traite le tir pour qu'il soit plus facilement exploitable

            int i = 0;

            // on construit un spectre vide avec des 0 dedans
            List<Float> spectralTreated = new ArrayList<>();
            for (i = 0; i < spectralFlux.size(); i++) {
                spectralTreated.add((float) 0);
            }

            // on tente de remplacer les petites valeurs par des 0 puis on fait une moyenne
            // on remplace a nouveau les petits chiffres par des 0
            // on compte les blocs de valeurs differentes de 0

            spectralFlux = replaceValBy(spectralFlux, (float) 0.03, (float) 0);
            spectralFlux = averageSpectrum(spectralFlux);
            spectralFlux = replaceValBy(spectralFlux, (float) 0.03, (float) 0);

            // on tente de detecter le nombre de variations de la courbe

            i = 1;
            boolean grow = true;
            boolean ungrow = false;

            while (i < spectralFlux.size()) {

                // croissance de la courbe
                if (spectralFlux.get(i - 1) < spectralFlux.get(i)) {

                    grow = true;
                    ungrow = false;
                }
                else if (spectralFlux.get(i - 1) > spectralFlux.get(i)){

                    ungrow = true;

                    if (grow) {
                        spectralTreated.set(i, (float) 1);
                        grow = false;
                    }
                }

                i++;
            }

            // on range les donnees dans la classe AudioTir
            long frameLen = audioInputStream.getFrameLength();
            double durationInSeconds = frameLen / format.getFrameRate();
            String ficName = tirFile.getName();
            String inertiaMoment = homeView.getInertiaMomentTextField().getText();

            AudioTir audioTir =
                    new AudioTir(spectralTreated, wavTirPath, ficName, 0,
                            durationInSeconds, Float.valueOf(inertiaMoment));

            // on range dans la liste des tirs

            if (audioTirList == null) {
                audioTirList = new ArrayList<>();
            }
            audioTirList.add(audioTir);

            // on met a jour la vue (listview and canvas
            updateListAndCanvas();
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateListAndCanvas() {

        // on met a jour la liste des tirs
        homeView.populateListView(audioTirList);

        // on met a jour le canvas des courbes
        homeView.updateCanvas(audioTirList);
    }

    // cette fonction remplace les valeur voulues par des 0
    private List<Float> replaceValBy (List<Float> spectrum, float compare, float val) {

        int i = 0;
        while (i != spectrum.size() - 1) {

            if (spectrum.get(i) < compare) {
                spectrum.set(i, val);
            }

            i++;
        }

        return spectrum;
    }

    // cette fonction sert a lisser le spectre pour eviter d'avoir trop de variations
    private List<Float> averageSpectrum (List<Float> spectrum) {

        int i = 6;
        float moyenne = 0;

        while (i != spectrum.size() - 6) {

            moyenne += spectrum.get(i - 1) + spectrum.get(i) + spectrum.get(i + 1);
            moyenne /= 3;
            spectrum.set(i, moyenne);

            i++;
        }

        return spectrum;
    }

    public List<AudioTir> getAudioTirList() { return audioTirList; }
}
