/*
 * Created on Sep 12, 2004
 *
 */
package aima.search.eightpuzzle;

import aima.basic.XYLocation;
import aima.search.framework.HeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */

public class MisplacedTilleHeuristicFunction implements HeuristicFunction {

	public double getHeuristicValue(Object state) {
		EightPuzzleBoard board = (EightPuzzleBoard) state;
		return getNumberOfMisplacedTiles(board);

	}

	private int getNumberOfMisplacedTiles(EightPuzzleBoard board) {
		int numberOfMisplacedTiles = 0;
		if (!(board.getLocationOf(0).equals(new XYLocation(0, 0)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(1).equals(new XYLocation(0, 1)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(2).equals(new XYLocation(0, 2)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(3).equals(new XYLocation(1, 0)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(4).equals(new XYLocation(1, 1)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(5).equals(new XYLocation(1, 2)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(6).equals(new XYLocation(2, 0)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(7).equals(new XYLocation(2, 1)))) {
			numberOfMisplacedTiles++;
		}
		if (!(board.getLocationOf(8).equals(new XYLocation(2, 2)))) {
			numberOfMisplacedTiles++;
		}
		return numberOfMisplacedTiles;
	}

}