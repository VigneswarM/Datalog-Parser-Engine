package comp6591.main;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
// import org.antlr.v4.runtime.Listener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;

import org.antlr.v4.gui.Trees;

import javax.print.PrintException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Run a lexer/parser combo, optionally printing tree string or generating
 *  postscript file. Optionally taking input file.
 *
 *  $ java org.antlr.v4.runtime.misc.DatalogMain GrammarName startRuleName
 *        [-tree]
 *        [-tokens] [-gui] [-ps file.ps]
 *        [-trace]
 *        [-diagnostics]
 *        [-SLL]
 *        [input-filename(s)]
 */
public class DatalogMainOriginal {
	public static final String LEXER_START_RULE_NAME = "tokens";

	protected String grammarName;
	protected String startRuleName;
	protected final List<String> inputFiles = new ArrayList<String>();
	protected boolean printTree = false;
	protected boolean gui = false;
	protected String psFile = null;
	protected boolean showTokens = false;
	protected boolean trace = false;
	protected boolean diagnostics = false;
	protected String encoding = null;
	protected boolean SLL = false;

	public DatalogMainOriginal(String[] args) throws Exception {
		if ( args.length < 2 ) {
			System.err.println("java org.antlr.v4.gui.DatalogMain GrammarName startRuleName\n" +
							   "  [-tokens] [-tree] [-gui] [-ps file.ps] [-encoding encodingname]\n" +
							   "  [-trace] [-diagnostics] [-SLL]\n"+
							   "  [input-filename(s)]");
			System.err.println("Use startRuleName='tokens' if GrammarName is a lexer grammar.");
			System.err.println("Omitting input-filename makes rig read from stdin.");
			return;
		}
		int i=0;
		grammarName = args[i];
		i++;
		startRuleName = args[i];
		i++;
		while ( i<args.length ) {
			String arg = args[i];
			i++;
			if ( arg.charAt(0)!='-' ) { // input file name
				inputFiles.add(arg);
				continue;
			}
			if ( arg.equals("-tree") ) {
				printTree = true;
			}
			if ( arg.equals("-gui") ) {
				gui = true;
			}
			if ( arg.equals("-tokens") ) {
				showTokens = true;
			}
			else if ( arg.equals("-trace") ) {
				trace = true;
			}
			else if ( arg.equals("-SLL") ) {
				SLL = true;
			}
			else if ( arg.equals("-diagnostics") ) {
				diagnostics = true;
			}
			else if ( arg.equals("-encoding") ) {
				if ( i>=args.length ) {
					System.err.println("missing encoding on -encoding");
					return;
				}
				encoding = args[i];
				i++;
			}
			else if ( arg.equals("-ps") ) {
				if ( i>=args.length ) {
					System.err.println("missing filename on -ps");
					return;
				}
				psFile = args[i];
				i++;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		DatalogMainOriginal DatalogMain = new DatalogMainOriginal(args);
 		if(args.length >= 2) {
			DatalogMain.process();
		}
	}

	public void process() throws Exception {
//		System.out.println("exec "+grammarName+"."+startRuleName);
		String lexerName = grammarName+"Lexer";
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Class<? extends Lexer> lexerClass = null;
		try {
			lexerClass = cl.loadClass(lexerName).asSubclass(Lexer.class);
		}
		catch (java.lang.ClassNotFoundException cnfe) {
			// might be pure lexer grammar; no Lexer suffix then
			lexerName = grammarName;
			try {
				lexerClass = cl.loadClass(lexerName).asSubclass(Lexer.class);
			}
			catch (ClassNotFoundException cnfe2) {
				// System.err.println("Cant load "+lexerName+" as lexer or parser");
				System.err.println(lexerName + " cannot be loaded as lexer/parser");
				return;
			}
		}

		Constructor<? extends Lexer> lexerCtor = lexerClass.getConstructor(CharStream.class);
		Lexer lexer = lexerCtor.newInstance((CharStream)null);

		// added by kamal to use custom syntax errors
		lexer.removeErrorListeners();
		lexer.addErrorListener(DatalogErrorListener.INSTANCE);

		Class<? extends Parser> parserClass = null;
		Parser parser = null;
		if ( !startRuleName.equals(LEXER_START_RULE_NAME) ) {
			String parserName = grammarName+"Parser";
			parserClass = cl.loadClass(parserName).asSubclass(Parser.class);
			Constructor<? extends Parser> parserCtor = parserClass.getConstructor(TokenStream.class);
			parser = parserCtor.newInstance((TokenStream)null);
		}

		// added by kamal to use extend base listener
//		DatalogListener parserListener = new DatalogCustomListener();
//		DatalogListener parserListener = new DatalogBaseListener();

		Charset charset = ( encoding == null ? Charset.defaultCharset () : Charset.forName(encoding) );
		if ( inputFiles.size()==0 ) {
			CharStream charStream = CharStreams.fromStream(System.in, charset);
			process(lexer, parserClass, parser, charStream);
			return;
		}
		for (String inputFile : inputFiles) {
	                CharStream charStream = CharStreams.fromPath(Paths.get(inputFile), charset);
			if ( inputFiles.size()>1 ) {
				System.err.println(inputFile);
			}
			process(lexer, parserClass, parser, charStream);
		}
	}

	protected void process(Lexer lexer, Class<? extends Parser> parserClass, Parser parser, CharStream input) throws IOException, IllegalAccessException, InvocationTargetException, PrintException {
			lexer.setInputStream(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			tokens.fill();

			if ( showTokens ) {
				for (Token tok : tokens.getTokens()) {
					if ( tok instanceof CommonToken ) {
						System.out.println(((CommonToken)tok).toString(lexer));
					}
					else {
						System.out.println(tok.toString());
					}
				}
			}

			if ( startRuleName.equals(LEXER_START_RULE_NAME) ) return;

			if ( diagnostics ) {
				parser.addErrorListener(new DiagnosticErrorListener());
				parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
			}

			if ( printTree || gui || psFile!=null ) {
				parser.setBuildParseTree(true);
			}

			if ( SLL ) { // overrides diagnostics
				parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
			}

			parser.setTokenStream(tokens);
			parser.setTrace(trace);

			try {
				Method startRule = parserClass.getMethod(startRuleName);
				ParserRuleContext tree = (ParserRuleContext)startRule.invoke(parser, (Object[])null);

				if ( printTree ) {
					System.out.println(tree.toStringTree(parser));
				}
				if ( gui ) {
					Trees.inspect(tree, parser);
				}
				if ( psFile!=null ) {
					Trees.save(tree, parser, psFile); // Generate postscript
				}
			}
			catch (NoSuchMethodException nsme) {
				System.err.println("No method for rule "+startRuleName+" or it has arguments");
			}
		}


		// custom function to test
		// private void printDatalog(Lexer lexer, Class<? extends Parser> parserClass, Parser parser, CharStream datalogSentence) {
		//     // Get our lexer
		//     // DatalogLexer lexer = new DatalogLexer(new ANTLRInputStream(datalogSentence));
		 
		//     // Get a list of matched tokens
		//     // CommonTokenStream tokens = new CommonTokenStream(lexer);
		 
		//     // Pass the tokens to the parser
		//     // DatalogParser parser = new DatalogParser(tokens);
		 
		//     // Specify our entry point
		//     DatalogSentenceContext DatalogSentenceContext = parser.datalogSentence();
		 
		//     // Walk it and attach our listener
		//     ParseTreeWalker walker = new ParseTreeWalker();
		//     DatalogListener listener = new DatalogListener();
		//     walker.walk(listener, datalogSentenceContext);
		// }
}