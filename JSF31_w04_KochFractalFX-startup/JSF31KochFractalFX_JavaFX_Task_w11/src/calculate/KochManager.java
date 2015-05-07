/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import jsf31kochfractalfx.JavaFXTask;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Jelle
 */
public class KochManager
{

    private JSF31KochFractalFX application;
    private KochFractal koch;                   //De KochFractal.
    private ArrayList<Edge> edges;              //ArrayList met alle edges.
    private ArrayList<String> timesAndEdges;    //ArrayList met de edges en tijd.
    private TimeStamp tsCalc;                   //TimeStamp voor het berekenen van de edges.
    private TimeStamp tsDraw;                   //TimeStamp voor het tekenen van de edges.
    private int count = 0;
    private ExecutorService pool = Executors.newFixedThreadPool(3);
    private CyclicBarrier cb;
    private Task task1 = null;
    private Task task2 = null;
    private Task task3 = null;

    public KochManager(JSF31KochFractalFX application)
    {
        this.application = application;
        koch = new KochFractal();
        //koch.addObserver(Runnable);
        edges = new ArrayList<>();
        tsCalc = new TimeStamp();
        tsDraw = new TimeStamp();
        timesAndEdges = new ArrayList<>();
        this.cb = new CyclicBarrier(3);
    }

    public synchronized void increaseCount()
    {
        count++;
    }

    public void executeTasks(int lvl) throws InterruptedException, ExecutionException, BrokenBarrierException
    {
        task1 = new JavaFXTask(cb, koch, lvl, 1, application);
        task2 = new JavaFXTask(cb, koch, lvl, 2, application);
        task3 = new JavaFXTask(cb, koch, lvl, 3, application);
        application.bindProgressBars(task1, task2, task3);

        Future<ArrayList<Edge>> fut1 = (Future<ArrayList<Edge>>) pool.submit(task1);
        Future<ArrayList<Edge>> fut2 = (Future<ArrayList<Edge>>) pool.submit(task2);
        Future<ArrayList<Edge>> fut3 = (Future<ArrayList<Edge>>) pool.submit(task3);
      //  edges.addAll(fut1.get());
        //  edges.addAll(fut2.get());
        // edges.addAll(fut3.get());

        application.requestDrawEdges();
        //pool.shutdown();
    }

    public void changeLevel(int nxt) throws InterruptedException, ExecutionException, BrokenBarrierException
    {
        //koch.setLevel(nxt);
        if (task1 != null && task2 != null && task3 != null)
        {
            if (task1.isRunning() || task2.isRunning() || task3.isRunning())
            {
                koch.cancel();

                task1.cancel();
                task2.cancel();
                task3.cancel();
            }
        }

        edges.clear();

        //Start de TimeStamp vóór het genereren van de edges en stop daarna.
        tsCalc.init();
        tsCalc.setBegin("Begin Calculate");
        executeTasks(nxt);
        tsCalc.setEnd("End Calculate");

        application.setTextCalc(tsCalc.toString());
    }

    public void drawEdges() throws InterruptedException, BrokenBarrierException
    {
        application.clearKochPanel();

        //Start de TimeStamp vóór het tekenen van de edges en stop daarna.
        tsDraw.init();
        tsDraw.setBegin("Begin Drawing");
        for (Edge e : edges) //Loop door de ArrayList van edges en teken deze.
        {
            application.drawEdge(e);
        }
        tsDraw.setEnd("End Drawing");
        application.setTextDraw(tsDraw.toString());

        Integer nrOfEdges = koch.getNrOfEdges();         //Haal het aantal edges op en sla deze op in nrOfEdges.
        application.setTextNrEdges(nrOfEdges.toString()); //Converteer nrOfEdges naar een String
        //zodat deze gebruikt kan worden in setTextNrEdges.
        timesAndEdges.add(tsDraw.toString());
        timesAndEdges.add(nrOfEdges.toString());
    }
}
