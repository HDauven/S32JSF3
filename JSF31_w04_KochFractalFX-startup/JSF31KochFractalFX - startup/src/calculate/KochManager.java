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
private KochFractal koch;                   //De KochFractal.
private ArrayList<Edge> edges;              //ArrayList met alle edges.
private ArrayList<String> timesAndEdges;    //ArrayList met de edges en tijd.
private TimeStamp tsCalc;                   //TimeStamp voor het berekenen van de edges.
private TimeStamp tsDraw;                   //TimeStamp voor het tekenen van de edges.

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
    edges.clear();
    
    //Start de TimeStamp vóór het genereren van de edges en stop daarna.
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
    
    //Start de TimeStamp vóór het tekenen van de edges en stop daarna.
    tsDraw.init();
    tsDraw.setBegin("Begin Drawing");
    for (Edge e : edges) //Loop door de ArrayList van edges en teken deze.
    {
        application.drawEdge(e);
    }
    
    tsDraw.setEnd("End Drawing");
    application.setTextDraw(tsDraw.toString());
    
    Integer nrOfEdges = koch.getNrOfEdges();          //Haal het aantal edges op en sla deze op in nrOfEdges.
    application.setTextNrEdges(nrOfEdges.toString()); //Converteer nrOfEdges naar een String
                                                      //zodat deze gebruikt kan worden in setTextNrEdges.
    timesAndEdges.add(tsDraw.toString());
    timesAndEdges.add(nrOfEdges.toString());
}

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge)arg); //Voeg de edge toe aan de ArrayList.
    }

}
