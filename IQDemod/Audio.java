import java.io.*;
import java.nio.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
/**
 * Plays audio from raw audio data
 * 
 * @author Felix Zheng 
 * @version Final
 */
public class Audio
{
    private float sampleRate;
    private byte[] buffer;

    /**
     * Constructor; initializes instance variables
     * 
     * @param data Raw audio data from IQ file
     * @param audioSampleRate sample rate of sound output
     */
    public Audio(byte[] data, float audioSampleRate)
    {
        sampleRate = audioSampleRate;

        buffer = smooth(data, 10);
    }

    /**
     * 'Smooths' the audio. Attempts to reduce some high frequency noise
     * 
     * @param bytes Raw audio data
     * @param smoothing The factor of smoothing. 1 is equivalent to no smoothing
     * 
     * @return Smoothed audio data
     */
    public byte[] smooth(byte[] bytes, int smoothing ){
        byte value = bytes[0]; // start with the first input
        for (int i=1 ; i<bytes.length; i++){
            byte currentValue = bytes[i];
            value += (currentValue - value) / smoothing;
            bytes[i] = value;
        }
        return bytes;
    }

    /**
     * Plays the audio. Creates an audio format based on sample rate and some other data and plays the raw audio data.
     * Most of code taken from a post on StackOverflow (http://stackoverflow.com/questions/32873596) and modified for
     * use in this application.
     */
    public void play()
    {
        try {
            // select audio format parameters
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            // prepare audio output
            line.open(af, 4096);
            line.start();
            // output sound
            line.write(buffer, 0, buffer.length);
            // shut down audio
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * Shows a SoundClipFrame
     */
    public void show()
    {
        JFrame frame = new SoundClipFrame();
        frame.setVisible(true);
    }
    /**
     * This class creates and displays a window with a graph that represents the sound data.
     * It also contains a play button to allow the audio data to be played.
     * Subclass taken from the SoundClip class of Unit4ArraysArrayLists and modified.
     * 
     * @author Felix Zheng
     */
    class SoundClipFrame extends JFrame
    {
        private int frames;
        private int offsets;
        private int[] samples; 

        public SoundClipFrame()
        {
            frames++;
            offsets++;
            int[] samples = new int[buffer.length];
            for(int i = 0; i < samples.length; i++)
            {
                samples[i] = (Byte.toUnsignedInt(buffer[i])-128); //copies the byte[] as an int[]
            }

            addWindowListener(new WindowAdapter()
                {
                    @Override public void windowClosing(WindowEvent event)
                    {
                        frames--;
                        if (frames == 0) System.exit(0);
                    }
                });

            final int FRAME_WIDTH = 800;
            final int FRAME_HEIGHT = 200;
            final int OFFSET_WIDTH = 100;

            setBounds(offsets * OFFSET_WIDTH, offsets * OFFSET_WIDTH, FRAME_WIDTH, FRAME_HEIGHT);

            JComponent component = new JComponent()
                {
                    public void paintComponent(Graphics graph)
                    {
                        int increment = samples.length / getWidth();

                        final int LARGEST = 130;
                        int x = 0;
                        for (int i = 0; i < samples.length; i = i + increment)
                        {
                            int value = samples[i];
                            value = Math.min(LARGEST, value);
                            value = Math.max(-LARGEST, value);

                            int height = getHeight() / 2;
                            int y = height - samples[i] * height / LARGEST;
                            graph.drawLine(x, y, x, height);
                            x++;
                        }
                    }
                };

            add(component);
            JPanel panel = new JPanel();
            JButton button = new JButton("Play");
            button.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent event) { play(); }
                });
            panel.add(button);
            add(panel, BorderLayout.SOUTH);
        }
    }
}
