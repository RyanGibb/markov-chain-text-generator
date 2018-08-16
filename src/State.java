import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class State<T> {
    private T value;
    private Map<State, Integer> nextStateOccurrences = new HashMap<>();
    private boolean startState = false;
    private boolean endState = false;

    public State(T value) {
        this.value = value;
    }

    public void addOccurrence(State nextState){
        nextStateOccurrences.merge(nextState, 1, (a, b) -> a + b);
    }

    public T getValue() {
        return value;
    }

    public State getNextState(Random random) {
        if (nextStateOccurrences.size() == 0){
            return null;
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof State){
            State that = (State) o;
            return this.value.equals(that.value);
        }
        return super.equals(o);
    }

    public boolean isStartState() {
        return startState;
    }

    public void setStartState(boolean startState) {
        this.startState = startState;
    }

    public boolean isEndState() {
        return endState;
    }

    public void setEndState(boolean endState) {
        this.endState = endState;
    }

    @Override
    public String toString() {
        return "State{" +
                "value=" + value +
                ", nextStateOccurrences=" + nextStateOccurrences +
                ", startState=" + startState +
                ", endState=" + endState +
                '}';
    }
}
