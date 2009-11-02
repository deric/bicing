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
        int balance;
        double score = 0.0;
        for(int i = 0; i<cnt; i++){
            balance =bicing.getBalance(i);
            if(balance < 0){
                score -= balance;
            }else{
                score += balance;
            }
        }
        return score;
    }

}
