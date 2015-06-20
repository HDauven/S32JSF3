/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week17_jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 *
 * @author Jelle
 */
public interface ILibrary extends Library
{

    ILibrary INSTANCE = (ILibrary) Native.loadLibrary("kernel32", ILibrary.class);

    int GetCurrentProcessId();

    int GetVersion();
}
