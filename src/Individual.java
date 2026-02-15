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
        // Base cases
        if (x < 0 || x >= rows || y < 0 || y >= cols || visited[x][y]) {
            return false;
        }
        if (x == destinationX && y == destinationY) {
            return true;
        }

        // Mark visited
        visited[x][y] = true;
        TileType currentTile = grid[x][y];

        // Explore neighbors by tile type (possible exits + neighbor entry check)
        switch (currentTile) {
            case HORIZONTAL:
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case VERTICAL:
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                break;

            case TOP_RIGHT:
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case BOTTOM_RIGHT:
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case BOTTOM_LEFT:
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case TOP_LEFT:
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case THREEWAY_TOP:
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                break;

            case THREEWAY_BOTTOM:
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                break;

            case THREEWAY_LEFT:
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case THREEWAY_RIGHT:
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                break;

            case CROSSROAD:
                // Up
                if (x > 0 && !visited[x - 1][y] && grid[x - 1][y].allowsDown()) {
                    if (dfs(x - 1, y, destinationX, destinationY, visited)) return true;
                }
                // Down
                if (x < rows - 1 && !visited[x + 1][y] && grid[x + 1][y].allowsUp()) {
                    if (dfs(x + 1, y, destinationX, destinationY, visited)) return true;
                }
                // Left
                if (y > 0 && !visited[x][y - 1] && grid[x][y - 1].allowsRight()) {
                    if (dfs(x, y - 1, destinationX, destinationY, visited)) return true;
                }
                // Right
                if (y < cols - 1 && !visited[x][y + 1] && grid[x][y + 1].allowsLeft()) {
                    if (dfs(x, y + 1, destinationX, destinationY, visited)) return true;
                }
                break;
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