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
    public static ArrayList<Edge> edges;
    private Socket socket;

    public Client(String IP, int port) throws IOException
    {
        socket = new Socket(IP, port);
        edges = new ArrayList<>();
        server = new ClientRunnable(socket);
        Thread thr = new Thread(server);
        thr.start();
    }

    public Edge getEdge()
    {
        if (edges.size() > 0)
        {
            Iterator<Edge> it = edges.iterator();
            return it.next();
        }
        return null;
    }
    
    public void sendLevel(int level)
    {
        server.writeLevel(level);
    }

}
