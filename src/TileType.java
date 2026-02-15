import java.util.Random;

public enum TileType {
    HORIZONTAL,
    VERTICAL,
    TOP_RIGHT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
    TOP_LEFT,
    THREEWAY_TOP,
    THREEWAY_BOTTOM,
    THREEWAY_LEFT,
    THREEWAY_RIGHT,
    CROSSROAD;
    public boolean allowsUp() {
        switch (this) {
            case VERTICAL:
            case CROSSROAD:
            case THREEWAY_TOP:
            case THREEWAY_LEFT:
            case THREEWAY_RIGHT:
            case TOP_LEFT:
            case TOP_RIGHT:
                return true;
            default:
                return false;
        }
    }

    public boolean allowsDown() {
        switch (this) {
            case VERTICAL:
            case CROSSROAD:
            case THREEWAY_BOTTOM:
            case THREEWAY_LEFT:
            case THREEWAY_RIGHT:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return true;
            default:
                return false;
        }
    }

    public boolean allowsLeft() {
        switch (this) {
            case HORIZONTAL:
            case CROSSROAD:
            case THREEWAY_TOP:
            case THREEWAY_LEFT:
            case THREEWAY_BOTTOM:
            case BOTTOM_LEFT:
            case TOP_LEFT:
                return true;
            default:
                return false;
        }
    }

    public boolean allowsRight() {
        switch (this) {
            case HORIZONTAL:
            case CROSSROAD:
            case THREEWAY_TOP:
            case THREEWAY_BOTTOM:
            case THREEWAY_RIGHT:
            case BOTTOM_RIGHT:
            case TOP_RIGHT:
                return true;
            default:
                return false;
        }
    }
    public static TileType getRandomTileType() {
        TileType[] types = values();
        Random random = new Random();
        return types[random.nextInt(types.length)];
    }

}
