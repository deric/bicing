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
public class SuccessorFunction2 implements SuccessorFunction {
    private int numVans;
    private int vanCapacity;
    private int cnt;
    private BicingState bicing;

    public SuccessorFunction2(int numVans, int vanCapacity){
        this.numVans = numVans;
        this.vanCapacity = vanCapacity;
    }
    public List getSuccessors(Object state) {
        ArrayList successors = new ArrayList();
        bicing = (BicingState) state;
        //we can make just as many move as we have vans
        if(bicing.getActionCount() < numVans){
            cnt = bicing.getStationsNum();
            //try to move bikes (if there are any) from each station
            for(int i=0; i< cnt; i++){
                if(bicing.hasNextAvailableBike(i)){
                    int num = bicing.getNextAvailableBikesNum(i);
                    expandPossibleDestinations(successors, i, num);
                }
            }
        }
        alterCurrentState(successors);
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
            if(i!=fromStation && bicing.getBikesDemanded(i) > 0){
                newState = bicing.clone();
                newState.moveBicicle(fromStation, i, numBikes,newState.getActionCount());
                succ.add(new Successor(newState.getLastAction(), newState));
            }
        }
        //one load, two unloads
        //possible combinations
        for(int k=1; k<numBikes;k++){
            for(int i=0; i < cnt; i++){
                if(i!=fromStation && bicing.getBikesDemanded(i) > 0){
                     //unload some bikes at one station
                     for(int j=0; j<cnt; j++){
                            if(j!=fromStation && j!=i && bicing.getBikesDemanded(j) > 0){
                                //and the rest we try to unload to every other station
                                 BicingState secondStepState = bicing.clone();
                                 secondStepState.dobleMoveBikes(fromStation, i, 
                                         k, fromStation, j, numBikes-k,secondStepState.getActionCount());
                                 succ.add(new Successor(secondStepState.getLastAction(), secondStepState));
                            }
                     }
                }
            }
        }
    }


    public boolean alterCurrentState(ArrayList successors){
            BicingState e;
            boolean ret;
            String msg;
            int last = bicing.getMoveCount() -1;
            //we dont want to return to initial state
            if(last > 1){
                for(int i =last; i>1; i--){
                    e = bicing.clone();
                    msg = "removed "+e.getMovementInfo(i);
                    e.removeMove(i);
                    successors.add(new Successor(msg, e));
                }
            }
            if(last < 0){
                return false;
            }
            for(int i = 0; i< bicing.getStationsNum(); i++){
                int bikesMoved = bicing.getNumMoved(last);
                //try to increase number of moved bikes
                do {
                   e = bicing.clone();
                   ret = e.changeMove(last, i, ++bikesMoved);
                   if(ret){
                       msg = e.getLastAction();
                    //   System.out.println(msg);
                       successors.add(new Successor(msg, e));
                   }
                }while(ret);

                bikesMoved = bicing.getNumMoved(last);
                //try to decrease number of moved bikes
                if(bikesMoved > 1){
                    do {
                       e = bicing.clone();
                       ret = e.changeMove(last, i, --bikesMoved);
                       if(ret){
                           msg = e.getLastAction();
                        //   System.out.println(msg);
                           successors.add(new Successor(msg, e));
                       }
                    }while(ret && bikesMoved>1);
                }
            }
            return true;
    }


}
