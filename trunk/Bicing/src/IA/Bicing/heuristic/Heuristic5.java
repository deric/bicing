package IA.Bicing.heuristic;

import IA.Bicing.*;
import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Tomas Barton 
 */
public class Heuristic5 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        BicingState bicing = (BicingState) state;
        int cnt = bicing.getStationsNum();
        double score = 0.0;
        int numStay, balance,mover,free =0,sumNeed=0;
        for(int i = 0; i<cnt; i++){
            numStay = bicing.getBikesNotMove(i);
            balance = bicing.getBalance(i);
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
