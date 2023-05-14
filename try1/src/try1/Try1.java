/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package try1;

import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 *
 * @author lcarl
 */
public class Try1 extends Application {

    private int Cylinders;
    private int headStart;
    private int[] requests;
    private int[] traversalPath;
    private TextField cylinderField;
    private TextField headStartField;
    private TextField requestsField;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("SSTF Algorithm");

        // Create labels and text fields for user input
        Label cylinderLabel = new Label("Number of cylinders:");
        cylinderField = new TextField();
        cylinderField.setPromptText("Enter the number of cylinders");

        Label headStartLabel = new Label("Starting head position:");
        headStartField = new TextField();
        headStartField.setPromptText("Enter the starting head position");

        Label requestsLabel = new Label("Disk requests (separated by spaces):");
        requestsField = new TextField();
        requestsField.setPromptText("Enter the disk requests");

        // Create button to trigger the algorithm calculation and result display
        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> {
            int cylinders = Integer.parseInt(cylinderField.getText());
            int headStart = Integer.parseInt(headStartField.getText());
            String[] requestStrings = requestsField.getText().split(" ");
            int[] requests = new int[requestStrings.length];

            for (int i = 0; i < requestStrings.length; i++) {
                try {
                    int request = Integer.parseInt(requestStrings[i]);
                    if (request < 0 || request >= cylinders) {
                        System.out.println("Error: Request " + request + " is out of range [0, " + (cylinders-1) + "]");
                        return;
                    }
                    requests[i] = request;
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Invalid request format: " + requestStrings[i]);
                    return;
                }
            }

            int[] traversalPath = calculateTraversalPath(headStart, requests);
            int seekTime = calculateShortestSeekTime(headStart, requests);
            printTraversalPath(traversalPath);
            System.out.println("Total head movement: " + seekTime);

            // Create a new stage to display the graph
            Stage graphStage = new Stage();
            graphStage.setTitle("Traversal Path");

            // Create axes for the graph
            NumberAxis xAxis = new NumberAxis(0, cylinders - 1, 1);
            NumberAxis yAxis = new NumberAxis(0, requests.length, 1);
            yAxis.setTickLabelRotation(90);

            // Create the line chart and add the traversal path data
            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < traversalPath.length; i++) {
                series.getData().add(new XYChart.Data<>(traversalPath[i], i));
            }
            lineChart.getData().add(series);

            // Set the chart title and show the stage
            lineChart.setTitle("Traversal Path");
            Scene graphScene = new Scene(lineChart, 800, 600);
            graphStage.setScene(graphScene);
            graphStage.show();
        });

        // Add UI elements to a grid pane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(cylinderLabel, 0, 0);
        gridPane.add(cylinderField, 1, 0);
        gridPane.add(headStartLabel, 0, 1);
        gridPane.add(headStartField, 1, 1);
        gridPane.add(requestsLabel, 0, 2);
        gridPane.add(requestsField, 1, 2);
        gridPane.add(calculateButton, 1, 3);

        Scene scene = new Scene(gridPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static int calculateShortestSeekTime(int headStart, int[] requests) {
        int totalSeekTime = 0;
        int currentPosition = headStart;
        int numRequests = requests.length;

        boolean[] visited = new boolean[numRequests];
        for (int i = 0; i < numRequests; i++) {
            int shortestDistance = Integer.MAX_VALUE;
            int shortestIndex = -1;

            for (int j = 0; j < numRequests; j++) {
                if (!visited[j]) {
                    int distance = Math.abs(requests[j] - currentPosition);
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        shortestIndex = j;
                    }
                }
            }

            visited[shortestIndex] = true;
            totalSeekTime += shortestDistance;
            currentPosition = requests[shortestIndex];
        }

        return totalSeekTime;
    }


    public static int[] calculateTraversalPath(int headStart, int[] requests) {
        int numRequests = requests.length;
        int[] traversalPath = new int[numRequests + 1];
        traversalPath[0] = headStart;

        boolean[] visited = new boolean[numRequests];
        for (int i = 0; i < numRequests; i++) {
            int shortestDistance = Integer.MAX_VALUE;
            int shortestIndex = -1;

            for (int j = 0; j < numRequests; j++) {
                if (!visited[j]) {
                    int distance = Math.abs(requests[j] - traversalPath[i]);
                    if (distance < shortestDistance || (distance == shortestDistance && j < shortestIndex)) {
                        shortestDistance = distance;
                        shortestIndex = j;
                    }
                }
            }

            visited[shortestIndex] = true;
            traversalPath[i + 1] = requests[shortestIndex];
        }

        return traversalPath;
    }



    public static void printTraversalPath(int[] traversalPath) {
        System.out.print("Traversal path: ");
        for (int i = 0; i < traversalPath.length - 1; i++) {
            int currentNode = traversalPath[i];
            int nextNode = traversalPath[i+1];
            int seekTime = Math.abs(nextNode - currentNode);
            System.out.println(currentNode + " -> " + nextNode + " [" + seekTime + "], ");
        }
        System.out.println(traversalPath[traversalPath.length-1]);
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
