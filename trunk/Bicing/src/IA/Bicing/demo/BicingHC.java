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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
        Date start = new Date();
        BicingGenerator b = new BicingGenerator(stations, bikes, mode, random);
        Object initialState = new BicingState(b.getCurrent(), b.getNext(),
                                    b.getDemand(), b.getStationsCoordinates(),numVan);
        System.out.println("\nInitial State  -->");
        System.out.println(initialState);

        HeuristicFunction h = new Heuristic4(0.5);
        hillClimbingSearch(initialState, h);
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime()+ " millisecond");
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
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
      private static void printInstrumentation(Properties properties) {
		Iterator keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = (String) actions.get(i);
			System.out.println(action);
		}
	}
}
