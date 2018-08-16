import java.util.*;

public class MarkovChain<T> {
    private Set<State<T>> states = new HashSet<>();
    private Set<State<T>> startStates = new HashSet<>();
    private Set<State<T>> endStates = new HashSet<>();
    private Random random = new Random();

    public getStateFromSetWithValue(Set<State<T>> statesSet, T value){
        statesSet.stream()
                .map(State::getValue)
                .f
    }

    public void updateWithOccurrence(T stateValue, T nextStateValue) {
        stateOccurrences.computeIfAbsent(state, k -> new HashMap<>()).merge(nextState, 1, (a, b) -> a + b);
        //find State in states with value equal to stateValue
        //add
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

    private T getRandomState(Set<State<T>> statesSet) {
        List<State<T>> statesList = new ArrayList<>(statesSet);
        return statesList.get(random.nextInt(states.size())).getValue();
    }

    public T getRandomState(){
        return getRandomState(states);
    }

    public T getStartState(){
        return getRandomState(startStates);
    }

    public T getEndState(){
        return getRandomState(endStates);
    }

    @Override
    public String toString() {
        return "MarkovChain{" +
                "states=" + states +
                ", startStates=" + startStates +
                ", endStates=" + endStates +
                '}';
    }
}
