package org.clemzux.home.model;

import javafx.stage.FileChooser;
import org.clemzux.constants.Constants;
import org.clemzux.home.view.HomeView;

import java.io.*;

public class HomeModel {

    private HomeView homeView;

    private short[] audioShort;

    public HomeModel(HomeView view) {

        homeView = view;
    }

    // cette fonction sert a ouvrir un fichier wav et ensuite le ranger dans la listView des tirs
    public void openTir() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Constants.strings.getProperty("fileChooserWindowName"));

        File tirBase = fileChooser.showOpenDialog(homeView.getPrimaryStage());

        chooseTirOpenner(tirBase);
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

        // l'ouverture et le rangement du son dans un short[] sont des algos pris sur le net
        try {
            // on ouvre le fichier

            File tirFile = new File(wavTirPath);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(tirFile));

            // on essaye de le lire

            int read;
            byte[] buff = new byte[1024];

            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }

            out.flush();
            byte[] audioBytes = out.toByteArray();

            // on transforme le byte[] en short[]
            int size = audioBytes.length;
            audioShort = new short[size];

            for (int index = 0; index < size; index++)
                audioShort[index] = (short) audioBytes[index];

            for (int index = 0; index<size; index++) {
                if (audioShort[index] > 0)
                System.out.println(audioShort[index]);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
