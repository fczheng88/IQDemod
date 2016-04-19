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
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IQIO
{
    public static int[][] readIQFile(String capture)
    {
        FileInputStream fileInputStream=null;
        File file = new File(capture);
        byte[] byteArr = new byte[(int) file.length()];
        int[][] IQ = new int[2][];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteArr);
            fileInputStream.close();

            ArrayList<Integer> intList = new ArrayList<Integer>();
            for (int i = 0; i < byteArr.length; i++) {
                intList.add(Byte.toUnsignedInt(byteArr[i])-127);//unsigned to signed
            }
            IQ[0] = new int[intList.size()/2];
            IQ[1] = new int[IQ[0].length];
            for(int i = 0; i < IQ[0].length; i++)
            {
                int twoI = i+i;
                IQ[0][i] = intList.get(twoI);
                IQ[1][i] = intList.get(twoI + 1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return IQ;
    }
}
