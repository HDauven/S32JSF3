/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_week12_gui;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Nico Kuijpers
 */
public class JSF32_Week12_GUI extends Application
{

    // Zoom and drag
    private double zoomTranslateX = 0.0;
    private double zoomTranslateY = 0.0;
    private double zoom = 1.0;
    private double startPressedX = 0.0;
    private double startPressedY = 0.0;
    private double lastDragX = 0.0;
    private double lastDragY = 0.0;

    // Koch manager
    // TO DO: Create class KochManager in package calculate
    private KochManager kochManager;

    // Current level of Koch fractal
    private int currentLevel = 1;

    // Labels for level, nr edges, calculation time, and drawing time
    public Label labelLevel;
    private Label labelNrEdges;
    private Label labelNrEdgesText;
    private Label labelCalc;
    private Label labelCalcText;
    private Label labelDraw;
    private Label labelDrawText;
    private Label labelProgressLeft;
    private Label labelProgressBottom;
    private Label labelProgressRight;
    private Label labelEdgesLeft;
    private Label labelEdgesBottom;
    private Label labelEdgesRight;

    //Progressbars
    private ProgressBar progressLeft;
    private ProgressBar progressBottom;
    private ProgressBar progressRight;

    // Koch panel and its size
    private Canvas kochPanel;
    private final int kpWidth = 500;
    private final int kpHeight = 500;

    private boolean textNoBuffer = false;
    private boolean textWithBuffer = false;
    private boolean binaryNoBuffer = false;
    private boolean binaryWithBuffer = false;

    public boolean getTextNoBuffer()
    {
        return this.textNoBuffer;
    }

    public boolean getTextWithBuffer()
    {
        return this.textWithBuffer;
    }

    public boolean getBinaryNoBuffer()
    {
        return this.binaryNoBuffer;
    }

