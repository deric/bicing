package aima.logic.fol.inference;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface InferenceProcedure {
	/**
	 * 
	 * @param kb
	 *            the knowledge base against which the query is to be made.
	 * @param aQuery
	 *            to be answered.
	 * @return an InferenceResult.
	 */
	InferenceResult ask(FOLKnowledgeBase kb, Sentence aQuery);
}
