/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Future;

/**
 *
 * @author Jelle
 */
public class Task implements Callable<ArrayList<Edge>>, Observer {

    KochFractal koch;
    int nxtlevel;
    int edge;
    ArrayList<Edge> edges;
    

    public Task(KochFractal kf, int nxtlvl, int edge) {
        this.koch = kf;
        this.nxtlevel = nxtlvl;
        this.edge = edge;
        this.edges = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.edges.add((Edge) arg);
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
        return edges;
    }
}
