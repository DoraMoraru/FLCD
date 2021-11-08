import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        SymbolTable symbolTable = new SymbolTable();
//        var a = symbolTable.insert("ab");
//        symbolTable.insert("ba");
//        System.out.println(symbolTable);
//        System.out.println(symbolTable.insert("ab"));
//        System.out.println(symbolTable.insert("ba"));
        String fileName = "data/p3.txt";
        String outFileName = "data/pif.txt";
        String outFileNameSymbol = "data/symbol.txt";
        String constantFAFileName = "data/intConstantFA.txt";
        String identifierFAFileName = "data/identifierFA.txt";
        try {
            // TODO moved for exception handling
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(constantFAFileName, identifierFAFileName);
            lexicalAnalyzer.readFromFile(fileName);
            lexicalAnalyzer.analyze();
            lexicalAnalyzer.writePifToFile(outFileName);
            lexicalAnalyzer.writeSymbolTableToFile(outFileNameSymbol);
        } catch (LexicalException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
