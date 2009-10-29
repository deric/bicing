package IA.Bicing;

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

    /**
     * move(s) to reach this state from initial state
     */
    private String[] moves;
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
    public BicingState(int[] current, int[] next, int[] demanded, int[][] coordinates, int maxDepth) {
        BicingState.demanded = demanded;
        this.current = current;
        this.next = next;
        BicingState.coordinates = coordinates;
        BicingState.stationsNum = demanded.length;
        BicingState.maxDepth = maxDepth;
        moves = new String[maxDepth];
        //maximum possible movements
        from = new int[2*maxDepth];
        to = new int[2*maxDepth];
        transfer = new int[2*maxDepth];
        vans = new int[2*maxDepth];
    }

    /**
     * Used for a deep copy of state
     * @param numStations
     * @param current
     * @param next
     * @param demanded
     */
    BicingState(int[] current, int[] next) {
        this.current = new int[stationsNum];
        this.next = new int[stationsNum];
        for (int i = 0; i < stationsNum; i++) {
            BicingState.demanded[i] = demanded[i];
            this.current[i] = current[i];
            this.next[i] = next[i];
        }
        moves = new String[maxDepth];
    }

    /**
     * OPERATOR
     * Moves bicicle from one station to another and record this move
     * @param fromSta
     * @param toSta
     * @param numBic
     */
    public void moveBicicle(int source, int destination, int bikesNum, int van) {
        setMove(moveCnt++, source, destination, bikesNum, van);
        actionCnt++;
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
    public void dobleMoveBikes(int source1, int destination1, int biciclesNum1,
            int source2, int destination2, int biciclesNum2, int van) {
        setMove(moveCnt++, source1, destination1, biciclesNum1, van);
        setMove(moveCnt++, source2, destination2, biciclesNum2, van);
        actionCnt++;
    }

    private void setMove(int action, int source, int destination, int bikeNum, int van){
        from[action] = source;
        to[action] = destination;
        transfer[action] = bikeNum;
        vans[action] = van;
        current[source] -= bikeNum;
        next[source] -= bikeNum;
        if(next[source] < 0){
           next[source] = 0;
        }
        next[destination] += bikeNum;
        double dist = getStationsDistance(source, destination);
        totalDistance += dist;
        /** TODO remove, for debugging only */
        String msg =  bikeNum + ": " +Integer.toString(source) + " -> " + Integer.toString(destination)
        +", dist: "+ dist;
        moves[actionCnt] = msg;
    }

    public void changeMove(int moveIdx, int destination, int bikesNum){
        int source = from[moveIdx];
        int van = vans[moveIdx];
        //TODO
        if(bikesNum == transfer[moveIdx]){
            removeMove(moveIdx);
        }else{
            //check if is possible to make more moves
        }
        
        setMove(moveCnt++, source, destination, bikesNum, van);
        actionCnt++;
    }

    public void removeMove(int moveIdx){
        int source = from[moveIdx];
        int destination = to[moveIdx];
        int bikeNum = transfer[moveIdx];
        double dist = getStationsDistance(from[moveIdx], to[moveIdx]);
        totalDistance-=dist;
        current[source] += bikeNum;
        next[source] += bikeNum;
        next[destination] -= bikeNum;
        moves[moveIdx]= null;
        moveCnt--;
        actionCnt--;
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
        return moves[(actionCnt-1)];
    }

    public int getActionCount() {
        return actionCnt;
    }

    public int getMoveCount(){
        return moveCnt;
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
                bs.moves[i] = new String(moves[i]);
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
            for(int i =0; i<actionCnt; i++){
                sb.append(moves[i]+"\n");
            }
        }
        return sb.toString();
    }
}
