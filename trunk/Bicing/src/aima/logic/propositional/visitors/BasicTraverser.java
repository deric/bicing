/*
 * Created on Dec 4, 2004
 *
 */
package aima.logic.propositional.visitors;

import java.util.Set;

import aima.logic.propositional.parsing.PLVisitor;
import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.FalseSentence;
import aima.logic.propositional.parsing.ast.MultiSentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.TrueSentence;
import aima.logic.propositional.parsing.ast.UnarySentence;
import aima.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */

/*
 * Super class of Visitors that are "read only" and gather information from an
 * existing parse tree .
 */

public class BasicTraverser implements PLVisitor {

	public Object visitSymbol(Symbol s, Object arg) {
		return arg;
	}

	public Object visitTrueSentence(TrueSentence ts, Object arg) {
		return arg;
	}

	public Object visitFalseSentence(FalseSentence fs, Object arg) {
		return arg;
	}

	public Object visitNotSentence(UnarySentence ns, Object arg) {
		Set s = (Set) arg;
		return new SetOps().union(s, (Set) ns.getNegated().accept(this, arg));
	}

	public Object visitBinarySentence(BinarySentence bs, Object arg) {
		Set s = (Set) arg;
		Set termunion = new SetOps().union((Set) bs.getFirst()
				.accept(this, arg), (Set) bs.getSecond().accept(this, arg));
		return new SetOps().union(s, termunion);
	}

	public Object visitMultiSentence(MultiSentence fs, Object arg) {
		throw new RuntimeException("Can't handle MultiSentence");
	}

}
