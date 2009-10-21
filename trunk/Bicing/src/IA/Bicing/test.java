/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IA.Bicing;

//import cern.colt.list.DoubleArrayList;
//import cern.jet.stat.Descriptive;

/**
 *
 * @author bejar
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int numStay = 0, numCurr = 0, numDem = 0;
        int balance, mover;
        int sumBic = 0, sumDem = 0, sumAvai = 0, sumNeed = 0;
        double stdDem[], stdStay[], stdCurr[];

        BicingGenerator b = new BicingGenerator(20, 1000, BicingGenerator.RUSH_HOUR, 200);

        stdDem = new double[b.getNumStations()];
        stdStay = new double[b.getNumStations()];
        stdCurr = new double[b.getNumStations()];

        System.out.println("Mov Cur Dem Dif Exc ");

        for (int i = 0; i < b.getNumStations(); i++) {
            numStay = b.getStationDoNotMove(i);
            numCurr = b.getStationState(i);
            numDem = b.getDemandNextHour(i);
            stdStay[i] = numStay;
            stdCurr[i] = numCurr;
            stdDem[i] = numDem;
            sumBic = sumBic + numCurr;
            sumDem = sumDem + numDem;
            balance = numCurr - numDem;
            if (balance > 0) {
                if (balance > numStay) {
                    mover = numStay;
                } else {
                    mover = balance;
                }
                sumAvai = sumAvai + mover;
            } else {
                mover = 0;
                sumNeed = sumNeed - balance;
            }

            System.out.format("%3d %3d %3d %3d %3d\n", numStay, numCurr, numDem, balance, mover);
        }


        System.out.format("\nBicis= %3d Demanda= %3d Disponibles= %3d Necesitan= %3d\n\n", sumBic, sumDem, sumAvai, sumNeed);

//        DoubleArrayList dal = new DoubleArrayList(stdDem);
//        System.out.format("Demanda = %f %f\n", Descriptive.mean(dal),
//                Descriptive.standardDeviation(Descriptive.variance(dal.size(), Descriptive.sum(dal), Descriptive.sumOfSquares(dal))));
//
//        dal = new DoubleArrayList(stdStay);
//        System.out.format("No mueven = %f %f\n", Descriptive.mean(dal),
//                Descriptive.standardDeviation(Descriptive.variance(dal.size(), Descriptive.sum(dal), Descriptive.sumOfSquares(dal))));
//
//        dal = new DoubleArrayList(stdCurr);
//        System.out.format("Hay = %f %f\n\n", Descriptive.mean(dal),
//                Descriptive.standardDeviation(Descriptive.variance(dal.size(), Descriptive.sum(dal), Descriptive.sumOfSquares(dal))));
//

        System.out.format(" [X]  [Y]\n");
        for (int i = 0; i < b.getNumStations(); i++) {
            System.out.format("est %2d = %2d %2d\n", i, b.getStationCoord(i)[0], b.getStationCoord(i)[1]);
        }
System.out.println();
        for (int i = 0; i < b.getNumStations(); i++) {
            for (int j = 0; j < b.getNumStations(); j++) {
                System.out.format("%4.1f ", b.getStationsDistance(i,j));
            }
            System.out.println();
        }


    }
}
