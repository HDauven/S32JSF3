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
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

/**
 *
 * @author Jelle
 */
public class JavaFXTask extends Task<ArrayList<Edge>> implements Observer {

    KochFractal koch;
    int nxtlevel;
    int edge;
    int MAX;
    int nrOfEdgesGenerated;
    ArrayList<Edge> edges;
    CyclicBarrier barrier;
    JSF31KochFractalFX application;

    public JavaFXTask(CyclicBarrier cb, KochFractal kf, int nxtlvl, int edge, JSF31KochFractalFX app) {
        this.koch = kf;
        this.nxtlevel = nxtlvl;
        this.edge = edge;
        this.edges = new ArrayList<>();
        this.barrier = cb;
        this.MAX = koch.getNrOfEdges();
        this.nrOfEdgesGenerated = 0;
        this.application = app;
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        e.color = Color.WHITE;
        //application.drawEdge(e);
        this.edges.add((Edge) arg);
        nrOfEdgesGenerated++;
        updateProgress(nrOfEdgesGenerated, MAX);
        updateMessage(String.valueOf(nrOfEdgesGenerated));
    }

    /**
     *
     * @return @throws Exception
     */
    @Override
    public ArrayList<Edge> call() throws Exception {
        koch.addObserver(this);
        koch.setLevel(nxtlevel);
        switch (edge) {
            case 1:
                koch.generateBottomEdge();
            case 2:
                koch.generateLeftEdge();
            case 3:
                koch.generateRightEdge();
        }
        barrier.await();
        return edges;
    }
}
