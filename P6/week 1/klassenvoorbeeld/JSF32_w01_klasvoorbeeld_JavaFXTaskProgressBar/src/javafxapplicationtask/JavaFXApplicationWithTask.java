/*
 * Source:http://java-buddy.blogspot.nl/2013/08/javafx-example-task1.html
 */
package javafxapplicationtask;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class JavaFXApplicationWithTask extends Application {

    private Task task = null;
    private int taskNumber = 0;
    
    private final ProgressBar progressBar = new ProgressBar();
    private final Label labelCount = new Label();
    
    @Override
    public void start(Stage primaryStage) {
   
        // Provides information about state
        final Label labelState = new Label();

        Button btnStart = new Button("Start New Task");
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (task != null)
                {
                    task.cancel();
                }
                createTask();
                new Thread(task).start();
            }
        });

        Button btnReadTaskState = new Button("Read Task State");
        btnReadTaskState.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                labelState.setText(task.getState().toString());
            }
        });
        
        Button btnCancelTask = new Button("Cancel Task");
        btnCancelTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                task.cancel();
            }
        });
                
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(6, 6, 6, 6));
        vBox.setSpacing(6);
        vBox.getChildren().addAll(
                progressBar,
                labelCount,
                btnStart,
                btnReadTaskState,
                btnCancelTask,
                labelState);

        StackPane root = new StackPane();
        root.getChildren().add(vBox);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void createTask() {
        // There's a task that performs some work
        task = new MyJavaFXTask("Task: " + taskNumber);
        taskNumber++;

        if(task!=null)
        {
            progressBar.progressProperty().unbind();
            labelCount.textProperty().unbind();
        }
        
        progressBar.setProgress(0);
        progressBar.progressProperty().bind(task.progressProperty());

        // Provides information about count        
        labelCount.textProperty().bind(task.messageProperty());       
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
