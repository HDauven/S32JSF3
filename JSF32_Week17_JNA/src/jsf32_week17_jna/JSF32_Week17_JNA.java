/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week17_jna;

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
        
        
//        System.out.println("Year is " + time.wYear);
//        System.out.println("Month is " + time.wMonth);
//        System.out.println("Day of Week is " + time.wDayOfWeek);
//        System.out.println("Day is " + time.wDay);
//        System.out.println("Hour is " + time.wHour);
//        System.out.println("Minute is " + time.wMinute);
//        System.out.println("Second is " + time.wSecond);
//        System.out.println("Milliseconds are " + time.wMilliseconds);
    }
}
