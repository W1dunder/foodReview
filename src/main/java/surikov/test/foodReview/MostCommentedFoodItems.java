package surikov.test.foodReview;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class MostCommentedFoodItems {

	public static void main(String[] args) {
		
		if (args.length == 0) {
			System.out.println("Input file name as command line parameter");
			return;
		}
		
		String fileName = args[0];
		
		Map<String, Integer> profiles = new HashMap<>();
		
		try (CSVParser parser = new CSVParser(new FileReader(fileName), CSVFormat.DEFAULT.withHeader())) {
			
			parser.forEach(r -> profiles.compute(r.get("ProductId"), (k, v) -> v == null ? 1 : v + 1));
			
			profiles.entrySet()
				.parallelStream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.limit(1000)
				.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
				.forEachOrdered((e) -> System.out.println(e.getKey()));
				
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " not found");
		} catch (IOException e) {
			System.err.println("Reading file error");
		}
	}
}
