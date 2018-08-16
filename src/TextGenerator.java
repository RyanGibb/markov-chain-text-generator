import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class TextGenerator {
    public static final String USAGE = "Usage: java TextGenerator <training text>\n" +
            "or: <input> | java TextGenerator";
    public static final int DEFAULT_WORDS_GENERATED = 500;
    public static final String MARKOV_CHAIN_FILE = "markov-chain.txt";

    public MarkovChain<String> markovChain = new MarkovChain<>();

    public static void main(String args[]) throws IOException {
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(MARKOV_CHAIN_FILE));
        writer.write(generator.markovChain.toString());
        System.out.println(generator.generate(DEFAULT_WORDS_GENERATED));
    }

    public void train(Scanner scanner) {
        String lastWord = null;
        String word = null;
        while (scanner.hasNext()) {
            word = scanner.next();
            //Add starting word.
            if (lastWord != null) {
                markovChain.updateWithOccurrence(lastWord, word);
            }
            else{
                markovChain.addStartState(word);
            }
            lastWord = word;
        }
        if (word != null) {
            markovChain.addEndState(word);
        }
    }

    public String generate(int words) {
        StringBuilder sb = new StringBuilder();
        String word = markovChain.getRandomState();
        for (int i = 0; i < words ; i++){
            sb.append(word).append(" ");
            word = markovChain.getNextState(word);
            if (word == null){
                sb.append(". ");
                word = markovChain.getRandomState();
            }
        }
        return sb.toString();
    }

}
