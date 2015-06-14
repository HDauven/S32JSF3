/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_gui;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import jsf32_week12_nogui.Edge;

/**
 *
 * @author Jelle
 */
public class ClientRunnable implements Runnable
{

    private Socket socket = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private JSF32_Week12_GUI app;
    private ArrayList<Edge> edges;

    public ClientRunnable(Socket socket, JSF32_Week12_GUI application) throws IOException
    {
        this.socket = socket;
        this.app = application;
        this.edges = new ArrayList<>();
        out = new ObjectOutputStream(this.socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Edge edge = (Edge) in.readObject();
                edges.add(edge);
                app.drawEdge(edge);
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
