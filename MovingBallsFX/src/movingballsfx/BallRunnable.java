/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable
{

    private Ball ball;
    private BallMonitorRW monitor;

    public BallRunnable(Ball ball, BallMonitorRW monitor)
    {
        this.ball = ball;
        this.monitor = monitor;
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                if (ball.getColor() == Color.RED)
                {
                    if (ball.isEnteringCs())
                    {
                        monitor.enterReader();
                    }
                    else if (ball.isLeavingCs())
                    {
                        monitor.exitReader();
                    }
                }
                else if (ball.getColor() == Color.BLUE)
                {
                    if (ball.isEnteringCs())
                    {
                        monitor.enterWriter();
                    }
                    else if (ball.isLeavingCs())
                    {
                        monitor.exitWriter();
                    }
                }
                ball.move();

                Thread.sleep(ball.getSpeed());
            }
            catch (InterruptedException ex)
            {
                if (ball.getColor() == Color.RED)
                {
                    monitor.interruptedReader(ball);
                }
                else if (ball.getColor() == Color.BLUE)
                {
                    monitor.interruptedWriter(ball);
                }
                Thread.currentThread().interrupt();
            }
        }
    }
}
