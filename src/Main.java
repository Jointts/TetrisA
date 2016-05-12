import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alexb on 25.04.2016.
 */
public class Main extends Application {
    public static final int SIZE = 30;
    private int index = 0;

    Pane main;
    Timeline dropTimeline;
    Tetris activeTetris = null;
    int dropCount = 0;
    Random rnd = new Random();
    Rectangle borders;
    Tetris[][] board;
    @Override
    public void start(Stage primaryStage) throws Exception {

        main = new Pane();
        Scene scene = new Scene(main, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        borders = new Rectangle(SIZE, SIZE, SIZE * 10, SIZE * 20);
        borders.setFill(Color.TRANSPARENT);
        borders.setStroke(Color.BLACK);
        borders.setStrokeWidth(2);
        main.getChildren().add(borders);

        dropTimeline = new Timeline();
        dropTimeline.setCycleCount(Timeline.INDEFINITE);
        dropTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), event -> drop()));
        dropTimeline.play();
        remove();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        rotate();
                        break;
                    case DOWN:
                        drop();
                        break;
                    case LEFT:
                        LEFT();
                        break;
                    case RIGHT:
                        RIGHT();
                        break;
                }
            }
        });
    }
    private void remove(){
        int[] rows = new int[20];
        int[] points = new int[1];
        for (Node n : main.getChildren()) {
            if (!(n instanceof Rectangle)) continue;
            Rectangle r = (Rectangle) n;
            index = (int) r.getY() / SIZE;
            rows[index]++;
            if (rows[index] == 10) {
                points[1] = 100;
                System.out.println(points[1]);
            }
        }
    }
    private void LEFT() {
        activeTetris.addX(-SIZE);
        for (Node n : activeTetris.getChildren()) {
            if (!(n instanceof Rectangle)) continue;
            Rectangle r = (Rectangle) n;
            if (r.getX() < borders.getX()) {
                activeTetris.addX(SIZE);
                return;
            }
            for (Node nn : main.getChildren()) {
                if (!(nn instanceof Tetris )) continue;
                Tetris s2 = (Tetris) nn;
                if (s2 == activeTetris) continue;
                for (Node nnn : s2.getChildren()) {
                    if (!(nnn instanceof Rectangle)) continue;
                    Rectangle rr = (Rectangle) nnn;
                    if (rr.getX() == r.getX() && rr.getY() == r.getY()) {
                        activeTetris.addX(SIZE);
                        return;
                    }
                }
            }
        }
    }
    private void RIGHT() {
        activeTetris.addX(SIZE);
        for (Node n : activeTetris.getChildren()) {
            if (!(n instanceof Rectangle)) continue;
            Rectangle r = (Rectangle) n;
            if (r.getX() > borders.getX() + 9*SIZE) {
                activeTetris.addX(-SIZE);
                return;
            }
            for (Node nn : main.getChildren()) {
                if (!(nn instanceof Tetris )) continue;
                Tetris s2 = (Tetris) nn;
                if (s2 == activeTetris) continue;
                for (Node nnn : s2.getChildren()) {
                    if (!(nnn instanceof Rectangle)) continue;
                    Rectangle rr = (Rectangle) nnn;
                    if (rr.getX() == r.getX() && rr.getY() == r.getY()) {
                        activeTetris.addX(-SIZE);
                        return;
                    }
                }
            }
        }
    }
    private void rotate() {
        System.out.println("clicked");
        activeTetris.rotate();
        for (Node n : activeTetris.getChildren()) {
            if (!(n instanceof Rectangle)) continue;
            Rectangle r = (Rectangle) n;
            if (r.getX() > borders.getX()+ 9*SIZE) {
                activeTetris.addX(-SIZE);
                return;
            }
            if (r.getX() < borders.getX()) {
                activeTetris.addX(SIZE);
                return;
            }
            if (r.getY() < borders.getY()) {
                activeTetris.addY(SIZE);
                return;
            }
            for (Node nn : main.getChildren()) {
                if (!(nn instanceof Tetris)) continue;
                Tetris s2 = (Tetris) nn;
                if (s2 == activeTetris) continue;
                for (Node nnn : s2.getChildren()) {
                    if (!(nnn instanceof Rectangle)) continue;
                    Rectangle rr = (Rectangle) nnn;
                    if (rr.getX() == r.getX() && rr.getY() == r.getY()) {
                        activeTetris.addY(-SIZE);
                        return;
                    }
                }
            }
        }
    }
    private void drop(){
        System.out.println("drop");
        if (activeTetris == null) {
            char t = Tetris.shapes[rnd.nextInt(Tetris.shapes.length)];
            activeTetris  = new Tetris(t, 0, 4, SIZE, SIZE);
            main.getChildren().add(activeTetris);
            dropCount = 0;
            return;
        }
        boolean result = drop(activeTetris);
            if (!result) {
                if (dropCount == 0) {
                    System.out.println("GAME OVER!");
                    dropTimeline.stop();
                }
            activeTetris = null;
        }
        checkRow();
        dropCount++;
    }

    private void dropAll(){
        for(Node node : main.getChildren()){
            if(!(node instanceof Tetris)){continue;}
            Tetris tetris = (Tetris) node;
            drop(tetris);
        }
    }

    /**
     *
     * @param s
     * @return true if the tetris can drop
     */
    public boolean drop(Tetris s) {
        s.addY(SIZE);
        for (Node n : s.getChildren()) {
            if (!(n instanceof Rectangle)) continue;
            Rectangle r = (Rectangle) n;
            if (r.getY() > SIZE * 20) {
                System.out.println("r.gety:" + r.getY() );
                s.addY(-SIZE);
                return false;
            }
            for (Node nn : main.getChildren()) {
                if (!(nn instanceof Tetris )) continue;
                Tetris s2 = (Tetris) nn;
                if (s2 == s) continue;
                for (Node nnn : s2.getChildren()) {
                    if (!(nnn instanceof Rectangle)) continue;
                    Rectangle rr = (Rectangle) nnn;
                    /*
                    System.out.println(
                            "r:" + r.getY() + "," + r.getY() +
                                    " rr:" + rr.getX() + "," + rr.getY()
                    );
                    */
                    if (rr.getX() == r.getX() && rr.getY() == r.getY()) {
                        s.addY(-SIZE);
                        System.out.println("Returned False AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void checkRow(){

        double yCoordinate = 0;

        Map<Double, List<Double>> coordinates = new ConcurrentHashMap<>();
        for (Node block : main.getChildren()) {
            if (!(block instanceof Tetris )) continue;
            Tetris blockTetris = (Tetris) block;
            for (Node tile : blockTetris.getChildren()) {
                if (!(tile instanceof Rectangle)) continue;
                Rectangle r = (Rectangle) tile;
                try{
                    coordinates.get(r.getY()).add(r.getX());
                }catch(NullPointerException e){
                    List<Double> newList = new ArrayList<>();
                    newList.add(r.getX());
                    coordinates.put(r.getY(), newList);
                }
            }
        }
        List<Tetris> tetrisList = new ArrayList<>();

        for (Map.Entry<Double, List<Double>> entry : coordinates.entrySet())
        {
            if(entry.getValue().size() == 10){
                System.out.println("Full block found on Y: " + entry.getKey());
                for(Node r: main.getChildren()) {
                    if (!(r instanceof Tetris )) continue;
                    Tetris rr = (Tetris) r;
                    for(double x : entry.getValue()){
                        System.out.println("X: " + x + " Y: " + entry.getKey());
                        tetrisList.add(rr);
                    }
                }
                dropAll();
                yCoordinate = entry.getKey();
            }
        }


        List<Rectangle> rectangleList = new ArrayList<>();
        for(Tetris tetris: tetrisList){
            for(Node rectangle : tetris.getChildren()){
                if(!(rectangle instanceof Rectangle)){continue;}
                Rectangle rr = (Rectangle) rectangle;
                if(rr.getY() == yCoordinate){
                    rectangleList.add(rr);
                }
            }
            tetris.getChildren().removeAll(rectangleList);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}