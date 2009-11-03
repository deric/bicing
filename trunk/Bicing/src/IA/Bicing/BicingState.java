package IA.Bicing;

import IA.Bicing.heuristic.Heuristic1;
import IA.Bicing.heuristic.Heuristic2;
import IA.Bicing.heuristic.Heuristic3;
import aima.search.framework.HeuristicFunction;
import java.security.InvalidParameterException;

/**
 * Represents number of bikes available now and in one hour (demanded) in a city
 * @author Tomas Barton 
 */
public class BicingState {
    /**
     * nuber of stations
     */
    private static int stationsNum;
    /**
     * Depth is determinated by number of vans
     */
    private static int maxDepth;
    /**
     * number of demanded bicycles in next hour
     */
    private static int demanded[];

    private static int vanCapacity;
     /**
     * coordinates of stations
     */
    private static int[][] coordinates;

    /**
     * current number of bicycles, that won't move this hour
     */
    private int current[];
    /**
     * number of bicycles after users' moves
     */
    private int next[];
    /**
     * depth in a tree, max == number of vans
     */
    private int actionCnt = 0;
    /**
     * as one action can be done two moves
     */
    private int moveCnt = 0;
    
    private static int maxMoves;

    /**
     * move(s) to reach this state from initial state
     */
    private int[] from;
    private int[] to;
    private int[] transfer;
    private int[] vans;

    /**
     * total distance for transporting bikes from one station to another
     */
    private double totalDistance = 0.0;

    /**
     * Constructor for initialization of this task
     * @param current
     * @param next
     * @param demanded
     * @param coordinates
     * @param maxDepth
     */
    public BicingState(int[] current, int[] next, int[] demanded, int[][] coordinates, int maxDepth, int vanCapacity) {
        //inicialization of static variables
        BicingState.demanded = demanded;
        BicingState.coordinates = coordinates;
        BicingState.stationsNum = demanded.length;
        BicingState.maxDepth = maxDepth;
        BicingState.maxMoves = 2*maxDepth;
        BicingState.vanCapacity = vanCapacity;
        variablesSetup(current, next);
    }

    /**
     * Used for a deep copy of state
     * @param numStations
     * @param current
     * @param next
     * @param demanded
     */
    private BicingState(int[] current, int[] next) {
        variablesSetup(current, next);
    }
    /**
     * Dynamic variables setup
     * @param current
     * @param next
     */
    private void variablesSetup(int[] current, int[] next){
        this.current = new int[stationsNum];
        this.next = new int[stationsNum];
        for (int i = 0; i < stationsNum; i++) {
            this.current[i] = current[i];
            this.next[i] = next[i];
        }
        //maximum possible movements
        from = new int[maxMoves];
        to = new int[maxMoves];
        transfer = new int[maxMoves];
        vans = new int[maxMoves];
    }

    /**
     * OPERATOR
     * Moves bicicle from one station to another and record this move
     * @param fromSta
     * @param toSta
     * @param numBic
     */
    public boolean addMove(int source, int destination, int bikesNum, int van) {
        if(actionCnt >= maxDepth || current[source]<= 0 || moveCnt >= maxMoves){
            return false;
        }
        if(bikesNum > vanCapacity){
            return false;
        }
        setMove(moveCnt++, source, destination, bikesNum, van);
        actionCnt++;
        return true;
    }

    /**
     * OPERATOR
     * One van moves bikes from one station to another two stations
     * @param source1
     * @param destination1
     * @param biciclesNum1
     * @param source2
     * @param destination2
     * @param biciclesNum2
     * @param van
     */
    public boolean dobleMoveBikes(int source1, int destination1, int biciclesNum1,
            int source2, int destination2, int biciclesNum2, int van) {
        if(actionCnt >= maxDepth || moveCnt > (maxMoves-2)){
            return false;
        }
        if((biciclesNum1+biciclesNum2)>vanCapacity){
            return false;
        }
        setMove(moveCnt++, source1, destination1, biciclesNum1, van);
        setMove(moveCnt++, source2, destination2, biciclesNum2, van);
        actionCnt++;
        return true;
    }

    private void setMove(int moveIdx, int source, int destination, int bikeNum, int van){
        from[moveIdx] = source;
        to[moveIdx] = destination;
        transfer[moveIdx] = bikeNum;
        vans[moveIdx] = van;
        current[source] -= bikeNum;
        next[source] -= bikeNum;
        if(next[source] < 0){
           next[source] = 0;
        }
        next[destination] += bikeNum;
        double dist = getStationsDistance(source, destination);
        totalDistance += dist;
    }

    /**
     * OPERATOR
     * changeMove -
     * @param moveIdx
     * @param destination
     * @param bikesNum
     * @return true if move is possible
     */
    public boolean changeMove(int moveIdx, int destination, int bikesNum){
        int source = from[moveIdx];
        int van = vans[moveIdx];
        //non-sense move
        if(source == destination){
            return false;
        }

        if(bikesNum == transfer[moveIdx]){
            undoneMove(moveIdx);
            //just modify move that has been already done
            setMove(moveIdx, source, destination, bikesNum, van);
        }else{
            //check if is possible to make more moves
            if((current[source]+transfer[moveIdx]) < bikesNum){
                return false;
            }
            //if possible remove move == return bikes to station
            undoneMove(moveIdx);
            //add new move
            setMove(moveIdx, source, destination, bikesNum, van);
        }
        return true;
    }

