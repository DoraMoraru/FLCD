package fa;

import java.util.*;

public class FAGenerators {
    //TODO class for preparing the two FAs
    public static FiniteAutomata generateIdentifierFA(){
        FiniteAutomata fa = new FiniteAutomata();

        fa.setStates(new HashSet<>(Arrays.asList("S", "F")));

        Set<String> alphabet = new HashSet<>();
        Map<Pair<String, String>, List<String>> transitions = new HashMap<>();

        fa.setInitialState("S");
        fa.setFinalStates(new HashSet<>(Arrays.asList("F")));

        // Prepare letters
        List<String> letters = new ArrayList<>();
        FAGenerators.populateListWithLetters(letters);

        // Add letters
        for (String strCh: letters) {
            alphabet.add(strCh);
            transitions.put(new Pair<>("S", strCh), Arrays.asList("F"));
            transitions.put(new Pair<>("F", strCh), Arrays.asList("F"));
        }

        // Prepare and add digits
        for (char ch = '0'; ch <= '9'; ch++) {
            String strCh = String.valueOf(ch);
            alphabet.add(strCh);
            transitions.put(new Pair<>("F", strCh), Arrays.asList("F"));
        }

        fa.setAlphabet(alphabet);
        fa.setTransitions(transitions);

        return fa;
    }

    public static FiniteAutomata generateConstantFA(List<String> specialCharacters) {
        FiniteAutomata fa = new FiniteAutomata();

        fa.setStates(new HashSet<>(Arrays.asList("S", "NS", "NB", "NZ", "CS", "CS2", "CF", "CD", "DF")));

        Set<String> alphabet = new HashSet<>();
        Map<Pair<String, String>, List<String>> transitions = new HashMap<>();

        fa.setInitialState("S");
        fa.setFinalStates(new HashSet<>(Arrays.asList("NB", "NZ", "CF", "DF")));

        // Numbers that start with a + / -
        transitions.put(new Pair<>("S", "+"), Arrays.asList("NS"));
        transitions.put(new Pair<>("S", "-"), Arrays.asList("NS"));

        // Zero numbers
        transitions.put(new Pair<>("S", "0"), Arrays.asList("NZ"));
        transitions.put(new Pair<>("NS", "0"), Arrays.asList("NZ"));

        // Prepare digits (first non-zero)
        List<String> digits = new ArrayList<>();
        for (char ch = '1'; ch <= '9'; ch++) {
            String strCh = String.valueOf(ch);
            digits.add(strCh);
        }

        // Non-zero numbers
        for (String digit: digits) {
            transitions.put(new Pair<>("S", digit), Arrays.asList("NB"));
            transitions.put(new Pair<>("NS", digit), Arrays.asList("NB"));
        }

        // Remaining digits of a number
        digits.add("0");
        for (String digit: digits) {
            transitions.put(new Pair<>("NB", digit), Arrays.asList("NB"));
        }

        // Prepare alphabet for string constants
        List<String> strConstAlphabet = new ArrayList<>();
        strConstAlphabet.addAll(digits);
        strConstAlphabet.addAll(specialCharacters);
        FAGenerators.populateListWithLetters(strConstAlphabet);

        // Single quotation string constants
        transitions.put(new Pair<>("S", "'"), Arrays.asList("CS"));

        for (String strCh: strConstAlphabet) {
            transitions.put(new Pair<>("CS", strCh), Arrays.asList("CS2"));
        }

        transitions.put(new Pair<>("CS2", "'"), Arrays.asList("CF"));

        // Double quotation string constants
        transitions.put(new Pair<>("S", "\""), Arrays.asList("CD"));

        for (String strCh: strConstAlphabet) {
            transitions.put(new Pair<>("CD", strCh), Arrays.asList("CD"));
        }

        transitions.put(new Pair<>("CD", "\""), Arrays.asList("DF"));

        alphabet.add("+");
        alphabet.add("-");
        alphabet.add("'");
        alphabet.add("\"");
        alphabet.addAll(strConstAlphabet);

        fa.setAlphabet(alphabet);
        fa.setTransitions(transitions);

        return fa;
    }

    public static FiniteAutomata generateIntConstantFA() {
        FiniteAutomata fa = new FiniteAutomata();

        fa.setStates(new HashSet<>(Arrays.asList("S", "NS", "NB", "NZ")));

        Set<String> alphabet = new HashSet<>();
        Map<Pair<String, String>, List<String>> transitions = new HashMap<>();

        fa.setInitialState("S");
        fa.setFinalStates(new HashSet<>(Arrays.asList("NB", "NZ")));

        // Numbers that start with a + / -
        transitions.put(new Pair<>("S", "+"), Arrays.asList("NS"));
        transitions.put(new Pair<>("S", "-"), Arrays.asList("NS"));

        // Zero numbers
        transitions.put(new Pair<>("S", "0"), Arrays.asList("NZ"));
        //transitions.put(new Pair<>("NS", "0"), Arrays.asList("NZ"));

        // Prepare digits (first non-zero)
        List<String> digits = new ArrayList<>();
        for (char ch = '1'; ch <= '9'; ch++) {
            String strCh = String.valueOf(ch);
            digits.add(strCh);
        }

        // Non-zero numbers
        for (String digit: digits) {
            transitions.put(new Pair<>("S", digit), Arrays.asList("NB"));
            transitions.put(new Pair<>("NS", digit), Arrays.asList("NB"));
        }

        // Remaining digits of a number
        digits.add("0");
        for (String digit: digits) {
            transitions.put(new Pair<>("NB", digit), Arrays.asList("NB"));
        }

        alphabet.add("+");
        alphabet.add("-");
        alphabet.addAll(digits);

        fa.setAlphabet(alphabet);
        fa.setTransitions(transitions);

        return fa;
    }

    private static void populateListWithLetters(List<String> characterList) {
        for (char ch = 'a'; ch <= 'z'; ch++) {
            String strCh = String.valueOf(ch);
            characterList.add(strCh);
            char capCh = (char)(ch - 'a' + 'A');
            strCh = String.valueOf(capCh);
            characterList.add(strCh);
        }
    }
}
