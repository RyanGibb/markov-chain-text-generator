import java.util.Scanner;

class TextGenerator {
    public static final String USAGE = "Usage: java TextGenerator <training text>\n" +
            "or: <input> | java TextGenerator";
    public static final int DEFAULT_WORDS_GENERATED = 100;

    public MarkovChain<String> markovChain = new MarkovChain<>();

    public static void main(String args[]) {
        Scanner scanner;
        if (args.length == 0) {
            scanner = new Scanner(System.in);
        } else if (args.length == 1) {
            scanner = new Scanner(args[0]);
        } else {
            System.out.println(USAGE);
            return;
        }
        TextGenerator generator = new TextGenerator();
        generator.train(scanner);
        System.out.println(generator.markovChain);
        System.out.println(generator.generate(DEFAULT_WORDS_GENERATED));
    }

    public void train(Scanner scanner) {
        String lastWord = null;
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (lastWord != null) {
                markovChain.updateWithOccurrence(lastWord, word);
            }
            lastWord = word;
        }
    }

    public String generate(int words) {
        StringBuilder sb = new StringBuilder();
        String word = markovChain.getInitialState();
        for (int i = 0; i < words; i++){
            sb.append(word).append(" ");
            word = markovChain.getState();
        }
        return sb.toString();
    }

}
