/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelle
 */
public class JSF32_Week12_NoGUI implements Observer
{

    private KochFractal fractal = new KochFractal();
    private ArrayList<Edge> edges = new ArrayList<>();

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        JSF32_Week12_NoGUI prog = new JSF32_Week12_NoGUI();
        prog.start();
    }

    public void start() throws IOException
    {
        fractal.addObserver(this);
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        System.out.println("Voor welk level moeten de edges gegenereerd worden?");
        String input = reader.readLine();
        try
        {
            Integer level = Integer.parseInt(input);
            if (level < 0)
            {
                throw new NumberFormatException();
            }
            
            fractal.setLevel(level);
            fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();
            writeEdges();
            

        } catch (NumberFormatException exc)
        {
            System.err.println("Ongeldig level!");
        }
    }

    public void writeEdges()
    {
        FileWriter fw;
        try
        {
            fw = new FileWriter("edges.txt");
            PrintWriter pr = new PrintWriter(fw);
            
            for (Edge e : edges)
            {
                pr.println(e.toString());
            }
            
            pr.close();
            
            System.out.println("De edges zijn opgeslagen!");
        } catch (IOException ex)
        {

        }
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        edges.add(e);
        System.out.println("updated");
    }
}
