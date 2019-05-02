package comp6591.main;

// http://www.vogella.com/tutorials/JavaPerformance/article.html
public class DatalogPerformance {
	
    private long startTime;

    // https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
    public static String readableBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
	public void showMemoryConsumption(){
		// Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory Usage: " + memory + " bytes (~"+readableBytes(memory, true)+")");
	}
	
	public void startTime() {
		startTime = System.currentTimeMillis();
	}
	
	public void showRunningTime() throws InstantiationException, IllegalAccessException {
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Running Time: " + elapsedTime + "ms");
	}

	public void showStats() throws InstantiationException, IllegalAccessException {
        System.out.println("\n>> Performance Evaluation Statistics:");
		showRunningTime();		
		showMemoryConsumption();
	}
}
