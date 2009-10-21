package aima.test.search.searches;

import junit.framework.TestCase;
import aima.basic.BasicEnvironmentView;
import aima.search.framework.HeuristicFunction;
import aima.search.informed.AStarEvaluationFunction;
import aima.search.informed.RecursiveBestFirstSearch;
import aima.search.map.Map;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.map.Point2D;
import aima.search.map.SimplifiedRoadMapOfPartOfRomania;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RecursiveBestFirstSearchTest extends TestCase {

	StringBuffer envChanges;

	Map aMap;

	RecursiveBestFirstSearch recursiveBestFirstSearch;

	HeuristicFunction heuristicFunction;

	@Override
	public void setUp() {
		envChanges = new StringBuffer();

		aMap = new SimplifiedRoadMapOfPartOfRomania();

		recursiveBestFirstSearch = new RecursiveBestFirstSearch(
				new AStarEvaluationFunction());

		heuristicFunction = new HeuristicFunction() {
			public double getHeuristicValue(Object state) {
				Point2D pt1 = aMap.getPosition((String) state);
				Point2D pt2 = aMap.getPosition(SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
				return pt1.distance(pt2);
			}
		};
	}

	public void testStartingAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(heuristicFunction);

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(Bucharest), Goal=In(Bucharest):NoOP:METRIC[pathCost]=0.0:METRIC[maxRecursiveDepth]=0:METRIC[nodesExpanded]=0:NoOP:",
				envChanges.toString());
	}

	public void testExampleFromBookFigure4_6Page103() {
		MapEnvironment me = new MapEnvironment(aMap);
		MapAgent ma = new MapAgent(me, recursiveBestFirstSearch,
				new String[] { SimplifiedRoadMapOfPartOfRomania.BUCHAREST });
		ma.setHeuristicFunction(heuristicFunction);

		me.addAgent(ma, SimplifiedRoadMapOfPartOfRomania.ARAD);
		me.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command).append(":");
			}
		});
		me.stepUntilDone();

		assertEquals(
				"CurrentLocation=In(Arad), Goal=In(Bucharest):Sibiu:RimnicuVilcea:Pitesti:Bucharest:METRIC[pathCost]=418.0:METRIC[maxRecursiveDepth]=4:METRIC[nodesExpanded]=6:NoOP:",
				envChanges.toString());
	}
}
