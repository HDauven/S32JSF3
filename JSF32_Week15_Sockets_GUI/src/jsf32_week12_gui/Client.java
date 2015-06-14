/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_gui;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import jsf32_week12_nogui.Edge;

/**
 *
 * @author Jelle
 */
public class Client
{

    public ClientRunnable server;
    private Socket socket;
    private JSF32_Week12_GUI app;

    public Client(String IP, int port, JSF32_Week12_GUI application) throws IOException
    {
        this.socket = new Socket(IP, port);
        this.app = application;
        server = new ClientRunnable(socket, app);
        Thread thr = new Thread(server);
        thr.setDaemon(false);
        thr.start();
    }
    
    public void sendLevel(int level)
    {
        server.writeLevel(level);
    }

}
