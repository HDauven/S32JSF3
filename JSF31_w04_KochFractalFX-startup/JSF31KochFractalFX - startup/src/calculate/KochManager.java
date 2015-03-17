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
private ArrayList<String> timesAndEdges;
private TimeStamp tsCalc;
private TimeStamp tsDraw;

public KochManager(JSF31KochFractalFX application) {
    this.application = application;
    koch = new KochFractal();
    koch.addObserver(this);
    edges = new ArrayList<>();
    tsCalc = new TimeStamp();
    tsDraw = new TimeStamp();
    timesAndEdges = new ArrayList<>();
}

public void changeLevel(int nxt) {
    koch.setLevel(nxt);
    
    tsCalc.init();
    tsCalc.setBegin("Begin Calculate");
    koch.generateLeftEdge();
    koch.generateBottomEdge();
    koch.generateRightEdge();
    tsCalc.setEnd("End Calculate");
    
    application.setTextCalc(tsCalc.toString());
    drawEdges();
}

public void drawEdges() {
    application.clearKochPanel();
    
    tsDraw.init();
    tsDraw.setBegin("Begin Drawing");
    for (Edge e : edges)
    {
        application.drawEdge(e);
    }
    tsDraw.setEnd("End Drawing");
    application.setTextDraw(tsDraw.toString());
    
    Integer nrOfEdges = koch.getNrOfEdges();
    application.setTextNrEdges(nrOfEdges.toString());
    
    timesAndEdges.add(tsDraw.toString());
    timesAndEdges.add(nrOfEdges.toString());
}

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge)arg);
    }

}
