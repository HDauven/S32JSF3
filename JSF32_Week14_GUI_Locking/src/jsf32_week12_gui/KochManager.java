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
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import jsf32_week12_nogui.Edge;

/**
 *
 * @author Jelle
 */
public class KochManager {

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

    public KochManager(JSF32_Week12_GUI application) {
        this.application = application;
        //koch.addObserver(Runnable);
        edges = new ArrayList<>();
        tsCalc = new TimeStamp();
        tsDraw = new TimeStamp();
    }

    public synchronized void increaseCount() {
        count++;
    }

    public void changeLevel(int nxt) throws InterruptedException, ExecutionException, BrokenBarrierException {

        application.clearKochPanel();

        //koch.setLevel(nxt);
        if (task1 != null && task2 != null && task3 != null) {
            if (task1.isRunning() || task2.isRunning() || task3.isRunning()) {
                task1.cancel();
                task2.cancel();
                task3.cancel();
            }
        }

        edges.clear();

        application.setTextCalc(tsCalc.toString());
    }

    public void drawEdges() throws InterruptedException, BrokenBarrierException, FileNotFoundException, IOException {
        application.clearKochPanel();

        if (application.getTextNoBuffer()) {
            ts.init();
            ts.setBegin("Start textNoBuffer");
            lineNumber = 1;
            Path dir = Paths.get(System.getProperty("user.dir"));
            dir = dir.getParent();
            dir = Paths.get(dir.toString() + "/JSF32_Week14_NoGUI_Locking");
            String file = dir.toString() + "/textNoBuffer.txt";

            FileReader fr = new FileReader(file);
            Scanner inputScanner = new Scanner(fr);
            try {
                while (inputScanner.hasNextLine()) {
                    String regel = inputScanner.nextLine();

                    if (lineNumber == 1) {
                        level = regel;
                    } else {
                        String[] parameters = regel.split(",");
                        double X1 = Double.valueOf(parameters[0]);
                        double Y1 = Double.valueOf(parameters[1]);
                        double X2 = Double.valueOf(parameters[2]);
                        double Y2 = Double.valueOf(parameters[3]);
                        Color color = Color.valueOf(parameters[4]);
                        Edge edge = new Edge(X1, Y1, X2, Y2, color, Integer.valueOf(level));
                        application.drawEdge(edge);

                    }

                    lineNumber++;
                }

                ts.setEnd("End textNoBuffer");

                System.out.println("De edges zijn ingelezen uit een text file zonder buffer! " + ts.toString());

                application.labelLevel.setText("Level: " + level);
                application.setTextNrOfEdges(String.valueOf(lineNumber - 2));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fr.close();
                inputScanner.close();
            }
        } else if (application.getTextWithBuffer()) {
            Path dir = Paths.get(System.getProperty("user.dir"));
            dir = dir.getParent();
            dir = Paths.get(dir.toString() + "/JSF32_Week14_NoGUI_Locking");
            String file = dir.toString() + "/textWithBuffer.txt";

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            Scanner inputScanner = new Scanner(br);
            int counter = 0;
            try {
                ts.init();
                ts.setBegin("Start textWithBuffer");
                while (inputScanner.hasNext()) {
                    String regel = inputScanner.next();
                    StringBuilder sb = new StringBuilder(regel);

                    level = regel.substring(0, 1);
                    sb.deleteCharAt(0);
                    regel = sb.toString();

                    String[] parameters = regel.split(",");
                    for (int i = 4; i < parameters.length; i = i + 5) {
                        double X1 = Double.valueOf(parameters[i - 4]);
                        double Y1 = Double.valueOf(parameters[i - 3]);
                        double X2 = Double.valueOf(parameters[i - 2]);
                        double Y2 = Double.valueOf(parameters[i - 1]);
                        String kleur = parameters[i];
                        Color color = Color.valueOf(kleur);
                        Edge edge = new Edge(X1, Y1, X2, Y2, color, Integer.valueOf(level));
                        application.drawEdge(edge);
                        counter++;
                    }

                    lineNumber++;
                }

                ts.setEnd("End textWithBuffer");

                System.out.println("De edges zijn uitgelezen uit een text file met buffer! " + ts.toString());
                application.labelLevel.setText("Level: " + level);
                application.setTextNrOfEdges(String.valueOf(counter));
            } catch (Exception e) {
                Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                fr.close();
                br.close();
                inputScanner.close();
            }

        } else if (application.getBinaryNoBuffer()) {
            Path dir = Paths.get(System.getProperty("user.dir"));
            dir = dir.getParent();
            dir = Paths.get(dir.toString() + "/JSF32_Week14_NoGUI_Locking");
            String file = dir.toString() + "/binaryNoBuffer.dat";

            RandomAccessFile ras = new RandomAccessFile(file, "rw");
            FileChannel fc = ras.getChannel();
            MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
            out.position(0);
            Edge edge = null;
            int levelEdge = 0, counter = 0;
            try {
                ts.init();
                ts.setBegin("Start binaryNoBuffer");
                while (out.hasRemaining()) {
                    double X1 = out.getDouble();
                    double Y1 = out.getDouble();
                    double X2 = out.getDouble();
                    double Y2 = out.getDouble();
                    double red = out.getDouble();
                    double green = out.getDouble();
                    double blue = out.getDouble();
                    levelEdge = out.getInt();
                    edge = new Edge(X1, Y1, X2, Y2, new Color(red, green, blue, 1), levelEdge);
                    application.drawEdge(edge);
                    //System.out.println(out.getDouble());
                    //edge = (Edge) in.readObject();
                    //edge.color = Color.valueOf(edge.colorValue);
                    //levelEdge = edge.level;
                    //System.out.println(edge.X1 + " " + edge.Y1 + " " + edge.X2 + " " + edge.Y2 + " " + edge.color.toString() + " " + edge.level);
                    //application.drawEdge(edge);
                    counter++;
                }

                ts.setEnd("End binaryNoBuffer");

            } catch (Exception ioe) {
                Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ioe);
            } finally {
                System.out.println("De edges zijn uitgelezen uit een binary file zonder buffer! " + ts.toString());
                application.labelLevel.setText("Level: " + levelEdge);
                application.setTextNrOfEdges(String.valueOf(counter));
            }
        } else if (application.getBinaryWithBuffer()) {
            Path dir = Paths.get(System.getProperty("user.dir"));
            dir = dir.getParent();
            dir = Paths.get(dir.toString() + "/JSF32_Week14_NoGUI_Locking");
            String file = dir.toString() + "/binaryWithBuffer.dat";

            RandomAccessFile ras = new RandomAccessFile(file, "rw");
            FileChannel fc = ras.getChannel();
            MappedByteBuffer in = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());