    public boolean getBinaryWithBuffer()
    {
        return this.binaryWithBuffer;
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException, ExecutionException, BrokenBarrierException
    {

        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);
        // Drawing panel for Koch fractal
        kochPanel = new Canvas(kpWidth, kpHeight);
        grid.add(kochPanel, 0, 3, 25, 1);

        // Labels to present number of edges for Koch fractal
        labelNrEdges = new Label("Nr edges:");
        labelNrEdgesText = new Label();
        grid.add(labelNrEdges, 0, 0, 4, 1);
        grid.add(labelNrEdgesText, 3, 0, 22, 1);

        // Labels to present time of calculation for Koch fractal
        labelCalc = new Label("Calculating:");
        labelCalcText = new Label();
        grid.add(labelCalc, 0, 1, 4, 1);
        grid.add(labelCalcText, 3, 1, 22, 1);

        // Labels to present time of drawing for Koch fractal
        labelDraw = new Label("Drawing:");
        labelDrawText = new Label();
        grid.add(labelDraw, 0, 2, 4, 1);
        grid.add(labelDrawText, 3, 2, 22, 1);

        // Label to present current level of Koch fractal
        labelLevel = new Label("Level: " + currentLevel);
        grid.add(labelLevel, 0, 6);

        // Labels edges
        labelProgressLeft = new Label("Progress left:");
        grid.add(labelProgressLeft, 0, 8);

        labelProgressBottom = new Label("Progress bottom:");
        grid.add(labelProgressBottom, 0, 10);

        labelProgressRight = new Label("Progress right:");
        grid.add(labelProgressRight, 0, 12);

        // Button to increase level of Koch fractal
//        Button buttonIncreaseLevel = new Button();
//        buttonIncreaseLevel.setText("Increase Level");
//        buttonIncreaseLevel.setOnAction(new EventHandler<ActionEvent>()
//        {
//            @Override
//            public void handle(ActionEvent event)
//            {
//                try
//                {
//                    increaseLevelButtonActionPerformed(event);
//                } catch (InterruptedException | ExecutionException | BrokenBarrierException ex)
//                {
//                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        grid.add(buttonIncreaseLevel, 3, 6);
//        buttonIncreaseLevel.setDisable(true);

        // Button to decrease level of Koch fractal
//        Button buttonDecreaseLevel = new Button();
//        buttonDecreaseLevel.setText("Decrease Level");
//        buttonDecreaseLevel.setOnAction(new EventHandler<ActionEvent>()
//        {
//            @Override
//            public void handle(ActionEvent event)
//            {
//                try
//                {
//                    decreaseLevelButtonActionPerformed(event);
//                } catch (InterruptedException | ExecutionException | BrokenBarrierException ex)
//                {
//                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        grid.add(buttonDecreaseLevel, 5, 6);
//        buttonDecreaseLevel.setDisable(true);

        // Progress bars
        progressLeft = new ProgressBar();
        progressLeft.setProgress(0);
        grid.add(progressLeft, 5, 8);

        progressBottom = new ProgressBar();
        progressBottom.setProgress(0);
        grid.add(progressBottom, 5, 10);

        progressRight = new ProgressBar();
        progressRight.setProgress(0);
        grid.add(progressRight, 5, 12);

        // Button to fit Koch fractal in Koch panel
//        Button buttonFitFractal = new Button();
//        buttonFitFractal.setText("Fit Fractal");
//        buttonFitFractal.setOnAction(new EventHandler<ActionEvent>()
//        {
//            @Override
//            public void handle(ActionEvent event)
//            {
//                try
//                {
//                    fitFractalButtonActionPerformed(event);
//                } catch (InterruptedException | BrokenBarrierException ex)
//                {
//                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex)
//                {
//                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        grid.add(buttonFitFractal, 10, 6);
//        buttonFitFractal.setDisable(true);

        CheckBox cbTextNoBuffer = new CheckBox();
        cbTextNoBuffer.setText("Textfile zonder buffer");
        cbTextNoBuffer.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    textNoBuffer = true;
                    textWithBuffer = false;
                    binaryNoBuffer = false;
                    binaryWithBuffer = false;
                    kochManager.drawEdges();
                } catch (InterruptedException | BrokenBarrierException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        grid.add(cbTextNoBuffer, 0, 4);

        CheckBox cbTextWithBuffer = new CheckBox();
        cbTextWithBuffer.setText("Textfile met buffer");
        cbTextWithBuffer.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    textWithBuffer = true;
                    textNoBuffer = false;
                    binaryNoBuffer = false;
                    binaryWithBuffer = false;
                    kochManager.drawEdges();
                } catch (InterruptedException | BrokenBarrierException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        grid.add(cbTextWithBuffer, 2, 4);

        CheckBox cbBinaryNoBuffer = new CheckBox();
        cbBinaryNoBuffer.setText("Binary zonder buffer");
        cbBinaryNoBuffer.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    binaryNoBuffer = true;
                    binaryWithBuffer = false;
                    textNoBuffer = false;
                    textWithBuffer = false;
                    kochManager.drawEdges();
                } catch (InterruptedException | BrokenBarrierException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        grid.add(cbBinaryNoBuffer, 4, 4);

        CheckBox cbBinaryWithBuffer = new CheckBox();
        cbBinaryWithBuffer.setText("Binary met buffer");
        cbBinaryWithBuffer.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    binaryWithBuffer = true;
                    binaryNoBuffer = false;
                    textNoBuffer = false;
                    textWithBuffer = false;
                    kochManager.drawEdges();
                } catch (InterruptedException | BrokenBarrierException | IOException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        grid.add(cbBinaryWithBuffer, 5, 4);

        // Label progress number of edges
        labelEdgesLeft = new Label("Nr edges: ");
        grid.add(labelEdgesLeft, 7, 8);

        labelEdgesBottom = new Label("Nr edges: ");
        grid.add(labelEdgesBottom, 7, 10);

        labelEdgesRight = new Label("Nr edges: ");
        grid.add(labelEdgesRight, 7, 12);

        // Add mouse clicked event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        try
                        {
                            kochPanelMouseClicked(event);
                        } catch (InterruptedException | BrokenBarrierException ex)
                        {
                            Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex)
                        {
                            Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        // Add mouse pressed event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        kochPanelMousePressed(event);
                    }
                });

        // Add mouse dragged event to Koch panel
        kochPanel.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                try
                {
                    kochPanelMouseDragged(event);
                } catch (InterruptedException | BrokenBarrierException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Create Koch manager and set initial level
        resetZoom();
        kochManager = new KochManager(this);
        kochManager.changeLevel(currentLevel);

        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, kpWidth + 50, kpHeight + 300);
        root.getChildren().add(grid);

        // Define title and assign the scene for main window
        primaryStage.setTitle("Koch Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void clearKochPanel()
    {
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        gc.clearRect(0.0, 0.0, kpWidth, kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0, 0.0, kpWidth, kpHeight);
    }

    public void drawEdge(final Edge e)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Graphics
                GraphicsContext gc = kochPanel.getGraphicsContext2D();

                // Adjust edge for zoom and drag
                Edge e1 = edgeAfterZoomAndDrag(e);

                // Set line color
                gc.setStroke(e1.color);

                // Set line width depending on level
                if (currentLevel <= 3)
                {
                    gc.setLineWidth(2.0);
                } else if (currentLevel <= 5)
                {
                    gc.setLineWidth(1.5);
                } else
                {
                    gc.setLineWidth(1.0);
                }

                // Draw line
                gc.strokeLine(e1.X1, e1.Y1, e1.X2, e1.Y2);
            }

        });

    }

    public void setTextNrOfEdges(String text)
    {
        labelNrEdgesText.setText(text);
    }

    public void setTextCalc(String text)
    {
        labelCalcText.setText(text);
    }

    public void setTextDraw(String text)
    {
        labelDrawText.setText(text);
    }

    public void bindProgressBars(Task t1, Task t2, Task t3)
    {
        progressLeft.progressProperty().bind(t1.progressProperty());
        progressBottom.progressProperty().bind(t2.progressProperty());
        progressRight.progressProperty().bind(t3.progressProperty());
        labelEdgesLeft.textProperty().bind(t1.messageProperty());
        labelEdgesBottom.textProperty().bind(t2.messageProperty());
        labelEdgesRight.textProperty().bind(t3.messageProperty());

    }

    public synchronized void requestDrawEdges()
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    kochManager.drawEdges();
                } catch (InterruptedException | BrokenBarrierException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex)
                {
                    Logger.getLogger(JSF32_Week12_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void increaseLevelButtonActionPerformed(ActionEvent event) throws InterruptedException, ExecutionException, BrokenBarrierException
    {
        if (currentLevel < 12)
        {
            // resetZoom();
            currentLevel++;
            labelLevel.setText("Level: " + currentLevel);
            kochManager.changeLevel(currentLevel);
        }
    }

    private void decreaseLevelButtonActionPerformed(ActionEvent event) throws InterruptedException, ExecutionException, BrokenBarrierException
    {
        if (currentLevel > 1)
        {
            // resetZoom();
            currentLevel--;
            labelLevel.setText("Level: " + currentLevel);
            kochManager.changeLevel(currentLevel);
        }
    }

    private void fitFractalButtonActionPerformed(ActionEvent event) throws InterruptedException, BrokenBarrierException, IOException
    {
        resetZoom();
        kochManager.drawEdges();
    }

    private void kochPanelMouseClicked(MouseEvent event) throws InterruptedException, BrokenBarrierException, IOException
    {
        if (Math.abs(event.getX() - startPressedX) < 1.0
                && Math.abs(event.getY() - startPressedY) < 1.0)
        {
            double originalPointClickedX = (event.getX() - zoomTranslateX) / zoom;
            double originalPointClickedY = (event.getY() - zoomTranslateY) / zoom;
            if (event.getButton() == MouseButton.PRIMARY)
            {
                zoom *= 2.0;
            } else if (event.getButton() == MouseButton.SECONDARY)
            {
                zoom /= 2.0;
            }
            zoomTranslateX = (int) (event.getX() - originalPointClickedX * zoom);
            zoomTranslateY = (int) (event.getY() - originalPointClickedY * zoom);
            kochManager.drawEdges();
        }
    }

    private void kochPanelMouseDragged(MouseEvent event) throws InterruptedException, BrokenBarrierException, IOException
    {
        zoomTranslateX = zoomTranslateX + event.getX() - lastDragX;
        zoomTranslateY = zoomTranslateY + event.getY() - lastDragY;
        lastDragX = event.getX();
        lastDragY = event.getY();
        kochManager.drawEdges();
    }

    private void kochPanelMousePressed(MouseEvent event)
    {
        startPressedX = event.getX();
        startPressedY = event.getY();
        lastDragX = event.getX();
        lastDragY = event.getY();
    }

    private void resetZoom()
    {
        int kpSize = Math.min(kpWidth, kpHeight);
        zoom = kpSize;
        zoomTranslateX = (kpWidth - kpSize) / 2.0;
        zoomTranslateY = (kpHeight - kpSize) / 2.0;
    }

    private Edge edgeAfterZoomAndDrag(Edge e)
    {
        return new Edge(
                e.X1 * zoom + zoomTranslateX,
                e.Y1 * zoom + zoomTranslateY,
                e.X2 * zoom + zoomTranslateX,
                e.Y2 * zoom + zoomTranslateY,
                e.color);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
