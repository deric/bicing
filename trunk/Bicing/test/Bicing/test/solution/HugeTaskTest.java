package Bicing.test.solution;

import IA.Bicing.BicingGenerator;
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
import aima.search.informed.Scheduler;
import aima.search.informed.SimulatedAnnealingSearch;
import java.util.Random;
import junit.framework.TestCase;

/**
 *
 * @author Tomas Barton
 */
public class HugeTaskTest  extends TestCase {
    	private BicingState b;
        private int numVan = 20;
        private static int vanCapacity = 30;
        private static int stations = 20;
        private static int bikes = 1000;

	public HugeTaskTest(String name) {
		super(name);
	}
	@Override
	public void setUp() {
              Random generator = new Random();
              BicingGenerator g = new BicingGenerator(stations, bikes,
                      BicingGenerator.EQUILIBRIUM, generator.nextInt());

              b  = new BicingState(g.getCurrent(), g.getNext(),
                                    g.getDemand(), g.getStationsCoordinates(),numVan, vanCapacity);
	}

        public void testHillClimbingHeristic1(){
                System.out.println(b);
            	System.out.println("\nHillClimbing  -->");
                HeuristicFunction h = new Heuristic1();
		try {
                        Problem problem=new Problem( (Object)b.clone(),
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


		} catch (Exception e) {
			e.printStackTrace();
		}
        }

        public void testSA(){
            System.out.println("\nSimulated Annealing  -->");
                BicingState bs = b.clone();
                Greedy g = new Greedy( bs);
                g.simplySolve();
                System.out.println("greedy solution");
                System.out.println(bs);

		try {
                          Problem problem=new Problem(bs,
                                new SuccessorFunction2(numVan, vanCapacity),
                                new BicingGoalTest(),
                                new Heuristic2()
                                );
                        Scheduler s = new Scheduler(20, 0.055, 300);
			SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(s);
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
                         BicingState finalState =(BicingState) search.getLastSearchState();


		} catch (Exception e) {
			e.printStackTrace();
		}
        }

}
