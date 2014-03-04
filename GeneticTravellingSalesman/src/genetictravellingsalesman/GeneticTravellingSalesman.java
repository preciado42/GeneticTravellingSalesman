/**
 * This program will implement a genetic algorithm to try to find a solution to
 * the Travelling salesman problem.
 *
 * Bio Computing Dr. Gurka March 1 2014
 *
 * @author Angel Preciado
 */
package genetictravellingsalesman;

import java.util.ArrayList;
import java.util.Random;

public class GeneticTravellingSalesman {

    private final int NUMBER_OF_CITIES = 7;  //number of cities to solve for
    private final int MAX_DISTANCE = 25;    //max distance between cities
    private final int MIN_DISTANCE = 5;     //min distance between cities
    private final int INDIVIDUAL_SIZE = this.NUMBER_OF_CITIES; //size of each individual
    private final int POPULATION_SIZE = 10; //number of tours per population
    private final double FIT_PARENT_LIMITER = .75; //rate at which a fit parent will be chosen
    private final double MUTATION_RATE = .01; //rate of mutation
    private ArrayList<Tour> population;
    private int[][] cityMap;

    public GeneticTravellingSalesman() {
        population = new ArrayList<Tour>();
    }

    public void run() {
        this.createCityMap();
        this.printCityMap();
        System.out.println();
        this.createInitialPopulation();

        for (int i = 0; i < 10; i++) {
            this.printPopulationStats(i);
            //this.createNextGeneration();
        }
    }

    public void createCityMap() {

        this.cityMap = new int[this.NUMBER_OF_CITIES][this.NUMBER_OF_CITIES];
        Random rng = new Random();
        for (int i = 0; i < this.NUMBER_OF_CITIES; i++) {
            for (int j = 0; j < this.NUMBER_OF_CITIES; j++) {
                cityMap[i][j] = rng.nextInt(this.MAX_DISTANCE - 4) + 5;
            }
        }
    }

    /**
     * Creates a random tour as an individual
     *
     * @return
     */
    public Tour createRandomTour() {
        Tour temp = null;
        ArrayList<Path> tempPaths = new ArrayList<Path>();
        //set up, all tours must begin with city 0 and end with city 0
        boolean[] used = new boolean[this.NUMBER_OF_CITIES];
        Random r = new Random();
        int firstCity = r.nextInt(this.NUMBER_OF_CITIES - 1);
        firstCity++;
        used[firstCity] = true;
        used[0] = true;
        int fromCity = firstCity;
        int nextCity = 0;
        tempPaths.add(new Path(0, this.cityMap[0][firstCity], firstCity));
        for (int i = 0; i < this.NUMBER_OF_CITIES - 2; i++) {
            do {
                nextCity = r.nextInt(this.NUMBER_OF_CITIES);
                //System.out.println("cityPicked "+nextCity +"used? "+used[nextCity]);
            } while (used[nextCity]);
            //System.out.println("addint to paths+ "+fromCity+":" +this.cityMap[fromCity][nextCity]+":"+ nextCity);
            tempPaths.add(new Path(fromCity, this.cityMap[fromCity][nextCity], nextCity));
            used[nextCity] = true;
            fromCity = nextCity;
        }
        tempPaths.add(new Path(fromCity, this.cityMap[fromCity][0], 0));
        temp = new Tour(tempPaths);
        return temp;
    }

    public void createInitialPopulation() {
        for (int i = 0; i < this.POPULATION_SIZE; i++) {
            this.population.add(this.createRandomTour());
            this.population.get(i).printTour();
        }
    }

    /**
     * Helper class that will contain a path from one city to another and its
     * distance will be used by each individual as part of their tour
     */
    public class Path {

        public int startCity;
        public int distance;
        public int endCity;

        public Path(int start, int dist, int end) {
            this.startCity = start;
            this.distance = dist;
            this.endCity = end;
        }
    }

    /**
     * Helper class that will also count as an "individual" of the population.
     * Each individual will hold an array of their chosen paths as a solution,
     * this class will help make the code easier to understand and manipulate.
     */
    public class Tour {

        public ArrayList<Path> myTour;

        public Tour(ArrayList<Path> tour) {
            this.myTour = tour;
        }

        public int getTotalDistance() {
            int sum = 0;
            for (int i = 0; i < myTour.size(); i++) {
                sum += myTour.get(i).distance;
            }
            return sum;
        }

        public void printTour() {
            for (int i = 0; i < myTour.size(); i++) {
                Path temp = myTour.get(i);
                String scPad = " ";
                String distPad = "0";
                String ecPad = " ";
                if (temp.startCity > 9) {
                    scPad = "";
                }
                if (temp.distance > 9) {
                    distPad = "";
                }
                if (temp.endCity > 9) {
                    ecPad = "";
                }
                System.out.print(" " + scPad + temp.startCity + " -" + distPad + temp.distance + "-> "
                        + ecPad + temp.endCity);
            }
            System.out.println("  Total Distance: " + this.getTotalDistance());
            System.out.println();
        }

    }

    public void printCityMap() {
        for (int i = 0; i < this.NUMBER_OF_CITIES; i++) {
            for (int j = 0; j < this.NUMBER_OF_CITIES; j++) {
                if (this.cityMap[i][j] > 9) {
                    System.out.print(this.cityMap[i][j] + "  ");
                } else {
                    System.out.print(" " + this.cityMap[i][j] + "  ");
                }
            }
        }
    }

