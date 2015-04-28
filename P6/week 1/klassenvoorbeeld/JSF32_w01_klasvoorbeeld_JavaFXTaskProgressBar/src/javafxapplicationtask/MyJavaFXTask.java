/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplicationtask;

import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author marcel
 */
public class MyJavaFXTask extends Task<Void> {

    // Identification string for the task
    private String name;

    private static final Logger LOG = Logger.getLogger(MyJavaFXTask.class.getName());

    public MyJavaFXTask(String name) {
        this.name = name;
    }

    /**
     * The call method is invoked on the background thread, therefore this
     * method can only manipulate states that are safe to read and write from a
     * background thread. For example, manipulating an active scene graph from
     * the call method throws runtime exceptions. On the other hand, the Task
     * class is designed to be used with JavaFX GUI applications, and it ensures
     * that any changes to public properties, change notifications for errors or
     * cancellation, event handlers, and states occur on the JavaFX Application
     * thread. Inside the call method, you can use the updateProgress,
     * updateMessage, updateTitle methods, which update the values of the
     * corresponding properties on the JavaFX Application thread. However, if
     * the task was canceled, a return value from the call method is ignored.
     * source:
     * https://docs.oracle.com/javase/8/javafx/interoperability-tutorial/concurrency.htm
     *
     * @return Void
     * @throws Exception
     */
    @Override
    protected Void call() throws Exception {
        final int MAX = 1000;
        for (int i = 1; i <= MAX; i++) {
            if (isCancelled()) {
                break;
            }
            updateProgress(i, MAX);
            updateMessage(name + " " + String.valueOf(i));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                if (isCancelled()) {
                    LOG.info("Exception occurred: " + e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * *
     * Called if execution state is Worker.State CANCELLED
     */
    @Override
    protected void cancelled() {
        super.cancelled();
        LOG.info(name + " cancelled()");
    }

    /***
     * Called if execution state is Worker.State FAILED
     * (see interface Worker<V>)
     */
    @Override
    protected void failed() {
        super.failed();
        LOG.info(name + " failed()");
    }

    /**
     * *
     * Called if execution state is Worker.State RUNNING
     */
    @Override
    protected void running() {
        super.running();
        LOG.info(name + " running()");
    }

    /**
     * *
     * Called if execution state is Worker.State SCHEDULED
     */
    @Override
    protected void scheduled() {
        super.scheduled();
        LOG.info(name + " scheduled()");
    }

    /**
     * *
     * Called if execution state is Worker.State SUCCEEDED
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        LOG.info(name + " succeeded()");
    }

    /***
     * Called if FutureTask behaviour is done
     */
    @Override
    protected void done() {
        super.done();
        LOG.info(name + " done()");
    }

}
