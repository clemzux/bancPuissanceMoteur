package org.clemzux.sound;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Mp3ToWavConverter {

    private InputStream input;
    private AudioFormat audioFormat;
    private boolean close;

    public Mp3ToWavConverter(InputStream input) {
        this(input, false);
    }

    public Mp3ToWavConverter(InputStream input, boolean close) {
        this.input = input;
        this.close = close;
    }

    public static Mp3ToWavConverter convertFrom(InputStream input) {
        return new Mp3ToWavConverter(input, false);
    }

    public static Mp3ToWavConverter convertFrom(byte[] mp3Content) {
        return new Mp3ToWavConverter(new ByteArrayInputStream(mp3Content), true);
    }

    public Mp3ToWavConverter withTargetFormat(AudioFormat targetAudioFormat) {
        this.audioFormat = targetAudioFormat;
        return this;
    }

    public void to(OutputStream output) {
        try (
                final ByteArrayOutputStream rawOutputStream = new ByteArrayOutputStream()
        ) {
            convert(input, rawOutputStream, getTargetFormat());
            final byte[] rawResult = rawOutputStream.toByteArray();
            final AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(rawResult),
                    getTargetFormat(), rawResult.length);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, output);
        } catch (Exception e) {
            System.out.println("GROSSE ERREUR 1");
        } finally {
            closeInput();
        }
    }

    public byte[] toByteArray() {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            to(output);
            return output.toByteArray();
        }  catch (IOException e) {
            System.out.println("GROSSE ERREUR 2");
        }

        return null;
    }

    private void closeInput() {
        if (this.close) {
            try {
                input.close();
            } catch (IOException e) {
                // Sad but true;
            }
        }
    }

    private void convert(InputStream input, OutputStream output, AudioFormat targetFormat) throws Exception {

        System.out.println("a");
        try (
                final AudioInputStream rawSourceStream = AudioSystem.getAudioInputStream(input)
        ) {
            final AudioFormat sourceFormat = rawSourceStream.getFormat();
            final AudioFormat convertFormat = getAudioFormat(sourceFormat);

            try (
                    final AudioInputStream sourceStream = AudioSystem
                            .getAudioInputStream(convertFormat, rawSourceStream);
                    final AudioInputStream convertStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
            ) {
                System.out.println("aa");
                int read;
                final byte[] buffer = new byte[8192];
                while ((read = convertStream.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, read);
                }
            }
        }
    }

    private AudioFormat getTargetFormat() {
        return this.audioFormat == null
                ? new AudioFormat(44100, 8, 1, true, false)
                : audioFormat;
    }

    private AudioFormat getAudioFormat(AudioFormat sourceFormat) {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(),
                16,
                sourceFormat.getChannels(),

                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
    }
}
