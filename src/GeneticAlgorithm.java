import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private Random random = new Random();

    public void evolve(Population population) {
        // Step 1: Sort the population by fitness
        population.sortByFitness();

        // Step 2: Remove the worst 50 individuals
        List<Individual> bestHalf = new ArrayList<>(population.getIndividuals().subList(0, 50)); // Keep the best 50
        List<Individual> newIndividuals = new ArrayList<>();

        for (int i = 0; i < 49; i++) {
            Individual parent1 = bestHalf.get(i % bestHalf.size());
            Individual parent2 = bestHalf.get((i + 1) % bestHalf.size());

            // Create a new individual by swapping a part of parent1 and parent2
            Individual offspring = crossoverSwapParts(parent1, parent2);
            mutate(offspring);
            newIndividuals.add(offspring);
        }

        // Step 4: Add one randomly generated individual
        Individual randomIndividual = new Individual(bestHalf.get(0).getGrid().length, bestHalf.get(0).getGrid()[0].length);
        newIndividuals.add(randomIndividual);

        // Step 5: Replace the worst 50 individuals with the 50 new offspring
        population.getIndividuals().clear();
        population.getIndividuals().addAll(bestHalf);  // Keep the best 50
        population.getIndividuals().addAll(newIndividuals);  // Add the 50 new offspring
    }

    // Method to create offspring by swapping parts of two parents
    public Individual crossoverSwapParts(Individual parent1, Individual parent2) {
        int size = parent1.getGrid().length;
        Individual offspring = new Individual(size, size);
        TileType[][] grid = new TileType[size][size];

        int crossoverPoint = random.nextInt(size);  // Choose a random row to swap

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row < crossoverPoint) {
                    grid[row][col] = parent1.getGrid()[row][col];
                } else {
                    grid[row][col] = parent2.getGrid()[row][col];
                }
            }
        }
        offspring.setGrid(grid);
        return offspring;
    }

    // Mutation method to introduce random changes in an offspring
    public void mutate(Individual individual) {

        TileType[][] grid = individual.getGrid();
        for (int i = 0; i < 2; i++) {  // Mutate 2 random tiles, for example
            int row = random.nextInt(grid.length);
            int col = random.nextInt(grid[0].length);
            grid[row][col] = TileType.getRandomTileType();
        }
    }
}
