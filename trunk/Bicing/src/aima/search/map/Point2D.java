package aima.search.map;

/**
 * Simplified version of <code>java.awt.geom.Point2D</code>. We do not
 * want dependencies to presentation layer packages here.
 * @author R. Lunde
 */
public class Point2D {
	private double x;
	private double y;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	/**
	 * Returns the Euclidean distance between a specified point
	 * and this point.
	 */
	public double distance(Point2D pt) {
		double result = (pt.getX() - x) * (pt.getX() - x);
		result += (pt.getY() - y) * (pt.getY() - y);
		return Math.sqrt(result);
	}
}
