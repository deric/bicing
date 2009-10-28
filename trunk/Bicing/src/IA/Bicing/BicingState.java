package IA.Bicing;

/**
 * Represents number of rented, demanded bikes in a city
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
     * current number of bicycles, that won't move this hour
     */
    private int current[];
    /**
     * number of bicycles after users' moves
     */
    private int next[];
    /**
     * depth in a tree
     */
    private int actionCnt = 0;
    /**
     * coordinates of stations
     */
    private static int[][] coordinates;
    /**
     * move(s) to reach this state from initial state
     */
    private String[] moves;
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
    BicingState(int[] current, int[] next, int[] demanded, int[][] coordinates, int maxDepth) {
        BicingState.demanded = demanded;
        this.current = current;
        this.next = next;
        BicingState.coordinates = coordinates;
        BicingState.stationsNum = demanded.length;
        BicingState.maxDepth = maxDepth;
        moves = new String[maxDepth];
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
     * Moves bicicle from one station to another and record this move
     * @param fromSta
     * @param toSta
     * @param numBic
     */
    public void moveBicicle(int fromSta, int toSta, int biciclesNum) {
        double dist = getStationsDistance(fromSta, toSta);
        String msg =  biciclesNum + ": " +Integer.toString(fromSta) + " -> " + Integer.toString(toSta)
                +", dist: "+ dist;
        totalDistance += dist;
        moves[actionCnt++] = msg;
        current[fromSta] -= biciclesNum;
        next[fromSta] -= biciclesNum;
        next[toSta] += biciclesNum;
    }

    public void dobleMoveBikes(int fromSta1, int toSta1, int biciclesNum1,
            int fromSta2, int toSta2, int biciclesNum2) {
        double dist1, dist2;
        dist1 = getStationsDistance(fromSta1, toSta1);
        dist2 = getStationsDistance(toSta1, toSta2);
        String msg = biciclesNum1+": "+Integer.toString(fromSta1) + " -> " + Integer.toString(toSta1)
                + ", dist: " + dist1 + "\n" +
                biciclesNum2+": "+Integer.toString(fromSta2) + " -> " + Integer.toString(toSta2)
                + ", dist: " + dist2;
        moves[actionCnt++] = msg;
        totalDistance += (dist1+dist2);
        
        current[fromSta1] -= biciclesNum1;
        next[fromSta1] -= biciclesNum1;
        next[toSta1] += biciclesNum1;
        current[fromSta2] -= biciclesNum2;
        next[fromSta2] -= biciclesNum2;
        next[toSta2] += biciclesNum2;
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

    public int getLevel() {
        return actionCnt;
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
        if(this.actionCnt > 0){
            for(int i =0; i<this.actionCnt; i++){
                bs.moves[i] = new String(moves[i]);
            }
        }
        bs.actionCnt = this.actionCnt;
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
