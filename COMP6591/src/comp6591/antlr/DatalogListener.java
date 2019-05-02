// Generated from Datalog.g4 by ANTLR 4.7

package comp6591.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DatalogParser}.
 */
public interface DatalogListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DatalogParser#datalogProgram}.
	 * @param ctx the parse tree
	 */
	void enterDatalogProgram(DatalogParser.DatalogProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#datalogProgram}.
	 * @param ctx the parse tree
	 */
	void exitDatalogProgram(DatalogParser.DatalogProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(DatalogParser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(DatalogParser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterFact(DatalogParser.FactContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitFact(DatalogParser.FactContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#factList}.
	 * @param ctx the parse tree
	 */
	void enterFactList(DatalogParser.FactListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#factList}.
	 * @param ctx the parse tree
	 */
	void exitFactList(DatalogParser.FactListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#aRule}.
	 * @param ctx the parse tree
	 */
	void enterARule(DatalogParser.ARuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#aRule}.
	 * @param ctx the parse tree
	 */
	void exitARule(DatalogParser.ARuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#ruleList}.
	 * @param ctx the parse tree
	 */
	void enterRuleList(DatalogParser.RuleListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#ruleList}.
	 * @param ctx the parse tree
	 */
	void exitRuleList(DatalogParser.RuleListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#headPredicate}.
	 * @param ctx the parse tree
	 */
	void enterHeadPredicate(DatalogParser.HeadPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#headPredicate}.
	 * @param ctx the parse tree
	 */
	void exitHeadPredicate(DatalogParser.HeadPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#builtInPredicate}.
	 * @param ctx the parse tree
	 */
	void enterBuiltInPredicate(DatalogParser.BuiltInPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#builtInPredicate}.
	 * @param ctx the parse tree
	 */
	void exitBuiltInPredicate(DatalogParser.BuiltInPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(DatalogParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(DatalogParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#predicateList}.
	 * @param ctx the parse tree
	 */
	void enterPredicateList(DatalogParser.PredicateListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#predicateList}.
	 * @param ctx the parse tree
	 */
	void exitPredicateList(DatalogParser.PredicateListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(DatalogParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(DatalogParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(DatalogParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(DatalogParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(DatalogParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(DatalogParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(DatalogParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(DatalogParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#queryList}.
	 * @param ctx the parse tree
	 */
	void enterQueryList(DatalogParser.QueryListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#queryList}.
	 * @param ctx the parse tree
	 */
	void exitQueryList(DatalogParser.QueryListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#stringList}.
	 * @param ctx the parse tree
	 */
	void enterStringList(DatalogParser.StringListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#stringList}.
	 * @param ctx the parse tree
	 */
	void exitStringList(DatalogParser.StringListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#lambda}.
	 * @param ctx the parse tree
	 */
	void enterLambda(DatalogParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#lambda}.
	 * @param ctx the parse tree
	 */
	void exitLambda(DatalogParser.LambdaContext ctx);
}