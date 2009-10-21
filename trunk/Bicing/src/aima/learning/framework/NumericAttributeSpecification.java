/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.framework;

/**
 * @author Ravi Mohan
 * 
 */
public class NumericAttributeSpecification implements AttributeSpecification {

	// a simple attribute representing a number reprsented as a double .
	private String name;

	public NumericAttributeSpecification(String name) {
		this.name = name;
	}

	public boolean isValid(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getAttributeName() {
		return name;
	}

	public Attribute createAttribute(String rawValue) {
		return new NumericAttribute(Double.parseDouble(rawValue), this);
	}

}
