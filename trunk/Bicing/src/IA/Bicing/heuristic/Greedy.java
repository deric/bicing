package IA.Bicing.heuristic;

import IA.Bicing.BicingState;

/**
 *
 * @author Tomas Barton 
 */
public class Greedy {
    private BicingState bicing;

    public Greedy(BicingState b){
           this.bicing = b;
    }

    public BicingState simplySolve(){
        int stations = bicing.getStationsNum();
        int maxAvailable = 0, avSta = 0;
        int maxDemand = 0, tmp, dmSta = 0, mov;
        for(int i=0; i<stations; i++){
            tmp = bicing.getBikesNotMove(i);
            if(tmp > maxAvailable){
                maxAvailable = tmp;
                avSta = i;
            }
            tmp = bicing.getBikesDemanded(i);
            if(tmp > maxDemand){
                maxDemand = tmp;
                dmSta = i;
            }
        }
        if(maxDemand > maxAvailable){
            mov = maxAvailable;
        }else{
            mov = maxDemand;
        }
        bicing.addMove(avSta, dmSta, mov, 0);
        return bicing;
    }

}
