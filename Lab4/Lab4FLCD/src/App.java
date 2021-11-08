import model.FiniteAutomata;
import model.Pair;

import java.util.Scanner;
import java.util.Set;

public class App {
    private FiniteAutomata finiteAutomata;
    private Scanner in;

    public App() {
        this.in = new Scanner(System.in);
        this.finiteAutomata = new FiniteAutomata();
    }

    public void run(){
        int option = 1;
        while(option != 0){
            showMenu();
            option = in.nextInt();
            switch (option) {
                case 1 : {loadFiniteAutomataFromFile();break;}
                case 2 : {showStates();break;}
                case 3 : {showAlphabet();break;}
                case 4 : {showTransitions();break;}
                case 5 : {showFinalStates();break;}
                case 6 : {checkSequence();break;}
            }
        }
    }

    private void checkSequence() {
        String sequence = in.next();
        if(finiteAutomata.checkSequence(sequence)){
            System.out.println("The sequence is accepted");
        } else{
            System.out.println("The sequence is NOT accepted");
        }
    }

    private void showFinalStates() {
        System.out.println("The set of final states: " + finiteAutomata.getFinalStates());
    }

    private void showTransitions() {
        System.out.println("The transitions: ");
//        for(var state: finiteAutomata.getTransitions().keySet()){
//            for(var value : finiteAutomata.getTransitions().get(state).keySet()){
//                System.out.println("delta(" + state + "," + value + ")=" + finiteAutomata.getTransitions().get(state).get(value));
//            }
//        }
        for(Pair<String, String> sourceAndRout: finiteAutomata.getTransitions().keySet()){
            for(String end : finiteAutomata.getTransitions().get(sourceAndRout)){
                System.out.println("delta(" + sourceAndRout.first + "," + sourceAndRout.second + ")=" + end);
            }
        }
    }

    private void showAlphabet() {
        System.out.println("The alphabet: " + finiteAutomata.getAlphabet());
    }

    private void showStates() {
        System.out.println("The set of states: "+ finiteAutomata.getStates());
    }

    private void showMenu(){
        System.out.println("0. Exit");
        System.out.println("1. Load finite automata from file");
        System.out.println("2. Show finite automata elements: the set of states");
        System.out.println("3. Show finite automata elements: the alphabet");
        System.out.println("4. Show finite automata elements: the transitions");
        System.out.println("5. Show finite automata elements: the set of final states");
        System.out.println("6. Check the sequence");
    }

    private void loadFiniteAutomataFromFile(){
        System.out.println("Give file name: ");
        String filename = in.next();
        try{
            finiteAutomata = new FiniteAutomata();
            finiteAutomata.readFiniteAutomata(filename);
        }catch (Exception exception){
            System.out.println("Error: " + exception.getMessage());
        }
    }
}
