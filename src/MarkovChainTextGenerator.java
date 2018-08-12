import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

class MarkovChainTextGenerator {
    public static final String USAGE = "Usage: java MarkovChainTextGenerator <training text>\n" +
            "or: <input> | java MarkovChainTextGenerator";
    public static final int DEFAULT_WORDS_GENERATED = 100;

	private Map<String, Map<String, Integer>> markovChain = new HashMap<>();

	public static void main(String args[]) {
		Scanner scanner;
	    if (args.length == 0){
		    scanner = new Scanner(System.in);
		}
		else if (args.length == 1){
	        scanner = new Scanner(args[0]);
        }
        else{
            System.out.println(USAGE);
            return;
        }
		MarkovChainTextGenerator generator = new MarkovChainTextGenerator();
		generator.train(scanner);
		generator.printMarkovChain();
		System.out.println(generator.generate(DEFAULT_WORDS_GENERATED));
	}

    public void train(Scanner scanner) {
		String lastWord = null;
		int counter = 0;
		while (scanner.hasNext()) {
		    counter++;
		    String word = scanner.next();
			if (lastWord != null){
				markovChain.computeIfAbsent(lastWord, k -> new HashMap<>());
				markovChain.get(lastWord).merge(word, 1, (a, b) -> a + b);
			}
			lastWord = word;
		}
        System.out.println(counter + " words processed");
	}

	public String generate(int words){
        return "";

    }

	public void printMarkovChain() {
		System.out.println(
			markovChain.entrySet()
				.stream()
				.map(e -> e.getKey() + ":\n" + 
					e.getValue().entrySet()
						.stream()
						.map(e2 -> "\t" + e2.getKey() + " - " + e2.getValue())
						.collect(Collectors.joining("\n"))
				)
				.collect(Collectors.joining("\n"))
		);
	}

}
