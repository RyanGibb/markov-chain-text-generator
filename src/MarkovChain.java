import java.util.*;

public class MarkovChain<T> {
    private Set<State<T>> states = new HashSet<>();
    private Set<State<T>> startStates = new HashSet<>();
    private Set<State<T>> endStates = new HashSet<>();
    private Random random = new Random();

    // Checks if State with a given value exists, adds it to the set states if not, and returns the State
    private State<T> getStateWithValue(T value){
        State state = states.stream()
                .filter(s -> s.getValue().equals(value))
                // As State.equals overridden to compare values, there should be no more than 1 value, so finAny is fine
                .findAny()
                .orElse(null);
        if (state == null) {
            state = new State<>(value);
            states.add(state);
        }
        return state;
    }

    public void updateWithOccurrence(T stateValue, T nextStateValue) {
        //find State in states with value equal to stateValue
        State state = getStateWithValue(stateValue);
        State nextState = getStateWithValue(nextStateValue);
        state.addOccurrence(nextState);

    }

    public void addStartState(T stateValue){
        startStates.add(getStateWithValue(stateValue));
    }

    public void addEndState(T stateValue){
        endStates.add(getStateWithValue(stateValue));
    }

    public State getNextState(State<?> state) {
        Map<State, Integer> nextStateOccurrences = state.getNextStateProbabilities();
        if (nextStateOccurrences.size() == 0){
            return getRandomState();
        }
        int totalOccurrences = nextStateOccurrences.values().parallelStream().reduce((a, b) -> a + b).orElse(0);
        double randomInt = random.nextInt(totalOccurrences) + 1;
        int sum = 0;
        State nextState = null;
        for (Map.Entry<State, Integer> entry : nextStateOccurrences.entrySet()) {
            int occurrences = entry.getValue();
            nextState = entry.getKey();
            sum += occurrences;
            if (sum >= randomInt){
                break;
            }
        }
        return nextState;
    }

    private State getRandomState(Set<State<T>> statesSet) {
        List<State<T>> statesList = new ArrayList<>(statesSet);
        return statesList.get(random.nextInt(states.size()));
    }

    public State getRandomState(){
        return getRandomState(states);
    }

    public State getStartState(){
        return getRandomState(startStates);
    }

    public State getEndState(){
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
