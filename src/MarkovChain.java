import java.util.*;

public class MarkovChain<T> {
    private Set<State<T>> states = new HashSet<>();
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
        State state = getStateWithValue(stateValue);
        State nextState = getStateWithValue(nextStateValue);
        state.addOccurrence(nextState);
    }

    private State getRandomState(Set<State<T>> statesSet) {
        List<State<T>> statesList = new ArrayList<>(statesSet);
        return statesList.get(random.nextInt(states.size()));
    }

    public State getRandomState(){
        return getRandomState(states);
    }

    @Override
    public String toString() {
        return "MarkovChain{" +
                "states=" + states +
                '}';
    }
}
