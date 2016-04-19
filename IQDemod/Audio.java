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
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Audio
{
    // instance variables - replace the example below with your own
    private double[] data;
    private float sampleRate;
    private byte[] buffer;

    public Audio(double[] data, float sampleRate, double signalBandwidth)
    {
        this.data = data;
        this.sampleRate = sampleRate;
        buffer = DataOps.doubleCastByte(DataOps.upscale(smooth(DataOps.decimate(data, signalBandwidth/sampleRate), 20), 64));
    }

        public double[] smooth(double[] doubles, int smoothing ){
        double value = doubles[0]; // start with the first input
        for (int i=1 ; i<doubles.length; i++){
            double currentValue = doubles[i];
            value += (currentValue - value) / smoothing;
            doubles[i] = value;
        }
        return doubles;
    }
    
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

    public void show()
    {
        JFrame frame = new SoundClipFrame();
        frame.setVisible(true);
    }

    private static int frames;
    private static int offsets;
    class SoundClipFrame extends JFrame
    {
        private int[] samples; 

        public SoundClipFrame()
        {
            frames++;
            offsets++;
            double[] samples = data;
            for(int i = 0; i < samples.length; i++)
            {
                samples[i] = samples[i]*100;
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

                        final int LARGEST = 250;
                        int x = 0;
                        for (int i = 0; i < samples.length; i = i + increment)
                        {
                            int value = (int) samples[i];
                            value = Math.min(LARGEST, value);
                            value = Math.max(-LARGEST, value);

                            int height = getHeight() / 2;
                            int y = height - (int)samples[i] * height / LARGEST;
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
