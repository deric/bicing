package aima.search.framework;

import aima.search.framework.Search;
import aima.search.framework.GraphSearch;
import aima.search.framework.TreeSearch;
import aima.search.framework.QueueSearch;
import aima.search.informed.AStarEvaluationFunction;
import aima.search.informed.AStarSearch;
import aima.search.informed.GreedyBestFirstSearch;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.RecursiveBestFirstSearch;
import aima.search.uninformed.BreadthFirstSearch;
import aima.search.uninformed.DepthFirstSearch;
import aima.search.uninformed.IterativeDeepeningSearch;
import aima.search.uninformed.UniformCostSearch;

/**
 * Useful factory for configuring search objects. Implemented
 * as a singleton.
 * @author R. Lunde
 */ 
public class SearchFactory {

	/** Search strategy: Depth first search. */
	public final static int DF_SEARCH = 0;
	/** Search strategy: Depth first search. */
	public final static int BF_SEARCH = 1;
	/** Search strategy: Iterative deepening search. */
	public final static int ID_SEARCH = 2;
	/** Search strategy: Uniform cost search. */
	public final static int UC_SEARCH = 3;
	/** Search strategy: Greedy best first search. */
	public final static int GBF_SEARCH = 4;
	/** Search strategy: A* search. */
	public final static int ASTAR_SEARCH = 5;
	/** Search strategy: Recursive best first search. */
	public final static int RBF_SEARCH = 6;
	/** Search strategy: Hill climbing search. */
	public final static int HILL_SEARCH = 7;

	/** Search mode: tree search. */
	public final static int TREE_SEARCH = 0;
	/** Search mode: graph search. */
	public final static int GRAPH_SEARCH = 1;
	
	/** Contains the only existing instance. */
	private static SearchFactory instance;
	/** Invisible constructor. */
	private SearchFactory() {};
	
	/** Provides access to the factory. Implemented with lazy instantiation. */
	public static SearchFactory getInstance() {
		if (instance == null)
			instance = new SearchFactory();
		return instance;
	}
	
	/**
	 * Returns the names of all search strategies, which are supported
	 * by this factory. The indices correspond to the parameter values
	 * of method {@link #createSearch(int, int)}.
	 */ 
	public String[] getSearchStrategyNames() {
		return new String[]{
				"Depth First", "Breadth First", "Iterative Deepening",
				"Uniform Cost", "Greedy Best First", "A*",
				"Recursive Best First", "Hill Climbing"};
	}
	
	/**
	 * Returns the names of all search modes, which are supported
	 * by this factory. The indices correspond to the parameter values
	 * of method {@link #createSearch(int, int)}.
	 */ 
	public String[] getSearchModeNames() {
		return new String[]{"Tree Search", "Graph Search"};
	}
	
	/**
	 * Creates a search instance.
	 * @param strategy search strategy. See static constants.
	 * @param mode     search mode: {@link #TREE_SEARCH} or {@link #GRAPH_SEARCH}
	 * 
	 */
	public Search createSearch(int strategy, int mode) {
		QueueSearch qs = null;
		Search result = null;
		switch(mode) {
		case TREE_SEARCH: qs = new TreeSearch(); break;
		case GRAPH_SEARCH: qs = new GraphSearch();
		}
		switch (strategy) {
			case DF_SEARCH:
				result = new DepthFirstSearch(qs); break;
			case BF_SEARCH:
				result = new BreadthFirstSearch(qs); break;
			case ID_SEARCH:
				result = new IterativeDeepeningSearch(); break;
			case UC_SEARCH:
				result = new UniformCostSearch(qs); break;
			case GBF_SEARCH:
				result = new GreedyBestFirstSearch(qs); break;
			case ASTAR_SEARCH:
				result = new AStarSearch(qs); break;
			case RBF_SEARCH:
				result = new RecursiveBestFirstSearch(new AStarEvaluationFunction()); break;
			case HILL_SEARCH:
				result = new HillClimbingSearch(); break;
		}
		return result;
	}
}
