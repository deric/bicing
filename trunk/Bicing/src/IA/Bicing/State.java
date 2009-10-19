
package IA.Bicing;

/**
 * Represents number of rented, demanded bikes in a city
 * @author Tomas Barton 
 */
public class State {
    double stdDem[], stdStay[], stdCurr[];

    State(double[] demanded, double[] stay, double[] current){
            stdDem = demanded;
            stdStay = stay;
            stdCurr = current;
    }
}