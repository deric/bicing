package Bicing.test.solution;

import IA.Bicing.BicingGenerator;
import IA.Bicing.BicingGoalTest;
import IA.Bicing.heuristic.Heuristic5;
import IA.Bicing.heuristic.Heuristic4;
import IA.Bicing.BicingState;
import IA.Bicing.heuristic.Greedy;
import IA.Bicing.heuristic.Heuristic2;
import IA.Bicing.succesor.SuccessorFunction1;
import IA.Bicing.succesor.SuccessorFunction2;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.Scheduler;
import aima.search.informed.SimulatedAnnealingSearch;
import junit.framework.TestCase;

/**
 *
 * @author Tomas Barton
 */
public class Est10Test extends TestCase {
    	private BicingState b;
        private int numVan = 2;
        private static int vanCapacity = 30;

	public  Est10Test(String name) {
		super(name);
	}

	@Override
	public void setUp() {
             int stations = 4;
             BicingGenerator bg = new BicingGenerator(10, 100, BicingGenerator.RUSH_HOUR, -2098605270);
              b = new BicingState(bg.getCurrent(), bg.getNext(),
                                    bg.getDemand(), bg.getStationsCoordinates(),2);
	}

        public void testHillClimbingHeristic4(){
                System.out.println(b);
            	System.out.println("\nHillClimbing  -->");
                HeuristicFunction h = new Heuristic2();
		try {
                        Problem problem=new Problem(b,
                                new SuccessorFunction1(numVan, vanCapacity),
                                new BicingGoalTest(),
                                h
                                );

			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch();
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			//printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
                        BicingState finalState =(BicingState) search.getLastSearchState();
                        System.out.print(finalState);
			//printInstrumentation(agent.getInstrumentation());

		} catch (Exception e) {
			e.printStackTrace();
		}
        }

         public void testSimulatedAnnealing(){
            	System.out.println("\nSimulated Annealing  -->");
                HeuristicFunction h = new Heuristic2();
                Greedy g = new Greedy(b);
                g.simplySolve();
                System.out.println("greedy solution");
                System.out.println(b);
		try {
                        Problem problem=new Problem(b,
                                new SuccessorFunction1(numVan, vanCapacity),
                                new BicingGoalTest(),
                                h
                                );

                        Scheduler s = new Scheduler(20, 0.045, 100);
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(s);
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			//printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
                        BicingState finalState =(BicingState) search.getLastSearchState();
                     //   System.out.print(finalState);
			//printInstrumentation(agent.getInstrumentation());

		} catch (Exception e) {
			e.printStackTrace();
		}
        }
}