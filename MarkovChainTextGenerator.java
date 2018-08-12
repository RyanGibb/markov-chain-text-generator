
class MarkovChainTextGenerator {
	Map<String, Map<String, Integer>> markovChain = new HashMap<>();

	public static void main(Stringa args[]) {
		if (args.length != 1){
			System.out.println("Usage: java MarkovChainTextGenerator <training text>");
			return;
		}
		train(args[0]);
	}

	public void train(String text) {
		String[] words = text.split(" ");
		String lastWord;
		for (String word : words) {
			if (lastWord != null){
				if (markovChain.get(lastWord) == null){
					markovChain.add(lastWord, new HashMap<String, Integer>());
				}
				markovChain.get(lastWord).merge(word, 1, (a, b) -> a + b);
			}
		}
	}

}
