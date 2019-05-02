// Generated from Datalog.g4 by ANTLR 4.4

package comp6591.antlr;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DatalogParser}.
 */
public interface DatalogListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DatalogParser#factList}.
	 * @param ctx the parse tree
	 */
	void enterFactList(@NotNull DatalogParser.FactListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#factList}.
	 * @param ctx the parse tree
	 */
	void exitFactList(@NotNull DatalogParser.FactListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#aRule}.
	 * @param ctx the parse tree
	 */
	void enterARule(@NotNull DatalogParser.ARuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#aRule}.
	 * @param ctx the parse tree
	 */
	void exitARule(@NotNull DatalogParser.ARuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterFact(@NotNull DatalogParser.FactContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitFact(@NotNull DatalogParser.FactContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#ruleList}.
	 * @param ctx the parse tree
	 */
	void enterRuleList(@NotNull DatalogParser.RuleListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#ruleList}.
	 * @param ctx the parse tree
	 */
	void exitRuleList(@NotNull DatalogParser.RuleListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(@NotNull DatalogParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(@NotNull DatalogParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(@NotNull DatalogParser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(@NotNull DatalogParser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#builtInPredicate}.
	 * @param ctx the parse tree
	 */
	void enterBuiltInPredicate(@NotNull DatalogParser.BuiltInPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#builtInPredicate}.
	 * @param ctx the parse tree
	 */
	void exitBuiltInPredicate(@NotNull DatalogParser.BuiltInPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(@NotNull DatalogParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(@NotNull DatalogParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#queryList}.
	 * @param ctx the parse tree
	 */
	void enterQueryList(@NotNull DatalogParser.QueryListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#queryList}.
	 * @param ctx the parse tree
	 */
	void exitQueryList(@NotNull DatalogParser.QueryListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(@NotNull DatalogParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(@NotNull DatalogParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#lambda}.
	 * @param ctx the parse tree
	 */
	void enterLambda(@NotNull DatalogParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#lambda}.
	 * @param ctx the parse tree
	 */
	void exitLambda(@NotNull DatalogParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#predicateList}.
	 * @param ctx the parse tree
	 */
	void enterPredicateList(@NotNull DatalogParser.PredicateListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#predicateList}.
	 * @param ctx the parse tree
	 */
	void exitPredicateList(@NotNull DatalogParser.PredicateListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#headPredicate}.
	 * @param ctx the parse tree
	 */
	void enterHeadPredicate(@NotNull DatalogParser.HeadPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#headPredicate}.
	 * @param ctx the parse tree
	 */
	void exitHeadPredicate(@NotNull DatalogParser.HeadPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#stringList}.
	 * @param ctx the parse tree
	 */
	void enterStringList(@NotNull DatalogParser.StringListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#stringList}.
	 * @param ctx the parse tree
	 */
	void exitStringList(@NotNull DatalogParser.StringListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(@NotNull DatalogParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(@NotNull DatalogParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(@NotNull DatalogParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(@NotNull DatalogParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#datalogProgram}.
	 * @param ctx the parse tree
	 */
	void enterDatalogProgram(@NotNull DatalogParser.DatalogProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#datalogProgram}.
	 * @param ctx the parse tree
	 */
	void exitDatalogProgram(@NotNull DatalogParser.DatalogProgramContext ctx);
}