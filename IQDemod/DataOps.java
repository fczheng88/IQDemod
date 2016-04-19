
/**
 * Write a description of class DataOps here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DataOps
{
    public static double[] unwrap(double[] u)
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
    public static byte[] doubleCastByte(double[] doubles)
    {
        byte[] bytes = new byte[doubles.length];
        for(int i = 0; i< doubles.length; i++)
        {
            bytes[i] = (byte)doubles[i];
        }
        return bytes;
    }
        public static int[] decimate(int[] arr, double factor)
    {
        int[] downSampledArr = new int[(int)(arr.length/factor)];
        for(int i = 0; i < downSampledArr.length; i++)
        {
            downSampledArr[i] = arr[(int)(i*factor)];
        }
        return downSampledArr;
    }
    public static double[] decimate(double[] arr, double factor)
    {
        double[] downSampledArr = new double[(int)(arr.length/factor)];
        for(int i = 0; i < downSampledArr.length; i++)
        {
            downSampledArr[i] = arr[(int)(i*factor)];
        }
        return downSampledArr;
    }
    
    public static double[] upscale(double[] doubles, double factor)
    {
        double[] upscaled = new double[doubles.length];
        for(int i = 0; i<doubles.length; i++)
        {
            upscaled[i] = doubles[i]*factor;
        }
        return upscaled;
    }
}
