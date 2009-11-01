package Bicing.test.solution;

import IA.Bicing.BicingGoalTest;
import IA.Bicing.BicingState;
import IA.Bicing.heuristic.Greedy;
import IA.Bicing.heuristic.Heuristic1;
import IA.Bicing.heuristic.Heuristic2;
import IA.Bicing.succesor.SuccessorFunction1;
import IA.Bicing.succesor.SuccessorFunction2;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import junit.framework.TestCase;

/**
 *
 * @author Tomas Barton 
 */
public class SolutionTest  extends TestCase {
    	private BicingState b;
        private int numVan = 2;
        private static int vanCapacity = 30;

	public SolutionTest(String name) {
		super(name);
	}
	@Override
	public void setUp() {
             int stations = 4;
             int[] current = new int[stations];
                   current[0] = 10;
                   current[1] = 0;
                   current[2] = 0;
                   current[3] = 0;
             int[] next = new int[stations];
                   next[0] = 0;
                   next[1] = 0;
                   next[2] = 0;
                   next[3] = 0;
             int[] demanded = new int[stations];
                   demanded[0] = 0;
                   demanded[1] = 0;
                   demanded[2] = 2;
                   demanded[3] = 8;
             int[][] coordinates = new int[stations][2];
                   coordinates[0][0] = 0;
                   coordinates[0][1] = 0;
                   coordinates[1][0] = 0;
                   coordinates[1][1] = 1;
                   coordinates[2][0] = 1;
                   coordinates[2][1] = 0;
                   coordinates[3][0] = 1;
                   coordinates[3][1] = 1;

              b = new BicingState(current,next,demanded, coordinates, numVan);
	}

        public void testHillClimbingHeristic1(){
                System.out.println(b);
            	System.out.println("\nHillClimbing  -->");
                HeuristicFunction h = new Heuristic1();
		try {
                        Problem problem=new Problem(b,
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
                        BicingState finalState =(BicingState) search.getLastSearchState();
                        assertEquals(2, finalState.getBikesNext(2));
                        assertEquals(8, finalState.getBikesNext(3));
			//printInstrumentation(agent.getInstrumentation());

		} catch (Exception e) {
			e.printStackTrace();
		}
        }

        public void testSA(){
            System.out.println("\nSimulated Annealing  -->");
                Greedy g = new Greedy(b);
                g.simplySolve();
                System.out.println("greedy solution");
                System.out.println(b);

		try {
                          Problem problem=new Problem(b,
                                new SuccessorFunction2(numVan, vanCapacity),
                                new BicingGoalTest(),
                                new Heuristic1()
                                );
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch();
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
                         BicingState finalState =(BicingState) search.getLastSearchState();
                        assertEquals(2, finalState.getBikesNext(2));
                        assertEquals(8, finalState.getBikesNext(3));

		} catch (Exception e) {
			e.printStackTrace();
		}
        }

}
