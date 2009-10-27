package IA.Bicing;

import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Tomas Barton 
 */
public class BicingHeuristicFunction1 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        BicingState bicing = (BicingState) state;
        int cnt = bicing.getStationsNum();
        double score = 0.0;
        for(int i = 0; i<cnt; i++){
            score+=bicing.getCurrentlyNotUsedBikesNum(i);
            if(bicing.hasNextAvailableBike(i)){
                score+= bicing.getNextAvailableBikesNum(i);
            }else{
                score+= Math.pow(bicing.getNextAvailableBikesNum(i),2);
            }
        }
        System.out.println("score: "+score);
        return score;
    }

}
