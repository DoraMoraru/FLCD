package fa;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class TestFAGenerators {
    //TODO test class for FAs
    public static void main(String[] args) {
        (new TestFAGenerators()).runTests();
    }

    public void runTests() {
        testIdentifierFA();
        testConstantFA();
        testIntConstantFA();
    }

    public void testIdentifierFA() {
//        FiniteAutomata identifierFA = FAGenerators.generateIdentifierFA();
        FiniteAutomata identifierFA = new FiniteAutomata();
        try {
            identifierFA.readFiniteAutomata("FLCD/data/identifierFA.txt");
        } catch (FileNotFoundException e) {
            assert false;
        }

        assert identifierFA.checkSequence("aaa");
        assert identifierFA.checkSequence("ab");
        assert identifierFA.checkSequence("x");
        assert identifierFA.checkSequence("ab2");
        assert identifierFA.checkSequence("ab234");
        assert identifierFA.checkSequence("ab234aa");
        assert !identifierFA.checkSequence("1");
        assert !identifierFA.checkSequence("1aa");
        assert !identifierFA.checkSequence("0aaa");
        assert !identifierFA.checkSequence("#");
        assert !identifierFA.checkSequence("aa#");
    }

    public void testConstantFA() {
        FiniteAutomata constantFA = FAGenerators.generateConstantFA(Arrays.asList(" _{}[];.,()|?!".split("")));

        assert constantFA.checkSequence("23");
        assert constantFA.checkSequence("0");
        assert constantFA.checkSequence("-0");
        assert constantFA.checkSequence("-1");
        assert constantFA.checkSequence("-10");
        assert constantFA.checkSequence("+0");
        assert constantFA.checkSequence("+123");
        assert constantFA.checkSequence("+1230");
        assert !constantFA.checkSequence("+00");
        assert !constantFA.checkSequence("00");
        assert !constantFA.checkSequence("a");
        assert !constantFA.checkSequence("0a");
        assert !constantFA.checkSequence("+0a");
        assert !constantFA.checkSequence("'as{'");
        assert constantFA.checkSequence("\"as{\"");
        assert !constantFA.checkSequence("\"as{");
        assert !constantFA.checkSequence("'as{");
        assert !constantFA.checkSequence("as{");
        assert !constantFA.checkSequence("as{'");
        assert !constantFA.checkSequence("as{\"");
    }


    public void testIntConstantFA() {
        FiniteAutomata constantFA = new FiniteAutomata();
        try {
            constantFA.readFiniteAutomata("FLCD/data/intConstantFA.txt");
        } catch (FileNotFoundException e) {
            assert false;
        }

        assert constantFA.checkSequence("23");
        assert constantFA.checkSequence("0");
        assert constantFA.checkSequence("-0");
        assert constantFA.checkSequence("-1");
        assert constantFA.checkSequence("-10");
        assert constantFA.checkSequence("+0");
        assert constantFA.checkSequence("+123");
        assert constantFA.checkSequence("+1230");
        assert !constantFA.checkSequence("+00");
        assert !constantFA.checkSequence("00");
        assert !constantFA.checkSequence("a");
        assert !constantFA.checkSequence("0a");
        assert !constantFA.checkSequence("+0a");
    }
}
