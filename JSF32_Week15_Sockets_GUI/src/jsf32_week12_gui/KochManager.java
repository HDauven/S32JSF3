/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_gui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import jsf32_week12_nogui.Edge;

/**
 *
 * @author Jelle
 */
public class KochManager
{

    private JSF32_Week12_GUI application;
    //private KochFractal koch;                   //De KochFractal.
    private ArrayList<Edge> edges;              //ArrayList met alle edges.
    private TimeStamp tsCalc;                   //TimeStamp voor het berekenen van de edges.
    private TimeStamp tsDraw;                   //TimeStamp voor het tekenen van de edges.
    private int count = 0;
    private ExecutorService pool = Executors.newFixedThreadPool(3);
    private Task<ArrayList<Edge>> task1 = null;
    private Task<ArrayList<Edge>> task2 = null;
    private Task<ArrayList<Edge>> task3 = null;
    private int lineNumber = 1;
    private String level = "0";
    private TimeStamp ts = new TimeStamp();
    public Client client;

    public KochManager(JSF32_Week12_GUI application) throws IOException
    {
        this.application = application;
        //koch.addObserver(Runnable);
        edges = new ArrayList<>();
        tsCalc = new TimeStamp();
        tsDraw = new TimeStamp();
        client = new Client("localhost", 4444);
        getEdges();
        
    }

    public synchronized void increaseCount()
    {
        count++;
    }

    public void changeLevel(int nxt) throws InterruptedException, ExecutionException, BrokenBarrierException
    {

        application.clearKochPanel();

        //koch.setLevel(nxt);
        if (task1 != null && task2 != null && task3 != null)
        {
            if (task1.isRunning() || task2.isRunning() || task3.isRunning())
            {
                task1.cancel();
                task2.cancel();
                task3.cancel();
            }
        }

        edges.clear();

        application.setTextCalc(tsCalc.toString());
    }

    public void drawEdges() throws InterruptedException, BrokenBarrierException, FileNotFoundException, IOException
    {
        application.clearKochPanel();

        if (application.getTextNoBuffer())
        {
            ts.init();
            ts.setBegin("Start textNoBuffer");
            lineNumber = 1;
            FileReader fr = new FileReader(System.getProperty("user.dir") + "/textNoBuffer.txt");
            Scanner inputScanner = new Scanner(fr);
            try
            {
                while (inputScanner.hasNextLine())
                {
                    String regel = inputScanner.nextLine();

                    if (lineNumber == 1)
                    {
                        level = regel;
                    }
                    else
                    {
                        String[] parameters = regel.split(",");
                        double X1 = Double.valueOf(parameters[0]);
                        double Y1 = Double.valueOf(parameters[1]);
                        double X2 = Double.valueOf(parameters[2]);
                        double Y2 = Double.valueOf(parameters[3]);
                        Color color = Color.valueOf(parameters[4]);
                        Edge edge = new Edge(X1, Y1, X2, Y2, color, Integer.valueOf(level));
                        application.drawEdge(edge);

                    }

                    lineNumber++;
                }

                ts.setEnd("End textNoBuffer");

                System.out.println("De edges zijn ingelezen uit een text file zonder buffer! " + ts.toString());

                application.labelLevel.setText("Level: " + level);
                application.setTextNrOfEdges(String.valueOf(lineNumber - 2));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                fr.close();
                inputScanner.close();
            }
        }
        else if (application.getTextWithBuffer())
        {
            FileReader fr = new FileReader(System.getProperty("user.dir") + "/textWithBuffer.txt");
            BufferedReader br = new BufferedReader(fr);
            Scanner inputScanner = new Scanner(br);
            int counter = 0;
            try
            {
                ts.init();
                ts.setBegin("Start textWithBuffer");
                while (inputScanner.hasNext())
                {
                    String regel = inputScanner.next();
                    StringBuilder sb = new StringBuilder(regel);
                    System.out.println(regel);

                    level = regel.substring(0, 1);
                    sb.deleteCharAt(0);
                    regel = sb.toString();
                    System.out.println(level);

                    String[] parameters = regel.split(",");
                    for (int i = 4; i < parameters.length; i = i + 5)
                    {
                        double X1 = Double.valueOf(parameters[i - 4]);
                        double Y1 = Double.valueOf(parameters[i - 3]);
                        double X2 = Double.valueOf(parameters[i - 2]);
                        double Y2 = Double.valueOf(parameters[i - 1]);
                        String kleur = parameters[i];
                        Color color = Color.valueOf(kleur);
                        Edge edge = new Edge(X1, Y1, X2, Y2, color, Integer.valueOf(level));
                        application.drawEdge(edge);
                        counter++;
                    }

                    lineNumber++;
                }

                ts.setEnd("End textWithBuffer");

                System.out.println("De edges zijn uitgelezen uit een text file met buffer! " + ts.toString());
                application.labelLevel.setText("Level: " + level);
                application.setTextNrOfEdges(String.valueOf(counter));
            }
            catch (Exception e)
            {
                Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, e);
            }
            finally
            {
                fr.close();
                br.close();
                inputScanner.close();
            }

        }
        else if (application.getBinaryNoBuffer())
        {
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/binaryNoBuffer.dat");
            ObjectInputStream in = new ObjectInputStream(fs);
            Edge edge = null;
            int levelEdge = 0, counter = 0;
            try
            {
                ts.init();
                ts.setBegin("Start binaryNoBuffer");
                while (true)
                {
                    edge = (Edge) in.readObject();
                    edge.color = Color.valueOf(edge.colorValue);
                    levelEdge = edge.level;
                    //System.out.println(edge.X1 + " " + edge.Y1 + " " + edge.X2 + " " + edge.Y2 + " " + edge.color.toString() + " " + edge.level);
                    application.drawEdge(edge);
                    counter++;
                }

            }
            catch (Exception ioe)
            {
                Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ioe);
            }
            finally
            {
                ts.setEnd("End binaryNoBuffer");

                System.out.println("De edges zijn uitgelezen uit een binary file zonder buffer! " + ts.toString());
                in.close();
                application.labelLevel.setText("Level: " + levelEdge);
                application.setTextNrOfEdges(String.valueOf(counter));
            }
        }
        else if (application.getBinaryWithBuffer())
        {
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/binaryWithBuffer.dat");
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fs));
            Edge edge = null;
            int levelEdge = 0, counter = 0;
            try
            {
                ts.init();
                ts.setBegin("Start binaryWithBuffer");
                while (true)
                {
                    edge = (Edge) in.readObject();
                    edge.color = Color.valueOf(edge.colorValue);
                    levelEdge = edge.level;
                    application.drawEdge(edge);
                    counter++;
                }
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                ts.setEnd("End binaryWithBuffer");

                System.out.println("De edges zijn uitgelezen uit een binary file met buffer! " + ts.toString());
                in.close();
                application.labelLevel.setText("Level: " + levelEdge);
                application.setTextNrOfEdges(String.valueOf(counter));
            }
        }
    }
    
        private void getEdges() {
        Thread thr = new Thread() {
            
            @Override
            public void run() {
                while (true) {
                    if (client.getEdge() != null) {
                            System.out.println("An edge has been received!");
                            Edge edge = client.getEdge();
                            edges.add(edge);
                            Platform.runLater(() -> {

                                application.drawEdge(edge);
                            });
                            //client.removeEdge(edge);
                    }
                }
            }
        };
        thr.setDaemon(true);
        thr.start();
    }
}
