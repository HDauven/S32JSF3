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
        int pid = ILibrary.INSTANCE.GetCurrentProcessId();
        System.out.println(pid);
        int version = ILibrary.INSTANCE.GetVersion();
        System.out.println(version);
    }

}
