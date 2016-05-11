import java.io.*;
import java.nio.*;
import java.nio.file.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
/**
 * This class is for dealing with raw IQ data from files.
 * 
 * @author Felix Zheng
 * @version Final
 */
public class IQIO
{
    /**
     * Reads a file and creates a 2D byte array representing the IQ data
     * 
     * @param capture The path of the IQ data file.
     */
    public static byte[][] readIQFile(String capture)
    {
        FileInputStream fileInputStream=null; 
        File file = new File(capture);
        byte[] byteArr = new byte[(int) file.length()];
        byte[][] IQ = new byte[2][];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteArr); //read data into a byte[]
            fileInputStream.close();
            
            //take data from byteArray and sorts it into the I and Q components.
            IQ[0] = new byte[byteArr.length/2];
            IQ[1] = new byte[IQ[0].length];
            for(int i = 0; i<IQ[0].length; i++)
            {
                int twoI = i+i;
                IQ[0][i] = byteArr[twoI];
                IQ[1][i] = byteArr[twoI+1];
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return IQ;
    }
}
