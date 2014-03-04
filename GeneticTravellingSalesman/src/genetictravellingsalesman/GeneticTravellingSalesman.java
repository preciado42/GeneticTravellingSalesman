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
        int firstCity = r.nextInt(this.NUMBER_OF_CITIES-1);
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
        tempPaths.add(new Path(fromCity, this.cityMap[fromCity][0],0));
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

        public void printTour() {
            for (int i = 0; i < myTour.size(); i++) {
                Path temp = myTour.get(i);
                System.out.print(" " + temp.startCity + "-" + temp.distance + "->" + temp.endCity + " ");
            }
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
            System.out.println("");
        }
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
