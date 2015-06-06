/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Jelle
 */
public class Client
{

    private ServerRunnable server;
    public static ArrayList<Edge> edges;
    private Socket socket;

    public Client(String IP, int port) throws IOException
    {
        socket = new Socket(IP, port);
        edges = new ArrayList<>();
        server = new ServerRunnable(socket);
    }

}
