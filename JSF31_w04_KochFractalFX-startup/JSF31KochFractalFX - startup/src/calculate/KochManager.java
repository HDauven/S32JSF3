/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Jelle
 */
public class KochManager implements Observer {
private JSF31KochFractalFX application;
private KochFractal koch;
private ArrayList<Edge> edges;
private TimeStamp ts;

public KochManager(JSF31KochFractalFX application) {
    this.application = application;
    koch = new KochFractal();
    koch.addObserver(this);
    edges = new ArrayList<>();
    ts = new TimeStamp();
}

public void changeLevel(int nxt) {
    koch.setLevel(nxt);
    drawEdges();
}

public void drawEdges() {
    ts.setBegin("Begin drawEdges()");
    application.clearKochPanel();
    koch.generateLeftEdge();
    koch.generateBottomEdge();
    koch.generateRightEdge();
    for (Edge e : edges)
    {
        application.drawEdge(e);
    }
    ts.setEnd("End drawEdges()");
    application.setTextCalc(ts.toString());
    Integer nrOfEdges = koch.getNrOfEdges();
    application.setTextNrEdges(nrOfEdges.toString());
}

    @Override
    public void update(Observable o, Object arg) {      
        edges.add((Edge)arg);
    }

}
