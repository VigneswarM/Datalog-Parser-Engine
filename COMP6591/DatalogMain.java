package comp6591.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import comp6591.antlr.DatalogLexer;
import comp6591.antlr.DatalogParser;
import comp6591.engine.DatalogEngine;
import comp6591.query.DatalogQuery;
import comp6591.utils.PrintColor;
import comp6591.utils.StringUtils;

public class DatalogMain {
	private final static String inputFile = "P.cdl";
	private final static String errorFile = "P.err";
	private static String CURRENT_FOLDER;
	private static HashMap<String, Object> userConfig;
	private DatalogCustomListener classlistener;
	private Scanner scanner;
	private List<String> evalMethods = Arrays.asList("Naive", "Semi-Naive");
	private static DatalogPerformance perf;	
	HashMap<Integer, List<DatalogPredicate>> tuples;

	public DatalogMain() {
		CURRENT_FOLDER = System.getProperty("user.dir") + File.separator + "src" + File.separator + "comp6591/main"
				+ File.separator;
		// TODO: should be user input/config file
		userConfig = new HashMap<String, Object>();
		userConfig.put("printToFile", true);
		userConfig.put("showTree", false);
		userConfig.put("currentFolder", CURRENT_FOLDER);
		userConfig.put("evalMethod", 1);
		userConfig.put("debug", false);
		scanner = new Scanner(System.in);
		perf = new DatalogPerformance();
	}
	
	public void setEvaluationMethod() {
		Scanner scanner = new Scanner(System.in);
		// choose evaluation method
		int evalMethod = 1;
		String evalMethodChoiceMsg = "\n» Select an Evaluation Method (1: Naive, 2: Semi-Naive)";
		System.out.println(evalMethodChoiceMsg);
		while (true) {
			evalMethod = scanner.nextInt();
			if (evalMethod < 1 || evalMethod > 2)
				System.out.println(evalMethodChoiceMsg);
			else
				break;
		}
		userConfig.put("evalMethod", evalMethod);
	}

	public CommonTokenStream produceTokens() throws IOException {

		// lexer
		CharStream inputStream = getInputFile();
		DatalogLexer lexer = new DatalogLexer(inputStream);
		// output errors to file instead of console -//new CommonTokenStream(lexer)
		// https://stackoverflow.com/questions/18132078/handling-errors-in-antlr4
		System.out.print("\n» Checking Syntax Errors... ");
		lexer.removeErrorListeners();
		lexer.addErrorListener(DatalogErrorListener.INSTANCE);
		return new CommonTokenStream(lexer);
	}

	public void parse() throws IOException, InstantiationException, IllegalAccessException {

		// parser
		CommonTokenStream commonTokenStream = produceTokens();
		DatalogParser parser = new DatalogParser(commonTokenStream);
		classlistener = new DatalogCustomListener(userConfig);
		try {

			// enter tree from root node datalogProgram
			ParserRuleContext tree = parser.datalogProgram();

			// check if lexical errors
			if (DatalogErrorListener.INSTANCE.getLexicalErrorsCount() > 0)
				return;

			System.out.println("No issues " + PrintColor.CHECK_MARK);
			perf.startTime("Parser");
			ParseTreeWalker.DEFAULT.walk(classlistener, tree);
			perf.stopTime("Parser");

			tuples = classlistener.getTuples();

			// print parse tree
			if ((boolean) userConfig.get("showTree")) {
				Trees.inspect(tree, parser);
			}

		} catch (ParseCancellationException e) { // lexer error, output to P.err
			String errors = e.getMessage();
			System.err.println(errors);
			Files.write(Paths.get(CURRENT_FOLDER + errorFile), errors.getBytes());
		}
	}

	public void processQuery() throws Exception {
		// ask for user input for the query
		String queryMsg = "\n» Please Enter a Query:";
		System.out.println(queryMsg);

		// TEST: to Remove
		// String query = "?-e('a','b','d'),p('b', 'c','x')."; //invalid
		// String query = "?-e('a','b','d'),z('b', 'c','x')."; //invalid
		// String query = "?-e('a','b'),p('b', 'c'),p('c','_')."; // valid
		 // String query = "?-edge(a,X)."; // valid
		
		while (true) {
			try {
				String query = scanner.nextLine();
				DatalogQuery dq = new DatalogQuery(query);
				dq.setTuples(tuples);
				perf.startTime("Query");
				dq.execute();
				perf.stopTime("Query");
				break;
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}		

		scanner.close();
	}

	public void evaluate() throws InstantiationException, IllegalAccessException {
		
		setEvaluationMethod();

		// USE array to store evaluation method string
		int evalMethod = (int) userConfig.get("evalMethod");
		System.out.println("\n» Evaluating using Engine with " + evalMethods.get(evalMethod-1) + " method...");

		// evaluate facts using engine and method
		DatalogEngine engine = new DatalogEngine(tuples, evalMethod);
		perf.startTime("Engine");
		engine.evaluate();
		perf.stopTime("Engine");
		tuples = engine.getTuples();

		// show tuples
		tuples.entrySet().forEach(t -> t.getValue().forEach(p -> p.output()));
		System.out.println("Total number of (IDB + EDB) facts:" + tuples.entrySet().size());
	}

	private static CharStream getInputFile() throws IOException { // read file, create lexer and parser
		String FIlENAME = CURRENT_FOLDER + inputFile;
		StringUtils.showCodeListing(FIlENAME);
		String inputFileText = new String(Files.readAllBytes(Paths.get(CURRENT_FOLDER + inputFile)));
		String datalogCode = StringUtils.addSingleQuotesFix(inputFileText);
//		System.out.println("QUOTED PROGRAM\n" + datalogCode);
		return CharStreams.fromString(datalogCode);
	}

	public static void main(String args[]) throws Exception {
		DatalogMain datalog = new DatalogMain();
		datalog.parse();
		datalog.evaluate();
		datalog.processQuery();
		perf.showStats();
	}
}
