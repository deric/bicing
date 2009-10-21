/*
 * Created on Sep 14, 2003 by Ravi Mohan
 *  
 */
package aima.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Predicate implements AtomicSentence {
	private String predicateName;
	private List<Term> terms = new ArrayList<Term>();
	private String stringRep = null;
	private int hashCode = 0;

	public Predicate(String predicateName, List<Term> terms) {
		this.predicateName = predicateName;
		this.terms.addAll(terms);
	}

	public String getPredicateName() {
		return predicateName;
	}

	public List<Term> getTerms() {
		return Collections.unmodifiableList(terms);
	}

	//
	// START-AtomicSentence
	public String getSymbolicName() {
		return getPredicateName();
	}

	public boolean isCompound() {
		return true;
	}

	public List<Term> getArgs() {
		return getTerms();
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitPredicate(this, arg);
	}

	public Predicate copy() {
		List<Term> copyTerms = new ArrayList<Term>();
		for (Term t : terms) {
			copyTerms.add(t.copy());
		}
		return new Predicate(predicateName, copyTerms);
	}

	// END-AtomicSentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Predicate)) {
			return false;
		}
		Predicate p = (Predicate) o;
		return p.getPredicateName().equals(getPredicateName())
				&& p.getTerms().equals(getTerms());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + predicateName.hashCode();
			for (Term t : terms) {
				hashCode = 37 * hashCode + t.hashCode();
			}
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(predicateName);
			sb.append("(");

			boolean first = true;
			for (Term t : terms) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(t.toString());
			}

			sb.append(")");
			stringRep = sb.toString();
		}

		return stringRep;
	}
}