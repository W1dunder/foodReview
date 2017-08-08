package surikov.test.foodReview;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Executor implements Callable<Map<String, Integer>> {
	
	private BufferedReader reader;
	private Map<String, Integer> values = new HashMap<>();

	public Executor(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public Map<String, Integer> call() throws Exception {
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			
			values.compute(line.split(",")[3], (k, v) -> v == null ? 1 : v + 1);
		}
		
		return values;
	}
}
