import java.io.*;
import java.nio.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
/**
 * Demodulates a signal based on a stream of IQ data and returns a raw audio stream
 * 
 * @author Felix Zheng
 * @version Final
 */
public class IQDemod
{
    /**
     * Demodulates IQ data originally modulated using FM
     * 
     * @param MSPS The samples per second of the original signal
     * @param signalBandwidth The bandwidth of the actual signal
     * @param oIQ The IQ data as represented by a 2D byte array
     * @param sampleRate The sampleRate of the audio to be played. Used to specify proper sampleRate of returned byte[]
     * 
     * @return A byte[] of raw audio data with the sampleRate specified.
     */
    public static  byte[] FM(double MSPS, double signalBandwidth, byte[][] oIQ, float sampleRate)
    {
        byte[][] IQ = new byte[2][];
        
        IQ[0] = decimate(oIQ[0], MSPS/signalBandwidth);
        IQ[1] = decimate(oIQ[1], MSPS/signalBandwidth);

        double[] phaseAngles = new double[IQ[0].length];
        //calculate each phase angle
        for(int i = 0; i < phaseAngles.length; i++)
        {
            phaseAngles[i] = Math.atan2(Byte.toUnsignedInt(IQ[0][i])-127, Byte.toUnsignedInt(IQ[1][i])-127);
        }
        IQ = null; System.gc(); //collect that garbage!
        
        //Matlab "unwrap" corrects phase angles
        phaseAngles = unwrap(phaseAngles);
        double[] paDerivs = new double[phaseAngles.length-1];
        for(int i = 1; i < phaseAngles.length; i++)
        {
            paDerivs[i-1]=phaseAngles[i]-phaseAngles[i-1];
        }
        phaseAngles = null; System.gc(); //garbage collect
        
        double[] audioDoubles = decimate(paDerivs, signalBandwidth/sampleRate);
        paDerivs = null; System.gc(); //garbage collect
        
        audioDoubles = upscale(audioDoubles, 20);
        
        return doubleCastByte(audioDoubles);
    }    
    /**
     * Decimate decimates an array by a factor. Modeled off of Matlab's decimate function.
     * @param arr The array to be decimated
     * @param factor The factor of decimation
     * 
     * @return A decimated array
     */
    private static byte[] decimate(byte[] arr, double factor)
    {
        byte[] downSampledArr = new byte[(int)(arr.length/factor)];
        for(int i = 0; i < downSampledArr.length; i++)
        {
             downSampledArr[i] = arr[(int)(i*factor)];
        }
        return downSampledArr;
    }
    /**
     * Decimate decimates an array by a factor. Modeled off of Matlab's decimate function.
     * 
     * @param arr The array to be decimated
     * @param factor The factor of decimation
     * 
     * @return A decimated array
     */
    private static double[] decimate(double[] arr, double factor)
    {
        double[] downSampledArr = new double[(int)(arr.length/factor)];
        for(int i = 0; i < downSampledArr.length; i++)
        {
            downSampledArr[i] = arr[(int)(i*factor)];
        }
        return downSampledArr;
    }
    /**
     * Unwraps the data. Ensures that all data is no more than PI away from the previous and next data points
     * and adjusts the data accordingly
     * 
     * @param u The array for unwrapping
     * 
     * @return The unwrapped array
     */
    private static double[] unwrap(double[] u)
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
    /**
     * Upscales the data in an array by a specified factor
     * 
     * @param doubles The array of doubles to be upscaled
     * @param factor The factor at which the data should be upscaled
     * 
     * @return The upscaled array
     */
    private static double[] upscale(double[] doubles, double factor)
    {
        double[] upscaled = new double[doubles.length];
        for(int i = 0; i<doubles.length; i++)
        {
            upscaled[i] = doubles[i]*factor;
        }
        return upscaled;
    }
    /**
     * DoubleCastByte dasts a double array to a byte array element by element
     * @param doubles The double array
     * @return The byte array created based on the double array
     */
    private static byte[] doubleCastByte(double[] doubles)
    {
        byte[] bytes = new byte[doubles.length];
        for(int i = 0; i< doubles.length; i++)
        {
            bytes[i] = (byte)doubles[i];
        }
        return bytes;
    }
}
