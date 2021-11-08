package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            model.Pair<String, String> sourceAndRoute = new model.Pair<>(currentState, String.valueOf(character));
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
}
