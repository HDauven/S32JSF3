/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelle
 */
public class JSF32_Week12_NoGUI implements Observer {

    private KochFractal fractal = new KochFractal();
    private ArrayList<Edge> edges = new ArrayList<>();
    private Integer level = 0;
    private TimeStamp ts = new TimeStamp();

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        JSF32_Week12_NoGUI prog = new JSF32_Week12_NoGUI();
        prog.start();
    }

    public void start() throws IOException {
        fractal.addObserver(this);
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        System.out.println("Voor welk level moeten de edges gegenereerd worden?");
        String input = reader.readLine();
        try {
            level = Integer.parseInt(input);
            if (level < 0) {
                throw new NumberFormatException();
            }

            fractal.setLevel(level);

            fractal.generateBottomEdge();
            fractal.generateLeftEdge();
            fractal.generateRightEdge();

            writeTextFileNoBuffer();
            writeTextFileWithBuffer();
            writeBinaryFileNoBuffer();
            writeBinaryFileWithBuffer();
        } catch (NumberFormatException exc) {
            System.err.println("Ongeldig level!");
        }
    }

    public void writeTextFileNoBuffer() {
        FileWriter fw;
        try {
            fw = new FileWriter("textNoBuffer.txt");
            PrintWriter pr = new PrintWriter(fw);

            ts.init();
            ts.setBegin("Start textNoBuffer");

            pr.println(level);

            for (Edge e : edges) {
                pr.println(e.toString());
            }

            pr.close();

            ts.setEnd("End textNoBuffer");

            System.out.println("De edges zijn opgeslagen in een text file zonder buffer! " + ts.toString());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {

        }

    }

    public void writeTextFileWithBuffer() {
        FileWriter fw;
        BufferedWriter bw;
        try {
            fw = new FileWriter("textWithBuffer.txt");
            bw = new BufferedWriter(fw);

            ts.init();
            ts.setBegin("Start textWithBuffer");

            bw.write(String.valueOf(level));

            for (Edge e : edges) {
                bw.write(e.toString());
            }

            bw.flush();
            bw.close();

            ts.setEnd("End textWithBuffer");

            System.out.println("De edges zijn opgeslagen in een text file met buffer! " + ts.toString());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void writeBinaryFileNoBuffer() {
        try {
            RandomAccessFile ras = new RandomAccessFile("binaryNoBuffer.dat", "rw");
            FileChannel fc = ras.getChannel();
            int bytes = 60 * edges.size();
            MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, bytes);
            out.position(0);
            //ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("binaryNoBuffer.dat"));

            ts.init();
            ts.setBegin("Start binaryNoBuffer");

            //out.write(level);
            for (Edge e : edges) {
                out.putDouble(e.X1);
                out.putDouble(e.Y1);
                out.putDouble(e.X2);
                out.putDouble(e.Y2);
                out.putDouble(e.color.getRed());
                out.putDouble(e.color.getGreen());
                out.putDouble(e.color.getBlue());
                out.putInt(e.level);
            }

            ts.setEnd("End binaryNoBuffer");

            System.out.println("De edges zijn opgeslagen in een binary file zonder buffer! " + ts.toString());
        } catch (IOException ioe) {
            Logger.getLogger(JSF32_Week12_NoGUI.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    public void writeBinaryFileWithBuffer() {
        try {
            RandomAccessFile ras = new RandomAccessFile("binaryWithBuffer.dat", "rw");
            FileChannel fc = ras.getChannel();
            int bytes = 64 * edges.size();
            MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, bytes);

            int startRegion = 4;
            int regionSize = 64;

            FileLock exclusiveLock = null;

            ts.init();
            ts.setBegin("Start binaryWithBuffer");

            //out.write(level);
            Iterator<Edge> it = edges.iterator();
            Edge e;
            exclusiveLock = fc.lock(0, Integer.BYTES, false);
            out.position(0);
            out.putInt(0, 0);
            out.position(startRegion);
            exclusiveLock.release();
            while (true) {
                exclusiveLock = fc.lock(0, Integer.BYTES, false);
                int status = out.getInt(0);
                exclusiveLock.release();
 
                if (status == 0) {
                    if (it.hasNext()) {
                        exclusiveLock = fc.lock(startRegion, regionSize, false);
                        e = it.next();
                        out.putInt(0, 0);
                        out.position(startRegion);
                        out.putDouble(e.X1);
                        out.putDouble(e.Y1);
                        out.putDouble(e.X2);
                        out.putDouble(e.Y2);
                        out.putDouble(e.color.getRed());
                        out.putDouble(e.color.getGreen());
                        out.putDouble(e.color.getBlue());
                        out.putInt(e.level);
                        startRegion += 64;
                        //Zet status op 1 (er mag gelezen worden)
                        out.putInt(0, 1);
                        exclusiveLock.release();
                    }
                }
            }
        } catch (IOException ioe) {
            Logger.getLogger(JSF32_Week12_NoGUI.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            ts.setEnd("End binaryWithBuffer");
            System.out.println("De edges zijn opgeslagen in een binary file met buffer! " + ts.toString());
        }

//        try
//        {
//            FileOutputStream file = new FileOutputStream("binaryWithBuffer.dat");
//            BufferedOutputStream bos = new BufferedOutputStream(file);
//            ObjectOutputStream out = new ObjectOutputStream(bos);
//
//            ts.init();
//            ts.setBegin("Start binaryWithBuffer");
//
//            for (Edge e : edges)
//            {
//                out.writeObject(e);
//            }
//
//            out.flush();
//            out.close();
//
//            ts.setEnd("End binaryWithBuffer");
//
//            System.out.println("De edges zijn opgeslagen in een binary file met buffer! " + ts.toString());
//        } catch (IOException ioe)
//        {
//            System.err.println(ioe.getMessage());
//        }
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        e.colorValue = e.color.toString();
        this.edges.add(e);
    }
}
