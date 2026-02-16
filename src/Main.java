import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize grid and parameters
        Scanner sce = new Scanner(System.in);
        System.out.println("Enter the size of your grid:");
        int size = sce.nextInt();
        System.out.println("Enter the number of trains:");
        int trains = sce.nextInt();
        int populationSize = 1000;
        int maxGenerations = 1000;
        long startTime = System.currentTimeMillis();

        // Create the grid and get the list of trains
        Gui grid = new Gui(size, trains);
        List<Train> trainsList = grid.getTrains();

        // Initialize population and genetic algorithm
        Population population = new Population(populationSize, size, size);
        GeneticAlgorithm ga = new GeneticAlgorithm();

        // Evolution process: iterate through generations
        for (int generation = 0; generation <= maxGenerations; generation++) {
            population.evaluate(trainsList);
            population.sortByFitness();
            ga.evolve(population);
            // Get the best individual from the current population
            Individual best = population.getBestIndividual();
            System.out.println("Generation " + generation + " - Best Fitness: " + best.getFitness());

            grid.updateGridWithSolution(best.getGrid());
        }

        Individual bestSolution = population.getBestIndividual();
        System.out.println("Final Best Fitness: " + bestSolution.getFitness());
        grid.updateGridWithSolution(bestSolution.getGrid());
        long endTime = System.currentTimeMillis();
        System.out.println("Execution took: "+(endTime-startTime)+"ms");
    }
}
