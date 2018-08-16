import java.util.*;
import java.util.stream.Collectors;

public class MarkovChain<T extends Comparable<T>> {
    private Map<T, Map<T, Integer>> stateOccurrences = new HashMap<>();
    private Set<T> startStates = new HashSet<>();
    private Set<T> endStates = new HashSet<>();
    private Random random = new Random();

    public void updateWithOccurrence(T state, T nextState) {
        stateOccurrences.computeIfAbsent(state, k -> new HashMap<>()).merge(nextState, 1, (a, b) -> a + b);
    }

    public void addStartState(T state){
        stateOccurrences.computeIfAbsent(state, k -> new HashMap<>());
        startStates.add(state);
    }

    public void addEndState(T state){
        stateOccurrences.computeIfAbsent(state, k -> new HashMap<>());
        endStates.add(state);
    }

    public T getNextState(T state) {
        Map<T, Integer> nextStateOccurrences = stateOccurrences.get(state);
        if (nextStateOccurrences.size() == 0){
            return getRandomState();
        }
        //test w/ empty values
        int totalOccurrences = nextStateOccurrences.values().parallelStream().reduce((a, b) -> a + b).get();
        double randomInt = random.nextInt(totalOccurrences) + 1;
        int sum = 0;
        T nextState = null;
        for (Map.Entry<T, Integer> entry : nextStateOccurrences.entrySet()) {
            int occurrences = entry.getValue();
            nextState = entry.getKey();
            sum += occurrences;
            if (sum >= randomInt){
                break;
            }
        }
        return nextState;
    }

    public T getRandomState() {
        List<T> states = new ArrayList<>(stateOccurrences.keySet());
        return states.get(random.nextInt(states.size()));
    }

    public T getStartState(){
        List<T> states = new ArrayList<>(startStates);
        return states.get(random.nextInt(states.size()));
    }

    public T getEndState(){
        List<T> states = new ArrayList<>(endStates);
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
