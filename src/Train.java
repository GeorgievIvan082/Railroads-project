public class Train {
    private final int startX;
    private final int startY;
    private final int destinationX;
    private final int destinationY;



    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public Train(int startX, int startY, int destinationX, int destinationY) {
        this.startX = startX;
        this.startY = startY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }
}