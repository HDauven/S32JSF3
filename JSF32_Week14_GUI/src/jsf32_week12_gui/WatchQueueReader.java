/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_gui;

import java.io.IOException;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Jelle
 */
public class WatchQueueReader implements Runnable
{

    private WatchService myWatcher;
    private JSF32_Week12_GUI application;

    public WatchQueueReader(WatchService myWatcher, JSF32_Week12_GUI application)
    {
        this.myWatcher = myWatcher;
        this.application = application;
    }

    @Override
    public void run()
    {
        KochManager manager = new KochManager(application);
        try
        {
            WatchKey key = myWatcher.take();
            while (key != null)
            {
                for (WatchEvent event : key.pollEvents())
                {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    System.out.println(kind.name() + ": " + fileName);

                    if (kind == ENTRY_MODIFY && fileName.toString().equals("binaryWithBuffer.dat"))
                    {
                        Platform.runLater(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                try
                                {
                                    manager.drawBinaryWithBuffer();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(WatchQueueReader.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                        });
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            Logger.getLogger(WatchQueueReader.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
