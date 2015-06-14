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
        client = new Client("localhost", 4444, application);     
    }

    public synchronized void increaseCount()
    {
        count++;
    }

    public void changeLevel(int nxt) throws InterruptedException, ExecutionException, BrokenBarrierException
    {
        application.clearKochPanel();
    }

    public void drawEdges() throws InterruptedException, BrokenBarrierException, FileNotFoundException, IOException
    {

    }
}
