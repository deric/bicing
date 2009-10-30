package IA.Bicing.demo;

import IA.Bicing.BicingGenerator;
import IA.Bicing.BicingGoalTest;
import IA.Bicing.heuristic.Heuristic4;
import IA.Bicing.BicingState;
import IA.Bicing.succesor.SuccessorFunction1;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

/**
 *
 * @author Tomas Barton 
 */
public class BicingHC {
     private static int vanCapacity = 30;
     private static int numVan = 2;

     public static void main(String[] args) {
         if(args.length != 5){
             System.out.println("invalid number of arguments");
             System.exit(0);
         }
        int stations = Integer.valueOf(args[0]);
        int bikes = Integer.valueOf(args[1]);
        int mode = Integer.valueOf(args[2]);
        int random = Integer.valueOf(args[3]);
        numVan = Integer.valueOf(args[4]);

        System.out.println(random);
        BicingGenerator b = new BicingGenerator(stations, bikes, mode, random);
        Object initialState = new BicingState(b.getCurrent(), b.getNext(),
                                    b.getDemand(), b.getStationsCoordinates(),numVan);
        System.out.println("\nInitial State  -->");
        System.out.println(initialState);

        HeuristicFunction h = new Heuristic4(0.5);
        hillClimbingSearch(initialState, h);
    }

     private static void hillClimbingSearch(Object initialState,HeuristicFunction h) {
		System.out.println("\nHillClimbing  -->");
		try {
                        Problem problem=new Problem(initialState,
                                new SuccessorFunction1(numVan, vanCapacity),
                                new BicingGoalTest(),
                                h
                                );

			HillClimbingSearch search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			//printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			//printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
