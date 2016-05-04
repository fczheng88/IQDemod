import java.util.*;
public class DemodRunner
{
    public static void main(String[] args)
    {
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Bandwidth of IQ data: ");
        double inputBandwidth = 0.25e6;//s.nextDouble();//
        
        System.out.print("\nBandwidth of signal: ");
        double signalBandwidth = 0.20e6;//s.nextDouble();//
        
        System.out.print("\nName of file: ");
        String fileName = "107.889_0.25e6_capture.bin";//s.next(); //
        
        System.out.print("\nAudio Sample Rate: ");
        float audioSampleRate = 44100F;//s.nextFloat(); //

        byte[][] IQ = IQIO.readIQFile(fileName);
        
        byte[] audioData = IQDemod.FM(inputBandwidth, signalBandwidth, IQ, audioSampleRate);
        
        (new Audio(audioData, audioSampleRate)).show();
    }
}
