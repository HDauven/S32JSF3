/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelle
 */
public class ServerRunnable implements Runnable
{

    private Socket socket = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public ServerRunnable(Socket socket) throws IOException
    {
        this.socket = socket;
        in = new ObjectInputStream(this.socket.getInputStream());
        out = new ObjectOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Edge edge = (Edge) in.readObject();
                Client.edges.add(edge);
                System.out.println(edge.toString());
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(ServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Send an Edge to the server.
     * @param edge 
     */
    public void sendToServer(Edge edge)
    {
        try
        {
            out.writeObject(edge);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
