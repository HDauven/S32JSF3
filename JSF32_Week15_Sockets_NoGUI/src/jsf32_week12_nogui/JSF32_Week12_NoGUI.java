/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelle
 */
public class JSF32_Week12_NoGUI implements Observer
{

    private static final Logger LOG = Logger.getLogger(JSF32_Week12_NoGUI.class.getName());
    private KochFractal fractal = new KochFractal();
    public ArrayList<Edge> edges = new ArrayList<>();
    private Integer level = 0;
    private TimeStamp ts = new TimeStamp();
    public ArrayList<ServerRunnable> connectedClients;
    private ServerSocket server;
    private ServerRunnable serverRunnable;
    private static JSF32_Week12_NoGUI prog;
    public String IPToSend = "";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        prog = new JSF32_Week12_NoGUI();
        prog.start();
    }

    public void start() throws IOException
    {
        fractal.addObserver(this);
        connectedClients = new ArrayList<>();
//        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
//        BufferedReader reader = new BufferedReader(inputStreamReader);
//        System.out.println("Voor welk level moeten de edges gegenereerd worden?");
//        String input = reader.readLine();

        try
        {
            server = new ServerSocket(4444);
            LOG.log(Level.INFO, "Server is listening on port: {0}", server.getLocalPort());
        } catch (IOException ex)
        {
            System.err.println("Failed to listen on port 4444.");
        }

        try
        {
            LOG.log(Level.INFO, "Searching for clients...");
            while (true)
            {
                try
                {
                    Socket s = server.accept();
                    LOG.log(Level.INFO, "New Client Connected: {0} ", s.getInetAddress());
                    serverRunnable = new ServerRunnable(s, prog);
                    connectedClients.add(serverRunnable);
                    Thread thr = new Thread(serverRunnable);
                    thr.setDaemon(false);
                    thr.start();
                } catch (IOException | ClassNotFoundException ex)
                {
                    Logger.getLogger(JSF32_Week12_NoGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception ex)
        {
            Logger.getLogger(JSF32_Week12_NoGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateEdges(int level)
    {
        try
        {
            if (level < 0)
            {
                throw new NumberFormatException();
            }

            fractal.setLevel(level);

            fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();
        } catch (NumberFormatException exc)
        {
            System.err.println("Ongeldig level!");
        }
    }

    public void writeTextFileNoBuffer()
    {
        FileWriter fw;
        try
        {
            fw = new FileWriter("textNoBuffer.txt");
            PrintWriter pr = new PrintWriter(fw);

            ts.init();
            ts.setBegin("Start textNoBuffer");

            pr.println(level);

            for (Edge e : edges)
            {
                pr.println(e.toString());
            }

            pr.close();

            ts.setEnd("End textNoBuffer");

            System.out.println("De edges zijn opgeslagen in een text file zonder buffer! " + ts.toString());
        } catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        } finally
        {

        }

    }

    public void writeTextFileWithBuffer()
    {
        FileWriter fw;
        BufferedWriter bw;
        try
        {
            fw = new FileWriter("textWithBuffer.txt");
            bw = new BufferedWriter(fw);

            ts.init();
            ts.setBegin("Start textWithBuffer");

            bw.write(String.valueOf(level));

            for (Edge e : edges)
            {
                bw.write(e.toString());
            }

            bw.flush();
            bw.close();

            ts.setEnd("End textWithBuffer");

            System.out.println("De edges zijn opgeslagen in een text file met buffer! " + ts.toString());
        } catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    public void writeBinaryFileNoBuffer()
    {
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("binaryNoBuffer.dat"));

            ts.init();
            ts.setBegin("Start binaryNoBuffer");

            //out.write(level);
            for (Edge e : edges)
            {
                out.writeObject(e);
            }

            out.flush();
            out.close();

            ts.setEnd("End binaryNoBuffer");

            System.out.println("De edges zijn opgeslagen in een binary file zonder buffer! " + ts.toString());
        } catch (IOException ioe)
        {
            System.err.println(ioe.getMessage());
        }
    }

    public void writeBinaryFileWithBuffer()
    {
        try
        {
            FileOutputStream file = new FileOutputStream("binaryWithBuffer.dat");
            BufferedOutputStream bos = new BufferedOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(bos);

            ts.init();
            ts.setBegin("Start binaryWithBuffer");

            for (Edge e : edges)
            {
                out.writeObject(e);
            }

            out.flush();
            out.close();

            ts.setEnd("End binaryWithBuffer");

            System.out.println("De edges zijn opgeslagen in een binary file met buffer! " + ts.toString());
        } catch (IOException ioe)
        {
            System.err.println(ioe.getMessage());
        }
    }

    @Override
    public synchronized void update(Observable o, Object arg)
    {
        //System.out.println("Update method called!");
        Edge e = (Edge) arg;
        e.colorValue = e.color.toString();
        this.edges.add(e);
        for (ServerRunnable client : connectedClients)
        {
            if (client.socket.getInetAddress().toString().equals(IPToSend))
            {
                client.writeEdge(e);
            }
        }
    }
}
