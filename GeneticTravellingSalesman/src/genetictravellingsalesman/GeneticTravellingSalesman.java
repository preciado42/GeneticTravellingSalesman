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
    private final int INDIVIDUAL_SIZE = this.NUMBER_OF_CITIES - 1; //size of each individual
    private ArrayList<Tour> population;
    private int[][] cityMap;

    public GeneticTravellingSalesman() {
        population = new ArrayList<Tour>();
    }

    public void run() {
        this.createCityMap();
        this.printCityMap();
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

    }

    public void printCityMap(){
        for (int i = 0; i < this.NUMBER_OF_CITIES; i++) {
            for (int j = 0; j < this.NUMBER_OF_CITIES; j++) {
                if(this.cityMap[i][j] > 9){
                    System.out.print(this.cityMap[i][j]+"  ");
                } else {
                    System.out.print(" "+this.cityMap[i][j]+"  ");
                }
            }
            System.out.println();
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
