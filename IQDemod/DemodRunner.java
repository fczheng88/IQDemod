import java.util.*;
public class DemodRunner
{
    public static void main(String[] args)
    {
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Bandwidth of IQ data: ");
        double inputBandwidth = s.nextDouble();//2.048e6;
        
        System.out.print("\nBandwidth of signal: ");
        double signalBandwidth = s.nextDouble();//0.20e6;
        
        System.out.print("\nName of file: ");
        String fileName = s.next(); //"107.889_0.25e6_capture.bin";
        
        System.out.print("\nAudio Sample Rate: ");
        float audioSampleRate = s.nextFloat();

        int[][] IQ = IQIO.readIQFile(fileName);
        double[] audioData = IQDemod.FM(inputBandwidth, signalBandwidth, IQ);
        (new Audio(audioData, audioSampleRate, signalBandwidth)).show();
    }
}
