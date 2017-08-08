package surikov.test.foodReview;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MostActiveUsers {
	
	public static int THREADS = 4;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		if (args.length == 0) {
			System.out.println("Input file name as command line parameter");
			return;
		}
		
		String fileName = args[0];
		
		Map<String, Integer> profiles = new HashMap<>();		
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			
			reader.readLine();
			
			ExecutorService service = Executors.newFixedThreadPool(THREADS);
			
			long start = System.nanoTime();
			
			List<Future<Map<String, Integer>>> futures = new ArrayList<>();
			for (int i = 0; i < 1; i++) {
				futures.add(service.submit(new Executor(reader)));
			}			
			
			while (futures.stream().anyMatch(f -> !f.isDone()));
			
			for (Future<Map<String, Integer>> future : futures) {
				
				profiles = mapsReducee(profiles, future.get());
			}
			
			long end = System.nanoTime();
			
			profiles.entrySet()
				.parallelStream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.limit(1000)
				.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
				.forEachOrdered((e) -> System.out.println(e.getKey() + " "+ e.getValue()));
			
			System.out.println(end - start);
				
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " not found");
		} catch (IOException e) {
			System.err.println("Reading file error");
		}
	}
	
	private static Map<String, Integer> mapsReducee(Map<String, Integer> m1, Map<String, Integer> m2) {
		
		m2.keySet().forEach(key -> m1.compute(key, (k, v) -> v == null ? m2.get(k) : v + m2.get(k)));
		
		return m1;
	}
}
