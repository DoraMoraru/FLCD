import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        SymbolTable symbolTable = new SymbolTable();
//        var a = symbolTable.insert("ab");
//        symbolTable.insert("ba");
//        System.out.println(symbolTable);
//        System.out.println(symbolTable.insert("ab"));
//        System.out.println(symbolTable.insert("ba"));
        String fileName = "data/pErr.txt";
        String outFileName = "data/pif.txt";
        String outFileNameSymbol = "data/symbol.txt";
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        try {
            lexicalAnalyzer.readFromFile(fileName);
            lexicalAnalyzer.analyze();
            lexicalAnalyzer.writePifToFile(outFileName);
            lexicalAnalyzer.writeSymbolTableToFile(outFileNameSymbol);
        } catch (LexicalException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
