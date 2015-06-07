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
    private Socket socket;
    private JSF32_Week12_NoGUI app;

    public ServerRunnable(Socket s) throws IOException
    {
        this.socket = s;
        app = new JSF32_Week12_NoGUI();
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    public ServerRunnable()
    {
        
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
                    Logger.getLogger(ServerRunnable.class.getName()).log(Level.INFO,
                            "Level to generate: {0}", level);

                    app.edges.clear();
                    app.generateEdges(level);
                }
            }
            catch (IOException | ClassNotFoundException | NumberFormatException ex)
            {
                Logger.getLogger(ServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

}
