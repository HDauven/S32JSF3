/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

/**
 *
 * @author Jelle
 */
public class JavaFXTask extends Task<ArrayList<Edge>> implements Observer
{

    KochFractal koch;
    int nxtlevel;
    int edgeNumber;
    int MAX;
    int nrOfEdgesGenerated;
    ArrayList<Edge> edges;
    JSF31KochFractalFX application;

    public JavaFXTask(int nxtlvl, int edgeNumber, JSF31KochFractalFX app)
    {
        this.koch = new KochFractal();
        this.nxtlevel = nxtlvl;
        this.edgeNumber = edgeNumber;
        this.edges = new ArrayList<>();
        this.MAX = koch.getNrOfEdges();
        this.nrOfEdgesGenerated = 0;
        this.application = app;
    }

    @Override
    public synchronized void update(Observable o, Object arg)
    {
        Edge e = (Edge) arg;
        Edge e2 = new Edge(e.X1, e.Y1, e.X2, e.Y2, e.color);
        e.color = Color.WHITE;
        application.drawEdge(e);
        try
        {
            Thread.sleep(6);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(JavaFXTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.edges.add(e2);
        nrOfEdgesGenerated++;
        updateProgress(nrOfEdgesGenerated, MAX);
        updateMessage(String.valueOf(nrOfEdgesGenerated));
    }

    /**
     *
     * @return @throws Exception
     */
    @Override
    public ArrayList<Edge> call() throws Exception
    {
        koch.addObserver(this);
        koch.setLevel(nxtlevel);
        switch (edgeNumber)
        {
            case 1:
                koch.generateBottomEdge();
                break;
            case 2:
                koch.generateLeftEdge();
                break;
            case 3:
                koch.generateRightEdge();
                break;
        }
        return edges;
    }
}
