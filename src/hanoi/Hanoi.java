package hanoi;

import java.util.Deque;
import java.util.LinkedList;

import javafx.scene.shape.Rectangle;

public class Hanoi {

    private static final int TOWER_ID_FROM  = 0;
    private static final int TOWER_ID_SPARE = 1;
    private static final int TOWER_ID_TO    = 2;

    private Deque<Process> processList = new LinkedList<>();

    Tower[] towers = { new Tower(TOWER_ID_FROM), new Tower(TOWER_ID_SPARE), new Tower(TOWER_ID_TO) };

    public Hanoi(Rectangle[] rectangles) {

        // 円盤の動かし方
        hanoi(TOWER_ID_FROM, TOWER_ID_TO, TOWER_ID_SPARE, rectangles.length);

        // ディスクの初期位置
        for (int i = 0; i < rectangles.length; i++) {
            towers[TOWER_ID_FROM].set(rectangles[i]);
        }
    }

    private void hanoi(int from, int to, int spare, int numberOfDisk) {
        if (numberOfDisk > 1) {
            hanoi(from, spare, to, numberOfDisk - 1);
            processList.add(new Process(from, to));
            hanoi(spare, to, from, numberOfDisk - 1);
        } else {
            processList.add(new Process(from, to));
        }
    }

    public Process move() {
        if (processList.size() == 0) {
            return null;
        }
        Process p = processList.pop();
        towers[p.getMoveFrom()].move(towers[p.getMoveTo()]);
        return p;
    }

    public Tower[] getTowers() {
        return towers;
    }

    public Tower getTowerFirst() {
        return towers[TOWER_ID_FROM];
    }

    public Tower getTowerLast() {
        return towers[TOWER_ID_TO];
    }

    public Deque<Process> getProcessList() {
        return processList;
    }

}
