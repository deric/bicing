package IA.Bicing.demo;

import IA.Bicing.BicingGenerator;
import IA.Bicing.BicingGoalTest;
import IA.Bicing.BicingState;
import IA.Bicing.heuristic.Heuristic1;
import IA.Bicing.heuristic.Heuristic2;
import IA.Bicing.heuristic.Heuristic3;
import IA.Bicing.succesor.SuccessorFunction1;
import IA.Bicing.succesor.SuccessorFunction2;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.Scheduler;
import aima.search.informed.SimulatedAnnealingSearch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Tomas Barton
 */
public class BicingSA {
     private static int vanCapacity = 30;
     private static int numVan = 2;
     private static HeuristicFunction h;
     private static SuccessorFunction succesor;
     private static int k, limit;
     private static double lam;
     private static String msg;
     private static String filename;

     public static void main(String[] args) {
         if(args.length != 12){
             System.out.println("invalid number of arguments");
             System.exit(0);
         }
        int stations = Integer.valueOf(args[0]);
        int bikes = Integer.valueOf(args[1]);
        int mode = Integer.valueOf(args[2]);
       // int random = Integer.valueOf(args[3]);
        numVan = Integer.valueOf(args[4]);
        k = Integer.valueOf(args[5]);
        lam = Double.valueOf(args[6]);
        limit = Integer.valueOf(args[7]);
        int heur = Integer.valueOf(args[8]);
        int succ = Integer.valueOf(args[9]);
        double rate = Double.valueOf(args[10]);
        filename = args[11];

        switch(heur){
            case 1:
                h = new Heuristic1();
                break;
            case 2:
                h = new Heuristic2();
                break;
            case 3:
                h = new Heuristic3(rate);
                break;

        }
        switch(succ){
            case 1:
                    succesor = new SuccessorFunction1(numVan, vanCapacity);
                break;
            case 2:
                    succesor = new SuccessorFunction2(numVan, vanCapacity);
                break;
        }

        Random r = new Random();
        int random = r.nextInt();
        h = new Heuristic2();
        BicingGenerator b = new BicingGenerator(stations, bikes, mode, random);
        Object initialState = new BicingState(b.getCurrent(), b.getNext(),
                                    b.getDemand(), b.getStationsCoordinates(),numVan, vanCapacity);
       // System.out.println("\nInitial State  -->");
       // System.out.println(initialState);
       msg = "st:"+stations+",bikes:"+bikes+",mode:"+mode+",random:"+random+","+"vans:"+numVan+
               ",k="+k+",lam="+lam+",limit="+limit+",heur="+heur+",succ="+succ+",rate="+rate;
        simulatedAnnealingSearch(initialState);
    }

     private static void simulatedAnnealingSearch(Object initialState) {
		try {
                    long start, stop, totalTime = 0;
                    int expandedNodes = 0, tries = 10;
                    double heur = 0.0, avgHeur;
                    BicingState finalNode;
                    BicingState hc = hillClimb((BicingState) initialState);

                    for(int i=0; i<tries; i++){
                        Problem problem=new Problem(initialState,
                                succesor,
                                new BicingGoalTest(),
                                h
                                );

                        Scheduler s = new Scheduler(k, lam, limit);
                        start = System.currentTimeMillis(); // start timing
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(s);
			SearchAgent agent = new SearchAgent(problem, search);

			//printInstrumentation(agent.getInstrumentation());

                        finalNode = (BicingState) search.getLastSearchState();
                        stop = System.currentTimeMillis(); // stop timing
                        totalTime += (stop - start);
                        heur += h.getHeuristicValue(finalNode);
                        expandedNodes += Integer.valueOf(agent.getInstrumentation().getProperty("nodesExpanded"));
                    }
                    avgHeur = heur/tries;
                    double diff = h.getHeuristicValue(hc) - avgHeur;

                    System.out.println("time: "+totalTime/tries+", avgNodes: "+expandedNodes/tries+
                            ", avgHeuristic: "+ avgHeur+ ", diff: "+diff);
                    String res = msg +";"+totalTime/tries+";"+expandedNodes/tries+";"+avgHeur+";"+diff+"\n";
                    saveToFile(filename, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

        private static BicingState hillClimb(BicingState initialState){
            BicingState result = null;
            try {
                        Problem problem=new Problem(initialState,
                                succesor,
                                new BicingGoalTest(),
                                h
                                );

			HillClimbingSearch search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent(problem, search);

			result = (BicingState) search.getLastSearchState();

		} catch (Exception e) {
			e.printStackTrace();
		}
            return result;
        }


      public static void saveToFile(String filename, String content){
        BufferedWriter out = null;

        try {
            File f = new File(filename);
            if(f.exists()){
               out = new BufferedWriter(new FileWriter(filename, true));
            }else{
               out = new BufferedWriter(new FileWriter(filename));
            }
           

            out.write(content);
        } catch (IOException ex) {
            System.out.println(ex);
           // Logger.getLogger(self.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //Close the BufferedWriter
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
