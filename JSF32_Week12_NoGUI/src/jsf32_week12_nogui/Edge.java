/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_nogui;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class Edge implements Serializable
{

    public double X1, Y1, X2, Y2;
    public transient Color color;

    public Edge(double X1, double Y1, double X2, double Y2, Color color)
    {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
        this.color = color;
    }

    @Override
    public String toString()
    {
        return this.X1 + " , " + this.Y1 + " , " + this.X2 + " , " + this.Y2 + " , " + this.color.toString();
    }
}