    public void printPopulationStats(int gen) {
        System.out.println("\nPopulation for Generation " + gen + "\n");
        for (int i = 0; i < this.population.size(); i++) {
            population.get(i).printTour();
        }
        System.out.println("\n Best Tour for this Generation \n");
        population.get(this.findBestTour()).printTour();
    }

    public int findBestTour() {
        int shortestPath = 99999;
        int index = 0;
        for (int i = 0; i < this.population.size(); i++) {
            if (this.population.get(i).getTotalDistance() < shortestPath) {
                shortestPath = this.population.get(index).getTotalDistance();
                index = i;
            }
        }
        return index;
    }

    public void createNextGeneration() {
        //parents
        int p1, p2;
        ArrayList<Tour> newPopulation = new ArrayList<Tour>();
        for (int i = 0; i < this.population.size(); i++) {
            //select parents
            p1 = this.chooseParent();
            p2 = this.chooseParent();
            //cross over
            Tour child = this.crossOver(p1, p2);
            //mutate
            this.mutation(child);
            newPopulation.add(child);
        }
        this.population = newPopulation;
    }

    /**
     * Will pick a parent that is more fit than the average 75% of the time, the
     * other times it will pick a random parent which could have a higher or
     * lower fitness ranking.
     *
     * @return int chosen parent index
     */
    public int chooseParent() {
        int avgDistance = this.getPopAverageDistance();
        int parent = 0;
        Random r = new Random();
        if (r.nextDouble() > this.FIT_PARENT_LIMITER) {
            //pick fit parent
            do {
                parent = r.nextInt(this.population.size());
                System.out.println("stuck in choosing parent!");
            } while (this.population.get(parent).getTotalDistance() > avgDistance);
            return parent;
        } else {
            return r.nextInt(this.population.size());
        }

    }

    /**
     *
     * @param p1
     * @param p2
     */
    public Tour crossOver(int p1, int p2) {
        ArrayList<Path> childPaths = new ArrayList<Path>();
        //pick crossover point
        Random r = new Random();
        int cp = r.nextInt(this.INDIVIDUAL_SIZE - 1);
        //do crossover
        for (int i = 0; i < cp; i++) {
            childPaths.add(this.population.get(p1).myTour.get(i));
        }
        //find out which cities are already included in the parent
        boolean[] used = new boolean[this.NUMBER_OF_CITIES];
        for (int i = 0; i < childPaths.size(); i++) {
            Path temp = childPaths.get(i);
            used[temp.startCity] = true;
            used[temp.endCity] = true;
        }

        for (int i = cp; i < this.INDIVIDUAL_SIZE - 1; i++) {
            Path temp = this.findAvailablePath(used, i);
            used[temp.startCity] = true;
            used[temp.endCity] = true;
            childPaths.add(temp);
        }
        //add the last path back to home, always
        Path temp = childPaths.get(childPaths.size() - 1);
        childPaths.add(new Path(temp.endCity, this.cityMap[temp.endCity][0], 0));

        return new Tour(childPaths);
    }

    public Tour mutation(Tour child) {
        Tour mutatedChild = null;
        //
        if (Math.random() > this.MUTATION_RATE) {
            ArrayList<Path> childPaths = new ArrayList<Path>();
            //pick mutate point
            Random r = new Random();
            int mut = r.nextInt(this.INDIVIDUAL_SIZE - 1);
            //copy up to mutation
            for (int i = 0; i < mut; i++) {
                childPaths.add(child.myTour.get(i));
            }
            //find out which cities are already included in the child
            boolean[] used = new boolean[this.NUMBER_OF_CITIES];
            int startCity = 0;
            for (int i = 0; i < childPaths.size(); i++) {
                Path temp = childPaths.get(i);
                used[temp.startCity] = true;
                used[temp.endCity] = true;
                startCity = temp.endCity;
            }
            //fill in teh rest with available paths
            for (int i = mut; i < this.INDIVIDUAL_SIZE - 1; i++) {
                Path temp = this.findAvailablePath(used, startCity);
                used[temp.startCity] = true;
                startCity = temp.endCity;
                childPaths.add(temp);
            }
            //add the last path back to home, always
            Path temp = childPaths.get(childPaths.size() - 1);
            childPaths.add(new Path(temp.endCity, this.cityMap[temp.endCity][0], 0));

            return new Tour(childPaths);
            //
        }
        return child;
    }

    public Path findAvailablePath(boolean[] used, int start) {
        Path path = new Path(0, 0, 0);
        Random r = new Random();
        int destination = 0;
        do {
            destination = r.nextInt(this.NUMBER_OF_CITIES);
            System.out.println("stuck in finding available path!!");
        } while (used[destination]);
        path = new Path(start, this.cityMap[start][destination], destination);
        return path;
    }

    /**
     * Gets the average distance for the population.
     *
     * @return avg distance
     */
    public int getPopAverageDistance() {
        int totalSum = 0;
        for (int i = 0; i < this.population.size(); i++) {
            totalSum += this.population.get(i).getTotalDistance();
        }
        if (totalSum != 0) {
            return totalSum / this.population.size();
        }
        return totalSum;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // run
        GeneticTravellingSalesman gts = new GeneticTravellingSalesman();
        gts.run();
    }

}
