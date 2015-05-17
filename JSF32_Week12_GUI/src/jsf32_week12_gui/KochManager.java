/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_gui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

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
    private CyclicBarrier cb;
    private Task<ArrayList<Edge>> task1 = null;
    private Task<ArrayList<Edge>> task2 = null;
    private Task<ArrayList<Edge>> task3 = null;
    private int lineNumber = 1;
    private String level = "0";

    public KochManager(JSF32_Week12_GUI application)
    {
        this.application = application;
        //koch.addObserver(Runnable);
        edges = new ArrayList<>();
        tsCalc = new TimeStamp();
        tsDraw = new TimeStamp();
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
                        Edge edge = new Edge(X1, Y1, X2, Y2, color);
                        application.drawEdge(edge);

                    }

                    lineNumber++;
                }

                application.labelLevel.setText("Level: " + level);
                application.setTextNrOfEdges(String.valueOf(lineNumber - 1));

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
                while (inputScanner.hasNextLine())
                {
                    String regel = inputScanner.nextLine();

                    String[] parameters = regel.split(",");
                    for (int i = 4; i < parameters.length; i = i + 4)
                    {
                        double X1 = Double.valueOf(parameters[i - 4]);
                        double Y1 = Double.valueOf(parameters[i - 3]);
                        double X2 = Double.valueOf(parameters[i - 2]);
                        double Y2 = Double.valueOf(parameters[i - 1]);
                        Color color = Color.valueOf(parameters[i]);
                        Edge edge = new Edge(X1, Y1, X2, Y2, color);
                        application.drawEdge(edge);
                    }

                    lineNumber++;
                }

                application.labelLevel.setText("Level: " + level);
                application.setTextNrOfEdges(String.valueOf(br.lines().count()));
            }
            catch (Exception e)
            {

            }
            finally
            {
                fr.close();
                br.close();
                inputScanner.close();
            }

        }
    }
}
