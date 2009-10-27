package IA.Bicing;

import java.util.HashMap;
import java.util.Map;

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
     * number of demanded bicycles in next hour
     */
    private int demanded[];
    /**
     * current number of bicycles, that won't move this hour
     */
    private int current[];
    /**
     * number of bicycles after users' moves
     */
    private int next[];
    private int actionsCnt = 0;
    /**
     * coordinates of stations
     */
    private static int[][] coordinates;
    private Map<Integer, String> moves = new HashMap<Integer, String>();

    BicingState(int[] current, int[] next, int[] demanded, int[][] coordinates) {
        this.demanded = demanded;
        this.current = current;
        this.next = next;
        BicingState.coordinates = coordinates;
        BicingState.stationsNum = demanded.length;
    }

    /**
     * Deep copy
     * @param numStations
     * @param current
     * @param next
     * @param demanded
     */
    BicingState(int[] current, int[] next, int[] demanded) {
        this.demanded = new int[stationsNum];
        this.current = new int[stationsNum];
        this.next = new int[stationsNum];
        for (int i = 0; i < stationsNum; i++) {
            this.demanded[i] = demanded[i];
            this.current[i] = current[i];
            this.next[i] = next[i];
        }
    }

    /**
     * Moves bicicle from one station to another and record this move
     * @param fromSta
     * @param toSta
     * @param numBic
     */
    public void moveBicicle(int fromSta, int toSta, int biciclesNum) {
        String msg = Integer.toString(fromSta) + " -> " + Integer.toString(toSta) + ": " + biciclesNum;
        moves.put(++actionsCnt, msg);
        current[fromSta] -= biciclesNum;
        next[fromSta] -= biciclesNum;
        next[toSta] += biciclesNum;
    }

    public void dobleMoveBikes(int fromSta1, int toSta1, int biciclesNum1,
            int fromSta2, int toSta2, int biciclesNum2) {
        String msg = Integer.toString(fromSta1) + " -> " + Integer.toString(toSta1) + ": " + biciclesNum1 + "\n" +
                Integer.toString(fromSta2) + " -> " + Integer.toString(toSta2) + ": " + biciclesNum2;
        moves.put(++actionsCnt, msg);
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
        String res = (String) moves.get(actionsCnt);
        // System.out.println(res);
        return res;
    }

    public int getLevel() {
        return actionsCnt;
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
   
    @Override
    public BicingState clone() {
        BicingState bs = new BicingState(current, next, demanded);
        if(this.actionsCnt > 0){
            for(int i =0; i<this.actionsCnt; i++){
                String s = moves.get(i);
              //  System.out.println(i+": "+s);
                    bs.moves.put(i, s);
            }
        }
        bs.actionsCnt = this.actionsCnt;
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
        if(actionsCnt > 0){
            sb.append("moves: \n");
            for(int i =0; i<actionsCnt; i++){
                sb.append(moves.get(i));
            }
        }
        return sb.toString();
    }
}
