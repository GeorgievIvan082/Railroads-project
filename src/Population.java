import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population {
    private List<Individual> individuals;

    public Population(int populationSize, int rows, int cols) {
        individuals = new ArrayList<>();

        // Initialize the population with random individuals
        for (int i = 0; i < populationSize; i++) {
            individuals.add(new Individual(rows, cols));
        }
    }

    // Evaluate the fitness of the entire population
    public void evaluate(List<Train> trains) {
        for (Individual individual : individuals) {
            individual.evaluateFitness(trains);
        }
    }

    public void sortByFitness() {
        // Sort in descending order so that the individual with the highest fitness is first
        individuals.sort((a, b) -> Integer.compare(b.getFitness(), a.getFitness()));
    }

    // Get the individual with the highest fitness
    public Individual getBestIndividual() {
        // After sorting by fitness, the first individual in the list will have the highest fitness
        return individuals.get(0);
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
}