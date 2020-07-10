package org.clemzux.home.model;

import javafx.stage.FileChooser;
import org.clemzux.constants.Constants;
import org.clemzux.home.view.HomeView;
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

            int bytesPerFrame =
                    audioInputStream.getFormat().getFrameSize();
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                // some audio formats may have unspecified frame size
                // in that case we may read any amount of bytes
                bytesPerFrame = 1;
            }

            int numBytes = 1024 * bytesPerFrame;
            byte[] audioBytes = new byte[numBytes];

            int totalFramesRead = 0;

            try {

                int numBytesRead = 0;
                int numFramesRead = 0;
                // Try to read numBytes bytes from the file.
                while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
                    // Calculate the number of frames actually read.
                    numFramesRead = numBytesRead / bytesPerFrame;
                    totalFramesRead += numFramesRead;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // on transforme le byte[] en short[]
            audioShort = new short[numBytes];

            for (int index = 0; index < numBytes; index++) {

                audioShort[index] = (short) audioBytes[index];
            }

            // test affichage du contenu du fichier audio
//            for (int index = 0; index<numBytes; index++) {
//                if (audioShort[index] > 0)
//                    System.out.println(audioShort[index]);
//            }

            // on range les donnees dans la classe AudioTir
            AudioFormat format = audioInputStream.getFormat();
            long frameLen = audioInputStream.getFrameLength();
            double durationInSeconds = (frameLen+0.0) / format.getFrameRate();
            String ficName = tirFile.getName();
            AudioTir audioTir = new AudioTir(audioShort, wavTirPath, ficName, totalFramesRead, durationInSeconds);

            // on range dans la liste des tirs
            audioTirList.add(audioTir);

            // on met a jour la liste des tirs
            homeView.populateListView(audioTirList);

            // on met a jour le canvas des courbes
            homeView.updateCanvas();
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
