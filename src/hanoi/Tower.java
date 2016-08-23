package hanoi;

import java.util.LinkedList;

import javafx.scene.shape.Rectangle;

public class Tower {

    private int id;
    private LinkedList<Rectangle> step;

    public Tower(int id) {
        this.id = id;
        this.step = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "id: " + id + step.toString();
    }

    public void set(Rectangle r) {
        this.step.push(r);
    }

    public Rectangle getSize() {
        if (this.step.size() > 0) {
            return this.step.pop();
        }
        return null;
    }

    public void move(Tower toTower) {
        toTower.set(this.getSize());
    }

    public int getId() {
        return id;
    }

    public LinkedList<Rectangle> getStep() {
        return step;
    }

}
