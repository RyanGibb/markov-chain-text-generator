import java.util.HashMap;
import java.util.Map;

public class State<T> {
    private T value;
    private Map<State, Integer> nextStateProbabilities = new HashMap<>();

    public State(T value) {
        this.value = value;
    }

    public void addOccurrence(State nextState){
        nextStateProbabilities.merge(nextState, 1, (a, b) -> a + b);
    }

    public T getValue() {
        return value;
    }

    public Map<State, Integer> getNextStateProbabilities() {
        return nextStateProbabilities;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof State){
            State that = (State) o;
            return this.value.equals(that.value);
        }
        return super.equals(o);
    }

}
