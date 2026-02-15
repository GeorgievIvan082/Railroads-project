import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gui extends JFrame {

    private int size;
    private static final int TILE_SIZE = 50;
    private List<Train> trains;
    private Tile[][] tiles;
    private JPanel gridPanel;
    public Gui(int size, int noOfTrains) {
        this.size = size;
        this.trains = new ArrayList<>();
        this.tiles = new Tile[size][size];

        setTitle("RailRoads");
        setSize(size * TILE_SIZE, size * TILE_SIZE);
        setVisible(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the grid
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(size, size));

        // Create tiles with random types and add them to the grid panel
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile tile = new Tile(TileType.getRandomTileType());
                tiles[row][col] = tile;  // Store the tile in the 2D array
                gridPanel.add(tile);
            }
        }

        add(gridPanel);
        setTrains(noOfTrains);

    }
    public List<Train> getTrains() {
        return trains;
    }
    private void setTrains(int noOfTrains) {
        Random random = new Random();
        List<Point> usedStartPositions = new ArrayList<>();  // List to track occupied start positions
        List<Point> usedEndPositions = new ArrayList<>();    // List to track occupied end positions

        for (int i = 0; i < noOfTrains; i++) {
            int startX, startY, destinationX, destinationY;

            // Ensure the start and destination positions do not overlap with other trains
            do {
                startX = random.nextInt(size);
                startY = random.nextInt(size);
                destinationX = random.nextInt(size);
                destinationY = random.nextInt(size);
            } while (isPositionOccupied(startX, startY, usedStartPositions, usedEndPositions) ||
                    isPositionOccupied(destinationX, destinationY, usedStartPositions, usedEndPositions) ||
                    (startX == destinationX && startY == destinationY)); // Ensure start and destination are different

            // Mark these positions as occupied
            usedStartPositions.add(new Point(startX, startY));
            usedEndPositions.add(new Point(destinationX, destinationY));

            // Create the train and add it to the list
            Train train = new Train(startX, startY, destinationX, destinationY);
            trains.add(train);

            // Mark start and end positions on the grid
            tiles[startX][startY].setTrainStart(i + 1);
            tiles[destinationX][destinationY].setTrainEnd(i + 1);

            System.out.println("Train " + (i + 1) + ": Start (" + startX + "," + startY + ") -> End (" + destinationX + "," + destinationY + ")");
        }
    }

    private boolean isPositionOccupied(int x, int y, List<Point> usedStartPositions, List<Point> usedEndPositions) {
        for (Point point : usedStartPositions) {
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        for (Point point : usedEndPositions) {
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        return false;
    }
    public void updateGridWithSolution(TileType[][] solutionGrid) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Update the TileType of each tile
                tiles[row][col].setTileType(solutionGrid[row][col]);
            }
        }

        // Repaint the grid panel to reflect the updated tiles
        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
