package surikov.test.foodReview;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Translating {
	
	private static String PREFIX = "{input_lang: 'en', output_lang: 'fr', text: \"";
	private static String POSTFIX = "\"}";

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		if (args.length == 0) {
			System.out.println("Input file name as command line parameter");
			return;
		}
		
		String fileName = args[0];
		
		try (CSVParser parser = new CSVParser(new FileReader(fileName), CSVFormat.DEFAULT.withHeader())) {
			
			ExecutorService service = Executors.newFixedThreadPool(100);
			
			StringBuilder sb = new StringBuilder(PREFIX);
			
			LinkedList<Future<String>> futures = new LinkedList<>();
			
			for (CSVRecord r : parser) {
				String text = r.get("Text");
				
				int charactersLeft = 1000 - (sb.length() + text.length() + POSTFIX.length());
				if (charactersLeft <= 0) {
					
					if (charactersLeft != 0) {
						sb.append(text.substring(0, charactersLeft));
						
						text = text.substring(charactersLeft);
					}
					
					futures.add(service.submit(new Sender(sb.append(POSTFIX).toString())));
					
					sb = new StringBuilder(PREFIX);
				}
				
				sb.append(text);
			}
			
			service.submit(new Sender(sb.append(POSTFIX).toString()));
			
			
			while(!futures.isEmpty()) {
				while(!futures.getFirst().isDone());
				
				System.out.println(futures.getFirst().get()); 
				
				futures.removeFirst();
			}
				
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " not found");
		} catch (IOException e) {
			System.err.println("Reading file error");
		}
	}
		
	private static String sendPost(String text) throws Exception {
		return "Salut Jean, comment vas tu?";
	}
	
	private static class Sender implements Callable<String> {
		private String text; 
		
		public Sender(String text) {
			this.text = text;
		}

		public String call() throws Exception {
			return sendPost(text);
		}
	} 
}
