package hanoi;

public class Process {

    private int moveFrom;
    private int moveTo;

    @Override
    public String toString() {
        return "Process [moveFrom=" + moveFrom + ", moveTo=" + moveTo + "]";
    }

    public Process(int moveFrom, int moveTo) {
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public int getMoveFrom() {
        return moveFrom;
    }

    public int getMoveTo() {
        return moveTo;
    }

}
