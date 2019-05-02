package comp6591.main;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class DatalogErrorListener extends BaseErrorListener {
	public static final DatalogErrorListener INSTANCE = new DatalogErrorListener();
	private static int totalLexicalErrors = 0;

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int pos, String msg,
			RecognitionException e) throws ParseCancellationException {
		System.err.println("\nLine " + line + ":" + pos + " | " + msg);
		totalLexicalErrors++;
	}
	
	public int getLexicalErrorsCount() {
		return totalLexicalErrors;
	}
}