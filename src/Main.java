public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        var a = symbolTable.insert("ab");
        symbolTable.insert("ba");
        System.out.println(symbolTable);
        System.out.println(symbolTable.insert("ab"));
        System.out.println(symbolTable.insert("ba"));
    }
}
