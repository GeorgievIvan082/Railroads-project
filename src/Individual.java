import java.util.List;

public class Individual {
    private TileType[][] grid;
    private final int rows;
    private final int cols;
    private int fitness;

    public Individual(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new TileType[rows][cols];

        // Randomly assign tile types to each tile in the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = TileType.getRandomTileType();
            }
        }

        this.fitness = -1;
    }

    public int evaluateFitness(List<Train> trains) {
        int trainsReachedDestination = 0;
        int totalGridCost = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                totalGridCost += getTileCost(grid[row][col]);
            }
        }
        // Evaluate the fitness based on the number of trains that reach their destination
        for (Train train : trains) {
            boolean pathValid = isTrainPathValid(train);
            if (pathValid) {
                trainsReachedDestination++;
            }
        }
        // Combine the total tile cost and the number of trains that reached their destination
        if (trainsReachedDestination < trains.size()) {
            fitness = trainsReachedDestination * 1000;
        } else {
            fitness = 100000 - totalGridCost;
        }
        return this.fitness;
    }
    private int getTileCost(TileType tileType) {
        switch (tileType) {
            case HORIZONTAL:
            case VERTICAL:
                return 1;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case BOTTOM_LEFT:
            case TOP_LEFT:
                return 2;
            case THREEWAY_TOP:
            case THREEWAY_BOTTOM:
            case THREEWAY_LEFT:
            case THREEWAY_RIGHT:
                return 3;
            case CROSSROAD:
                return 4;
            default:
                return 0;
        }
    }

    // Check if a valid path exists for a train
    private boolean isTrainPathValid(Train train) {
        int startX = train.getStartX();
        int startY = train.getStartY();
        int destinationX = train.getDestinationX();
        int destinationY = train.getDestinationY();

        boolean[][] visited = new boolean[rows][cols];
        return dfs(startX, startY, destinationX, destinationY, visited);
    }

    private boolean dfs(int x, int y, int destinationX, int destinationY, boolean[][] visited) {
        if (x < 0 || x >= rows || y < 0 || y >= cols || visited[x][y]) {
            return false;
        }
        if (x == destinationX && y == destinationY) {
            return true;
        }

        visited[x][y] = true;
        TileType currentTile = grid[x][y];

        // Directions: {up, down, left, right}
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] exitDirs = {"up", "down", "left", "right"};  // For current exit check

        for (int d = 0; d < 4; d++) {
            int dx = dirs[d][0], dy = dirs[d][1];
            int nx = x + dx, ny = y + dy;

            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && !visited[nx][ny]) {
                // Current allows exit?
                boolean currentExit = switch (d) {
                    case 0 -> currentTile.allowsUp();
                    case 1 -> currentTile.allowsDown();
                    case 2 -> currentTile.allowsLeft();
                    case 3 -> currentTile.allowsRight();
                    default -> false;
                };

                // Neighbor allows entry (opposite)?
                boolean neighborEntry = switch (d) {
                    case 0 -> grid[nx][ny].allowsDown();  // Up move: neigh bottom
                    case 1 -> grid[nx][ny].allowsUp();    // Down: neigh top
                    case 2 -> grid[nx][ny].allowsRight(); // Left: neigh right
                    case 3 -> grid[nx][ny].allowsLeft();  // Right: neigh left
                    default -> false;
                };

                if (currentExit && neighborEntry) {
                    if (dfs(nx, ny, destinationX, destinationY, visited)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // Accessors
    public TileType[][] getGrid() {
        return grid;
    }

    public void setGrid(TileType[][] grid) {
        this.grid = grid;
    }

    public int getFitness() {
        return fitness;
    }

}