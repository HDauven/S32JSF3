/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelle
 */
public class ServerRunnable implements Runnable
{

    private ObjectInputStream in;
    private ObjectOutputStream out;
    public Socket socket;
    private JSF32_Week12_NoGUI app;

    public ServerRunnable(Socket s, JSF32_Week12_NoGUI application) throws IOException, ClassNotFoundException
    {
        this.socket = s;
        app = application;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
     public void writeEdge(Edge edge)
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

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                int level = (int) in.readObject();
                if (level > 0)
                {
                    Logger.getLogger(ServerRunnable.class.getName()).log(Level.INFO, "Level to generate: {0}", level);
                    app.IPToSend = (String) socket.getInetAddress().toString();
                    app.generateEdges(level);
                }
            }
            catch (NumberFormatException | IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(ServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

