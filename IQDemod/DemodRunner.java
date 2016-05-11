import java.util.*;
/**
 * Runs the demodulation and stuff
 * @author Felix Zheng
 * @version Final
 */
public class DemodRunner
{
    public static void main(String[] args)
    {
        System.out.println();
        Scanner s = new Scanner(System.in);
        
        String fileNamePath = FileChooser.pickAFile();
        System.out.println("You have chosen: " + fileNamePath);
        
        System.out.print("Bandwidth of IQ data: ");
        double inputBandwidth = s.nextDouble();//
        
        System.out.print("\nBandwidth of signal: ");
        double signalBandwidth = s.nextDouble();//
        
        System.out.print("\nAudio Sample Rate: ");
        float audioSampleRate = s.nextFloat(); //default 44100

        byte[][] IQ = IQIO.readIQFile(fileNamePath);
        byte[] audioData = IQDemod.FM(inputBandwidth, signalBandwidth, IQ, audioSampleRate);
        
        (new Audio(audioData, audioSampleRate)).show();
    }
}
