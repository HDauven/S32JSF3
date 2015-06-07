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
public class ClientRunnable implements Runnable
{

    private Socket socket = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientRunnable(Socket socket) throws IOException
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
                Logger.getLogger(ClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Writes a level to to the server
     *
     * @param level
     */
    public void writeLevel(int level)
    {
        try
        {
            out.writeObject(level);
        }
        catch (IOException ex)
        {
        }
    }

}
