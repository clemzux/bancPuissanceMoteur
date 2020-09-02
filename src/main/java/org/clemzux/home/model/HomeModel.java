package org.clemzux.home.model;

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
import java.util.List;

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

                openMp3Tir(tirBase.getAbsolutePath());
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

            spectralFlux = replaceValBy(spectralFlux, (float) 0.03, (float) 0);
            spectralFlux = averageSpectrum(spectralFlux);
            spectralFlux = replaceValBy(spectralFlux, (float) 0.03, (float) 0);

            // on tente de detecter le nombre de variations de la courbe en comptant
            // en comptant les blocs de valeurs differents de 0

            i = 1;
            boolean grow = true;

            while (i < spectralFlux.size()) {

                // croissance de la courbe
                if (spectralFlux.get(i - 1) < spectralFlux.get(i)) {

                    grow = true;
                }
                else if (spectralFlux.get(i - 1) > spectralFlux.get(i)){

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
            String demultiplication = homeView.getDemultiplicationTextField().getText();
            String pulseByRound = homeView.getPulseByRoundTextField().getText();

            AudioTir audioTir =
                    new AudioTir(spectralTreated, wavTirPath, ficName, 0,
                            durationInSeconds, Float.valueOf(inertiaMoment), Float.valueOf(demultiplication),
                            Float.valueOf(pulseByRound));

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

    private void openMp3Tir(String mp3TirPath) {

        try {
            AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(new File(mp3TirPath));

            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(mp3TirPath));

            AudioFormat audioFormat = ais.getFormat();

//            System.out.println("File Format Type: "+inputFileFormat.getType());
//            System.out.println("File Format String: "+inputFileFormat.toString());
//            System.out.println("File lenght: "+inputFileFormat.getByteLength());
//            System.out.println("Frame length: "+inputFileFormat.getFrameLength());
//            System.out.println("Channels: "+audioFormat.getChannels());
//            System.out.println("Encoding: "+audioFormat.getEncoding());
//            System.out.println("Frame Rate: "+audioFormat.getFrameRate());
//            System.out.println("Frame Size: "+audioFormat.getFrameSize());
//            System.out.println("Sample Rate: "+audioFormat.getSampleRate());
//            System.out.println("Sample size (bits): "+audioFormat.getSampleSizeInBits());
//            System.out.println("Big endian: "+audioFormat.isBigEndian());
//            System.out.println("Audio Format String: "+audioFormat.toString());

            byte [] sourceBytes = ais.readAllBytes();
            byte [] encodedBytes = getAudioDataBytes(sourceBytes, audioFormat);

            File outFile = new File("c:\\tmp.wav");
            FileOutputStream out = new FileOutputStream(outFile);

            out.write(encodedBytes);

            out.close();

//            try{
////                int i = AudioSystem.write(encodedBytes, AudioFileFormat.Type.WAVE, new File("/tmp/tmp.wav"));
////                System.out.println("Bytes Written: "+i);
//            }catch(Exception e){
//                e.printStackTrace();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        InputStream inputStream = getClass().getResourceAsStream(mp3TirPath);
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//        AudioFormat audioFormat = new AudioFormat(44100, 8, 1, false, false);
//
//        Mp3ToWavConverter.convertFrom(inputStream).withTargetFormat(audioFormat).to(output);
//
//        byte[] wavContent = output.toByteArray();
//
//        try {
//            final AudioFileFormat actualFileFormat = AudioSystem
//                    .getAudioFileFormat(new ByteArrayInputStream(wavContent));
//            Files.write(Paths.get("/tmp/tmp.wav"), wavContent);
//
//
//        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static byte [] getAudioDataBytes(byte [] sourceBytes, AudioFormat audioFormat) throws UnsupportedAudioFileException, IllegalArgumentException, Exception {
        if(sourceBytes == null || sourceBytes.length == 0 || audioFormat == null){
            throw new IllegalArgumentException("Illegal Argument passed to this method");
        }

        try (final ByteArrayInputStream bais = new ByteArrayInputStream(sourceBytes);
             final AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(bais)) {
            AudioFormat sourceFormat = sourceAIS.getFormat();
            AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels()*2, sourceFormat.getSampleRate(), false);
//            AudioFormat convertFormat = new AudioFormat(
//                    AudioFormat.Encoding.PCM_SIGNED,
//                    2,
//                    16,
//                    2,
//                    (1152 / 44100) * 1000,
//                    1152 ,
//                    false);
            try (final AudioInputStream convert1AIS = AudioSystem.getAudioInputStream(convertFormat, sourceAIS);
                 final AudioInputStream convert2AIS = AudioSystem.getAudioInputStream(audioFormat, convert1AIS);
                 final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte [] buffer = new byte[8192];
                while(true){
                    int readCount = convert2AIS.read(buffer, 0, buffer.length);
                    if(readCount == -1){
                        break;
                    }
                    baos.write(buffer, 0, readCount);
                }
                return baos.toByteArray();
            }
        }
    }

    // cette methode est appelee dans pas mal de cas : nouveau tir, changement de couleur,
    // suppression tir ...
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

    public void openCalculateInertiaMomentWindow() {

        homeView.openCalculateInertiamomentWindow();
    }

    public List<AudioTir> getAudioTirList() { return audioTirList; }
}
