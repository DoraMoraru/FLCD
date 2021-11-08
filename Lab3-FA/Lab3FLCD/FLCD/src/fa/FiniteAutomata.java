package fa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class FiniteAutomata {
    private Set<String> states;
    private Set<String> alphabet;
//    private Map<String, Map<String, String>> transitions;
    private Map<Pair<String, String>, List<String>> transitions;
    private String initialState;
    private Set<String> finalStates;

    public FiniteAutomata() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.transitions = new HashMap<>();
        this.finalStates = new HashSet<>();
    }

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

//    public Map<String, Map<String, String>> getTransitions() {
//        return transitions;
//    }
//
//    public void setTransitions(Map<String, Map<String, String>> transitions) {
//        this.transitions = transitions;
//    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<String> finalStates) {
        this.finalStates = finalStates;
    }

    public void addTransition(String startState, String value, String endState){
//        if(!transitions.containsKey(startState)){
//            transitions.put(startState, new HashMap<>());
//        }
//        transitions.get(startState).put(value, endState);
        if(!transitions.containsKey(new Pair<>(startState, value))){
            transitions.put(new Pair<>(startState, value), new ArrayList<>());
        }
        transitions.get(new Pair<>(startState, value)).add(endState);
    }

    public void readFiniteAutomata(String filename) throws FileNotFoundException {
        try(Scanner scanner = new Scanner(new FileInputStream(filename))){
            //read the states
            int numberOfStates = scanner.nextInt();
            for(int i = 0; i < numberOfStates; i++){
                states.add(scanner.next());
            }

            //read the alphabet
            int numberOfValues = scanner.nextInt();
            for(int i = 0; i < numberOfValues; i++){
                alphabet.add(scanner.next());
            }

            //read the transitions
            int numberOfTransitions = scanner.nextInt();
            for(int i = 0; i < numberOfTransitions; i++){
                addTransition(scanner.next(), scanner.next(), scanner.next());
            }

            //read the inital state
            initialState = scanner.next();

            //read the final states
            int numberOfFinalStates = scanner.nextInt();
            for(int i = 0 ; i < numberOfFinalStates; i++){
                finalStates.add(scanner.next());
            }
        }
    }

    // TODO add to check sequence
    public boolean checkSequence(String sequence) {
        String currentState = initialState;

        for(char character : sequence.toCharArray()) {
            Pair<String, String> sourceAndRoute = new Pair<>(currentState, String.valueOf(character));
            if (!this.transitions.containsKey(sourceAndRoute)) {
                return false;
            }
            // Get first element because we have a DFA, so it will only contain one value (?)
            currentState = this.transitions.get(sourceAndRoute).get(0);
        }

        return this.finalStates.contains(currentState);
    }

    public Map<Pair<String, String>, List<String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<Pair<String, String>, List<String>> transitions) {
        this.transitions = transitions;
    }

    public void writeFiniteAutomata(String filename) throws FileNotFoundException {
        try (PrintStream out = new PrintStream(new FileOutputStream(filename))){
            //write the states
            out.print("" + states.size());
            for (String state: states) {
                out.print(" " + state);
            }
            out.println();

            //write the alphabet
            out.print("" + alphabet.size());
            for (String ch: alphabet) {
                out.print(" " + ch);
            }
            out.println();

            //write the transitions
            //compute total number of transitions
            int numberOfTransitions = 0;
            for(Map.Entry<Pair<String, String>, List<String>> entry: transitions.entrySet()){
                numberOfTransitions += entry.getValue().size();
            }
            out.println("" + numberOfTransitions);
            for(Map.Entry<Pair<String, String>, List<String>> entry: transitions.entrySet()){
                for (String state: entry.getValue()) {
                    out.println(entry.getKey().first + " " + entry.getKey().second + " " + state);
                }
            }

            //write the initial state
            out.println(initialState);

            //write the final states
            out.print("" + finalStates.size());
            for(String state: finalStates){
                out.print(" " + state);
            }
            out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
