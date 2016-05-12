import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by alexb on 25.04.2016.
 */
public class Tetris extends Group{
    private int gSize = Main.SIZE; //grid size
    private char shape;
    private int rotation;
    private int column;
    public double x;
    public double y;
    private int[][] matrix = new int[4][4];
    private int height;
    private int width;
    private int index = -1;
    public static final char[] shapes = {'I', 'O', 'J', 'L', 'T', 'Z', 'S'};
    private static final Color[] colors = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.PURPLE,
            Color.PINK
    };
    private static final int[] SHAPE_I_0 = {0,0,1,0,2,0,3,0};
    private static final int[] SHAPE_I_1 = {0,0,0,1,0,2,0,3};
    private static final int[] SHAPE_O = {0,0,0,1,1,0,1,1};
    private static final int[] SHAPE_J_0 = {0,2,1,2,2,1,2,2};
    private static final int[] SHAPE_J_1 = {0,0,1,0,1,1,1,2};
    private static final int[] SHAPE_J_2 = {0,1,0,2,1,1,2,1};
    private static final int[] SHAPE_J_3 = {0,0,0,1,0,2,1,2};
    private static final int[] SHAPE_L_0 = {0,1,1,1,2,1,2,2};
    private static final int[] SHAPE_L_1 = {0,0,0,1,0,2,1,0};
    private static final int[] SHAPE_L_2 = {1,0,1,1,2,1,3,1};
    private static final int[] SHAPE_L_3 = {0,2,1,0,1,1,1,2};
    private static final int[] SHAPE_T_0 = {0,1,1,0,1,1,1,2};
    private static final int[] SHAPE_T_1 = {0,0,1,0,1,1,2,0};
    private static final int[] SHAPE_T_2 = {0,0,0,1,0,2,1,1};
    private static final int[] SHAPE_T_3 = {0,1,1,0,1,1,2,1};
    private static final int[] SHAPE_Z_0 = {0,0,0,1,1,1,1,2};
    private static final int[] SHAPE_Z_1 = {0,1,1,0,1,1,2,0};
    private static final int[] SHAPE_S_0 = {0,1,0,2,1,0,1,1};
    private static final int[] SHAPE_S_1 = {0,0,1,0,1,1,2,1};
    public Tetris(char shape, int rotation, int column, double x, double y) {
        this.shape = shape;
        for (int i = 0; i < shapes.length; i++) {
            if (shape == shapes[i]) {
                index = i;
                break;
            }
        }
        this.column = column;
        this.x = x;
        this.y = y;
        rotate(rotation);
        draw();
        addX(column * gSize);
    }
    public void rotate () {
        rotate((rotation + 1) % 4);
    }
    public void rotate(int rotation) {
        matrix = new int[4][4];
        this.rotation = rotation;
        if (shape == 'I') {
            if (rotation % 2 == 0) {
                make(SHAPE_I_0, 1);
            }
            if (rotation % 2 == 1) {
                make(SHAPE_I_1, 1);
            }
        }
        if (shape == 'O') {
            make(SHAPE_O, 2);
        }
        if (shape == 'J') {
            if (rotation % 4 == 0) {
                make(SHAPE_J_0, 1);
            }
            if (rotation % 4 == 1) {
                make(SHAPE_J_1, 1);
            }
            if (rotation % 4 == 2) {
                make(SHAPE_J_2, 1);
            }
            if (rotation % 4 == 3) {
                make(SHAPE_J_3, 1);
            }
        }
        if (shape == 'L') {
            if (rotation % 4 == 0) {
                make(SHAPE_L_0, 1);
            }
            if (rotation % 4 == 1) {
                make(SHAPE_L_1, 1);
            }
            if (rotation % 4 == 2) {
                make(SHAPE_L_2, 1);
            }
            if (rotation % 4 == 3) {
                make(SHAPE_L_3, 1);
            }
        }
        if (shape == 'T') {
            if (rotation % 4 == 0) {
                make(SHAPE_T_0, 1);
            }
            if (rotation % 4 == 1) {
                make(SHAPE_T_1, 1);
            }
            if (rotation % 4 == 2) {
                make(SHAPE_T_2, 1);
            }
            if (rotation % 4 == 3) {
                make(SHAPE_T_3, 1);
            }
        }
        if (shape == 'Z') {
            if (rotation % 2 == 0) {
                make(SHAPE_Z_0, 1);
            }
            if (rotation % 2 == 1) {
                make(SHAPE_Z_1, 1);
            }
        }
        if (shape == 'S') {
            if (rotation % 2 == 0) {
                make(SHAPE_S_0, 1);
            }
            if (rotation % 2 == 1) {
                make(SHAPE_S_1, 1);
            }
        }
        draw();
    }
    private void make (int[] data, int nr) {
        for (int i = 0; i < data.length ; i+= 2) {
            matrix[data[i]][data[i+1]] = nr;
            if(data[i] > height) {
                height = data[i];
            }
            if(data[i + 1] > width){
                width = data[i + 1];
            }
        }
    }
    public void draw() {
        getChildren().clear();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length ; j++) {
                if (matrix[i][j] > 0) {
                    Rectangle rect = new Rectangle(x + j * gSize, y + i * gSize, gSize, gSize);
                    rect.setFill(colors[index]);
                    getChildren().add(rect);
                }
            }
        }
    }

    public void addY(double dy) {
        y += dy;
        for (Node n : getChildren()) {
            if (n instanceof Rectangle) {
                Rectangle r = (Rectangle) n;
                r.setY(r.getY() + dy);
            }
        }
    }
    public void addX(double dx) {
        x += dx;
        for (Node n : getChildren()) {
            if (n instanceof Rectangle) {
                Rectangle r = (Rectangle) n;
                r.setX(r.getX() + dx);
            }
        }
    }

    public void removeTile(double dx, double dy){
        for(Node n : getChildren()){
            if (n instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) n;
                if(rectangle.getX() == dx && rectangle.getY() == dy){
                    getChildren().remove(rectangle);
                }
            }
        }
    }
}