// Generated from Datalog.g4 by ANTLR 4.7

package comp6591.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DatalogParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COMMA=1, PERIOD=2, Q_MARK=3, LEFT_PAREN=4, RIGHT_PAREN=5, COLON=6, COLON_DASH=7, 
		EQUAL=8, NONEQUAL=9, GREATER=10, GREATERSTRICT=11, LESS=12, LESSSTRICT=13, 
		PREDICATE_NAME=14, ID=15, STRING=16, COMMENT=17, WS=18;
	public static final int
		RULE_datalogProgram = 0, RULE_idList = 1, RULE_fact = 2, RULE_factList = 3, 
		RULE_aRule = 4, RULE_ruleList = 5, RULE_headPredicate = 6, RULE_builtInPredicate = 7, 
		RULE_predicate = 8, RULE_predicateList = 9, RULE_parameter = 10, RULE_parameterList = 11, 
		RULE_operator = 12, RULE_query = 13, RULE_queryList = 14, RULE_stringList = 15, 
		RULE_lambda = 16;
	public static final String[] ruleNames = {
		"datalogProgram", "idList", "fact", "factList", "aRule", "ruleList", "headPredicate", 
		"builtInPredicate", "predicate", "predicateList", "parameter", "parameterList", 
		"operator", "query", "queryList", "stringList", "lambda"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'.'", "'?'", "'('", "')'", "':'", "':-'", "'='", "'<>'", 
		"'>'", "'>='", "'<'", "'<='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "COMMA", "PERIOD", "Q_MARK", "LEFT_PAREN", "RIGHT_PAREN", "COLON", 
		"COLON_DASH", "EQUAL", "NONEQUAL", "GREATER", "GREATERSTRICT", "LESS", 
		"LESSSTRICT", "PREDICATE_NAME", "ID", "STRING", "COMMENT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Datalog.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DatalogParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DatalogProgramContext extends ParserRuleContext {
		public RuleListContext ruleList() {
			return getRuleContext(RuleListContext.class,0);
		}
		public FactListContext factList() {
			return getRuleContext(FactListContext.class,0);
		}
		public DatalogProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datalogProgram; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDatalogProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDatalogProgram(this);
		}
	}

	public final DatalogProgramContext datalogProgram() throws RecognitionException {
		DatalogProgramContext _localctx = new DatalogProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_datalogProgram);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			ruleList();
			setState(35);
			factList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdListContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(DatalogParser.COMMA, 0); }
		public TerminalNode ID() { return getToken(DatalogParser.ID, 0); }
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public IdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterIdList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitIdList(this);
		}
	}

	public final IdListContext idList() throws RecognitionException {
		IdListContext _localctx = new IdListContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_idList);
		try {
			setState(41);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMA:
				enterOuterAlt(_localctx, 1);
				{
				setState(37);
				match(COMMA);
				setState(38);
				match(ID);
				setState(39);
				idList();
				}
				break;
			case RIGHT_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(40);
				lambda();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FactContext extends ParserRuleContext {
		public TerminalNode PREDICATE_NAME() { return getToken(DatalogParser.PREDICATE_NAME, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(DatalogParser.LEFT_PAREN, 0); }
		public TerminalNode STRING() { return getToken(DatalogParser.STRING, 0); }
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(DatalogParser.RIGHT_PAREN, 0); }
		public TerminalNode PERIOD() { return getToken(DatalogParser.PERIOD, 0); }
		public FactContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fact; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterFact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitFact(this);
		}
	}

	public final FactContext fact() throws RecognitionException {
		FactContext _localctx = new FactContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_fact);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(PREDICATE_NAME);
			setState(44);
			match(LEFT_PAREN);
			setState(45);
			match(STRING);
			setState(46);
			stringList();
			setState(47);
			match(RIGHT_PAREN);
			setState(48);
			match(PERIOD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FactListContext extends ParserRuleContext {
		public FactContext fact() {
			return getRuleContext(FactContext.class,0);
		}
		public FactListContext factList() {
			return getRuleContext(FactListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public FactListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterFactList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitFactList(this);
		}
	}

	public final FactListContext factList() throws RecognitionException {
		FactListContext _localctx = new FactListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_factList);
		try {
			setState(54);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREDICATE_NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				fact();
				setState(51);
				factList();
				}
				break;
			case EOF:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				lambda();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ARuleContext extends ParserRuleContext {
		public HeadPredicateContext headPredicate() {
			return getRuleContext(HeadPredicateContext.class,0);
		}
		public TerminalNode COLON_DASH() { return getToken(DatalogParser.COLON_DASH, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public PredicateListContext predicateList() {
			return getRuleContext(PredicateListContext.class,0);
		}
		public TerminalNode PERIOD() { return getToken(DatalogParser.PERIOD, 0); }
		public ARuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterARule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitARule(this);
		}
	}

	public final ARuleContext aRule() throws RecognitionException {
		ARuleContext _localctx = new ARuleContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_aRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			headPredicate();
			setState(57);
			match(COLON_DASH);
			setState(58);
			predicate();
			setState(59);
			predicateList();
			setState(60);
			match(PERIOD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleListContext extends ParserRuleContext {
		public ARuleContext aRule() {
			return getRuleContext(ARuleContext.class,0);
		}
		public RuleListContext ruleList() {
			return getRuleContext(RuleListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public RuleListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterRuleList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitRuleList(this);
		}
	}

	public final RuleListContext ruleList() throws RecognitionException {
		RuleListContext _localctx = new RuleListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ruleList);
		try {
			setState(66);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(62);
				aRule();
				setState(63);
				ruleList();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				lambda();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadPredicateContext extends ParserRuleContext {
		public TerminalNode PREDICATE_NAME() { return getToken(DatalogParser.PREDICATE_NAME, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(DatalogParser.LEFT_PAREN, 0); }
		public TerminalNode ID() { return getToken(DatalogParser.ID, 0); }
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(DatalogParser.RIGHT_PAREN, 0); }
		public HeadPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterHeadPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitHeadPredicate(this);
		}
	}

	public final HeadPredicateContext headPredicate() throws RecognitionException {
		HeadPredicateContext _localctx = new HeadPredicateContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_headPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(PREDICATE_NAME);
			setState(69);
			match(LEFT_PAREN);
			setState(70);
			match(ID);
			setState(71);
			idList();
			setState(72);
			match(RIGHT_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BuiltInPredicateContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public BuiltInPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_builtInPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterBuiltInPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitBuiltInPredicate(this);
		}
	}

	public final BuiltInPredicateContext builtInPredicate() throws RecognitionException {
		BuiltInPredicateContext _localctx = new BuiltInPredicateContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_builtInPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			parameter();
			setState(75);
			operator();
			setState(76);
			parameter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateContext extends ParserRuleContext {
		public BuiltInPredicateContext builtInPredicate() {
			return getRuleContext(BuiltInPredicateContext.class,0);
		}
		public TerminalNode PREDICATE_NAME() { return getToken(DatalogParser.PREDICATE_NAME, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(DatalogParser.LEFT_PAREN, 0); }
		public ParameterContext parameter() {
			return getRuleContext(ParameterContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(DatalogParser.RIGHT_PAREN, 0); }
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitPredicate(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_predicate);
		try {
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(78);
				builtInPredicate();
				}
				break;
			case PREDICATE_NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(79);
				match(PREDICATE_NAME);
				setState(80);
				match(LEFT_PAREN);
				setState(81);
				parameter();
				setState(82);
				parameterList();
				setState(83);
				match(RIGHT_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateListContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(DatalogParser.COMMA, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public PredicateListContext predicateList() {
			return getRuleContext(PredicateListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public PredicateListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterPredicateList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitPredicateList(this);
		}
	}

	public final PredicateListContext predicateList() throws RecognitionException {
		PredicateListContext _localctx = new PredicateListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_predicateList);
		try {
			setState(92);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMA:
				enterOuterAlt(_localctx, 1);
				{
				setState(87);
				match(COMMA);
				setState(88);
				predicate();
				setState(89);
				predicateList();
				}
				break;
			case PERIOD:
				enterOuterAlt(_localctx, 2);
				{
				setState(91);
				lambda();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DatalogParser.STRING, 0); }
		public TerminalNode ID() { return getToken(DatalogParser.ID, 0); }
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitParameter(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			_la = _input.LA(1);
			if ( !(_la==ID || _la==STRING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterListContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(DatalogParser.COMMA, 0); }
		public ParameterContext parameter() {
			return getRuleContext(ParameterContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitParameterList(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_parameterList);
		try {
			setState(101);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMA:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				match(COMMA);
				setState(97);
				parameter();
				setState(98);
				parameterList();
				}
				break;
			case RIGHT_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				lambda();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(DatalogParser.EQUAL, 0); }
		public TerminalNode NONEQUAL() { return getToken(DatalogParser.NONEQUAL, 0); }
		public TerminalNode GREATER() { return getToken(DatalogParser.GREATER, 0); }
		public TerminalNode LESS() { return getToken(DatalogParser.LESS, 0); }
		public TerminalNode GREATERSTRICT() { return getToken(DatalogParser.GREATERSTRICT, 0); }
		public TerminalNode LESSSTRICT() { return getToken(DatalogParser.LESSSTRICT, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitOperator(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << NONEQUAL) | (1L << GREATER) | (1L << GREATERSTRICT) | (1L << LESS) | (1L << LESSSTRICT))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryContext extends ParserRuleContext {
		public TerminalNode Q_MARK() { return getToken(DatalogParser.Q_MARK, 0); }
		public TerminalNode COLON_DASH() { return getToken(DatalogParser.COLON_DASH, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitQuery(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_query);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(Q_MARK);
			setState(106);
			match(COLON_DASH);
			setState(107);
			predicate();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryListContext extends ParserRuleContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public QueryListContext queryList() {
			return getRuleContext(QueryListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public QueryListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterQueryList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitQueryList(this);
		}
	}

	public final QueryListContext queryList() throws RecognitionException {
		QueryListContext _localctx = new QueryListContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_queryList);
		try {
			setState(113);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(109);
				query();
				setState(110);
				queryList();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(112);
				lambda();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringListContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(DatalogParser.COMMA, 0); }
		public TerminalNode STRING() { return getToken(DatalogParser.STRING, 0); }
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public StringListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterStringList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitStringList(this);
		}
	}

	public final StringListContext stringList() throws RecognitionException {
		StringListContext _localctx = new StringListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_stringList);
		try {
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMA:
				enterOuterAlt(_localctx, 1);
				{
				setState(115);
				match(COMMA);
				setState(116);
				match(STRING);
				setState(117);
				stringList();
				}
				break;
			case RIGHT_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				lambda();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LambdaContext extends ParserRuleContext {
		public LambdaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterLambda(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitLambda(this);
		}
	}

	public final LambdaContext lambda() throws RecognitionException {
		LambdaContext _localctx = new LambdaContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_lambda);
		try {
			enterOuterAlt(_localctx, 1);
			{
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\24~\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\5\3,\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\5\59\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\5\7E\n\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\nX\n"+
		"\n\3\13\3\13\3\13\3\13\3\13\5\13_\n\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\5\r"+
		"h\n\r\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\5\20t\n\20\3\21"+
		"\3\21\3\21\3\21\5\21z\n\21\3\22\3\22\3\22\2\2\23\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36 \"\2\4\3\2\21\22\3\2\n\17\2t\2$\3\2\2\2\4+\3\2\2\2"+
		"\6-\3\2\2\2\b8\3\2\2\2\n:\3\2\2\2\fD\3\2\2\2\16F\3\2\2\2\20L\3\2\2\2\22"+
		"W\3\2\2\2\24^\3\2\2\2\26`\3\2\2\2\30g\3\2\2\2\32i\3\2\2\2\34k\3\2\2\2"+
		"\36s\3\2\2\2 y\3\2\2\2\"{\3\2\2\2$%\5\f\7\2%&\5\b\5\2&\3\3\2\2\2\'(\7"+
		"\3\2\2()\7\21\2\2),\5\4\3\2*,\5\"\22\2+\'\3\2\2\2+*\3\2\2\2,\5\3\2\2\2"+
		"-.\7\20\2\2./\7\6\2\2/\60\7\22\2\2\60\61\5 \21\2\61\62\7\7\2\2\62\63\7"+
		"\4\2\2\63\7\3\2\2\2\64\65\5\6\4\2\65\66\5\b\5\2\669\3\2\2\2\679\5\"\22"+
		"\28\64\3\2\2\28\67\3\2\2\29\t\3\2\2\2:;\5\16\b\2;<\7\t\2\2<=\5\22\n\2"+
		"=>\5\24\13\2>?\7\4\2\2?\13\3\2\2\2@A\5\n\6\2AB\5\f\7\2BE\3\2\2\2CE\5\""+
		"\22\2D@\3\2\2\2DC\3\2\2\2E\r\3\2\2\2FG\7\20\2\2GH\7\6\2\2HI\7\21\2\2I"+
		"J\5\4\3\2JK\7\7\2\2K\17\3\2\2\2LM\5\26\f\2MN\5\32\16\2NO\5\26\f\2O\21"+
		"\3\2\2\2PX\5\20\t\2QR\7\20\2\2RS\7\6\2\2ST\5\26\f\2TU\5\30\r\2UV\7\7\2"+
		"\2VX\3\2\2\2WP\3\2\2\2WQ\3\2\2\2X\23\3\2\2\2YZ\7\3\2\2Z[\5\22\n\2[\\\5"+
		"\24\13\2\\_\3\2\2\2]_\5\"\22\2^Y\3\2\2\2^]\3\2\2\2_\25\3\2\2\2`a\t\2\2"+
		"\2a\27\3\2\2\2bc\7\3\2\2cd\5\26\f\2de\5\30\r\2eh\3\2\2\2fh\5\"\22\2gb"+
		"\3\2\2\2gf\3\2\2\2h\31\3\2\2\2ij\t\3\2\2j\33\3\2\2\2kl\7\5\2\2lm\7\t\2"+
		"\2mn\5\22\n\2n\35\3\2\2\2op\5\34\17\2pq\5\36\20\2qt\3\2\2\2rt\5\"\22\2"+
		"so\3\2\2\2sr\3\2\2\2t\37\3\2\2\2uv\7\3\2\2vw\7\22\2\2wz\5 \21\2xz\5\""+
		"\22\2yu\3\2\2\2yx\3\2\2\2z!\3\2\2\2{|\3\2\2\2|#\3\2\2\2\n+8DW^gsy";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}