import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
/**
 * Write a description of class IQCalc here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IQCalc
{
    private byte[] byteArr;
    private ArrayList<Integer> intList;
    private int[] I;
    private int[] Q;
    private double[] phaseAngles;
    private double[] paDerivs;

    private double sampleRate;

    

    /**
     * Constructor for objects of class IQCalc
     */
    public IQCalc(double sampleRate)
    {
        this.sampleRate = sampleRate;
    }

    public void readIQFile(String capture)
    {
        FileInputStream fileInputStream=null;
        File file = new File(capture);
        byteArr = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteArr);
            fileInputStream.close();

            intList = new ArrayList<Integer>();
            for (int i = 0; i < byteArr.length; i++) {
                intList.add(Byte.toUnsignedInt(byteArr[i])-127);//unsigned to signed
            }
            I = new int[intList.size()/2];
            Q = new int[I.length];
            for(int i = 0; i < I.length; i++)
            {
                int twoI = i+i;
                I[i] = intList.get(twoI);
                Q[i] = intList.get(twoI + 1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void demodulate()
    {
        //I = decimate(I, sampleRate/2.5e6);
        //Q = decimate(Q, sampleRate/2.5e6);
        
        phaseAngles = new double[I.length];
        //calculate each phase angle
        for(int i = 0; i < phaseAngles.length; i++)
        {
            phaseAngles[i] = Math.atan2(Q[i], I[i]);
        }

        //Matlab "unwrap" corrects phase angles
        phaseAngles = unwrap(phaseAngles);
        paDerivs = new double[phaseAngles.length-1];
        for(int i = 1; i < phaseAngles.length; i++)
        {
            paDerivs[i-1]=phaseAngles[i]-phaseAngles[i-1];
        }
        for(int i = 0; i< 10; i++)
        {
            System.out.print(phaseAngles[i]+" "+phaseAngles[i+1]+" ");
            System.out.println(paDerivs[i]);
        }
    }

    public double[] unwrap(double[] u)
    {
        int  k=0;               // initialize k to 0
        double[] yout = new double[u.length];
        for(int i = 0; i < u.length-1; i++)
        {
            yout[i]=u[i]+(2*Math.PI*k); // add 2*pi*k to ui
            if((Math.abs(u[i+1]-u[i]))>Math.PI) //if diff is greater than alpha, increment or decrement k
            {
                if(u[i+1]<u[i])  // if the phase jump is negative, increment k
                {
                    k=k+1;
                }
                else            // if the phase jump is positive, decrement k
                {
                    k=k-1;
                }
            }
        }
        yout[yout.length-1]=u[u.length-1]+(2*Math.PI*k); // add 2*pi*k to the last element of the input
        return yout;
    }

    public double[] decimate(double[] arr, double factor)
    {
        double[] downSampledArr = new double[(int)(arr.length/factor)];
        for(int i = 0; i < downSampledArr.length; i++)
        {
            downSampledArr[i] = arr[(int)(i*factor)];
        }
        return downSampledArr;
    }
    public int[] decimate(int[] arr, double factor)
    {
        int[] downSampledArr = new int[(int)(arr.length/factor)];
        for(int i = 0; i < downSampledArr.length; i++)
        {
            downSampledArr[i] = arr[(int)(i*factor)];
        }
        return downSampledArr;
    }
    
    public void play()
    {

    }

    public ArrayList<Integer> getIntList()
    {return intList;}

    public void show()
    {
        JFrame frame = new SoundClipFrame();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        IQCalc calc = new IQCalc(1.8e6);
        calc.readIQFile("capture2.bin");
        System.out.println(calc.getIntList().size());
        calc.demodulate();

        calc.show();
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
            double[] samples = decimate(paDerivs,sampleRate/2.5e6);
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
