
package IA.Bicing;

import java.io.InputStream;
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

    BicingState(int[] current, int[] next, int[] demanded ){
            this.demanded = demanded;
            this.current = current;
            this.next = next;
            stationsNum = demanded.length;
    }

    /**
     * Deep copy
     * @param numStations
     * @param current
     * @param next
     * @param demanded
     */
    BicingState(int numStations, int[] current, int[] next, int[] demanded){
        BicingState.stationsNum = numStations;
        this.demanded = new int[numStations];
        this.current = new int[numStations];
        this.next = new int[numStations];
        for(int i=0; i<numStations;i++){
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
    public void moveBicicle(int fromSta, int toSta, int biciclesNum){
        moves.put(++actionsCnt,Integer.toString(fromSta)+" -> "
                            +Integer.toString(toSta)+": "+biciclesNum);
        current[fromSta] = current[fromSta]-biciclesNum;
        current[toSta] = current[toSta]+biciclesNum;
    }

    public void dobleMoveBikes(int fromSta1, int toSta1, int biciclesNum1,
                        int fromSta2, int toSta2, int biciclesNum2){
        moves.put(++actionsCnt,Integer.toString(fromSta1)+" -> "
                            +Integer.toString(toSta1)+": "+biciclesNum1+"\n"+
                            Integer.toString(fromSta2)+" -> "
                            +Integer.toString(toSta2)+": "+biciclesNum2);
        current[fromSta1] = current[fromSta1]-biciclesNum1;
        current[toSta1] = current[toSta1]+biciclesNum1;
        current[fromSta2] = current[fromSta2]-biciclesNum2;
        current[toSta2] = current[toSta2]+biciclesNum2;
    }

    public static void setStationsNum(int num){
        stationsNum = num;
    }
   
    public int getStationsNum(){
        return stationsNum;
    }

    /**
     * Return true if we expect in next hour to have available bikes
     * @param stationIdx
     * @return
     */
    public boolean hasNextAvailableBike(int stationIdx){
        return ((current[stationIdx] - demanded[stationIdx]) > 0);
    }

    /**
     * Number of bikes available for transfer to another station
     * @param stationIdx
     * @return
     */
    public int getNextAvailableBikesNum(int stationIdx){
        return (int) (current[stationIdx] - demanded[stationIdx]);
    }

    public int getBikesNotMove(int idx){
        return current[idx];
    }

    public int getBikesNext(int idx){
        return next[idx];
    }

    public int getBikesDemanded(int idx){
        return demanded[idx];
    }

    /**
     * Number of bikes which are now at station
     * @param stationIdx
     * @return
     */
    public int getCurrentlyNotUsedBikesNum(int stationIdx){
       return (int) current[stationIdx];
    }

    public String getLastAction(){
       String res = (String) moves.get(actionsCnt);
      // System.out.println(res);
       return res;
    }

    @Override
    public BicingState clone(){
        return new BicingState(getStationsNum(),current, next, demanded);
    }

    @Override
    public String toString(){
        System.out.println("number of stations "+getStationsNum());
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
            String line = String.format("%3d: %3d %3d %3d %3d %3d\n",i, numStay, numNext, numDem, balance, mover);
            sb.append(line);
        }
        sb.append(String.format("\nBicis= %3d Demanda= %3d Disponibles= %3d Necesitan= %3d\n\n", sumBic, sumDem, sumAvai, sumNeed));
        return sb.toString();
    }

}