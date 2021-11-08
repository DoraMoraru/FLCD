import fa.FAGenerators;
import fa.FiniteAutomata;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class StoreFAs {
    public static void main(String[] args) {
        (new StoreFAs()).run();
    }

    public void run() {
        FiniteAutomata identifierFA = FAGenerators.generateIdentifierFA();
        FiniteAutomata intConstantsFA = FAGenerators.generateIntConstantFA();
        try {
            identifierFA.writeFiniteAutomata("FLCD/data/identifierFA.txt");
            intConstantsFA.writeFiniteAutomata("FLCD/data/intConstantFA.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
