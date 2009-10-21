
package IA.Bicing;

import java.util.ArrayList;

/**
 * Represents number of rented, demanded bikes in a city
 * @author Tomas Barton 
 */
public class BicingState {
    /**
     * number of demanded bicicles in next hour
     */
    private double demanded[];
    /**
     * current number of bicicles
     */
    private double current[];
    /**
     * number of bicicles at station in an hour
     */
    private double next[];

     /**
     * coordinates of stations
     */
    private static int[][] coordinates;

    private ArrayList moves = new ArrayList();

    BicingState(double[] current, double[] next, double[] demanded ){
            this.demanded = demanded;
            this.current = current;
    }

    /**
     * Moves bicicle from one station to another and record this move
     * @param fromSta
     * @param toSta
     * @param numBic
     */
    public void moveBicicle(int fromSta, int toSta, int numBic){
        ///moves.add();
    }

    
    public double countScore(){
        return 0.0;
    }



}