            try {
                Thread t1 = new Thread(new Runnable() {
                    Edge edge = null;
                    int levelEdge = 0, counter = 0;

                    int startRegion = 4;
                    int regionSize = 64;
                    FileLock exclusiveLock = null;

                    @Override
                    public void run() {
                        try {
                            ts.init();
                            ts.setBegin("Start binaryWithBuffer");
                            int status = -1;

                            while (startRegion < fc.size()) {
                                exclusiveLock = fc.lock(0, 4, true);
                                status = in.getInt(0);
                                exclusiveLock.release();

                                if (status == 1) {
                                    exclusiveLock = fc.lock(startRegion, regionSize, true);
                                    in.position(startRegion);
                                    double X1 = in.getDouble();
                                    double Y1 = in.getDouble();
                                    double X2 = in.getDouble();
                                    double Y2 = in.getDouble();
                                    double red = in.getDouble();
                                    double green = in.getDouble();
                                    double blue = in.getDouble();
                                    levelEdge = in.getInt();
                                    edge = new Edge(X1, Y1, X2, Y2, new Color(red, green, blue, 1), levelEdge);
                                    application.drawEdge(edge);
                                    startRegion += 64;
                                    in.putInt(0, 0);
                                    exclusiveLock.release();
                                    counter++;
                                } else {
                                    Thread.sleep(20);
                                }
                            }
                        } catch (IOException | InterruptedException ex) {
                            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            if (exclusiveLock != null) {
                                try {
                                    exclusiveLock.release();
                                    application.labelLevel.setText("Level: " + levelEdge);
                                    application.setTextNrOfEdges(String.valueOf(counter));
                                } catch (IOException ex) {
                                    Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                });
                t1.start();
            } catch (Exception ioe) {
                Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ioe);
            } finally {
                ts.setEnd("End binaryWithBuffer");
                System.out.println("De edges zijn uitgelezen uit een binary file met buffer! " + ts.toString());
            }
//            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/binaryWithBuffer.dat");
//            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fs));
//            Edge edge = null;
//            int levelEdge = 0, counter = 0;
//            try
//            {
//                ts.init();
//                ts.setBegin("Start binaryWithBuffer");
//                while (true)
//                {
//                    edge = (Edge) in.readObject();
//                    edge.color = Color.valueOf(edge.colorValue);
//                    levelEdge = edge.level;
//                    application.drawEdge(edge);
//                    counter++;
//                }
//            }
//            catch (ClassNotFoundException ex)
//            {
//                Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            finally
//            {
//                ts.setEnd("End binaryWithBuffer");
//
//                System.out.println("De edges zijn uitgelezen uit een binary file met buffer! " + ts.toString());
//                in.close();
//                application.labelLevel.setText("Level: " + levelEdge);
//                application.setTextNrOfEdges(String.valueOf(counter));
//            }
        }
    }
}
