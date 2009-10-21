package IA.Bicing;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

/**
 *
 * @author Tomas Barton 
 */
public class BicingDemo {
    private int numVan = 1;
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BicingGenerator initialState = new BicingGenerator(100, 5, 1);
        try {

            Problem problem=new Problem(initialState,
                    new BicingSuccessorFunction1(),
                    new BicingGoalTest(),
                    new BicingHeuristicFunction1()
                    );
            Search search= new HillClimbingSearch();
           // SearchAgent agent = new SearchAgent (problem,search);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
