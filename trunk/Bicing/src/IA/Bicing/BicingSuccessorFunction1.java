package IA.Bicing;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tomas Barton 
 */
public class BicingSuccessorFunction1 implements SuccessorFunction {
    private int numVans;
    private int vanCapacity;
    private int cnt;
    private BicingState bicing;

    public BicingSuccessorFunction1(int numVans, int vanCapacity){
        this.numVans = numVans;
        this.vanCapacity = vanCapacity;
    }
    public List getSuccessors(Object state) {
        ArrayList successors = new ArrayList();
        bicing = (BicingState) state;
        //we can make just as many move as we have vans
        if(bicing.getLevel() < numVans){
            cnt = bicing.getStationsNum();
            //try to move bikes (if there are any) from each station
            for(int i=0; i< cnt; i++){
                if(bicing.hasNextAvailableBike(i)){
                    int num = bicing.getNextAvailableBikesNum(i);
                    expandPossibleDestinations(successors, i, num);
                }
            }
        }
       // System.out.println("next states: "+successors.size());
        return successors;
    }

    private void expandPossibleDestinations(ArrayList succ,int fromStation, int numBikes){
        BicingState newState;
        //we can only move the maximum capacity of van
        if(numBikes > vanCapacity){
            numBikes = vanCapacity;
        }
        //all available bikes to different station than is this one
        for(int i=0; i < cnt; i++){
            if(i!=fromStation){
                newState = bicing.clone();
                newState.moveBicicle(fromStation, i, numBikes);
                succ.add(new Successor(newState.getLastAction(), newState));
            }
        }
        //one load, two unloads
        //possible combinations
        for(int k=1; k<numBikes;k++){
            for(int i=0; i < cnt; i++){
                if(i!=fromStation){
                     //unload some bikes at one station
                     for(int j=0; j<cnt; j++){
                            if(j!=fromStation && j!=i){
                                //and the rest we try to unload to every other station
                                 BicingState secondStepState = bicing.clone();
                                 secondStepState.dobleMoveBikes(fromStation, i, k, fromStation, j, numBikes-k);
                                 succ.add(new Successor(secondStepState.getLastAction(), secondStepState));
                            }
                     }
                }
            }
        }
    }


}