    /**
     * Return bikes to station from where it were taken, counters of moves and
     * actions are not changed
     * @param moveIdx
     */
    private void undoneMove(int moveIdx){
        int source = from[moveIdx];
        int destination = to[moveIdx];
        int bikeNum = transfer[moveIdx];
        double dist = getStationsDistance(from[moveIdx], to[moveIdx]);
        totalDistance-=dist;
        current[source] += bikeNum;
        next[source] += bikeNum;
        next[destination] -= bikeNum;
    }

    public void removeMove(int moveIdx){
        if(moveIdx >= moveCnt){
            throw new InvalidParameterException("out of moves range");
        }
        undoneMove(moveIdx);
        moveCnt--;
        actionCnt--;
        if(actionCnt == 0 && moveCnt > 0){
            actionCnt = 1;
        }
    }

    public void removeLastMove(){
        removeMove((moveCnt-1));
    }

    public static void setStationsNum(int num) {
        stationsNum = num;
    }

    public int getStationsNum() {
        return stationsNum;
    }

    /**
     * Return true if we expect in next hour to have available bikes
     * @param stationIdx
     * @return
     */
    public boolean hasNextAvailableBike(int stationIdx) {
        return (current[stationIdx] > 0);
    }

    /**
     * Number of bikes available for transfer to another station
     * @param stationIdx
     * @return
     */
    public int getNextAvailableBikesNum(int stationIdx) {
        return (int) (current[stationIdx]);
    }

    public int getBikesNotMove(int idx) {
        return current[idx];
    }

    public int getBikesNext(int idx) {
        return next[idx];
    }

    public int getBikesDemanded(int idx) {
        return demanded[idx];
    }

    /**
     * Number of bikes which are now at station
     * @param stationIdx
     * @return
     */
    public int getCurrentlyNotUsedBikesNum(int stationIdx) {
        return (int) current[stationIdx];
    }

    public String getLastAction() {
        return getMovementInfo(moveCnt-1);
    }

    public String getMovementInfo(int moveIdx){
        return transfer[moveIdx]+" bikes: "+from[moveIdx]+" -> "+to[moveIdx]+" by van "+vans[moveIdx]+"\n";
    }

    /**
     * Return number of moved bikes in specified movement
     * @param moveIdx
     * @return
     */
    public int getNumMoved(int moveIdx){
        return transfer[moveIdx];
    }

    public int getActionCount() {
        return actionCnt;
    }

    public int getMoveCount(){
        return moveCnt;
    }

    public boolean isPossibleAddMove(){
        if(actionCnt < maxDepth){
            return true;
        }
        return false;
    }

    /**
     * Count balance of bikes for next hour
     * @param stationIdx
     * @return
     */
    public int getBalance(int stationIdx){
        return next[stationIdx]-demanded[stationIdx];
    }

    /**
     * Retutns the distance between two stations
     *
     * @param est1 Station one number
     * @param est2 Station two number
     * @return
     */
    public double getStationsDistance(int est1, int est2) {
        return (double) Math.sqrt(Math.pow(coordinates[est1][0] - coordinates[est2][0], 2) + Math.pow(coordinates[est1][1] - coordinates[est2][1], 2));
    }

    /**
     * Distance for transporting bikes
     * @return totalDistance
     */
    public double getTotalDistance(){
        return totalDistance;
    }
   
    @Override
    public BicingState clone() {
        BicingState bs = new BicingState(current, next);
        if(this.moveCnt > 0){
            for(int i =0; i<this.moveCnt; i++){
                bs.from[i] = this.from[i];
                bs.to[i] = this.to[i];
                bs.transfer[i] = this.transfer[i];
                bs.vans[i] = this.vans[i];
            }
        }
        bs.actionCnt = this.actionCnt;
        bs.moveCnt = this.moveCnt;
        bs.totalDistance = this.totalDistance;
        return bs;
    }

    @Override
    public String toString() {
        int numStay = 0, numNext = 0, numDem = 0;
        int balance, mover;
        int sumBic = 0, sumDem = 0, sumAvai = 0, sumNeed = 0;
        double stdDem[], stdStay[], stdNext[];
        StringBuffer sb = new StringBuffer();
        stdDem = new double[getStationsNum()];
        stdStay = new double[getStationsNum()];
        stdNext = new double[getStationsNum()];
        sb.append(" #   Sty Nex Dem Dif Exc \n");
        for (int i = 0; i < getStationsNum(); i++) {
            numStay = getBikesNotMove(i);
            numNext = getBikesNext(i);
            numDem = getBikesDemanded(i);
            stdStay[i] = numStay;
            stdNext[i] = numNext;
            stdDem[i] = numDem;
            sumBic += numNext;
            sumDem += numDem;
            balance = numNext - numDem;
            if (balance > 0) {
                if (balance > numStay) {
                    mover = numStay;
                } else {
                    mover = balance;
                }
                sumAvai += mover;
            } else {
                mover = 0;
                sumNeed = sumNeed - balance;
            }
            String line = String.format("%3d: %3d %3d %3d %3d %3d\n", i, numStay, numNext, numDem, balance, mover);
            sb.append(line);
        }
        sb.append(String.format("\nBicis= %3d Demanda= %3d Disponibles= %3d Necesitan= %3d\n\n", sumBic, sumDem, sumAvai, sumNeed));
        if(actionCnt > 0){
            sb.append("moves: \n");
            for(int i =0; i<moveCnt; i++){
                sb.append(getMovementInfo(i));
            }
        }
        HeuristicFunction h = new Heuristic1();
        sb.append("h1: "+h.getHeuristicValue(this)+"\n");
        h = new Heuristic2();
        sb.append("h2: "+h.getHeuristicValue(this)+"\n");
        h = new Heuristic3(0.5);
        sb.append("h3: "+h.getHeuristicValue(this)+"\n");
        return sb.toString();
    }
}
