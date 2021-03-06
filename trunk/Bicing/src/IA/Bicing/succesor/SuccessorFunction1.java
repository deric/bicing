package IA.Bicing.succesor;

import IA.Bicing.*;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tomas Barton 
 */
public class SuccessorFunction1 implements SuccessorFunction {
    private int numVans;
    private int vanCapacity;
    private int cnt;
    private BicingState bicing;

    public SuccessorFunction1(int numVans, int vanCapacity){
        this.numVans = numVans;
        this.vanCapacity = vanCapacity;
    }
    public List getSuccessors(Object state) {
        ArrayList successors = new ArrayList();
        bicing = (BicingState) state;
        int mov;
        //we can make just as many move as we have vans
        if(bicing.getActionCount() < numVans){
            cnt = bicing.getStationsNum();
            //try to move bikes (if there are any) from each station
            for(int i=0; i< cnt; i++){
                mov = bicing.getMoveableBikes(i);
                if(mov > 0){
                    expandPossibleDestinations(successors, i, mov);
                }
            }
        }
       // System.out.println("next states: "+successors.size());
        return successors;
    }

    private boolean expandPossibleDestinations(ArrayList succ,int fromStation, int numBikes){
        BicingState newState;
        //we can only move the maximum capacity of van
        if(numBikes >= vanCapacity){
            numBikes = vanCapacity;
        }
        if(!bicing.isPossibleAddMove()){
            return false;
        }
        //all available bikes to different station than is this one
        for(int i=0; i < cnt; i++){
            if(i!=fromStation && bicing.getDemandedBikes(i) > 0){
                newState = bicing.clone();
                if(newState.addMove(fromStation, i, numBikes, newState.getActionCount())){
                    succ.add(new Successor(newState.getLastAction(), newState));
                }
            }
        }
        //one load, two unloads
        //possible combinations
        for(int k=1; k<numBikes;k++){
            for(int i=0; i < cnt; i++){
                if(i!=fromStation && bicing.getDemandedBikes(i) > 0){
                     //unload some bikes at one station
                     for(int j=0; j<cnt; j++){
                            if(j!=fromStation && j!=i && bicing.getDemandedBikes(j) > 0){
                                //and the rest we try to unload to every other station
                                 BicingState secondStepState = bicing.clone();
                                 if(secondStepState.dobleMoveBikes(fromStation, i, k,
                                         fromStation, j, numBikes-k,secondStepState.getActionCount())){
                                     succ.add(new Successor(secondStepState.getLastAction(), secondStepState));
                                 }
                                 
                            }
                     }
                }
            }
        }
        return true;
    }


}
