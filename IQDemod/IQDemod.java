import java.io.*;
import java.nio.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
/**
 * Takes IQ samples and turns them into raw audio streams
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IQDemod
{
    public static  double[] FM(double MSPS, double signalBandwidth, byte[][] oIQ)
    {
        byte[][] IQ = new byte[2][];
        
        IQ[0] = DataOps.decimate(oIQ[0], MSPS/signalBandwidth);
        IQ[1] = DataOps.decimate(oIQ[1], MSPS/signalBandwidth);

        double[] phaseAngles = new double[IQ[0].length];
        //calculate each phase angle
        for(int i = 0; i < phaseAngles.length; i++)
        {
            phaseAngles[i] = Math.atan2(Byte.toUnsignedInt(IQ[0][i])-127, Byte.toUnsignedInt(IQ[1][i])-127);
        }

        //Matlab "unwrap" corrects phase angles
        phaseAngles = DataOps.unwrap(phaseAngles);
        double[] paDerivs = new double[phaseAngles.length-1];
        for(int i = 1; i < phaseAngles.length; i++)
        {
            paDerivs[i-1]=phaseAngles[i]-phaseAngles[i-1];
        }
        return paDerivs;
    }    
}
