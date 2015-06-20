/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week17_jna;

import com.sun.jna.ptr.LongByReference;

/**
 *
 * @author Jelle
 */
public class JSF32_Week17_JNA
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SYSTEMTIME time = new SYSTEMTIME();
        
        //OPDRACHT 1
        TimeStamp ts = new TimeStamp();
        TimeStamp ts2 = new TimeStamp();
        ts.init();
        ts2.init();
        ts.setBegin("Start GetLocalTime()");
        for (int i = 0; i < 1000000; i++)
        {
           ILibrary.INSTANCE.GetLocalTime(time); 
        }
        ts.setEnd("End GetLocalTime()");
        
        ts2.setBegin("Start System.nanoTime()");
        for (int i = 0; i < 1000000; i++)
        {
            System.nanoTime();
        }
        ts2.setEnd("End System.nanoTime()");
        
        System.out.println(ts.toString());
        System.out.println(ts2.toString());
        
        //OPDRACHT 2
        LongByReference lpFreeBytesAvailable = new LongByReference();
        LongByReference lpTotalNumberOfByes = new LongByReference();
        LongByReference lpTotalNumberOfFreeBytes = new LongByReference();
        
        ILibrary.INSTANCE.GetDiskFreeSpaceExA("C:\\", lpFreeBytesAvailable, lpTotalNumberOfByes, lpTotalNumberOfFreeBytes);
        System.out.println("Free bytes in MB " + (lpFreeBytesAvailable.getValue() / 1024) / 1024);
        System.out.println("Total number of bytes in MB " + (lpTotalNumberOfByes.getValue() / 1024) / 1024) ;
        System.out.println("Total number of free bytes in MB " + (lpTotalNumberOfFreeBytes.getValue() / 1024) / 1024);
    }
}
