package org.clemzux.home.model;

import javafx.stage.FileChooser;
import org.clemzux.constants.Constants;
import org.clemzux.home.view.HomeView;
import org.clemzux.sound.FFT;
import org.clemzux.sound.WaveDecoder;
import org.clemzux.utils.AudioTir;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HomeModel {

    private HomeView homeView;

    private List<AudioTir> audioTirList;

    private short[] audioShort;

    public HomeModel(HomeView view) {

        homeView = view;
        audioTirList = new ArrayList<>();
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
        homeView.drawCanvasContent();
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

            WaveDecoder waveDecoder = new WaveDecoder(new FileInputStream(wavTirPath));
            FFT fft = new FFT(1024, audioInputStream.getFormat().getSampleRate());

            float[] samples = new float[1024];
            float[] spectrum = new float[1024 / 2 + 1];
            float[] lastSpectrum = new float[1024 / 2 + 1];
            List<Float> spectralFlux = new ArrayList<Float>();
            int i = 0;

//            int numBytes = 1024;
//            byte[] audioBytes = new byte[numBytes];
//            int numBytesRead = 0;
//            int numFramesRead = 0;
//            // Try to read numBytes bytes from the file.
//            while (audioInputStream.read(audioBytes) != -1) {
//
//                for (byte b : audioBytes) {
//                    spectralFlux.add((float) b);
//                    System.out.println(b);
//                }
//            }

            while (waveDecoder.readSamples(samples) > 0) {
                fft.forward(samples, spectralFlux);
                System.arraycopy(spectrum, 0, lastSpectrum, 0, spectrum.length);
                System.arraycopy(fft.getSpectrum(), 0, spectrum, 0, spectrum.length);

                // on range le spectre en entier dans le tableau
//                for (float f : fft.getSpectrum()) {
//                    spectralFlux.add(f);
//                }
            }

            /////////////////////////////////////////////////////////////////
            // on traite le tir pour qu'il soit plus facilement exploitable

            // on construit un spectre vide avec des 0 dedans
            List<Float> spectralTreated = new ArrayList<>();
            for (int j = 0; j < spectralFlux.size(); j++) {
                spectralTreated.add((float) 0);
            }

            // on tente de remplacer les petites valeurs par des 0 puis on fait une moyenne
            // on remplace a nouveau les petits chiffres par des 0
            // on compte les blocs de valeurs differentes de 0

            spectralFlux = replaceValBy(spectralFlux, (float) 0.03, (float) 0);
            spectralFlux = averageSpectrum(spectralFlux);
//            spectralFlux = averageSpectrum(spectralFlux);
            spectralFlux = replaceValBy(spectralFlux, (float) 0.01, (float) 0);
//            spectralFlux = averageSpectrum(spectralFlux);
//            spectralFlux = replaceValBy0(spectralFlux);

//            i = 0;
//            boolean diff0 = false;
//
//            while (i < spectralFlux.size()) {
//
//                if (spectralFlux.get(i) != 0) {
//
//                    diff0 = true;
//                }
//                else {
//
//                    if (diff0) {
//                        spectralTreated.set(i, (float) 1);
//                    }
//                    diff0 = false;
//                }
//
//                i++;
//            }

            // ici on remplace tous les ensembles de val > 0 par des 0
            // par ex : ... , 8, 12, 13 ,15, ... sera remplace par ... , 0, 0, 0, 15, ...

//            i = 1;
//            while (i != spectralFlux.size() - 1) {
//
//                // s'il y a deux valeurs d'affilee != 0, on la remplace par 0
//                if (spectralFlux.get(i-1) != 0 && spectralFlux.get(i) != 0) {
//                    spectralFlux.set(i-1, (float) 0);
//                }
//
//                i++;
//            }

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
            AudioFormat format = audioInputStream.getFormat();
            long frameLen = audioInputStream.getFrameLength();
            double durationInSeconds = (frameLen+0.0) / format.getFrameRate();
            String ficName = tirFile.getName();
            AudioTir audioTir = new AudioTir(spectralTreated, wavTirPath, ficName, 0, durationInSeconds);

            // on range dans la liste des tirs
            audioTirList.add(audioTir);

            // on met a jour la liste des tirs
            homeView.populateListView(audioTirList);

            // on met a jour le canvas des courbes
            homeView.updateCanvas();

            //////////////////////////////////////////////// a retirer a la fin des tests
            // test calcul tpm
            double seconds = frameLen / format.getFrameRate();

            // donnees test
            int inOneSecond = spectralFlux.size() / (int) seconds;
            int nbValSup = 0;
            for (i = inOneSecond*0; i < inOneSecond*0.5; i++) {

                System.out.println(spectralFlux.get(i));

                if (spectralTreated.get(i) != 0)
                    nbValSup++;
            }

            System.out.println("nb val sup : " + nbValSup);
            System.out.println("En une seconde : " + inOneSecond);
            System.out.println("Duree totale : " + spectralTreated.size());

            /////////////////////////////////////////////////
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

//            moyenne = spectrum.get(i - 6) + spectrum.get(i - 5) + spectrum.get(i - 4);
            moyenne += spectrum.get(i - 1) + spectrum.get(i) + spectrum.get(i + 1);
//            moyenne += spectrum.get(i + 4) + spectrum.get(i + 5) + spectrum.get(i + 6);
            moyenne /= 3;
            spectrum.set(i, moyenne);

            i++;
        }

        return spectrum;
    }

//    private void openWavTir(String wavTirPath) {
//
//        // l'ouverture et le rangement du son dans un short[] sont des algos pris sur le net
//        try {
//            // on ouvre le fichier
//
//            File tirFile = new File(wavTirPath);
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            BufferedInputStream in = new BufferedInputStream(new FileInputStream(tirFile));
//
//            // on essaye de le lire
//
//            int read;
//            byte[] buff = new byte[1024];
//
//            while ((read = in.read(buff)) > 0) {
//                out.write(buff, 0, read);
//            }
//
//            out.flush();
//            byte[] audioBytes = out.toByteArray();
//
//            // on transforme le byte[] en short[]
//            int size = audioBytes.length;
//            audioShort = new short[size];
//
//            for (int index = 0; index < size; index++)
//                audioShort[index] = (short) audioBytes[index];
//
//            for (int index = 0; index<size; index++) {
//                if (audioShort[index] > 0)
//                System.out.println(audioShort[index]);
//            }
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
