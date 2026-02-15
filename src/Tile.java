import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tile extends JButton {

    private final Gui gui;
    private TileType tileType;
    private boolean isTrainStart;
    private boolean isTrainEnd;
    private int trainNumber;
    private static Map<Integer, Color> trainColors = new HashMap<>();  // Map to store unique colors for each train

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
        repaint();
    }

    public Tile(Gui gui, TileType tileType) {
        this.gui = gui;
        this.tileType = tileType;
        this.isTrainEnd = false;
        this.isTrainStart = false;
        this.trainNumber = -1;

        setPreferredSize(new Dimension(50, 50));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.blue);

        // Add action listener to show dropdown on button click
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDropdownMenu();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the path color
        g.setColor(Color.red);

        // Center points of the panel
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw based on the tile type
        switch (tileType) {
            case HORIZONTAL:
                g.drawLine(0, centerY, getWidth(), centerY);  // Horizontal line
                break;
            case VERTICAL:
                g.drawLine(centerX, 0, centerX, getHeight());  // Vertical line
                break;
            case TOP_RIGHT:
                g.drawLine(centerX, 0, centerX, centerY);  // Top to center
                g.drawLine(centerX, centerY, getWidth(), centerY);  // Center to right
                break;
            case BOTTOM_RIGHT:
                g.drawLine(centerX, centerY, centerX, getHeight());  // Center to bottom
                g.drawLine(centerX, centerY, getWidth(), centerY);  // Center to right
                break;
            case BOTTOM_LEFT:
                g.drawLine(centerX, centerY, centerX, getHeight());  // Center to bottom
                g.drawLine(0, centerY, centerX, centerY);  // Left to center
                break;
            case TOP_LEFT:
                g.drawLine(centerX, 0, centerX, centerY);  // Top to center
                g.drawLine(0, centerY, centerX, centerY);  // Left to center
                break;
            case THREEWAY_TOP:
                g.drawLine(0, centerY, getWidth(), centerY);  // Left to right
                g.drawLine(centerX, 0, centerX, centerY);  // Top to center
                break;
            case THREEWAY_BOTTOM:
                g.drawLine(0, centerY, getWidth(), centerY);  // Left to right
                g.drawLine(centerX, centerY, centerX, getHeight());  // Center to bottom
                break;
            case THREEWAY_LEFT:
                g.drawLine(centerX, 0, centerX, getHeight());  // Top to bottom
                g.drawLine(0, centerY, centerX, centerY);  // Left to center
                break;
            case THREEWAY_RIGHT:
                g.drawLine(centerX, 0, centerX, getHeight());  // Top to bottom
                g.drawLine(centerX, centerY, getWidth(), centerY);  // Center to right
                break;
            case CROSSROAD:
                g.drawLine(centerX, 0, centerX, getHeight());  // Top to bottom
                g.drawLine(0, centerY, getWidth(), centerY);  // Left to right
                break;
        }

        // Draw start and end positions with unique random colors
        if (isTrainStart) {
            g.setColor(trainColors.get(trainNumber));  // Use the unique color for the start position
            g.fillOval(centerX - 10, centerY - 10, 20, 20);  // Draw a circle at the center
            g.setColor(Color.BLACK);
            g.drawString("start", centerX - 13, centerY + 5);
        }

        if (isTrainEnd) {
            g.setColor(trainColors.get(trainNumber));  // Use the same unique color for the end position
            g.fillRect(centerX - 10, centerY - 10, 20, 20);  // Draw a square at the center
            g.setColor(Color.BLACK);
            g.drawString("end", centerX - 10, centerY + 5);
        }
    }

    // Setters to indicate if a tile is a start or end of a train
    public void setTrainStart(int trainNumber) {
        this.isTrainStart = true;
        this.trainNumber = trainNumber;
        assignTrainColor(trainNumber);
        repaint();
    }

    public void setTrainEnd(int trainNumber) {
        this.isTrainEnd = true;
        this.trainNumber = trainNumber;
        assignTrainColor(trainNumber);
        repaint();
    }

    // Method to show a dropdown to select tile type
    private void showDropdownMenu() {
        int size = gui.tiles.length;  // Get the size of the grid
        // Create a dropdown (combo box) with all tile types
        TileType[] types = TileType.values();
        JComboBox<TileType> comboBox = new JComboBox<>(types);
        comboBox.setSelectedItem(tileType);  // Set current tile type as selected

        // Show the combo box in a dialog
        int result = JOptionPane.showConfirmDialog(this, comboBox, "Select Tile Type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If OK is pressed, update the tile type
        if (result == JOptionPane.OK_OPTION) {
            tileType = (TileType) comboBox.getSelectedItem();
            repaint();  // Repaint the tile with the new type
            this.gui.onTileChange();  // Notify the GUI about the tile change
        }
    }

    private void assignTrainColor(int trainNumber) {
        if (!trainColors.containsKey(trainNumber)) {
            trainColors.put(trainNumber, generateRandomColor());
        }
    }

    private Color generateRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b);
    }

    public TileType getTileType() {
        return tileType;
    }
}
