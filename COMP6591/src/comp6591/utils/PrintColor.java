package comp6591.utils;

/* Taken from http://cesarloachamin.github.io/2015/03/31/System-out-print-colors-and-unicode-characters/*/
public enum PrintColor {

	  BLACK("\u001B[30m"),
	  RED("\u001B[31m"),
	  GREEN("\u001B[32m"),
	  YELLOW("\u001B[33m"),
	  BLUE("\u001B[34m"),
	  PURPLE("\u001B[35m"),
	  CYAN("\u001B[36m"),
	  WHITE("\u001B[37m");

	  private String ansiColor;
	  public static final String CHECK_MARK = "\u2713";
	  public static final String ERROR_MARK = "\u2717";

	  PrintColor(String ansiColor) {
	    this.ansiColor = ansiColor;
	  }

	  public String getAnsiColor() {
	    return ansiColor;
	  }
	}