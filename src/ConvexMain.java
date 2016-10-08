/**
 * Created by igor on 26.04.16.
 */
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;

import com.controls.Convex;
import com.objects.*;
import com.controls.Task;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConvexMain extends Application {
  final public static int CANVAS_WIDTH = 480;
  final public static int CANVAS_HEIGHT = 480;
  final public static int CANVAS_POINT_RADIUS = 4;
  final public static double CANVAS_LINE_WIDTH = 2.0;
  final public static int RANDOM_MAX_N = 40;
  final public static int RANDOM_MAX_XY = 100;

  Canvas canvas;
  TextArea pointsTextArea;
  double minX, maxX, minY, maxY, scaleX, scaleY;

  public static void main(String[] args) {
    launch(args);
  }

  public void clearCanvas() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setFill(Color.WHITE);
    gc.setStroke(new Color(0.53, 0.53, 0.53, 1.));
    gc.clearRect(0,0,CANVAS_WIDTH, CANVAS_HEIGHT);
    gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    gc.strokeLine(0, 0, 0, CANVAS_HEIGHT);
    gc.strokeLine(0, CANVAS_HEIGHT, 480, CANVAS_HEIGHT);
    gc.strokeLine(CANVAS_WIDTH, CANVAS_HEIGHT, CANVAS_WIDTH, 0);
    gc.strokeLine(CANVAS_WIDTH, 0, 0, 0);
  }

  @Override
  public void start(Stage primaryStage) {
    GridPane pane = new GridPane();
    pane.setPadding(new Insets(15, 15, 15, 15));
    pane.setVgap(15);
    pane.setHgap(15);

    canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    clearCanvas();

    pane.add(canvas, 0, 0, 1, 3);

    Button fileBtn = new Button("Файл");
    fileBtn.setMinWidth(80);
    fileBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(primaryStage);
        if (f != null) {
          try {
            Scanner textScan = new Scanner(f);
            String text = "";
            while (textScan.hasNextLine()) {
              text += textScan.nextLine() + "\n";
            }
            pointsTextArea.setText(text);
            scanText(new Scanner(f));
          } catch (FileNotFoundException e) {
          }
        }
      }
    });
    pane.add(fileBtn, 1, 0);

    Button randButton = new Button("Случайно");
    randButton.setMinWidth(80);
    randButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String text = "";
        int n = (int)Math.round(Math.random() * Integer.toUnsignedLong(RANDOM_MAX_N - 1)) + 1;
        for (int i = 0; i < n; i++) {
          text += Math.round(Math.random() * (RANDOM_MAX_XY - 1)) + 1 + "," + Math.round(Math.random() * 10) + " ";
          text += Math.round(Math.random() * (RANDOM_MAX_XY - 1)) + 1 + "," + Math.round(Math.random() * 10) + "\n";
        }
        pointsTextArea.setText(text);
        Scanner scanner = new Scanner(text);
        scanText(scanner);
      }
    });
    pane.add(randButton, 2, 0);

    pointsTextArea = new TextArea();
    pointsTextArea.setFont(Font.font("Monospace", FontWeight.BLACK, 14));
    pointsTextArea.setMaxWidth(175);
    pointsTextArea.setMinHeight(397);
    pane.add(pointsTextArea, 1, 1, 2, 1);

    Button buildButton = new Button("Построить оболочку");
    buildButton.setMinWidth(175);
    buildButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String text = pointsTextArea.getText();
        Scanner scanner = new Scanner(text);
        scanText(scanner);
      }
    });
    pane.add(buildButton, 1, 2, 2, 1);

    Scene sc = new Scene(pane, 700, 510);

    primaryStage.setTitle("Внешняя оболочка");
    primaryStage.setScene(sc);
    primaryStage.setResizable(true);
    primaryStage.show();
  }
  public void scanText(Scanner scanner) {
    double x, y;

    ArrayList<Point> points = new ArrayList<>();

    while (scanner.hasNextDouble()) {
      x = scanner.nextDouble();
      if (scanner.hasNextDouble()) {
        y = scanner.nextDouble();
        points.add(new Point(x, y));
      }
    }
    if (points.size() == 0) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, введите точки.");
      alert.showAndWait();
    } else
      buildConvex(points);
  }

  public void buildConvex(ArrayList<Point> points) {
    minX = points.get(0).X(); maxX = points.get(0).X();
    minY = points.get(0).Y(); maxY = points.get(0).Y();
    for (int i = 0; i < points.size(); i++) {
      Point p = points.get(i);
      if (p.X() > maxX) maxX = p.X();
      if (p.X() < minX) minX = p.X();
      if (p.Y() > maxY) maxY = p.Y();
      if (p.Y() < minY) minY = p.Y();
    }

    Convex convex = new Convex(points);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    clearCanvas();
    scaleX = CANVAS_WIDTH / (maxX - minX + 2);
    scaleY = CANVAS_HEIGHT / (maxY - minY + 2);
    scaleX = Math.min(scaleX, scaleY);
    scaleY = scaleX;
    gc.setFill(Color.BLACK);
    for (int i = 0; i < points.size(); i++) {
      Point p = points.get(i);
      gc.fillOval(getScaleX(p.X()) - CANVAS_POINT_RADIUS, getScaleY(p.Y()) - CANVAS_POINT_RADIUS, CANVAS_POINT_RADIUS * 2, CANVAS_POINT_RADIUS * 2);
    }
    gc.setStroke((new Color(0.05,0.45,0.67,1.)));
    gc.setLineWidth(CANVAS_LINE_WIDTH);
    gc.setFill((new Color(0.05,0.45,0.67,1.)));
    int size = convex.getConvex().size();
    for (int i = 0; i < size; i++) {
      Point p = convex.getConvex().get(i);
      gc.fillOval(getScaleX(p.X()) - CANVAS_POINT_RADIUS, getScaleY(p.Y()) - CANVAS_POINT_RADIUS, CANVAS_POINT_RADIUS * 2, CANVAS_POINT_RADIUS * 2);
      gc.strokeLine(getScaleX(convex.getConvex().get(i % size).X()), getScaleY(convex.getConvex().get(i % size).Y()), getScaleX(convex.getConvex().get((i+1) % size).X()), getScaleY(convex.getConvex().get((i+1) % size).Y()));
    }
  }

  private double getScaleX(double x) {
    return (x - minX + 1) * scaleX;
  }
  private double getScaleY(double y) {
    return CANVAS_HEIGHT - (y - minY + 1) * scaleY;
  }
}