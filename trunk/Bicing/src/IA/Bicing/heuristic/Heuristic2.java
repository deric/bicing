package IA.Bicing.heuristic;

import IA.Bicing.*;
import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Tomas Barton 
 */
public class Heuristic2 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        BicingState bicing = (BicingState) state;
        int cnt = bicing.getStationsNum();
        double score = 0.0;
        int numNext, numDem,numStay, balance,mover,free =0,sumNeed=0;
        for(int i = 0; i<cnt; i++){
            numNext = bicing.getBikesNext(i);
            numDem = bicing.getBikesDemanded(i);
            numStay = bicing.getBikesNotMove(i);
            balance = numNext - numDem;
            if (balance > 0) {
                if (balance > numStay) {
                    mover = numStay;
                } else {
                    mover = balance;
                }
                free += mover;
            } else {
                mover = 0;
                sumNeed = sumNeed - balance;
            }
            score = free+sumNeed;   
        }
        return score;
    }
}
