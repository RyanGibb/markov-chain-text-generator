import java.util.*;
import java.util.stream.Collectors;

public class MarkovChain<T extends Comparable<T>> {
    private Map<T, Map<T, Integer>> stateOccurrences = new HashMap<>();
    private Map<T, Map<Double, T>> stateProbabilities;
    private Random random = new Random();

    public void updateWithOccurrence(T state, T nextState) {
        stateOccurrences.computeIfAbsent(state, k -> new HashMap<>());
        stateOccurrences.get(state).merge(nextState, 1, (a, b) -> a + b);
    }

    public void calculateProbabilities() {
        stateProbabilities = new HashMap<>();
        for (Map.Entry<T, Map<T, Integer>> stateEntry : stateOccurrences.entrySet()) {
            T currentState = stateEntry.getKey();
            //test w/ empty values
            int totalOcurrences = stateEntry.getValue().values().parallelStream().reduce((a, b) -> a + b).get();
            Map<Double, T> probabilities =  new HashMap<>();
            for (Map.Entry<T, Integer> possibleNextStatesEntry : stateEntry.getValue().entrySet()){
                int occurences = possibleNextStatesEntry.getValue();
                double probability = (double) occurences / totalOcurrences;
                T possibleNextState = possibleNextStatesEntry.getKey();
                probabilities.put(probability, possibleNextState);
            }
            stateProbabilities.put(currentState, probabilities);
        }
    }

    public T getNextState(T state) {
        double randomDouble = random.nextDouble();
        Map<Double, T> probabilities = stateProbabilities.get(state);
        double sum = 0;
        T nextState = null;
        if (probabilities.size() == 0){
            return null;
        }
        for (Map.Entry<Double, T> entry : probabilities.entrySet()) {
            double probability = entry.getKey();
            nextState = entry.getValue();
            sum += probability;
            if (randomDouble > sum){
                break;
            }
        }
        return nextState;
    }

    public T getInitialState() {
        List<T> states = new ArrayList<>(stateProbabilities.keySet());
        return states.get(random.nextInt(states.size()));
    }

    public String toString() {
        return stateOccurrences.entrySet()
                .stream().sorted(new Comparator<Map.Entry<T, Map<T, Integer>>>() {
                    @Override
                    public int compare(Map.Entry<T, Map<T, Integer>> tMapEntry, Map.Entry<T, Map<T, Integer>> t1) {
                        return tMapEntry.<T>getKey().compareTo(t1.getKey());
                    }
                })
                .map(e -> e.getKey() + ":\n" +
                        e.getValue().entrySet()
                                .stream()
                                .sorted(new Comparator<Map.Entry<T, Integer>>() {
                                    @Override
                                    public int compare(Map.Entry<T, Integer> tIntegerEntry, Map.Entry<T, Integer> t1) {
                                        return tIntegerEntry.getKey().compareTo(t1.getKey());
                                    }
                                })
                                .map(e2 -> "\t" + e2.getKey() + " - " + e2.getValue())
                                .collect(Collectors.joining("\n"))
                )
                .collect(Collectors.joining("\n"));
    }

}
